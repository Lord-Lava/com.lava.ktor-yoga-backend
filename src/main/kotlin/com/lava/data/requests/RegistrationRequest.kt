package com.lava.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(
    val uid: String,
    val name: String,
    val age: Int,
    val slot: String,
    val amount: Int,
)
