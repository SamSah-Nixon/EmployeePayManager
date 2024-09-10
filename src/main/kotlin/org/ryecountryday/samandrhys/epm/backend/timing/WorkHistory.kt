package org.ryecountryday.samandrhys.epm.backend.timing

class WorkHistory {
    private val history = mutableListOf<WorkEntry>()

    fun addEntry(entry: WorkEntry) {
        history.add(entry)
    }

    fun getEntries(): List<WorkEntry> {
        return history
    }
}