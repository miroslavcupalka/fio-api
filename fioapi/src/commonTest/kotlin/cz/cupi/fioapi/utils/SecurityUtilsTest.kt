package cz.cupi.fioapi.utils

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SecurityUtilsTest {

    @Test
    fun `sanitizeToken should remove invalid characters`() {
        // Arrange
        val dirtyToken = " abc123!@#$%^&*()_-+={[}]|:;\"'<,>.?/"

        // Act
        val result = dirtyToken.sanitizeToken()

        // Assert
        assertEquals("abc123_-", result)
    }

    @Test
    fun `sanitizeToken should pass valid token unchanged`() {
        // Arrange
        val validToken = "abc123_-XYZ789"

        // Act
        val result = validToken.sanitizeToken()

        // Assert
        assertEquals(validToken, result)
    }

    @Test
    fun `sanitizeToken should trim whitespace`() {
        // Arrange
        val tokenWithWhitespace = "  abc123_-XYZ789  "

        // Act
        val result = tokenWithWhitespace.sanitizeToken()

        // Assert
        assertEquals("abc123_-XYZ789", result)
    }

    @Test
    fun `sanitizeToken should throw when result is empty`() {
        // Arrange
        val invalidToken = "!@#$%^&*()"  // Contains only invalid chars

        // Act & Assert
        val exception = assertFailsWith<IllegalArgumentException> {
            invalidToken.sanitizeToken()
        }
        assertEquals("Token cannot be empty after sanitization", exception.message)
    }

    @Test
    fun `sanitizeToken should throw when result is too short`() {
        // Arrange
        val shortToken = "abc123"  // Less than 10 chars

        // Act & Assert
        val exception = assertFailsWith<IllegalArgumentException> {
            shortToken.sanitizeToken()
        }
        assertEquals("Token too short (min 10 chars)", exception.message)
    }

    @Test
    fun `sanitizeToken should throw when result is too long`() {
        // Arrange
        val longToken = "a".repeat(101)  // 101 chars

        // Act & Assert
        val exception = assertFailsWith<IllegalArgumentException> {
            longToken.sanitizeToken()
        }
        assertEquals("Token too long (max 100 chars)", exception.message)
    }

    @Test
    fun `sanitizeDateString should pass valid date format`() {
        // Arrange
        val validDate = "2024-07-31"

        // Act
        val result = validDate.sanitizeDateString()

        // Assert
        assertEquals(validDate, result)
    }

    @Test
    fun `sanitizeDateString should throw on invalid date format`() {
        // Arrange - various invalid formats
        val invalidFormats = listOf(
            "2024/07/31",
            "31-07-2024",
            "2024-7-31",
            "2024-07-3",
            "24-07-31",
            "20240731",
            "2024-07-31 12:00",
            "not-a-date"
        )

        // Act & Assert
        invalidFormats.forEach { invalidDate ->
            val exception = assertFailsWith<IllegalArgumentException> {
                invalidDate.sanitizeDateString()
            }
            assertEquals("Invalid date format. Expected YYYY-MM-DD, got: $invalidDate", exception.message)
        }
    }

    @Test
    fun `validateId should pass non-negative values`() {
        // Arrange
        val validIds = listOf(0L, 1L, 100L, Long.MAX_VALUE)

        // Act & Assert
        validIds.forEach { id ->
            val result = id.validateId()
            assertEquals(id, result)
        }
    }

    @Test
    fun `validateId should throw on negative values`() {
        // Arrange
        val invalidIds = listOf(-1L, -100L, Long.MIN_VALUE)

        // Act & Assert
        invalidIds.forEach { id ->
            val exception = assertFailsWith<IllegalArgumentException> {
                id.validateId()
            }
            assertEquals("id must be non-negative, got: $id", exception.message)
        }
    }

    @Test
    fun `validateId should use custom param name in error message`() {
        // Arrange
        val invalidId = -1L
        val paramName = "customId"

        // Act & Assert
        val exception = assertFailsWith<IllegalArgumentException> {
            invalidId.validateId(paramName)
        }
        assertEquals("$paramName must be non-negative, got: $invalidId", exception.message)
    }

    @Test
    fun `validateYear should pass years in valid range`() {
        // Arrange
        val validYears = listOf(2000, 2024, 2100)

        // Act & Assert
        validYears.forEach { year ->
            val result = year.validateYear()
            assertEquals(year, result)
        }
    }

    @Test
    fun `validateYear should throw on years outside valid range`() {
        // Arrange
        val invalidYears = listOf(1999, 2101, -2024, 0)

        // Act & Assert
        invalidYears.forEach { year ->
            val exception = assertFailsWith<IllegalArgumentException> {
                year.validateYear()
            }
            assertEquals("Invalid year: $year", exception.message)
        }
    }

    @Test
    fun `validateMonth should pass months in valid range`() {
        // Arrange
        val validMonths = listOf(1, 6, 12)

        // Act & Assert
        validMonths.forEach { month ->
            val result = month.validateMonth()
            assertEquals(month, result)
        }
    }

    @Test
    fun `validateMonth should throw on months outside valid range`() {
        // Arrange
        val invalidMonths = listOf(0, 13, -1, 100)

        // Act & Assert
        invalidMonths.forEach { month ->
            val exception = assertFailsWith<IllegalArgumentException> {
                month.validateMonth()
            }
            assertEquals("Invalid month: $month", exception.message)
        }
    }
}
