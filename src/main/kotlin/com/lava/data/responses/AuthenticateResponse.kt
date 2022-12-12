package com.lava.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticateResponse(
    val isRegistered: Boolean
)