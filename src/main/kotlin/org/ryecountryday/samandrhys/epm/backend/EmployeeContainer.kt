/*
 * This file is a part of EmployeePayManager.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.epm.backend

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.ryecountryday.samandrhys.epm.backend.employee.Employee
import org.ryecountryday.samandrhys.epm.util.NonRemovalIterator
import java.util.TreeSet
import java.util.function.Predicate

/**
 * Represents a set of employees. Removing employees is disallowed and will throw [UnsupportedOperationException].
 */
@Serializable
class EmployeeContainer(private val employees: MutableSet<Employee> = TreeSet()) : MutableSet<Employee> by employees {
    constructor(vararg employees: Employee) : this(employees.toMutableSet())

    /**
     * A list of blocks to call when this container is updated.
     */
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
        employees.removeIf {
            (it.id == employee.id).apply {
                if(this) {
                    it.clearChangeListeners()
                }
            }
        }
        employees.add(employee)
        listeners.forEach { it(this) }
    }

    override fun add(element: Employee): Boolean {
        element.addChangeListener { callListeners() }
        val a = employees.add(element)
        callListeners()
        return a
    }

    override fun addAll(elements: Collection<Employee>): Boolean {
        elements.forEach { it.addChangeListener { callListeners() } }
        val a = employees.addAll(elements)
        callListeners()
        return a
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

    fun addChangeListener(listener: (EmployeeContainer) -> Unit) {
        listeners.add(listener)
    }

    private fun callListeners() {
        listeners.forEach { it(this) }
    }
}