package com.cornellappdev.uplift.networking

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.models.UpliftGym
import com.cornellappdev.uplift.networking.UpliftApiRepository.classesApiFlow
import com.cornellappdev.uplift.networking.UpliftApiRepository.gymApiFlow
import com.cornellappdev.uplift.util.defaultGymUrl
import com.example.rocketreserver.ClassListQuery
import com.example.rocketreserver.GymListQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * A repository dealing with all API backend connection in Uplift.
 *
 * See [gymApiFlow] and [classesApiFlow].
 */
object UpliftApiRepository {
    private val apolloClient = ApolloClient.Builder()
        .serverUrl("http://uplift-backend.cornellappdev.com/graphql")
        .build()

    private val gymQuery = apolloClient.query(GymListQuery())
    private val classQuery = apolloClient.query(ClassListQuery())

    private val _gymApiFlow: MutableStateFlow<ApiResponse<List<UpliftGym>>> =
        MutableStateFlow(ApiResponse.Loading)

    /**
     * A flow emitting [ApiResponse]s for a list of gyms queried from backend.
     */
    val gymApiFlow = _gymApiFlow.asStateFlow()

    private val _classesApiFlow: MutableStateFlow<ApiResponse<List<UpliftClass>>> =
        MutableStateFlow(ApiResponse.Loading)

    /**
     * A flow emitting [ApiResponse]s for a list of classes queried from backend.
     */
    val classesApiFlow = _classesApiFlow.asStateFlow()

    private lateinit var activeGymJob: Job

    private lateinit var activeClassJob: Job

    /**
     * Attempts to load the backend data. Makes another query for all gym and class data.
     */
    fun reload() {
        // Close previous flows to help performance.
        if (::activeGymJob.isInitialized)
            activeGymJob.cancel()

        if (::activeClassJob.isInitialized)
            activeClassJob.cancel()

        // Idea: Have cancellable flows in the background that are emitted down classesApiFlow
        //  and gymApiFlow. On retry, cancel the previous flows, then start new ones.
        activeGymJob = CoroutineScope(Dispatchers.IO).launch {
            gymQuery.toFlow().cancellable()
                .map {
                    val gymList = it.data?.gyms?.filterNotNull()
                    if (gymList == null) {
                        ApiResponse.Error
                    } else {
                        ApiResponse.Success(gymList.map { query -> query.toUpliftGyms() }.flatten())
                    }
                }
                .catch {
                    emit(ApiResponse.Error)
                }.stateIn(
                    CoroutineScope(Dispatchers.Main),
                    SharingStarted.Eagerly,
                    ApiResponse.Loading
                ).collect {
                    _gymApiFlow.emit(it)
                }
        }

        // TODO: Backend has refactored such that classes are under gyms now.
        //  Thus, in pretty much a gymQuery, we have to map and then flatten
        //  to get a list of all classes. Then, pretty much pipeline it down [_classesApiFlow].
        //  This can be done with a query that just queries each gym's classes list.
        //  I'll do that here.
        activeClassJob = CoroutineScope(Dispatchers.IO).launch {
            classQuery.toFlow().cancellable()
                .map {
                    val gyms = it.data?.gyms
                    if (gyms == null) {
                        ApiResponse.Error
                    } else {
                        val classList =
                            gyms.filterNotNull().mapNotNull { gym ->
                                gym.classes?.map { classQuery ->
                                    Pair(
                                        classQuery,
                                        gym.imageUrl ?: defaultGymUrl
                                    )
                                }
                            }.flatten().filter { pair -> pair.first != null }

                        val upliftClasses =
                            classList.mapNotNull { query -> query.first!!.toUpliftClass(query.second) }

                        Log.d("classes", upliftClasses.map {
                            it.date.get(Calendar.DAY_OF_MONTH)
                        }.toString())

                        ApiResponse.Success(
                            upliftClasses.distinctBy { upliftClass ->
                                listOf(
                                    upliftClass.name,
                                    upliftClass.location,
                                    upliftClass.time,
                                    upliftClass.date
                                )
                            }
                        )
                    }
                }
                .catch {
                    emit(ApiResponse.Error)
                }.stateIn(
                    CoroutineScope(Dispatchers.Main),
                    SharingStarted.Eagerly,
                    ApiResponse.Loading
                ).collect {
                    _classesApiFlow.emit(it)
                }
        }
    }

    init {
        // Load on startup.
        reload()
    }
}
