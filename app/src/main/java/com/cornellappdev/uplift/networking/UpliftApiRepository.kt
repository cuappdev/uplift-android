package com.cornellappdev.uplift.networking

import com.apollographql.apollo3.ApolloClient
import com.cornellappdev.uplift.models.UpliftClass
import com.cornellappdev.uplift.models.UpliftGym
import com.example.rocketreserver.ClassListQuery
import com.example.rocketreserver.GymListQuery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val apolloClient = ApolloClient.Builder()
    .serverUrl("https://uplift-backend.cornellappdev.com")
    .build()

object UpliftApiRepository {
    val gymApiFlow =
        apolloClient.query(GymListQuery()).toFlow()
            .map {
                val gymList = it.data?.gyms?.filterNotNull()
                if (gymList == null) {
                    ApiResponse.Error
                } else {
                    ApiResponse.Success(gymList)
                }
            }
            .catch {
                emit(ApiResponse.Error)
            }

    val classesApiFlow =
        apolloClient.query(ClassListQuery()).toFlow()
            .map {
                val classList = it.data?.classes?.filterNotNull()
                if (classList == null) {
                    ApiResponse.Error
                } else {
                    ApiResponse.Success(classList)
                }
            }
            .catch {
                emit(ApiResponse.Error)
            }

    private val _upliftGymsFlow : MutableStateFlow<ApiResponse<List<UpliftGym>>> = MutableStateFlow(ApiResponse.Loading)

    val upliftGymsFlow = _upliftGymsFlow.asStateFlow()

    private val _upliftClassesFlow : MutableStateFlow<ApiResponse<List<UpliftClass>>> = MutableStateFlow(ApiResponse.Loading)

    val upliftClassesFlow = _upliftClassesFlow.asStateFlow()

    fun loadUpliftGyms(gyms : List<UpliftGym>) {
        _upliftGymsFlow.value = ApiResponse.Success(gyms)
    }

    fun loadUpliftClasses(classes : List<UpliftClass>) {
        _upliftClassesFlow.value = ApiResponse.Success(classes)
    }
}