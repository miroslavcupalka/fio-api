package cz.cupi.fioexample

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.RoundingMode

/**
 * Formats a nullable BigDecimal to a string representation with two decimal places
 */
fun BigDecimal?.formatAmount(): String =
	this?.roundToDigitPositionAfterDecimalPoint(2, RoundingMode.ROUND_HALF_AWAY_FROM_ZERO)?.toPlainString() ?: ""
