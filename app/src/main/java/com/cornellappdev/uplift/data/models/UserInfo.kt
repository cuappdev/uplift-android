package com.cornellappdev.uplift.data.models
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id: String,
    val email: String,
    val name: String,
    val netId: String,
)