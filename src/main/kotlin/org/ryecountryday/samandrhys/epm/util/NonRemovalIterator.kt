package org.ryecountryday.samandrhys.epm.util

class NonRemovalIterator<T>(private val delegate: MutableIterator<T>) : MutableIterator<T> by delegate {
    override fun remove() {
        throw UnsupportedOperationException("Cannot remove employees")
    }
}