package org.ryecountryday.samandrhys.epm.backend.employee

import kotlinx.serialization.Serializable
import org.ryecountryday.samandrhys.epm.backend.PayStrategy
import org.ryecountryday.samandrhys.epm.util.EmployeeSerializer
import org.ryecountryday.samandrhys.epm.util.HasListener
import java.util.*

/**
 * Represents an employee in the company.
 * @property id the employee's unique identifier.
 * @property dateOfBirth the day on which this employee was born. Does not include the time (and time won't be serialized)
 */
@Serializable(with = EmployeeSerializer::class)
class Employee(
    lastName: String,
    firstName: String,
    val id: String,
    pay: PayStrategy,
    val dateOfBirth: Date,
    address: Address,
    active: Boolean = true
) : Comparable<Employee>, HasListener<Employee>() {

    constructor(name: String, id: String, pay: PayStrategy, dateOfBirth: Date, address: Address, active: Boolean = true):
            this(name.split(' ', limit = 2)[1], name.split(' ', limit = 2)[0], id, pay, dateOfBirth, address, active)

    var lastName by value(lastName)
    var firstName by value(firstName)
    var pay by value(pay)
    var address by value(address)
    var status by value(Status.bool(active))

    val name: String
        get() = "$firstName $lastName"

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun compareTo(other: Employee): Int {
        if(this.status == Status.ACTIVE && other.status == Status.INACTIVE) return -1
        if(this.status == Status.INACTIVE && other.status == Status.ACTIVE) return 1
        return this.id.compareTo(other.id)
    }

    override fun equals(other: Any?): Boolean {
        return this === other || (other is Employee && this.id == other.id)
    }

    override fun toString(): String {
        return "Employee(id=$id, name=$name, pay=$pay, dateOfBirth=$dateOfBirth, address=$address)"
    }

    enum class Status {
        ACTIVE,
        INACTIVE;

        operator fun not(): Status {
            return if(this == ACTIVE) INACTIVE else ACTIVE
        }

        fun toBoolean(): Boolean {
            return this == ACTIVE
        }

        companion object {
            fun bool(b: Boolean): Status {
                return if(b) ACTIVE else INACTIVE
            }
        }
    }
}