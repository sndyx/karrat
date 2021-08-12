package org.karrat

import org.karrat.auth.requestToken

class Client(val token: String)

suspend fun Client(username: String, password: String): Client = Client(requestToken(username, password))