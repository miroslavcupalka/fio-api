package cz.cupi.fioapi.utils

import cz.cupi.fioapi.dto.FioColDate
import cz.cupi.fioapi.dto.FioColString
import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// Serializer pro FioColDate objekty
internal object FioDateColSerializer : KSerializer<FioColDate> {
    override val descriptor: SerialDescriptor = FioColDate.serializer().descriptor

    override fun serialize(encoder: Encoder, value: FioColDate) {
        // Pro serialize necháme původní implementaci
        FioColDate.serializer().serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): FioColDate {
        // Dočasně deserializujeme jako FioColString a pak konvertujeme
        val temp = FioColString.serializer().deserialize(decoder)
        // Použijeme stejnou logiku jako v FioDateSerializer
        val dateOnly = if (temp.value.contains('+')) {
            temp.value.substringBefore('+')
        } else if (temp.value.contains('-') && temp.value.length > 10) {
            temp.value.substring(0, 10)
        } else {
            temp.value
        }
        val localDate = LocalDate.Companion.parse(dateOnly)
        return FioColDate(localDate, temp.name, temp.id)
    }
}