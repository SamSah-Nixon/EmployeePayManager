package org.ryecountryday.samandrhys.epm.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import org.ryecountryday.samandrhys.epm.util.toDateString
import java.util.*

@Composable
fun LabeledCard(value: String,
                modifier: Modifier = Modifier,
                border: BorderStroke? = null,
                content: @Composable () -> Unit) {
    Box(modifier = modifier) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            border = border
        ) {
            Column(modifier = Modifier.padding(top = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                content()
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Label text that overlaps with the card's border
        Text(value,
            modifier = Modifier.padding(start = 16.dp)
                .align(Alignment.TopStart).offset(y = (-8).dp)
                .background(MaterialTheme.colors.background) // Add a background color to match the parent - gives the appearance of interrupting the border
                .padding(horizontal = 4.dp), // Padding for the text itself
            color = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
            style = MaterialTheme.typography.caption
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun LabeledButton(value: String,
                  modifier: Modifier = Modifier,
                  border: BorderStroke? = null,
                  onClick: () -> Unit,
                  content: @Composable () -> Unit) {
    Box(modifier = modifier) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            border = border,
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
        ) {
            ProvideTextStyle(MaterialTheme.typography.body1) {
                Column(modifier = Modifier.padding(top = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    content()
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Label text that overlaps with the button's border
        Text(value,
            modifier = Modifier.padding(start = 16.dp)
                .align(Alignment.TopStart).offset(y = (-8).dp)
                .background(MaterialTheme.colors.background) // Add a background color to match the parent - gives the appearance of interrupting the border
                .padding(horizontal = 4.dp), // Padding for the text itself
            color = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
            style = MaterialTheme.typography.caption
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}


@Composable
fun DropdownButton(
    items: List<String>,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Button(onClick = { expanded = true },
        content = {
            Column {
                this@Button.content()
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    items.forEach { item ->
                        DropdownMenuItem(onClick = {
                            onItemSelected(item)
                            expanded = false
                        }) {
                            Text(item)
                        }
                    }
                }
            }
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
        border = maybeSelectedBorder(expanded)
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun InlineDatePicker(state: DatePickerState, modifier: Modifier = Modifier) {
    var showDatePicker by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
    ) {
        val timeMs = state.selectedDateMillis?.plus(86400000) ?: System.currentTimeMillis()
        OutlinedTextField(
            value = Date(timeMs).toDateString(),
            onValueChange = { },
            label = { Text("DOB") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().height(64.dp)
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .width(400.dp)
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colors.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = state,
                        showModeToggle = false,
                    )
                }
            }
        }
    }
}

@Composable
fun mutedBorder() = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled))

@Composable
fun maybeSelectedBorder(selected: Boolean): BorderStroke {
    return if(selected)
        BorderStroke(TextFieldDefaults.FocusedBorderThickness, MaterialTheme.colors.primary)
    else
        BorderStroke(TextFieldDefaults.UnfocusedBorderThickness, MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled))
}