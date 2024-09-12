package org.ryecountryday.samandrhys.epm.backend.employee

import kotlinx.serialization.Serializable

/**
 * Represents an address for a building in the United States.
 */
@Serializable
data class Address(
    var street: String,
    var city: String,
    var state: String,
    var zip: String
) {
    override fun toString(): String {
        return "$street; $city, $state $zip"
    }

    fun toStringMultiline(): String {
        return "$street\n$city, $state\n$zip"
    }
}