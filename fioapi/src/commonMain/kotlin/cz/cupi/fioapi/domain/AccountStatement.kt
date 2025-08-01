package cz.cupi.fioapi.domain

data class AccountStatement(
	val accountInfo: AccountInfo,
	val transactions: List<Transaction>
)
