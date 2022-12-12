package com.lava.data.source

import com.lava.data.models.User
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.set
import org.litote.kmongo.setTo

class MongoUserDataSource(
    db: CoroutineDatabase
) : UserDataSource {

    private val users = db.getCollection<User>()

    override suspend fun getUserByUserName(username: String): User? {
        return users.findOne(User::username eq username)
    }

    override suspend fun getUserById(uid: String): User? {
        return users.findOneById(ObjectId(uid))
    }

    override suspend fun insertUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }

    override suspend fun updateUserById(id: ObjectId, user: User): Boolean {
        return users.updateOne(
            User::id eq id, set(
                User::username setTo user.username,
                User::password setTo user.password,
                User::salt setTo user.salt,
                User::name setTo user.name,
                User::age setTo user.age,
                User::slot setTo user.slot,
                User::amount setTo user.amount,
                User::isRegistered setTo user.isRegistered,
                User::registrationDate setTo user.registrationDate
            )
        ).wasAcknowledged()
    }
    override suspend fun resetRegistration(): Boolean {
        return users.updateMany(
            User::isRegistered eq true, set(
                User::isRegistered setTo false,
                User::registrationDate setTo null
            )
        ).wasAcknowledged()
    }
}