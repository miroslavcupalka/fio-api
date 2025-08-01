package cz.cupi.fioapi.api

sealed class FioApiException(message: String, cause: Throwable? = null) : Exception(message, cause) {
	class Network(message: String, cause: Throwable? = null) : FioApiException(message, cause)
	class Unauthorized(message: String = "Invalid or unauthorized token", cause: Throwable? = null) : FioApiException(message, cause)
	class NotFound(message: String = "Resource not found", cause: Throwable? = null) : FioApiException(message, cause)
	class RateLimit(message: String = "Too many requests (rate limited)", cause: Throwable? = null) : FioApiException(message, cause)
	class Conflict(message: String = "Request conflict (possible repeated fetch)", cause: Throwable? = null) : FioApiException(message, cause)
	class InvalidRequest(message: String = "Invalid request (check parameters)", cause: Throwable? = null) : FioApiException(message, cause)
	class UnprocessableEntity(message: String = "Request cannot be processed (422)", cause: Throwable? = null) : FioApiException(message, cause)
	class ApiError(val code: Int, message: String, cause: Throwable? = null) : FioApiException(message, cause)
	class Parse(message: String = "Failed to parse API response", cause: Throwable? = null) : FioApiException(message, cause)
	class Business(message: String = "Business error from Fio API", cause: Throwable? = null) : FioApiException(message, cause)
	class Unknown(message: String = "Unknown error", cause: Throwable? = null) : FioApiException(message, cause)
}
