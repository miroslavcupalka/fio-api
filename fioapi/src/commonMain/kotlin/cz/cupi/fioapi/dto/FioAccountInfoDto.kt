package cz.cupi.fioapi.dto

import cz.cupi.fioapi.utils.FioDateSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
internal data class FioAccountInfoDto(
    val accountId: String,
    val bankId: String,
    val currency: String,
    val iban: String,
    val bic: String,
    val openingBalance: Double,
    val closingBalance: Double,
    @Serializable(with = FioDateSerializer::class)
    val dateStart: LocalDate? = null,
    @Serializable(with = FioDateSerializer::class)
    val dateEnd: LocalDate? = null,
    val yearList: Int? = null,
    val idList: Int? = null,
    val idFrom: Long? = null,
    val idTo: Long? = null,
    val idLastDownload: Long? = null
)