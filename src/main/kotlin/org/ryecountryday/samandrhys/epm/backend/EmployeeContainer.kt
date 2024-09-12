package org.ryecountryday.samandrhys.epm.backend

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.ryecountryday.samandrhys.epm.backend.employee.Employee
import java.util.TreeSet
import java.util.function.Predicate

@Serializable
class EmployeeContainer(private val employees: MutableSet<Employee> = TreeSet()) : MutableSet<Employee> by employees {
    constructor(vararg employees: Employee) : this(employees.toMutableSet())

    @Transient
    private val listeners = mutableListOf<(EmployeeContainer) -> Unit>()

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
        listeners.forEach { it(this) }
    }

    override fun add(element: Employee): Boolean {
        val a = employees.add(element)
        listeners.forEach { it(this) }
        return a
    }

    override fun addAll(elements: Collection<Employee>): Boolean {
        val a = employees.addAll(elements)
        listeners.forEach { it(this) }
        return a
    }

    override fun clear() {
        throw UnsupportedOperationException("Cannot clear employees")
    }

    override fun retainAll(elements: Collection<Employee>): Boolean {
        throw UnsupportedOperationException("Cannot retain employees")
    }

    override fun iterator(): MutableIterator<Employee> {
        return Itr(employees.iterator())
    }

    fun findById(id: String): Employee? {
        return find { it.id == id }
    }

    fun addChangeListener(listener: (EmployeeContainer) -> Unit) {
        listeners.add(listener)
    }

    private class Itr(private val delegate: MutableIterator<Employee>) : MutableIterator<Employee> by delegate {
        override fun remove() {
            throw UnsupportedOperationException("Cannot remove employees")
        }
    }
}