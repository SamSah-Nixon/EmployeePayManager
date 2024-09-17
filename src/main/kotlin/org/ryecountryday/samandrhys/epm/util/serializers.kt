package org.ryecountryday.samandrhys.epm.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import org.ryecountryday.samandrhys.epm.backend.PayStrategy
import org.ryecountryday.samandrhys.epm.backend.employee.Address
import org.ryecountryday.samandrhys.epm.backend.employee.Employee
import org.ryecountryday.samandrhys.epm.backend.timing.WorkEntry
import java.time.Instant
import java.time.LocalDate

// Serializers for different objects using kotlinx.serialization
// I'll document the first one, the rest are similar

object WorkEntrySerializer : KSerializer<WorkEntry> {

    /**
     * The descriptor for the WorkEntry class. A descriptor describes (duh) the structure of
     * the serialized form of the class (which is all JSON in this case).
     */
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("WorkEntry") {
        element<Long>("start")
        element<Long?>("end", isOptional = true)
        element<String>("id")
    }

    /**
     * Adds the WorkEntry object to the encoder. This is called when the object is being serialized.
     * The encoder is used to write the object to an output stream.
     * @param encoder The encoder to write the object to
     * @param value The WorkEntry object to serialize
     */
    override fun serialize(encoder: Encoder, value: WorkEntry) {
        encoder.encodeStructure(descriptor) { // Start a new structure (json object)
            encodeLongElement(descriptor, 0, value.start.epochSecond) // Write the start time as a long - index 0 corresponds to "start" in the descriptor above
            if(value.end != null) { // If the end time exists, write it too
                encodeLongElement(descriptor, 1, value.end!!.epochSecond)
            }
            encodeStringElement(descriptor, 2, value.id)
        }
    }

    /**
     * Deserializes a WorkEntry object from the decoder. This is called when the object is being deserialized.
     * The decoder is used to read the object from an input stream.
     * @param decoder The decoder to read the object from
     * @return The WorkEntry object that was deserialized
     */
    override fun deserialize(decoder: Decoder): WorkEntry {
        var start: Instant? = null
        var end: Instant? = null
        var id: String? = null
        decoder.decodeStructure(descriptor) { // Start reading the json object
            while (true) {
                when (val index = decodeElementIndex(descriptor)) { // Get the index of the next element

                    // Depending on the index we're on, turn the correct element from the descriptor into the right object
                    0 -> start = Instant.ofEpochSecond(decodeLongElement(descriptor, 0))
                    1 -> end = Instant.ofEpochSecond(decodeLongElement(descriptor, 1))
                    2 -> id = decodeStringElement(descriptor, 2)
                    CompositeDecoder.DECODE_DONE -> break // if there's no more data, exit the loop
                    else -> error("Unexpected index: $index")
                }
            }
        }

        // Return the WorkEntry object that was deserialized
        // end can be null since it's optionally serialized
        return WorkEntry(start!!, end, id!!)
    }
}

object PayStrategySerializer : KSerializer<PayStrategy> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("PayStrategy") {
        element<String>("type")
        element<Double>("rate")
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
            PayStrategy.Hourly.TYPE -> PayStrategy.Hourly(rate!!)
            PayStrategy.Salaried.TYPE -> PayStrategy.Salaried(rate!!)
            else -> error("Could not find a PayStrategy class for type: \"$type\"")
        }
    }
}

object EmployeeSerializer : KSerializer<Employee> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Employee") {
        element<String>("lastName")
        element<String>("firstName")
        element<String>("id")
        element<PayStrategy>("pay")
        element<String>("dateOfBirth")
        element<Address>("address")
        element<Boolean>("active")
    }

    override fun serialize(encoder: Encoder, value: Employee) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.lastName)
            encodeStringElement(descriptor, 1, value.firstName)
            encodeStringElement(descriptor, 2, value.id)
            encodeSerializableElement(descriptor, 3, PayStrategySerializer, value.pay)
            encodeStringElement(descriptor, 4, value.dateOfBirth.toDateString())
            encodeSerializableElement(descriptor, 5, Address.serializer(), value.address)
            encodeBooleanElement(descriptor, 6, value.status == Employee.Status.ACTIVE)
        }
    }

    override fun deserialize(decoder: Decoder): Employee {
        var lastName: String? = null
        var firstName: String? = null
        var id: String? = null
        var pay: PayStrategy? = null
        var dateOfBirth: LocalDate? = null
        var address: Address? = null
        var active: Boolean? = null
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> lastName = decodeStringElement(descriptor, 0)
                    1 -> firstName = decodeStringElement(descriptor, 1)
                    2 -> id = decodeStringElement(descriptor, 2)
                    3 -> pay = decodeSerializableElement(descriptor, 3, PayStrategySerializer)
                    4 -> dateOfBirth = parseDate(decodeStringElement(descriptor, 4))
                    5 -> address = decodeSerializableElement(descriptor, 5, Address.serializer())
                    6 -> active = decodeBooleanElement(descriptor, 6)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
        }

        return Employee(lastName!!, firstName!!, id!!, pay!!, dateOfBirth!!, address!!, active == true)
    }
}