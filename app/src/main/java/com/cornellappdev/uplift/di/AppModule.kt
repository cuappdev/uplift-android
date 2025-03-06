package com.cornellappdev.uplift.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.apollographql.apollo3.ApolloClient
import com.cornellappdev.uplift.BuildConfig
import com.cornellappdev.uplift.data.repositories.PopularTimesRepositoryImpl
import com.cornellappdev.uplift.data.repositories.ReportRepositoryImpl
import com.cornellappdev.uplift.domain.gym.populartimes.PopularTimesRepository
import com.cornellappdev.uplift.domain.report.ReportRepository
import com.cornellappdev.uplift.data.repositories.UserInfoRepositoryImpl
import com.cornellappdev.uplift.domain.repositories.UserInfoRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(BuildConfig.BACKEND_URL)
            .build()
    }

    @Provides
    @Singleton
    fun providePopularTimesRepository(apolloClient: ApolloClient): PopularTimesRepository {
        return PopularTimesRepositoryImpl(apolloClient)
    }

    @Provides
    @Singleton
    fun provideReportRepository(apolloClient: ApolloClient): ReportRepository {
        return ReportRepositoryImpl(apolloClient)
    }

    @Provides
    @Singleton
    fun provideUserInfoRepository(
        firebaseAuth: FirebaseAuth,
        apolloClient: ApolloClient,
        dataStore: DataStore<Preferences>
    ): UserInfoRepository {
        return UserInfoRepositoryImpl(firebaseAuth, apolloClient, dataStore)
    }

}