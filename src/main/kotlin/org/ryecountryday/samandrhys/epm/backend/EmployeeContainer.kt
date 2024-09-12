package org.ryecountryday.samandrhys.epm.backend

import kotlinx.serialization.Serializable
import org.ryecountryday.samandrhys.epm.backend.employee.Employee
import java.util.TreeSet
import java.util.function.Predicate

@Serializable
class EmployeeContainer(private val employees: MutableSet<Employee> = TreeSet()) : MutableSet<Employee> by employees {
    constructor(vararg employees: Employee) : this(employees.toMutableSet())

    override fun remove(element: Employee): Boolean {
        throw UnsupportedOperationException("Cannot remove employees")
    }

    override fun removeAll(elements: Collection<Employee>): Boolean {
        throw UnsupportedOperationException("Cannot remove employees")
    }

    override fun removeIf(filter: Predicate<in Employee>): Boolean {
        throw UnsupportedOperationException("Cannot remove employees")
    }

    fun replaceEmployee(employee: Employee) {
        employees.removeIf { it.id == employee.id }
        employees.add(employee)
    }

    fun findById(id: String): Employee? {
        return find { it.id == id }
    }
}