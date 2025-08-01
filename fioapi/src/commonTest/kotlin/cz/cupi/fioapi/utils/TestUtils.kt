package cz.cupi.fioapi.utils

import kotlinx.datetime.LocalDate

/**
 * Utilities for testing.
 */
internal object TestUtils {
    /**
     * Sample valid token for testing.
     */
    const val SAMPLE_TOKEN = "abcdefghij1234567890"

    /**
     * Sample start date for testing.
     */
    val SAMPLE_FROM_DATE = LocalDate(2024, 1, 1)

    /**
     * Sample end date for testing.
     */
    val SAMPLE_TO_DATE = LocalDate(2024, 1, 31)
}
