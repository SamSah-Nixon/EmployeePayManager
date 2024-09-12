package org.ryecountryday.samandrhys.epm.backend.timing

import java.time.Instant
import java.util.TreeSet

public object WorkHistory{

    val periods: MutableList<Instant> = ArrayList()
    val clockedIn: MutableSet<WorkEntry> = TreeSet<WorkEntry>()
    val entries: MutableSet<WorkEntry> = TreeSet<WorkEntry>()

    fun payPeriod(){

        var currentWorkers : MutableSet<String> = TreeSet<String>()
        //Separates a workEntry when there is a pay period
        for(entry : WorkEntry in clockedIn){
            currentWorkers.add(entry.id)
            clockOut(entry.id)
        }
        periods.add(Instant.now())
        for(id : String in currentWorkers){
            clockIn(id)
        }
    }

    fun clockIn(id: String) {
        clockedIn.add(WorkEntry(Instant.now(),null,id))
    }

    fun clockOut(id: String) {
        for (entry : WorkEntry in clockedIn) {
            if(entry.id == id) {
                val ent: WorkEntry = entry
                ent.end = Instant.now()
                clockedIn.remove(entry)
                entries.add(ent)
                break
            }
        }
    }
}