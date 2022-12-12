package com.lava

import com.lava.data.source.MongoUserDataSource
import com.lava.plugins.*
import com.lava.security.hashing.SHA256HashingService
import com.lava.security.token.JwtTokenService
import com.lava.security.token.TokenConfig
import io.ktor.server.application.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    val mongoPw = System.getenv("MONGO_PW")
    val dbName = "ktor-auth"
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://snehilsinha:$mongoPw@cluster0.j5hoiwh.mongodb.net/$dbName?retryWrites=true&w=majority"
    ).coroutine
        .getDatabase(name = dbName)
    val userDataSource = MongoUserDataSource(db)
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 30L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()

    launch {
        while (true) {
            val daysUntilExpiry = 30L * 1000L * 60L * 60L * 24L
            delay(daysUntilExpiry)
            userDataSource.resetRegistration()
        }
    }

    configureSerialization()
    configureMonitoring()
    configureSecurity(tokenConfig)
    configureRouting(userDataSource, hashingService, tokenService, tokenConfig)
}
