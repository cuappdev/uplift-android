package com.cornellappdev.uplift.networking

import com.apollographql.apollo3.ApolloClient

val apolloClient = ApolloClient.Builder()
    .serverUrl("https://uplift-backend.cornellappdev.com")
    .build()