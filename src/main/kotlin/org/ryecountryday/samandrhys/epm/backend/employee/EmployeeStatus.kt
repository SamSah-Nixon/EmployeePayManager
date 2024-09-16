package org.ryecountryday.samandrhys.epm.backend.employee

enum class EmployeeStatus {
    ACTIVE,
    INACTIVE;

    operator fun not(): EmployeeStatus {
        return if(this == ACTIVE) INACTIVE else ACTIVE
    }
}