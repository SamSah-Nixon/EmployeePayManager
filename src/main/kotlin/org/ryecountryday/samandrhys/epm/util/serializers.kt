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