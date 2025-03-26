package com.cornellappdev.uplift.di

import com.apollographql.apollo.ApolloClient
import com.cornellappdev.uplift.BuildConfig
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

}