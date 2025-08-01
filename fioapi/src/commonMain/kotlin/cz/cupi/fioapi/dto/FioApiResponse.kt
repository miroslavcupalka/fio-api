package cz.cupi.fioapi.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class FioApiResponse(
    val accountStatement: FioAccountStatementDto
)