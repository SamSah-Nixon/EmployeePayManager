package org.ryecountryday.samandrhys.epm.backend.employee

import org.ryecountryday.samandrhys.epm.backend.PayStrategy
import org.ryecountryday.samandrhys.epm.backend.timing.WorkHistory
import java.util.Date

data class Employee(
    var name: String,
    val id: String,
    var pay: PayStrategy,
    val birthday: Date,
    var address: Address
) : Comparable<Employee> {
    val workHistory = WorkHistory()

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return this === other || (other is Employee && this.id == other.id)
    }

    override fun compareTo(other: Employee): Int {
        return this.id.compareTo(other.id)
    }
}