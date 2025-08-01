package cz.gpt.fioapi

import kotlinx.datetime.LocalDate

interface FioApi {
    suspend fun getTransactions(
        token: String,
        dateFrom: LocalDate,
        dateTo: LocalDate
    ): FioTransactionResponse

    suspend fun getLastTransactions(
        token: String
    ): FioTransactionResponse
}