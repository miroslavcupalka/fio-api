package cz.cupi.fioapi.utils

/**
 * Security utility functions for Fio API.
 * Contains validation and sanitization methods to prevent security issues
 * like URL injection and parameter tampering.
 *
 * @since 1.0.0
 */

/**
 * Sanitizes a token for safe usage in URLs.
 * 
 * @return A sanitized token safe for URL inclusion
 * @throws IllegalArgumentException if the token is empty after sanitization or doesn't meet length requirements
 * @since 1.0.0
 */
internal fun String.sanitizeToken(): String {
	// Remove whitespace and potentially dangerous characters
	val cleaned = this.trim()
		.replace(Regex("[^a-zA-Z0-9_-]"), "") // Only allow alphanumeric, underscore, hyphen

	// Validation
	require(cleaned.isNotEmpty()) { "Token cannot be empty after sanitization" }
	require(cleaned.length >= 10) { "Token too short (min 10 chars)" }
	require(cleaned.length <= 100) { "Token too long (max 100 chars)" }

	return cleaned
}

/**
 * Validates a date string to prevent injection attacks.
 * 
 * This function ensures that the date string follows the ISO format (YYYY-MM-DD).
 * It protects against path traversal and injection attacks in URL parameters.
 *
 * @return The validated date string
 * @throws IllegalArgumentException if the date format is invalid
 * @since 1.0.0
 */
internal fun String.sanitizeDateString(): String {
	require(this.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$"))) {
		"Invalid date format. Expected YYYY-MM-DD, got: $this"
	}
	return this
}

/**
 * Validates a numeric ID to ensure it's non-negative.
 *
 * @param paramName The parameter name for the error message
 * @return The validated ID value
 * @throws IllegalArgumentException if the ID is negative
 * @since 1.0.0
 */
internal fun Long.validateId(paramName: String = "id"): Long {
	require(this >= 0) { "$paramName must be non-negative, got: $this" }
	return this
}

/**
 * Validates a year value to ensure it's within a reasonable range (2000-2100).
 *
 * @return The validated year value
 * @throws IllegalArgumentException if the year is outside the valid range
 * @since 1.0.0
 */
internal fun Int.validateYear(): Int {
	require(this in 2000..2100) { "Invalid year: $this" }
	return this
}

/**
 * Validates a month value to ensure it's between 1 and 12.
 *
 * @return The validated month value
 * @throws IllegalArgumentException if the month is outside the valid range
 * @since 1.0.0
 */
internal fun Int.validateMonth(): Int {
	require(this in 1..12) { "Invalid month: $this" }
	return this
}
