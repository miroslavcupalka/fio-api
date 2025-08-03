package cz.cupi.fioapi.mapper

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import cz.cupi.fioapi.domain.Transaction
import cz.cupi.fioapi.domain.TransactionType
import cz.cupi.fioapi.dto.FioTransactionDto
import cz.cupi.fioapi.dto.FioApiResponse
import kotlinx.datetime.LocalDate

internal fun FioTransactionDto.toDomain(): Transaction = Transaction(
	transactionId = column22?.value ?: 0L,
	date = column0?.value ?: LocalDate(1970, 1, 1),
	amount = BigDecimal.fromDouble(column1?.value ?: 0.0),
	currency = column14?.value ?: "",
	counterAccountNumber = column2?.value,
	counterAccountName = column10?.value,
	counterBankCode = column3?.value,
	counterBankName = column12?.value,
	constantSymbol = column4?.value,
	variableSymbol = column5?.value,
	specificSymbol = column6?.value,
	userIdentification = column7?.value,
	messageForRecipient = column16?.value,
	transactionType = TransactionType.fromString(column8?.value),
	performedBy = column9?.value,
	specification = column18?.value,
	comment = column25?.value,
	bic = column26?.value,
	instructionId = column17?.value,
	payerReference = column27?.value
)

internal fun FioApiResponse.toTransactions(): List<Transaction> =
	accountStatement.transactionList?.transaction?.map { it.toDomain() }.orEmpty()