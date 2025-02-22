package com.cornellappdev.uplift.di

import com.apollographql.apollo3.ApolloClient
import com.cornellappdev.uplift.BuildConfig
import com.cornellappdev.uplift.data.clients.ApolloPopularTimesClient
import com.cornellappdev.uplift.data.clients.ApolloReportClient
import com.cornellappdev.uplift.domain.gym.populartimes.GetPopularTimesUseCase
import com.cornellappdev.uplift.domain.gym.populartimes.PopularTimesClient
import com.cornellappdev.uplift.domain.report.CreateReportUseCase
import com.cornellappdev.uplift.domain.report.ReportClient
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
    fun providePopularTimesClient(apolloClient: ApolloClient): PopularTimesClient {
        return ApolloPopularTimesClient(apolloClient)
    }

    @Provides
    @Singleton
    fun provideGetPopularTimesUseCase(popularTimesClient: PopularTimesClient): GetPopularTimesUseCase {
        return GetPopularTimesUseCase(popularTimesClient)
    }

    @Provides
    @Singleton
    fun provideReportClient(apolloClient: ApolloClient): ReportClient {
        return ApolloReportClient(apolloClient)
    }

    @Provides
    @Singleton
    fun provideCreateReportUseCase(reportClient: ReportClient): CreateReportUseCase {
        return CreateReportUseCase(reportClient)
    }

}