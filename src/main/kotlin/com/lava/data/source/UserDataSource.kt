package com.lava.data.source

import com.lava.data.models.User
import org.bson.types.ObjectId

interface UserDataSource {

    suspend fun getUserByUserName(username: String): User?
    suspend fun getUserById(uid: String): User?
    suspend fun insertUser(user: User): Boolean
    suspend fun updateUserById(id: ObjectId, user: User): Boolean
}