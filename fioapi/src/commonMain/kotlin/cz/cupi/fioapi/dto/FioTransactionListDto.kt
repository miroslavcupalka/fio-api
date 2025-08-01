package cz.cupi.fioapi.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class FioTransactionListDto(
    val transaction: List<FioTransactionDto>? = null
)