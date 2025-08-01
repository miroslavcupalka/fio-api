package cz.cupi.fioapi.utils

import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// Serializer for standalone date (dateStart, dateEnd)
internal object FioDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("FioDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        val dateString = decoder.decodeString()
        // Proper removal of timezone offset (e.g. "2025-07-16+0200" -> "2025-07-16")
        val dateOnly = if (dateString.contains('+')) {
            dateString.substringBefore('+')
        } else if (dateString.contains('-') && dateString.length > 10) {
            // If it contains minus and is longer than standard date (YYYY-MM-DD = 10 characters)
            // then it might be timezone with minus (e.g. "2025-07-16-0500")
            dateString.substring(0, 10)
        } else {
            dateString
        }
        return LocalDate.Companion.parse(dateOnly)
    }
}