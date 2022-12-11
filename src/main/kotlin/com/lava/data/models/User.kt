package com.lava.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDate

data class User(
    @BsonId val id: ObjectId = ObjectId(),
    val username: String,
    val password: String,
    val salt: String,
    val name: String,
    val age: Int,
    val slot: String,
    val amount: Int,
    val isRegistered: Boolean,
    val registrationDate: LocalDate? = null
)
