package com.example.app.modules

import com.example.app.generated.apis.DataApiModule
import com.example.app.generated.models.GetDataResponse
import com.example.app.infrastructure.koin.KtorKoinComponent
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.principal
import org.koin.dsl.module
import java.util.UUID

val dataApiModule = module {
    single<DataApiModule> { DataApi() }
}

class DataApi : DataApiModule, KtorKoinComponent() {
    override suspend fun getData(call: ApplicationCall): GetDataResponse {
        val principal = call.principal<UserIdPrincipal>()

        return GetDataResponse(
            dataId = UUID.randomUUID()
        )
    }
}
