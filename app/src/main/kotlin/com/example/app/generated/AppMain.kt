package com.example.app.generated

import org.koin.ktor.plugin.Koin
import io.ktor.server.application.*
import io.ktor.server.routing.routing
import com.example.app.generated.apis.DataApi
import com.example.app.generated.apis.HealthApi

import com.example.app.modules.*
import org.koin.core.logger.PrintLogger
import com.example.app.infrastructure.koin.KoinContext
import com.example.app.configureAll

fun Application.main() {
    configureAll()

    install(Koin) {
        modules(
            dataApiModule,
            healthApiModule,
            
       ).let {
          KoinContext.setContext(it)
       }
       createEagerInstances()
       PrintLogger()
    }
    routing {
        DataApi()
        HealthApi()
    }

}
