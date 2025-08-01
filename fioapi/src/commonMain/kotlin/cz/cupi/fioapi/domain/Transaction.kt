package cz.cupi.fioapi.domain

import kotlinx.datetime.LocalDate

data class Transaction(
	val transactionId: Long,             // column22
	val date: LocalDate,                 // column0
	val amount: Double,                  // column1
	val currency: String,                // column14
	val counterAccountNumber: String?,   // column2
	val counterAccountName: String?,     // column10
	val counterBankCode: String?,        // column3
	val counterBankName: String?,        // column12
	val constantSymbol: String?,         // column4
	val variableSymbol: String?,         // column5
	val specificSymbol: String?,         // column6
	val userIdentification: String?,     // column7
	val messageForRecipient: String?,    // column16
	val transactionType: TransactionType?, // column8
	val performedBy: String?,            // column9
	val specification: String?,          // column18
	val comment: String?,                // column25
	val bic: String?,                    // column26
	val instructionId: Long?,            // column17
	val payerReference: String?          // column27
)
