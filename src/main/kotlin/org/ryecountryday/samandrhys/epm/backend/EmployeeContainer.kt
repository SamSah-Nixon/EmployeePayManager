package org.ryecountryday.samandrhys.epm.backend

import org.ryecountryday.samandrhys.epm.backend.employee.Employee
import java.util.TreeSet
import java.util.function.Predicate

class EmployeeContainer(private val employees: MutableSet<Employee> = TreeSet()) : MutableSet<Employee> by employees {

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
        return employees.find { it.id == id }
    }
}