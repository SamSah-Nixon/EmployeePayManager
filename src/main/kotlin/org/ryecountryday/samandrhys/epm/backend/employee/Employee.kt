package org.ryecountryday.samandrhys.epm.backend.employee

import kotlinx.serialization.Serializable
import org.ryecountryday.samandrhys.epm.backend.PayStrategy
import org.ryecountryday.samandrhys.epm.util.DateSerializer
import java.util.Date

@Serializable
data class Employee(
    var lastName: String,
    var firstName: String,
    val id: String,
    var pay: PayStrategy,
    @Serializable(with = DateSerializer::class)
    val dateOfBirth: Date,
    var address: Address
) : Comparable<Employee> {

    constructor(name: String, id: String, pay: PayStrategy, dateOfBirth: Date, address: Address):
            this(name.split(' ', limit = 2)[1], name.split(' ', limit = 2)[0], id, pay, dateOfBirth, address)

    val name: String
        get() = "$firstName $lastName"

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun compareTo(other: Employee): Int {
        return this.id.compareTo(other.id)
    }

    override fun equals(other: Any?): Boolean {
        return this === other || (other is Employee && this.id == other.id)
    }
}