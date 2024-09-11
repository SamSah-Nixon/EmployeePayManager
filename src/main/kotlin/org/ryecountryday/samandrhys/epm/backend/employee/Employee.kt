package org.ryecountryday.samandrhys.epm.backend.employee

import org.ryecountryday.samandrhys.epm.backend.PayStrategy
import org.ryecountryday.samandrhys.epm.backend.timing.WorkHistory
import java.util.Date

data class Employee(
    var name: String,
    val id: String,
    val salary: PayStrategy,
    val birthday: Date,
    val address: Address,
    val workHistory: WorkHistory
)