package org.ryecountryday.samandrhys.epm.backend.timing

import java.time.Instant

object WorkHistory {

    val periods = mutableListOf<Instant>()
    val clockedIn = mutableSetOf<WorkEntry>()
    val entries = mutableListOf<WorkEntry>()

    fun payPeriod() {
        val currentWorkers = mutableSetOf<String>()
        //Separates a workEntry when there is a pay period
        for(entry in clockedIn) {
            currentWorkers.add(entry.id)
            clockOut(entry.id)
        }

        periods.add(Instant.now())
        for(id in currentWorkers) {
            clockIn(id)
        }
    }

    fun clockIn(id: String) {
        clockedIn.add(WorkEntry(Instant.now(), id))
    }

    fun clockOut(id: String) {
        clockedIn.firstOrNull { it.id == id }?.let {
            it.end = Instant.now()
            entries.add(it)
        }
    }
}