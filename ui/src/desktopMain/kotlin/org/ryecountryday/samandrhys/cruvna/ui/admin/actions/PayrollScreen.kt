package org.ryecountryday.samandrhys.cruvna.ui.admin.actions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.ryecountryday.samandrhys.cruvna.backend.employee.Employee
import org.ryecountryday.samandrhys.cruvna.backend.timing.WorkHistory
import org.ryecountryday.samandrhys.cruvna.backend.timing.WorkHistory.payPeriods
import org.ryecountryday.samandrhys.cruvna.ui.InlineDatePicker
import org.ryecountryday.samandrhys.cruvna.util.toInstant
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayrollScreen(employees: MutableSet<Employee>) {
    Box(modifier = Modifier.fillMaxWidth().padding(16.dp).background(MaterialTheme.colors.surface), contentAlignment = Alignment.BottomCenter) {
        val previousEnd = payPeriods.getOrNull(0)



        val datePickerState1 = rememberDatePickerState()
        val date1 by remember { derivedStateOf { datePickerState1.selectedDateMillis } }

        val datePickerState2 = rememberDatePickerState()
        val date2 by remember { derivedStateOf { datePickerState2.selectedDateMillis } }

        Column(modifier = Modifier.width(200.dp).align(Alignment.CenterStart).padding(16.dp)) {
            InlineDatePicker("Start Date", datePickerState1, modifier = Modifier.height(100.dp).width(200.dp).padding(16.dp))
            InlineDatePicker("End Date", datePickerState2, modifier = Modifier.height(100.dp).width(200.dp).padding(16.dp))
            datePickerState1.selectedDateMillis = Instant.now().toEpochMilli()
            datePickerState2.selectedDateMillis = if(previousEnd==null) Instant.now().toEpochMilli() else previousEnd.payPeriodEnd.toInstant().toEpochMilli()
            Button(
                onClick = {
                    var bool = WorkHistory.addPayPeriod(
                        Instant.ofEpochMilli(date1!!).atZone(ZoneId.systemDefault()).toLocalDate(),
                        Instant.ofEpochMilli(date2!!).atZone(ZoneId.systemDefault()).toLocalDate())
                    print(bool)
                          },
                modifier = Modifier.size(300.dp).padding(16.dp)
            ) {
                Text("Create Pay Period" , modifier = Modifier.padding(16.dp), color = MaterialTheme.colors.onSurface, textAlign = TextAlign.Center)
            }
        }
        Column(modifier = Modifier.width(200.dp).align(Alignment.Center).padding(16.dp)) {

        }
        Column(modifier = Modifier.width(200.dp).align(Alignment.CenterEnd).padding(16.dp)) {

        }




    }

}