package org.ryecountryday.samandrhys.epm.util

import org.ryecountryday.samandrhys.epm.backend.employee.Employee
import kotlin.reflect.KProperty

/**
 * A property delegate that allows for listeners to be notified when the value of the property changes.
 */
class ValueWithListener<T, C>(
    private val listeners: MutableList<(C) -> Unit>,
    private var value: T,
    private val container: C
) {
    operator fun getValue(employee: Employee, property: KProperty<*>): T {
        return value
    }

    operator fun setValue(employee: Employee, property: KProperty<*>, value: T) {
        listeners.forEach { it(container) }
        this.value = value
    }
}