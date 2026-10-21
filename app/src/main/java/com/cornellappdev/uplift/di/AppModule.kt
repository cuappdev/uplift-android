package com.cornellappdev.uplift.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import com.cornellappdev.uplift.BuildConfig
import com.cornellappdev.uplift.data.auth.ApolloAuthInterceptor
import com.cornellappdev.uplift.data.auth.AuthInterceptor
import com.cornellappdev.uplift.data.auth.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class ApplicationScope

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

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
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("main")
    fun provideApolloClient(
        @Named("main") okHttpClient: OkHttpClient,
        apolloAuthInterceptor: ApolloAuthInterceptor
    ): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(BuildConfig.BACKEND_URL)
            .okHttpClient(okHttpClient)
            .addInterceptor(apolloAuthInterceptor)
            .build()
    }

}
