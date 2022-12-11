package com.lava.plugins

import com.lava.data.source.UserDataSource
import com.lava.routes.authenticate
import com.lava.routes.registerUser
import com.lava.routes.signIn
import com.lava.routes.signUp
import com.lava.security.hashing.HashingService
import com.lava.security.token.TokenConfig
import com.lava.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        signIn(userDataSource, hashingService, tokenService, tokenConfig)
        signUp(hashingService, userDataSource, tokenService, tokenConfig)
        authenticate()
        registerUser(userDataSource)
        get("/") {
            call.respond(HttpStatusCode.OK, "Welcome to the Yoga App!")
        }
    }
}
