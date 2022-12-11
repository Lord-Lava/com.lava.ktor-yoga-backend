package com.lava.routes

import com.lava.data.models.User
import com.lava.data.requests.RegistrationRequest
import com.lava.data.responses.RegistrationResponse
import com.lava.data.source.UserDataSource
import com.lava.utils.Config
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import java.time.LocalDate

fun Route.registerUser(
    userDataSource: UserDataSource,
) {
    post("register") {
        val request = kotlin.runCatching { call.receiveNullable<RegistrationRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userDataSource.getUserById(request.uid)
        if (user == null) {
            call.respond(HttpStatusCode.NotFound, "User doesn't exist")
            return@post
        }
        val isSameMonth = LocalDate.now().month == user.registrationDate?.month
        if (isSameMonth && user.isRegistered) {
            call.respond(HttpStatusCode.Forbidden, "Already Registered")
            return@post
        }

        val isValidAge = request.age in 18..65
        if (!isValidAge) {
            call.respond(HttpStatusCode.Forbidden, "Invalid Age!")
            return@post
        }

        val isValidSlot = Config.availableSlots.contains(request.slot)
        if (!isValidSlot) {
            call.respond(HttpStatusCode.Forbidden, "Invalid slot requested!")
            return@post
        }

        val wasPaymentSuccessful = completePayment(request.amount)
        if (wasPaymentSuccessful) {
            val updatedUser = User(
                id = ObjectId(request.uid),
                username = user.username,
                password = user.password,
                salt = user.salt,
                name = request.name,
                age = request.age,
                slot = request.slot,
                amount = request.amount,
                isRegistered = true,
                registrationDate = LocalDate.now()
            )
            val wasAcknowledged = userDataSource.updateUserById(ObjectId(request.uid), updatedUser)
            if (!wasAcknowledged) {
                call.respond(HttpStatusCode.Conflict, "Unknown error!")
                return@post
            }
            call.respond(
                HttpStatusCode.OK, RegistrationResponse(
                    uid = request.uid,
                    name = request.name,
                    age = request.age,
                    slot = request.slot,
                    amount = request.amount
                )
            )
            return@post
        }

        call.respond(HttpStatusCode.PaymentRequired, "Payment Failed!")
        return@post
    }
}

fun completePayment(amount: Int): Boolean {
    return amount == 500
}