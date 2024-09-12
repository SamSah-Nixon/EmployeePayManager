package org.ryecountryday.samandrhys.epm.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.*
import org.ryecountryday.samandrhys.epm.backend.timing.WorkEntry
import java.time.Instant

object WorkEntrySerializer : KSerializer<WorkEntry> {
    // serialize this as an object with two fields: start and end
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("WorkEntry") {
        element("start", PrimitiveSerialDescriptor("start", PrimitiveKind.LONG))
        element("end", PrimitiveSerialDescriptor("end", PrimitiveKind.LONG))
    }

    override fun serialize(encoder: Encoder, value: WorkEntry) {
        encoder.encodeStructure(descriptor) {
            encodeLongElement(descriptor, 0, value.start.epochSecond)
            encodeLongElement(descriptor, 1, value.end.epochSecond)
        }
    }

    override fun deserialize(decoder: Decoder): WorkEntry {
        var start: Instant? = null
        var end: Instant? = null
        decoder.decodeStructure(descriptor) {
            loop@ while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> start = Instant.ofEpochSecond(decodeLongElement(descriptor, 0))
                    1 -> end = Instant.ofEpochSecond(decodeLongElement(descriptor, 1))
                    CompositeDecoder.DECODE_DONE -> break@loop
                    else -> error("Unexpected index: $index")
                }
            }
        }
        return WorkEntry(start!!, end!!)
    }
}