package cz.cupi.fioapi.api.internal

import cz.cupi.fioapi.api.FioApiException
import cz.cupi.fioapi.dto.FioApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * Creates default HttpClient configured for Fio API communication.
 */
internal fun createDefaultFioHttpClient(debugLog: Boolean = false) = HttpClient {
	install(ContentNegotiation) {
		json(
			Json {
				ignoreUnknownKeys = true
			}
		)
	}
	if (debugLog) {
		install(Logging) {
			logger = Logger.DEFAULT
			level = LogLevel.ALL
		}
	} else {
		install(Logging) {
			logger = Logger.DEFAULT
			level = LogLevel.NONE
		}
	}
}

/**
 * Internal API client for executing Fio API HTTP requests.
 * Handles common error scenarios and response processing.
 */
internal suspend inline fun <T> HttpClient.executeApiCall(
	url: String,
	crossinline responseProcessor: (FioApiResponse) -> T
): T {
	try {
		val response = get(url)
		return when (response.status.value) {
			200 -> {
				try {
					val dto = response.body<FioApiResponse>()
					responseProcessor(dto)
				} catch (e: SerializationException) {
					throw FioApiException.Parse("Error parsing response: ${e.message}", e)
				}
			}
			400 -> throw FioApiException.InvalidRequest("Request error (400): Check parameters.")
			401, 403 -> throw FioApiException.Unauthorized()
			404 -> throw FioApiException.NotFound()
			409 -> throw FioApiException.Conflict("409: This statement has already been downloaded or conflicts with another request.")
			422 -> throw FioApiException.UnprocessableEntity("422: Request cannot be processed, possibly bad date or token.")
			429 -> throw FioApiException.RateLimit("429: Request limit exceeded, wait 30 seconds.")
			in 500..599 -> throw FioApiException.ApiError(response.status.value, "Server error: ${response.status.value}")
			else -> throw FioApiException.ApiError(response.status.value, "Unknown HTTP status: ${response.status.value}")
		}
	} catch (e: SerializationException) {
		throw FioApiException.Parse("Error parsing response: ${e.message}", e)
	} catch (e: IOException) {
		throw FioApiException.Network("Network error: ${e.message}", e)
	} catch (e: ResponseException) {
		throw FioApiException.ApiError(e.response.status.value, "API error: ${e.message}", e)
	} catch (e: FioApiException) {
		throw e
	} catch (e: Exception) {
		throw FioApiException.Unknown("Unknown error: ${e.message}", e)
	}
}
