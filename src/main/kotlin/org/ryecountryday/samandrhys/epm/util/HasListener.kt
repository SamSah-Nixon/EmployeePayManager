package org.ryecountryday.samandrhys.epm.util

import kotlinx.serialization.Transient
import org.ryecountryday.samandrhys.epm.backend.employee.Employee
import kotlin.reflect.KProperty

/**
 * A class that allows for listeners to be notified when the value of their fields change.
 * I really wish this could be an interface but they both can't have properties or protected methods.
 * @param T the type of `this` object.
 */
abstract class HasListener<T> {
    @Transient
    protected val listeners = mutableListOf<(T) -> Unit>()

    fun addChangeListener(listener: (T) -> Unit) {
        listeners.add(listener)
    }

    fun clearChangeListeners() {
        listeners.clear()
    }

    protected fun notifyListeners() {
        listeners.forEach { it(t()) }
    }

    @Suppress("UNCHECKED_CAST")
    private fun t(): T {
        return this as T
    }

    protected fun <V> value(value: V): ValueWithListener<V, T> {
        return ValueWithListener(listeners, value, t())
    }
}


/**
 * A property delegate that allows for listeners to be notified when the value of the property changes.
 * @param listeners the list of listeners to notify when the value changes.
 * @param value the initial value of the property.
 * @param container the object that contains the property.
 * @param T the type of the property.
 * @param C the type of the object that holds the property.
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