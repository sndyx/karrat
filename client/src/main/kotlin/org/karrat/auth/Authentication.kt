package org.karrat.auth

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

const val ENDPOINT = "https://authserver.mojang.com"
const val AUTHENTICATE = "$ENDPOINT/authenticate"
const val REFRESH = "$ENDPOINT/refresh"

val json = Json {
    ignoreUnknownKeys = true
}

val client = HttpClient(CIO) {
    install(JsonFeature)
}

suspend fun requestToken(username: String, password: String): String {
    val response = client.post<HttpResponse>(AUTHENTICATE) {
        contentType(ContentType.Application.Json)
        body = AuthPayload(username, password)
    }
    val content = response.content.readUTF8Line() ?: throw AuthenticationException(AuthError("AuthenticationException", "No response was provided by the server"))
    if (response.status.value != 200) {
        val error = json.decodeFromString<AuthError>(content)
        throw AuthenticationException(error)
    }
    return json.decodeFromString<AuthResponse>(content).accessToken
}

@Serializable
class AuthPayload(val username: String, val password: String) {
    
    val agent = Agent
    
    @Serializable
    object Agent {
        
        val name = "Minecraft"
        val version = 1
        
    }
    
}

@Serializable
class AuthResponse(val accessToken: String)

@Serializable
class AuthError(val error: String, val errorMessage: String, val cause: String = "")

class AuthenticationException(val error: AuthError) : RuntimeException()