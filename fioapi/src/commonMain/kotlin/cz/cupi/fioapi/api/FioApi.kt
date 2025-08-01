package cz.cupi.fioapi.api

import cz.cupi.fioapi.domain.AccountInfo
import cz.cupi.fioapi.domain.AccountStatement
import cz.cupi.fioapi.domain.Transaction
import kotlinx.datetime.LocalDate

interface FioApi {

	suspend fun getTransactions(dateFrom: LocalDate, dateTo: LocalDate): List<Transaction>

	suspend fun getLastTransactions(): List<Transaction>

	suspend fun getTransactionsByIdRange(fromId: Long, toId: Long): List<Transaction>

	suspend fun getTransactionsFromId(fromId: Long): List<Transaction>

	suspend fun getTransactionsByMonth(year: Int, month: Int): List<Transaction> // 1=leden, 12=prosinec

	suspend fun getAccountInfo(): AccountInfo

	suspend fun getAccountStatement(dateFrom: LocalDate, dateTo: LocalDate): AccountStatement

	suspend fun getLastAccountStatement(): AccountStatement
}
