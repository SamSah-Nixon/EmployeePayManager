package org.ryecountryday.samandrhys.epm.backend.employee

class Address(
    val street: String,
    val city: String,
    val state: String,
    val zip: String
) {
    override fun toString(): String {
        return "$street\n$city, $state $zip"
    }
}