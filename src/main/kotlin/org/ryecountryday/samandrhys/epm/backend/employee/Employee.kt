package org.ryecountryday.samandrhys.epm.backend.employee

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.ryecountryday.samandrhys.epm.backend.PayStrategy
import org.ryecountryday.samandrhys.epm.util.EmployeeSerializer
import org.ryecountryday.samandrhys.epm.util.ValueWithListener
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
    id: String,
    pay: PayStrategy,
    dateOfBirth: Date,
    address: Address
) : Comparable<Employee> {

    @Transient
    private val listeners = mutableListOf<(Employee) -> Unit>()

    constructor(name: String, id: String, pay: PayStrategy, dateOfBirth: Date, address: Address):
            this(name.split(' ', limit = 2)[1], name.split(' ', limit = 2)[0], id, pay, dateOfBirth, address)

    var lastName by ValueWithListener(listeners, lastName, this)
    var firstName by ValueWithListener(listeners, firstName, this)
    val id by ValueWithListener(listeners, id, this)
    var pay by ValueWithListener(listeners, pay, this)
    val dateOfBirth by ValueWithListener(listeners, dateOfBirth, this)
    var address by ValueWithListener(listeners, address, this)

    var status: EmployeeStatus by ValueWithListener(listeners, EmployeeStatus.ACTIVE, this)

    val name: String
        get() = "$firstName $lastName"

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun compareTo(other: Employee): Int {
        if(this.status == EmployeeStatus.ACTIVE && other.status == EmployeeStatus.INACTIVE) return -1
        if(this.status == EmployeeStatus.INACTIVE && other.status == EmployeeStatus.ACTIVE) return 1
        return this.id.compareTo(other.id)
    }

    override fun equals(other: Any?): Boolean {
        return this === other || (other is Employee && this.id == other.id)
    }

    fun addChangeListener(listener: (Employee) -> Unit) {
        listeners.add(listener)
    }

    fun clearChangeListeners() {
        listeners.clear()
    }

    override fun toString(): String {
        return "Employee(id=$id, name=$name, pay=$pay, dateOfBirth=$dateOfBirth, address=$address)"
    }
}