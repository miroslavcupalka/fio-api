package cz.cupi.fioexample

import cz.cupi.fioapi.api.FioApi
import cz.cupi.fioapi.api.FioApiImpl
import cz.cupi.fioapi.api.FioApiException
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.TimeZone
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun main(args: Array<String>) = runBlocking {
	val token = System.getenv("FIO_API_TOKEN") ?: error("Set env FIO_API_TOKEN")
	val fio: FioApi = FioApiImpl(token)

	// Today's date and 30 days ago
	val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
	val dateFrom = today.minus(DatePeriod(days = 30))
	val dateTo = today

	try {
		val transactions = fio.getTransactions(dateFrom, dateTo) // byDate - Change date as needed
		// val transactions = fio.getTransactionsByMonth(2025, 7) // byMonth - Change year and month as needed
		// val transactions = fio.getLastTransactions() // fromLastDownload - Last downloaded transactions
		// val transactions = fio.getTransactionsByIdRange(123456, 123789) // byId - Change ID as needed
		// val transactions = fio.getTransactionsFromId(1234567) // fromId - Change ID as needed

		println("Transactions from $dateFrom to $dateTo:")
		transactions.forEach { t ->
			println("Date: ${t.date} | Amount: ${t.amount.formatAmount()} ${t.currency} | Type: ${t.transactionType?.fioLabel} | Counter Account: ${t.counterAccountNumber} | Variable Symbol: ${t.variableSymbol} | Comment: ${t.comment ?: "N/A"}")
		}
	} catch (e: FioApiException.NotFound) {
		println("No transactions for this month or account")
	}  catch (e: FioApiException.RateLimit) {
		println("Request limit exceeded: ${e.message}")
	} catch (e: FioApiException.Unauthorized) {
		println("Invalid or expired token: ${e.message}")
	} catch (e: FioApiException) {
		println("Error communicating with Fio API: ${e.message}")
	}
}
