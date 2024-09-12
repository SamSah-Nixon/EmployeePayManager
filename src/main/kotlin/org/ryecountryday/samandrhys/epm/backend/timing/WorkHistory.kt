package org.ryecountryday.samandrhys.epm.backend.timing

import java.time.Instant

object WorkHistory {

    val payPeriod = mutableListOf<Instant>()
    var clockedIn = mutableSetOf<WorkEntry>()
    var currentPeriod = mutableSetOf<WorkEntry>()
    val entries = mutableMapOf<Instant, MutableSet<WorkEntry>>()

    //GET PAID HERE
    fun payPeriod() {
        val now = Instant.now()

        //Move clocked In workers to current period
        val currentWorkers = mutableSetOf<String>()
        for(entry in clockedIn) {
            currentWorkers.add(entry.id)
            clockOut(entry.id)
        }
        clockedIn = mutableSetOf()
        //Move current period to entries

        entries[now] = currentPeriod

        payPeriod.add(now)
        //clock current workers back in
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
            currentPeriod.add(it)
        }
    }
}