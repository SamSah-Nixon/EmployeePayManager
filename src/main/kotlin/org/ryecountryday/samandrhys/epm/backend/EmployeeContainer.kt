package org.ryecountryday.samandrhys.epm.backend

import org.ryecountryday.samandrhys.epm.backend.employee.Employee
import java.util.TreeSet

class EmployeeContainer(private val employees: MutableSet<Employee> = TreeSet()) : Iterable<Employee> by employees {

    val size
        get() = employees.size

    fun addEmployee(employee: Employee) {
        if(getEmployeeById(employee.id) == null) {
            employees.add(employee)
        } else {
            throw IllegalArgumentException("Employee with id ${employee.id} already exists")
        }
    }

    fun getAllEmployees(): List<Employee> {
        return employees.toList()
    }

    fun getEmployeeById(id: String): Employee? {
        return employees.find { it.id == id }
    }
}