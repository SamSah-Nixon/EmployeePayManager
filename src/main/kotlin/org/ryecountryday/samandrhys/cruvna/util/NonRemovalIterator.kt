/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.cruvna.util

/**
 * A [MutableIterator] that does not allow removal of elements.
 */
class NonRemovalIterator<T>(private val delegate: MutableIterator<T>) : MutableIterator<T> by delegate {
    override fun remove() {
        throw UnsupportedOperationException("Cannot remove employees")
    }
}