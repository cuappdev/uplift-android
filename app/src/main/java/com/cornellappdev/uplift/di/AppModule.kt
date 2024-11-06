package com.cornellappdev.uplift.di

import com.apollographql.apollo3.ApolloClient
import com.cornellappdev.uplift.BuildConfig
import com.cornellappdev.uplift.data.ApolloReportClient
import com.cornellappdev.uplift.domain.ReportClient
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
            .serverUrl(BuildConfig.API_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideReportClient(apolloClient: ApolloClient): ReportClient {
        return ApolloReportClient(apolloClient)
    }
}