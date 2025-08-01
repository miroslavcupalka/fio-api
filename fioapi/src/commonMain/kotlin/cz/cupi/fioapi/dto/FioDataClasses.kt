package cz.cupi.fioapi.dto

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable


@Serializable
internal data class FioColLong(val value: Long, val name: String, val id: Int)

@Serializable
internal data class FioColDouble(val value: Double, val name: String, val id: Int)

@Serializable
internal data class FioColString(val value: String, val name: String, val id: Int)

@Serializable
internal data class FioColDate(val value: LocalDate, val name: String, val id: Int)

