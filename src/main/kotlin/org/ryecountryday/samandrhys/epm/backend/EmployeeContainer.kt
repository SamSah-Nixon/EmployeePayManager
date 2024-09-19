/*
 * This file is a part of EmployeePayManager.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.epm.backend

import kotlinx.serialization.Serializable
import org.ryecountryday.samandrhys.epm.backend.employee.Employee
import org.ryecountryday.samandrhys.epm.util.NonRemovalIterator
import java.util.function.Predicate

/**
 * Represents a set of employees. Removing employees is disallowed and will throw [UnsupportedOperationException].
 */
@Serializable
class EmployeeContainer(private val employees: MutableSet<Employee> = sortedSetOf()) : MutableSet<Employee> by employees {
    constructor(vararg employees: Employee) : this(employees.toMutableSet())

    /**
     * Replaces the employee with the same ID as the given employee with the given employee.
     */
    fun replaceEmployee(employee: Employee): Boolean {
        employees.removeIf { it.id == employee.id }
        return employees.add(employee)
    }

    override fun clear() {
        throw UnsupportedOperationException("Cannot clear employees")
    }

    override fun retainAll(elements: Collection<Employee>): Boolean {
        throw UnsupportedOperationException("Cannot retain employees")
    }

    override fun remove(element: Employee): Boolean {
        throw UnsupportedOperationException("Cannot remove employees")
    }

    override fun removeAll(elements: Collection<Employee>): Boolean {
        throw UnsupportedOperationException("Cannot remove employees")
    }

    override fun removeIf(filter: Predicate<in Employee>): Boolean {
        throw UnsupportedOperationException("Cannot remove employees")
    }

    override fun iterator(): MutableIterator<Employee> {
        return NonRemovalIterator(employees.iterator())
    }

    override fun toString(): String {
        return "EmployeeContainer$employees"
    }

    /**
     * @return The employee in this container with the given ID, or null if no such employee exists.
     */
    fun findById(id: String): Employee? {
        return find { it.id == id }
    }

    /**
     * shorthand for [findById]
     */
    operator fun get(id: String) = findById(id)
}