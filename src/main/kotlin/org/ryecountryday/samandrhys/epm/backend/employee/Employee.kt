/*
 * This file is a part of EmployeePayManager.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.epm.backend.employee

import kotlinx.serialization.Serializable
import org.ryecountryday.samandrhys.epm.backend.PayStrategy
import org.ryecountryday.samandrhys.epm.backend.timing.WorkHistory
import org.ryecountryday.samandrhys.epm.util.EmployeeSerializer
import org.ryecountryday.samandrhys.epm.util.LocalDate
import org.ryecountryday.samandrhys.epm.util.toLocalDate
import org.ryecountryday.samandrhys.epm.util.zeroToNull
import java.time.Instant
import java.time.LocalDate

/**
 * Represents an employee in the company.
 * @property id the employee's unique identifier.
 * @property dateOfBirth the day on which this employee was born. Does not include the time (and time won't be serialized)
 * @property address the employee's address.
 */
@Serializable(with = EmployeeSerializer::class)
open class Employee(
    var lastName: String,
    var firstName: String,
    val id: String,
    var pay: PayStrategy,
    val dateOfBirth: LocalDate,
    var address: Address,
    active: Boolean = true
) : Comparable<Employee> {

    constructor(name: String, id: String, pay: PayStrategy, dateOfBirth: LocalDate, address: Address, active: Boolean = true):
            this(name.split(' ', limit = 2)[1], name.split(' ', limit = 2)[0], id, pay, dateOfBirth, address, active)

    /**
     * Whether this employee is currently active. This doesn't actually make any functional differences except in [compareTo].
     */
    var status = Status.bool(active)

    open val name: String
        get() = "$firstName $lastName"

    override fun hashCode() = id.hashCode()

    override fun compareTo(other: Employee) = Employees.defaultComparator.compare(this, other)

    override fun equals(other: Any?): Boolean {
        return this === other || (other is Employee && this.id == other.id)
    }

    open fun isBirthday(): Boolean {
        return dateOfBirth.dayOfYear == Instant.now().toLocalDate().dayOfYear
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

object Employees {
    object ADMIN : Employee(
        lastName = "Admin",
        firstName = "Admin",
        id = "000100",
        pay = PayStrategy.Salaried(0),
        dateOfBirth = LocalDate(0),
        address = Address(
            street = "3 Five Cedar",
            city = "Rye Land",
            state = "New York",
            zip = "11122-1111"
        ),
    ) {
        override val name = "Admin"

        override fun isBirthday(): Boolean = false

        override fun compareTo(other: Employee): Int {
            return if(other is ADMIN) 0 else -1
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Comparable<T>> comparator(element: (Employee) -> Comparable<T>): Comparator<Employee> {
        return Comparator { e1, e2 -> element(e1).compareTo(element(e2) as T) }
    }

    val adminFirstComparator = Comparator<Employee> { e1, e2 ->
        if(e1 is ADMIN) -1
        else if(e2 is ADMIN) 1
        else 0
    }

    val defaultComparator: Comparator<Employee> = Comparator { e1, e2 ->
        e1.status.compareTo(e2.status).zeroToNull() ?: e1.id.compareTo(e2.id)
    }

    val compareByTimeWorking = comparator { WorkHistory.getClockedInEntry(it.id)?.durationSeconds ?: -1 }
}
