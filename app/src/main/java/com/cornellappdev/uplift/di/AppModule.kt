package com.cornellappdev.uplift.di

import com.apollographql.apollo3.ApolloClient
import com.cornellappdev.uplift.BuildConfig
import com.cornellappdev.uplift.data.repositories.PopularTimesRepositoryImpl
import com.cornellappdev.uplift.data.repositories.ReportRepositoryImpl
import com.cornellappdev.uplift.domain.gym.populartimes.PopularTimesRepository
import com.cornellappdev.uplift.domain.report.ReportRepository
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

}