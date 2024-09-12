package org.ryecountryday.samandrhys.epm.backend.employee

data class Address(
    val street: String,
    val city: String,
    val state: String,
    val zip: String
) {
    override fun toString(): String {
        return "$street; $city, $state $zip"
    }

    fun toStringMultiline(): String {
        return "$street\n$city, $state\n$zip"
    }
}