package cz.cupi.fioapi.mapper

import cz.cupi.fioapi.domain.AccountStatement
import cz.cupi.fioapi.dto.FioApiResponse

internal fun FioApiResponse.toAccountStatement(): AccountStatement =
	AccountStatement(
		accountInfo = accountStatement.info.toDomain(),
		transactions = accountStatement.transactionList?.transaction?.map { it.toDomain() }.orEmpty()
	)
