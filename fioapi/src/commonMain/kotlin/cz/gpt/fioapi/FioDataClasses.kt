package cz.gpt.fioapi

import kotlinx.serialization.Serializable

@Serializable
data class FioTransactionResponse(
    val accountStatement: FioAccountStatement
)

@Serializable
data class FioAccountStatement(
    val info: FioAccountInfo,
    val transactionList: FioTransactionList? = null
)

@Serializable
data class FioAccountInfo(
    val accountId: String,
    val bankId: String,
    val currency: String,
    val iban: String,
    val bic: String,
    val openingBalance: Double,
    val closingBalance: Double,
    val dateStart: Long? = null,
    val dateEnd: Long? = null,
    val yearList: Int? = null,
    val idList: Int? = null,
    val idFrom: Long? = null,
    val idTo: Long? = null,
    val idLastDownload: Long? = null
)

@Serializable
data class FioTransactionList(
    val transaction: List<FioTransaction>? = null
)

@Serializable
data class FioTransaction(
    val column22: FioColLong? = null,
    val column0: FioColLong? = null,
    val column1: FioColDouble? = null,
    val column14: FioColString? = null,
    val column2: FioColString? = null,
    val column10: FioColString? = null,
    val column3: FioColString? = null,
    val column12: FioColString? = null,
    val column4: FioColString? = null,
    val column5: FioColString? = null,
    val column6: FioColString? = null,
    val column7: FioColString? = null,
    val column16: FioColString? = null,
    val column8: FioColString? = null,
    val column9: FioColString? = null,
    val column18: FioColString? = null,
    val column25: FioColString? = null,
    val column26: FioColString? = null,
    val column17: FioColLong? = null,
    val column27: FioColString? = null
)

@Serializable
data class FioColLong(val value: Long, val name: String, val id: Int)
@Serializable
data class FioColDouble(val value: Double, val name: String, val id: Int)
@Serializable
data class FioColString(val value: String, val name: String, val id: Int)