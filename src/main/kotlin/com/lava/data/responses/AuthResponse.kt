package com.lava.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val uid: String,
    val jwtAuthToken: String,
    val isRegistered: Boolean
)
