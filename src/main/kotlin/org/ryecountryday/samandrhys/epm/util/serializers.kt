package org.ryecountryday.samandrhys.epm.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.*
import org.ryecountryday.samandrhys.epm.backend.PayStrategy
import org.ryecountryday.samandrhys.epm.backend.timing.WorkEntry
import java.time.Instant
import java.util.*

object WorkEntrySerializer : KSerializer<WorkEntry> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("WorkEntry") {
        element("start", PrimitiveSerialDescriptor("start", PrimitiveKind.LONG))
        element("end", PrimitiveSerialDescriptor("end", PrimitiveKind.LONG))
        element("id", PrimitiveSerialDescriptor("id", PrimitiveKind.STRING))
    }

    override fun serialize(encoder: Encoder, value: WorkEntry) {
        encoder.encodeStructure(descriptor) {
            encodeLongElement(descriptor, 0, value.start.epochSecond)
            value.end?.let { encodeLongElement(descriptor, 1, it.epochSecond) }
            encodeStringElement(descriptor, 2, value.id)
        }
    }

    override fun deserialize(decoder: Decoder): WorkEntry {
        var start: Instant? = null
        var end: Instant? = null
        var id: String? = null
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> start = Instant.ofEpochSecond(decodeLongElement(descriptor, 0))
                    1 -> end = Instant.ofEpochSecond(decodeLongElement(descriptor, 1))
                    2 -> id = decodeStringElement(descriptor, 2)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
        }
        return WorkEntry(start!!, end, id!!)
    }
}

object DateSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(value.toDateString())
    }

    override fun deserialize(decoder: Decoder): Date {
        return parseDate(decoder.decodeString())
    }
}

object PayStrategySerializer : KSerializer<PayStrategy> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("PayStrategy") {
        element("type", descriptor = PrimitiveSerialDescriptor("type", PrimitiveKind.STRING))
        element("rate", descriptor = PrimitiveSerialDescriptor("rate", PrimitiveKind.DOUBLE))
    }

    override fun serialize(encoder: Encoder, value: PayStrategy) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type)
            encodeDoubleElement(descriptor, 1, value.rate)
        }
    }

    override fun deserialize(decoder: Decoder): PayStrategy {
        var type: String? = null
        var rate: Double? = null
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> type = decodeStringElement(descriptor, 0)
                    1 -> rate = decodeDoubleElement(descriptor, 1)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
        }
        return when (type) {
            "Hourly" -> PayStrategy.Hourly(rate!!)
            "Salaried" -> PayStrategy.Salaried(rate!!)
            else -> error("Unknown PayStrategy type: $type")
        }
    }
}