package com.cornellappdev.uplift.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import com.cornellappdev.uplift.BuildConfig
import com.cornellappdev.uplift.data.auth.AuthInterceptor
import com.cornellappdev.uplift.data.auth.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("refresh") // Use a named annotation to distinguish them
    fun provideRefreshApolloClient(): ApolloClient {
        // This client does NOT have interceptors to avoid loops
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()

        return ApolloClient.Builder()
            .serverUrl(BuildConfig.BACKEND_URL)
            .okHttpClient(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Named("main")
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    @Named("main")
    fun provideApolloClient(@Named("main") okHttpClient: OkHttpClient): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(BuildConfig.BACKEND_URL)
            .okHttpClient(okHttpClient)
            .build()
    }

}
