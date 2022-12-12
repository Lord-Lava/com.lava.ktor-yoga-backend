package com.lava.routes

import com.lava.data.models.User
import com.lava.data.requests.LoginRequest
import com.lava.data.responses.AuthenticateResponse
import com.lava.data.responses.LoginResponse
import com.lava.data.source.UserDataSource
import com.lava.security.hashing.HashingService
import com.lava.security.hashing.SaltedHash
import com.lava.security.token.TokenClaim
import com.lava.security.token.TokenConfig
import com.lava.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signUp(
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("signup") {
        val request = kotlin.runCatching { call.receiveNullable<LoginRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
        val isPwTooShort = request.password.length < 4
        if (areFieldsBlank || isPwTooShort) {
            call.respond(HttpStatusCode.Conflict, message = "Invalid username or password")
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt,
            name = "",
            age = 0,
            slot = "",
            amount = 0,
            isRegistered = false
        )
        val wasAcknowledged = userDataSource.insertUser(user)
        if (!wasAcknowledged) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        val jwtAuthToken = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )

        call.respond(
            HttpStatusCode.Created, message = LoginResponse(
                uid = user.id.toString(),
                jwtAuthToken = jwtAuthToken,
                isRegistered = user.isRegistered
            )
        )
    }
}

fun Route.signIn(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("signin") {
        val request = kotlin.runCatching { call.receiveNullable<LoginRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userDataSource.getUserByUserName(request.username)
        if (user == null) {
            call.respond(HttpStatusCode.NotAcceptable, "Incorrect username or password")
            return@post
        }

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if (!isValidPassword) {
            call.respond(HttpStatusCode.NotAcceptable, "Incorrect username or password")
            return@post
        }

        val jwtAuthToken = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = LoginResponse(
                uid = user.id.toString(),
                jwtAuthToken = jwtAuthToken,
                isRegistered = user.isRegistered
            )
        )
    }
}

fun Route.authenticate(
    userDataSource: UserDataSource
) {
    authenticate {
        get("authenticate") {
            val uid = call.request.queryParameters["uid"].toString()
            val user = userDataSource.getUserById(uid)
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, "token expired")
                return@get
            }
            call.respond(status = HttpStatusCode.OK, message = AuthenticateResponse(user.isRegistered))
        }
    }
}