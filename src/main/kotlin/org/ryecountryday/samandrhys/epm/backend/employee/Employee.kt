package org.ryecountryday.samandrhys.epm.backend.employee

import org.ryecountryday.samandrhys.epm.backend.PayStrategy
import java.util.Date

data class Employee(
    var lastName: String,
    var firstName: String,
    val id: String,
    var pay: PayStrategy,
    val dateOfBirth: Date,
    var address: Address
) : Comparable<Employee> {

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun compareTo(other: Employee): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        return this === other || (other is Employee && this.id == other.id)
    }
}