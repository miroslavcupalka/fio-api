package cz.cupi.fioapi.dto

import cz.cupi.fioapi.utils.FioDateColSerializer
import kotlinx.serialization.Serializable

@Serializable
internal data class FioTransactionDto(
	val column22: FioColLong? = null,                // Transaction ID
	@Serializable(with = FioDateColSerializer::class)
	val column0: FioColDate? = null,                 // Transaction date
	val column1: FioColDouble? = null,               // Amount
	val column14: FioColString? = null,              // Currency
	val column2: FioColString? = null,               // Counter account (account number of counterparty)
	val column10: FioColString? = null,              // Counter account name
	val column3: FioColString? = null,               // Counterparty bank code
	val column12: FioColString? = null,              // Counterparty bank name
	val column4: FioColString? = null,               // Constant symbol (KS)
	val column5: FioColString? = null,               // Variable symbol (VS)
	val column6: FioColString? = null,               // Specific symbol (SS)
	val column7: FioColString? = null,               // User identification (user description)
	val column16: FioColString? = null,              // Message for recipient
	val column8: FioColString? = null,               // Transaction type (e.g. "Income", "Expense")
	val column9: FioColString? = null,               // Executed by (user name or automatic)
	val column18: FioColString? = null,              // Specification (type specification)
	val column25: FioColString? = null,              // Comment
	val column26: FioColString? = null,              // Counter account BIC (if exists)
	val column17: FioColLong? = null,                // Instruction ID
	val column27: FioColString? = null               // Payer reference
)
