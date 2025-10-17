package com.alapierre.turnero.infra.http

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/ping")
class PingController {
    @Get
    fun ping() = "pong - v2"
}
