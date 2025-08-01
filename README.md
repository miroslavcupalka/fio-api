# FIO API Multiplatform Library (Kotlin)

Kotlin Multiplatform library for accessing FIO Bank API. Supports Android, iOS, JVM and JS projects.

## Installation

## Quick Start

### 1. Getting API Token
1. Log in to FIO internet banking
2. Go to Settings → API
3. Generate new token for reading

### 2. Basic Usage

```kotlin
fun main() = runBlocking {
    val token = "your-fio-api-token"
    val fioApi: FioApi = FioApiImpl(token)
    
    try {
        // Get transactions for the last 30 days
        val dateFrom = LocalDate(2024, 1, 1)
        val dateTo = LocalDate(2024, 1, 31)
        val transactions = fioApi.getTransactions(dateFrom, dateTo)
        
        transactions.forEach { transaction ->
            println("${transaction.date}: ${transaction.amount} ${transaction.currency}")
        }
    } catch (e: FioApiException) {
        println("Error: ${e.message}")
    }
}
```

## API Methods

### Getting Transactions

#### `getTransactions(dateFrom: LocalDate, dateTo: LocalDate): List<Transaction>`
Gets transactions in the specified period.

```kotlin
val transactions = fioApi.getTransactions(
    LocalDate(2024, 1, 1), 
    LocalDate(2024, 1, 31)
)
```

#### `getLastTransactions(): List<Transaction>`
Gets transactions from the last download (uses internal FIO pointer).

```kotlin
val newTransactions = fioApi.getLastTransactions()
```

#### `getTransactionsByIdRange(fromId: Long, toId: Long): List<Transaction>`
Gets transactions by ID range.

```kotlin
val transactions = fioApi.getTransactionsByIdRange(123456, 123789)
```

#### `getTransactionsFromId(fromId: Long): List<Transaction>`
Sets pointer to given ID and returns newer transactions.

```kotlin
val newerTransactions = fioApi.getTransactionsFromId(123456)
```

#### `getTransactionsByMonth(year: Int, month: Int): List<Transaction>`
Gets transactions for specific month (1=January, 12=December).

```kotlin
val transactions = fioApi.getTransactionsByMonth(2024, 7) // July 2024
```

### Account Information

#### `getAccountInfo(): AccountInfo`
Gets basic account information.

```kotlin
val accountInfo = fioApi.getAccountInfo()
println("Account number: ${accountInfo.accountNumber}")
println("Balance: ${accountInfo.balance} ${accountInfo.currency}")
```

#### `getAccountStatement(dateFrom: LocalDate, dateTo: LocalDate): AccountStatement`
Gets complete account statement including transactions.

```kotlin
val statement = fioApi.getAccountStatement(
    LocalDate(2024, 1, 1), 
    LocalDate(2024, 1, 31)
)
```

#### `getLastAccountStatement(): AccountStatement`
Gets last account statement.

```kotlin
val lastStatement = fioApi.getLastAccountStatement()
```

## Data Structures

### Transaction
```kotlin
data class Transaction(
    val transactionId: Long,
    val date: LocalDate,
    val amount: Double,
    val currency: String,
    val counterAccountNumber: String?,
    val counterAccountName: String?,
    val counterBankCode: String?,
    val counterBankName: String?,
    val constantSymbol: String?,
    val variableSymbol: String?,
    val specificSymbol: String?,
    val userIdentification: String?,
    val messageForRecipient: String?,
    val transactionType: TransactionType?,
    val performedBy: String?,
    val specification: String?,
    val comment: String?,
    val bic: String?,
    val instructionId: Long?,
    val payerReference: String?
)
```

### AccountInfo
```kotlin
data class AccountInfo(
    val accountNumber: String,
    val bankCode: String,
    val currency: String,
    val iban: String?,
    val bic: String?,
    val balance: Double,
    val dateStart: LocalDate?,
    val dateEnd: LocalDate?
)
```

## Error Handling

The library uses sealed class `FioApiException` for different types of errors:

### Basic Error Handling
```kotlin
try {
    val transactions = fioApi.getLastTransactions()
} catch (e: FioApiException.Unauthorized) {
    println("Invalid or expired token")
} catch (e: FioApiException.RateLimit) {
    println("Request limit exceeded - try again later")
} catch (e: FioApiException.NotFound) {
    println("No transactions found")
} catch (e: FioApiException.Network) {
    println("Network problem: ${e.message}")
} catch (e: FioApiException) {
    println("General API error: ${e.message}")
}
```

### Exception Types

| Exception | Description | Solution |
|---------|-------|--------|
| `FioApiException.Unauthorized` | Invalid or expired token | Check token in FIO settings |
| `FioApiException.RateLimit` | Request limit exceeded | Wait and try again later |
| `FioApiException.NotFound` | Resource not found | Check request parameters |
| `FioApiException.Network` | Network error | Check internet connection |
| `FioApiException.Parse` | Response parsing error | Contact support |
| `FioApiException.InvalidRequest` | Invalid request | Check parameters |
| `FioApiException.Conflict` | Request conflict | Possible duplicate download |
| `FioApiException.Business` | Business error from FIO API | Check account status |

### Parameter Validation
```kotlin
try {
    // Invalid ID throws IllegalArgumentException
    val transactions = fioApi.getTransactionsByIdRange(-1, 100)
} catch (e: IllegalArgumentException) {
    println("Invalid parameters: ${e.message}")
}

try {
    // Invalid month throws IllegalArgumentException  
    val transactions = fioApi.getTransactionsByMonth(2024, 13)
} catch (e: IllegalArgumentException) {
    println("Invalid month: ${e.message}")
}
```

## Advanced Usage

### Custom HTTP Client
```kotlin
val customHttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json()
    }
    install(Logging) {
        level = LogLevel.INFO
    }
}

val fioApi = FioApiImpl(
    token = "your-token",
    httpClient = customHttpClient,
    debugLog = true
)
```

### Token Security
```kotlin
// Token is automatically sanitized for safe use in URL
val token = System.getenv("FIO_API_TOKEN") ?: error("Set FIO_API_TOKEN")
val fioApi = FioApiImpl(token)
```

### Working with Data
```kotlin
val transactions = fioApi.getTransactions(dateFrom, dateTo)

// Filter incoming payments
val incomingPayments = transactions.filter { it.amount > 0 }

// Group by month
val byMonth = transactions.groupBy { "${it.date.year}-${it.date.monthNumber}" }

// Sum for period
val totalAmount = transactions.sumOf { it.amount }
```

## Common Problems and Solutions

### 1. "Invalid or unauthorized token"
- **Cause**: Invalid, expired or incorrectly entered token
- **Solution**: Generate new token in FIO internet banking

### 2. "Too many requests (rate limited)"
- **Cause**: API request limit exceeded (max 1 request per 30 seconds)
- **Solution**: Wait at least 30 seconds between requests

### 3. "Resource not found"
- **Cause**: No transactions in specified period or invalid parameters
- **Solution**: Check date or transaction ID

### 4. "Request conflict (possible repeated fetch)"
- **Cause**: Repeated download of same data
- **Solution**: Use `getLastTransactions()` for new transactions

### 5. Empty transaction list
- **Cause**: No transactions in period or all already downloaded
- **Solution**: Check period or use different endpoint

### 6. Data parsing error
- **Cause**: Unexpected response format from FIO API
- **Solution**: Check library version or contact support

## Usage Examples

### Monitoring New Payments
```kotlin
suspend fun monitorNewPayments(fioApi: FioApi) {
    while (true) {
        try {
            val newTransactions = fioApi.getLastTransactions()
            newTransactions.forEach { transaction ->
                if (transaction.amount > 0) {
                    println("New incoming payment: ${transaction.amount} ${transaction.currency}")
                    // Process payment...
                }
            }
            delay(30_000) // Wait 30 seconds (rate limit)
        } catch (e: FioApiException.RateLimit) {
            delay(60_000) // Wait a minute on rate limit
        } catch (e: FioApiException) {
            println("Error during monitoring: ${e.message}")
            delay(60_000)
        }
    }
}
```

### Export to CSV
```kotlin
fun exportTransactionsToCsv(transactions: List<Transaction>, filename: String) {
    File(filename).printWriter().use { out ->
        out.println("Date,Amount,Currency,Counter Account,Description")
        transactions.forEach { t ->
            out.println("${t.date},${t.amount},${t.currency},${t.counterAccountNumber ?: ""},${t.specification ?: ""}")
        }
    }
}
```

### Expense Analysis
```kotlin
fun analyzeExpenses(transactions: List<Transaction>) {
    val expenses = transactions.filter { it.amount < 0 }
    val totalExpenses = expenses.sumOf { -it.amount }
    val avgExpense = totalExpenses / expenses.size
    
    println("Total expenses: $totalExpenses")
    println("Average expense: $avgExpense")
    println("Number of transactions: ${expenses.size}")
}
```

## Running Example

1. Set environment variable:
```bash
export FIO_API_TOKEN="your-fio-api-token"
```

2. Run example:
```bash
./gradlew :example:run
```

## Multiplatform Support

The library supports all Kotlin Multiplatform targets:
- **JVM** - Java applications, Android
- **JS** - Node.js, browser  
- **Native** - iOS, macOS, Linux, Windows

### Android

### iOS (Kotlin/Native)
```kotlin
// commonMain
expect fun getPlatformName(): String

// iosMain  
actual fun getPlatformName(): String = "iOS"
```

## License

MIT License - see LICENSE file for details.