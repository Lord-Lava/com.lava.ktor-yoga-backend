package com.lava.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationResponse(
    val uid: String,
    val name: String,
    val age: Int,
    val slot: String,
    val amount: Int,
)
