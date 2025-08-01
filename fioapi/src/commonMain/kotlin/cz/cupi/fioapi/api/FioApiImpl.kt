package cz.cupi.fioapi.api

import cz.cupi.fioapi.api.internal.createDefaultFioHttpClient
import cz.cupi.fioapi.api.internal.executeApiCall
import cz.cupi.fioapi.domain.AccountInfo
import cz.cupi.fioapi.domain.AccountStatement
import cz.cupi.fioapi.domain.Transaction
import cz.cupi.fioapi.mapper.toAccountInfo
import cz.cupi.fioapi.mapper.toAccountStatement
import cz.cupi.fioapi.mapper.toTransactions
import cz.cupi.fioapi.utils.sanitizeToken
import cz.cupi.fioapi.utils.validateId
import cz.cupi.fioapi.utils.validateMonth
import cz.cupi.fioapi.utils.validateYear
import io.ktor.client.HttpClient
import kotlinx.datetime.LocalDate

/**
 * Configuration for Fio API endpoints.
 */
internal object FioApiConfig {
	/**
	 * Base URL for all Fio API REST endpoints.
	 */
	const val BASE_URL = "https://fioapi.fio.cz/v1/rest"
}

/**
 * Implementation of Fio Bank API client.
 *
 * This class provides access to Fio Bank account data including transactions, account statements,
 * and account information. It handles API communication, error handling, and data mapping.
 *
 * @property token The Fio API authentication token (will be sanitized for security)
 * @property httpClient Optional custom HTTP client (creates default if null)
 * @property debugLog Whether to enable detailed HTTP request/response logging
 * 
 * @throws IllegalArgumentException if token validation fails
 * @since 1.0.0
 */
class FioApiImpl(
	private val token: String,
	httpClient: HttpClient? = null,
	debugLog: Boolean = false
) : FioApi {

	/**
	 * Sanitized token safe for URL inclusion.
	 * Processed through sanitizeToken() to remove potentially dangerous characters.
	 */
	private val safeToken = token.sanitizeToken()

	/**
	 * HTTP client used for API communication.
	 * Either provided by the caller or created with default configuration.
	 */
	private val httpClient = httpClient ?: createDefaultFioHttpClient(debugLog)

	/**
	 * Retrieves transactions for the specified date range.
	 *
	 * @param dateFrom Start date for the transaction range (inclusive)
	 * @param dateTo End date for the transaction range (inclusive)
	 * @return List of transactions within the specified date range
	 * @throws FioApiException if the API request fails
	 */
	override suspend fun getTransactions(
		dateFrom: LocalDate,
		dateTo: LocalDate
	): List<Transaction> {
		val url = "${FioApiConfig.BASE_URL}/periods/$safeToken/$dateFrom/$dateTo/transactions.json"
		return httpClient.executeApiCall(url) { it.toTransactions() }
	}

	/**
	 * Retrieves transactions that occurred since the last API call.
	 *
	 * @return List of new transactions since the previous request
	 * @throws FioApiException if the API request fails
	 */
	override suspend fun getLastTransactions(): List<Transaction> {
		val url = "${FioApiConfig.BASE_URL}/last/$safeToken/transactions.json"
		return httpClient.executeApiCall(url) { it.toTransactions() }
	}

	/**
	 * Retrieves transactions by ID range.
	 *
	 * @param fromId Starting transaction ID (inclusive)
	 * @param toId Ending transaction ID (inclusive)
	 * @return List of transactions within the specified ID range
	 * @throws FioApiException if the API request fails
	 * @throws IllegalArgumentException if IDs are invalid
	 */
	override suspend fun getTransactionsByIdRange(
		fromId: Long,
		toId: Long
	): List<Transaction> {
		val safeFromId = fromId.validateId("fromId")
		val safeToId = toId.validateId("toId")
		require(safeToId >= safeFromId) { "toId must be >= fromId" }

		val url = "${FioApiConfig.BASE_URL}/by-id/$safeToken/$safeFromId/$safeToId/transactions.json"
		return httpClient.executeApiCall(url) { it.toTransactions() }
	}

	/**
	 * Sets last accessed ID and retrieves newer transactions.
	 * 
	 * This method marks the specified ID as the last seen transaction
	 * and returns transactions with higher IDs.
	 *
	 * @param fromId The transaction ID to set as last accessed
	 * @return List of transactions with IDs greater than fromId
	 * @throws FioApiException if the API request fails
	 * @throws IllegalArgumentException if ID is invalid
	 */
	override suspend fun getTransactionsFromId(
		fromId: Long
	): List<Transaction> {
		val safeFromId = fromId.validateId("fromId")

		val url = "${FioApiConfig.BASE_URL}/set-last-id/$safeToken/$safeFromId/transactions.json"
		return httpClient.executeApiCall(url) { it.toTransactions() }
	}

	/**
	 * Retrieves transactions for a specific month.
	 *
	 * @param year The year (e.g., 2024)
	 * @param month The month (1-12, where 1=January, 12=December)
	 * @return List of transactions for the specified month
	 * @throws FioApiException if the API request fails
	 * @throws IllegalArgumentException if year or month is invalid
	 */
	override suspend fun getTransactionsByMonth(
		year: Int,
		month: Int
	): List<Transaction> {
		val safeYear = year.validateYear()
		val safeMonth = month.validateMonth()

		val url = "${FioApiConfig.BASE_URL}/by-month/$safeToken/$safeYear/$safeMonth/transactions.json"
		return httpClient.executeApiCall(url) { it.toTransactions() }
	}

	/**
	 * Retrieves basic account information.
	 *
	 * @return AccountInfo containing account details
	 * @throws FioApiException if the API request fails
	 */
	override suspend fun getAccountInfo(): AccountInfo {
		val url = "${FioApiConfig.BASE_URL}/last/$safeToken/transactions.json"
		return httpClient.executeApiCall(url) { it.toAccountInfo() }
	}

	/**
	 * Retrieves a complete account statement for the specified date range.
	 *
	 * @param dateFrom Start date for the statement (inclusive)
	 * @param dateTo End date for the statement (inclusive)
	 * @return AccountStatement containing account details and transactions
	 * @throws FioApiException if the API request fails
	 */
	override suspend fun getAccountStatement(
		dateFrom: LocalDate,
		dateTo: LocalDate
	): AccountStatement {
		val url = "${FioApiConfig.BASE_URL}/periods/$safeToken/$dateFrom/$dateTo/transactions.json"
		return httpClient.executeApiCall(url) { it.toAccountStatement() }
	}

	/**
	 * Retrieves the latest account statement with transactions since the last request.
	 *
	 * @return AccountStatement containing account details and new transactions
	 * @throws FioApiException if the API request fails
	 */
	override suspend fun getLastAccountStatement(): AccountStatement {
		val url = "${FioApiConfig.BASE_URL}/last/$safeToken/transactions.json"
		return httpClient.executeApiCall(url) { it.toAccountStatement() }
	}
}
