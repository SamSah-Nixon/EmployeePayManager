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

    override fun clear() {
        throw UnsupportedOperationException("Cannot clear employees")
    }

    override fun retainAll(elements: Collection<Employee>): Boolean {
        throw UnsupportedOperationException("Cannot retain employees")
    }

    override fun iterator(): MutableIterator<Employee> {
        return NonRemovalIterator(employees.iterator())
    }

    override fun toString(): String {
        return "EmployeeContainer$employees"
    }

    fun findById(id: String): Employee? {
        return find { it.id == id }
    }

    operator fun get(id: String): Employee? = findById(id)
}