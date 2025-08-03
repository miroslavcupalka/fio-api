package cz.cupi.fioapi.api

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FioApiImplTest {

    private val validToken = "abcdefghij1234567890"
    private val testFromDate = LocalDate(2024, 1, 1)
    private val testToDate = LocalDate(2024, 1, 31)
    private val testYear = 2024
    private val testMonth = 7
    private val testFromId = 123456L
    private val testToId = 123789L

    // Sample response data
    private val sampleEmptyResponse = """
        {
            "accountStatement": {
                "info": {
                    "accountId": "2000000000",
                    "bankId": "2010",
                    "currency": "CZK",
                    "iban": "CZ0000000000000000000000",
                    "bic": "FIOBCZPPXXX",
                    "openingBalance": 1000.0,
                    "closingBalance": 1500.0,
                    "dateStart": "2024-01-01",
                    "dateEnd": "2024-01-31",
                    "yearList": null,
                    "idList": null,
                    "idFrom": null,
                    "idTo": null
                },
                "transactionList": {
                    "transaction": []
                }
            }
        }
    """.trimIndent()

    private val sampleTransactionResponse = """
        {
            "accountStatement": {
                "info": {
                    "accountId": "2000000000",
                    "bankId": "2010",
                    "currency": "CZK",
                    "iban": "CZ0000000000000000000000",
                    "bic": "FIOBCZPPXXX",
                    "openingBalance": 1000.0,
                    "closingBalance": 1500.0,
                    "dateStart": "2024-01-01",
                    "dateEnd": "2024-01-31",
                    "yearList": null,
                    "idList": null,
                    "idFrom": null,
                    "idTo": null
                },
                "transactionList": {
                    "transaction": [
                        {
                            "column22": {"value": 1234567, "name": "ID pohybu", "id": 22},
                            "column0": {"value": "2024-01-15", "name": "Datum", "id": 0},
                            "column1": {"value": 500.0, "name": "Objem", "id": 1},
                            "column14": {"value": "CZK", "name": "Měna", "id": 14},
                            "column8": {"value": "Test transaction", "name": "Typ", "id": 8},
                            "column2": {"value": "123456789", "name": "Protiúčet", "id": 2},
                            "column10": {"value": "Test bank", "name": "Název banky", "id": 10},
                            "column3": {"value": "PAYMENT", "name": "Kód banky", "id": 3}
                        }
                    ]
                }
            }
        }
    """.trimIndent()

    /**
     * Creates a mock HTTP client that returns a predefined response
     */
    private fun createMockClient(
        responseContent: String,
        statusCode: HttpStatusCode = HttpStatusCode.OK,
        requestValidator: (HttpRequestData) -> Unit = {}
    ): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    requestValidator(request)
                    respond(
                        content = responseContent,
                        status = statusCode,
                        headers = headersOf("Content-Type", ContentType.Application.Json.toString())
                    )
                }
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    @Test
    fun `constructor should sanitize token`() = runTest {
        // Arrange
        val dirtyToken = "abc_123456789!@#$%^&*()"
        val expectedSanitizedToken = "abc_123456789"
        var capturedUrl = ""

        val mockClient = createMockClient(sampleEmptyResponse) { request ->
            capturedUrl = request.url.toString()
        }

        // Act
        val fioApi = FioApiImpl(dirtyToken, mockClient)
        fioApi.getLastTransactions() // Make any API call to check the token

        // Assert
        assertTrue(
            capturedUrl.contains(expectedSanitizedToken), 
            "URL should contain sanitized token. URL: $capturedUrl, Expected token: $expectedSanitizedToken"
        )
    }

    @Test
    fun `constructor should throw on invalid token`() {
        // Arrange
        val shortToken = "short"

        // Act & Assert
        assertFailsWith<IllegalArgumentException> {
            FioApiImpl(shortToken)
        }
    }

    @Test
    fun `getTransactions should use correct URL format`() = runTest {
        // Arrange
        var capturedUrl = ""
        val mockClient = createMockClient(sampleEmptyResponse) { request ->
            capturedUrl = request.url.toString()
        }

        val fioApi = FioApiImpl(validToken, mockClient)

        // Act
        fioApi.getTransactions(testFromDate, testToDate)

        // Assert
        val expectedUrlPart = "/periods/$validToken/$testFromDate/$testToDate/transactions.json"
        assertTrue(
            capturedUrl.contains(expectedUrlPart),
            "URL should match expected format. URL: $capturedUrl, Expected: $expectedUrlPart"
        )
    }

    @Test
    fun `getLastTransactions should use correct URL format`() = runTest {
        // Arrange
        var capturedUrl = ""
        val mockClient = createMockClient(sampleEmptyResponse) { request ->
            capturedUrl = request.url.toString()
        }

        val fioApi = FioApiImpl(validToken, mockClient)

        // Act
        fioApi.getLastTransactions()

        // Assert
        val expectedUrlPart = "/last/$validToken/transactions.json"
        assertTrue(
            capturedUrl.contains(expectedUrlPart),
            "URL should match expected format. URL: $capturedUrl, Expected: $expectedUrlPart"
        )
    }

    @Test
    fun `getTransactionsByIdRange should use correct URL format`() = runTest {
        // Arrange
        var capturedUrl = ""
        val mockClient = createMockClient(sampleEmptyResponse) { request ->
            capturedUrl = request.url.toString()
        }

        val fioApi = FioApiImpl(validToken, mockClient)

        // Act
        fioApi.getTransactionsByIdRange(testFromId, testToId)

        // Assert
        val expectedUrlPart = "/by-id/$validToken/$testFromId/$testToId/transactions.json"
        assertTrue(
            capturedUrl.contains(expectedUrlPart),
            "URL should match expected format. URL: $capturedUrl, Expected: $expectedUrlPart"
        )
    }

    @Test
    fun `getTransactionsFromId should use correct URL format`() = runTest {
        // Arrange
        var capturedUrl = ""
        val mockClient = createMockClient(sampleEmptyResponse) { request ->
            capturedUrl = request.url.toString()
        }

        val fioApi = FioApiImpl(validToken, mockClient)

        // Act
        fioApi.getTransactionsFromId(testFromId)

        // Assert
        val expectedUrlPart = "/set-last-id/$validToken/$testFromId/transactions.json"
        assertTrue(
            capturedUrl.contains(expectedUrlPart),
            "URL should match expected format. URL: $capturedUrl, Expected: $expectedUrlPart"
        )
    }

    @Test
    fun `getTransactionsByMonth should use correct URL format`() = runTest {
        // Arrange
        var capturedUrl = ""
        val mockClient = createMockClient(sampleEmptyResponse) { request ->
            capturedUrl = request.url.toString()
        }

        val fioApi = FioApiImpl(validToken, mockClient)

        // Act
        fioApi.getTransactionsByMonth(testYear, testMonth)

        // Assert
        val expectedUrlPart = "/by-month/$validToken/$testYear/$testMonth/transactions.json"
        assertTrue(
            capturedUrl.contains(expectedUrlPart),
            "URL should match expected format. URL: $capturedUrl, Expected: $expectedUrlPart"
        )
    }

    @Test
    fun `getAccountInfo should return parsed account info`() = runTest {
        // Arrange
        val mockClient = createMockClient(sampleTransactionResponse)
        val fioApi = FioApiImpl(validToken, mockClient)

        // Act
        val result = fioApi.getAccountInfo()

        // Assert
        assertEquals("2000000000", result.accountId)
        assertEquals("2010", result.bankId)
        assertEquals("CZK", result.currency)
        assertEquals("CZ0000000000000000000000", result.iban)
    }

    @Test
    fun `getTransactions should return parsed transactions`() = runTest {
        // Arrange
        val mockClient = createMockClient(sampleTransactionResponse)
        val fioApi = FioApiImpl(validToken, mockClient)

        // Act
        val result = fioApi.getTransactions(testFromDate, testToDate)

        // Assert
        assertEquals(1, result.size)
        with(result[0]) {
            assertEquals(1234567, transactionId)
            assertEquals(LocalDate(2024, 1, 15), date)
            assertEquals(BigDecimal.fromDouble(500.0), amount)
            assertEquals("CZK", currency)
            assertEquals("123456789", counterAccountNumber)
        }
    }

    @Test
    fun `API should throw NotFound on 404 response`() = runTest {
        // Arrange
        val mockClient = createMockClient("", HttpStatusCode.NotFound)
        val fioApi = FioApiImpl(validToken, mockClient)

        // Act & Assert
        assertFailsWith<FioApiException.NotFound> {
            fioApi.getTransactions(testFromDate, testToDate)
        }
    }

    @Test
    fun `API should throw Unauthorized on 401 response`() = runTest {
        // Arrange
        val mockClient = createMockClient("", HttpStatusCode.Unauthorized)
        val fioApi = FioApiImpl(validToken, mockClient)

        // Act & Assert
        assertFailsWith<FioApiException.Unauthorized> {
            fioApi.getTransactions(testFromDate, testToDate)
        }
    }

    @Test
    fun `API should throw RateLimit on 429 response`() = runTest {
        // Arrange
        val mockClient = createMockClient("", HttpStatusCode.TooManyRequests)
        val fioApi = FioApiImpl(validToken, mockClient)

        // Act & Assert
        assertFailsWith<FioApiException.RateLimit> {
            fioApi.getTransactions(testFromDate, testToDate)
        }
    }
}