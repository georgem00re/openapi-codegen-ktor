package com.example.app.modules

import com.example.app.generated.models.SuccessResponse
import com.example.app.infrastructure.koin.KoinContext
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HealthApiTest : KoinTest {
    @BeforeEach
    fun before() {
        startKoin {
            modules(
                module {
                    single { HealthApi() }
                },
            )
        }.let {
            KoinContext.setContext(it)
        }
    }

    @Test
    fun `Returns a success response`() = runBlocking {
        assertEquals(
            SuccessResponse(ok = true),
            get<HealthApi>().getHealth(mockk())
        )
    }
}
