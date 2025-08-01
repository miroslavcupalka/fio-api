package cz.gpt.fioapi

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.datetime.LocalDate

class FioApiImpl(
    private val httpClient: HttpClient = defaultFioHttpClient()
) : FioApi {

    override suspend fun getTransactions(
        token: String,
        dateFrom: LocalDate,
        dateTo: LocalDate
    ): FioTransactionResponse {
        val url = "https://fioapi.fio.cz/v1/rest/periods/$token/$dateFrom/$dateTo/transactions.json"
        return httpClient.get(url).body()
    }

    override suspend fun getLastTransactions(token: String): FioTransactionResponse {
        val url = "https://fioapi.fio.cz/v1/rest/last/$token/transactions.json"
        return httpClient.get(url).body()
    }
}

fun defaultFioHttpClient() = HttpClient {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
            }
        )
    }
}