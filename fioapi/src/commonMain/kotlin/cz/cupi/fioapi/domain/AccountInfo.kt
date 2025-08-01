package cz.cupi.fioapi.domain

import kotlinx.datetime.LocalDate

data class AccountInfo(
    val accountId: String,
    val bankId: String,
    val currency: String,
    val iban: String,
    val bic: String,
    val openingBalance: Double,
    val closingBalance: Double,
    val dateStart: LocalDate? = null,
    val dateEnd: LocalDate? = null,
    val yearList: Int? = null,
    val idList: Int? = null,
    val idFrom: Long? = null,
    val idTo: Long? = null,
    val idLastDownload: Long? = null
)