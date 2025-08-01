package cz.cupi.fioapi.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class FioAccountStatementDto(
    val info: FioAccountInfoDto,
    val transactionList: FioTransactionListDto? = null
)
