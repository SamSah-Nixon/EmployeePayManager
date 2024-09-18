/*
 * This file is a part of EmployeePayManager.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.epm.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import org.ryecountryday.samandrhys.epm.util.LocalDate
import org.ryecountryday.samandrhys.epm.util.toDateString

/**
 * An [OutlinedCard] with a label that overlaps the top border. The card's content is defined by [content], and the label
 * is defined by [value].
 */
@Composable
fun LabeledCard(value: String,
                modifier: Modifier = Modifier,
                border: BorderStroke = mutedBorder(),
                content: @Composable () -> Unit) {
    LabeledCardImpl(value, modifier, mutableStateOf(false), border, null, content)
}

/**
 * An [OutlinedCard] with a label that overlaps the top border. The card's content is defined by [content], and the label
 * is defined by [value]. When clicked, the card's border changes to the primary color, and [onClick] is called.
 */
@Composable
fun LabeledButton(value: String,
                  modifier: Modifier = Modifier,
                  selected: MutableState<Boolean>? = null,
                  border: BorderStroke = selected?.let { maybeSelectedBorder(it.value) } ?: mutedBorder(),
                  onClick: () -> Unit,
                  content: @Composable () -> Unit) {
    LabeledCardImpl(value, modifier, selected, border, onClick, content)
}

/**
 * Common logic for [LabeledCard] and [LabeledButton].
 */
@Composable
private fun LabeledCardImpl(value: String,
                            modifier: Modifier = Modifier,
                            selected: MutableState<Boolean>? = null,
                            border: BorderStroke = selected?.let { maybeSelectedBorder(it.value) } ?: mutedBorder(),
                            onClick: (() -> Unit)?,
                            content: @Composable () -> Unit) {
    Box(modifier = modifier) {
        val shape = MaterialTheme.shapes.small
        val colors = CardDefaults.outlinedCardColors().let { // completely clear background
            it.copy(
                containerColor = MaterialTheme.colors.background.copy(alpha = 0f),
                contentColor = it.contentColor.copy(alpha = 0f),
                disabledContainerColor = it.disabledContainerColor.copy(alpha = 0f),
                disabledContentColor = it.disabledContentColor.copy(alpha = 0f),
            )
        }
        val content1 = @Composable {
            Column(modifier = Modifier.padding(top = 12.dp), horizontalAlignment = Alignment.Start) {
                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    content()
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        if(onClick != null) {
            OutlinedCard(
                onClick = onClick,
                shape = shape,
                colors = colors,
                content = { content1() },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            OutlinedCard(
                shape = shape,
                colors = colors,
                content = { content1() },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Label text that overlaps with the card's border
        Text(value,
            modifier = Modifier.padding(start = 16.dp)
                .align(Alignment.TopStart).offset(y = (-8).dp)
                .let { // Add a background color to match the parent - gives the appearance of interrupting the border
                    if(!MaterialTheme.colors.isLight) it.background(Color(0xFF1E1E1E))
                    else it.background(MaterialTheme.colors.background)
                }
                .padding(horizontal = 4.dp), // Padding for the text itself
            color = if(selected?.value == true) MaterialTheme.colors.primary
                else MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
            style = MaterialTheme.typography.caption
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}

/**
 * A button that opens a dropdown menu when clicked. The button's content is defined by [content], and the
 * dropdown menu items are defined by [items]. [onItemSelected] is called when an item is selected.
 */
@Composable
fun <T> DropdownButton(
    items: List<T>,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Button(onClick = { expanded = true },
        content = {
            Column {
                content()
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    items.forEach { item ->
                        DropdownMenuItem(onClick = {
                            onItemSelected(item)
                            expanded = false
                        }) {
                            Text(item.toString())
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

/**
 * A TextField with a trailing icon that opens a DatePicker when clicked. The text field isn't editable, but instead
 * 
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun InlineDatePicker(label: String, state: DatePickerState, modifier: Modifier = Modifier) {
    var showDatePicker by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = LocalDate(state.selectedDateMillis).toDateString(),
            onValueChange = {},
            label = { Text(label) },
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

/**
 * A border that is 1dp thick and colored gray
 */
@Composable
fun mutedBorder() = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled))

/**
 * A border that is thicker and colored when selected, and thinner and muted/gray when not selected
 */
@Composable
fun maybeSelectedBorder(selected: Boolean): BorderStroke {
    return if(selected)
        BorderStroke(TextFieldDefaults.FocusedBorderThickness, MaterialTheme.colors.primary)
    else
        BorderStroke(TextFieldDefaults.UnfocusedBorderThickness, MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled))
}

/**
 * @see androidx.compose.material.icons.filled.Person
 */
private fun PathBuilder.person(x: Int, y: Int, scale: Float = 1.0F) {
    @Suppress("NAME_SHADOWING")
    val scale = 1.0F / scale
    moveTo(x.toFloat(), y.toFloat())
    curveToRelative(2.21f / scale, 0.0f, 4.0f / scale, -1.79f / scale, 4.0f / scale, -4.0f / scale)
    reflectiveCurveToRelative(-1.79f / scale, -4.0f / scale, -4.0f / scale, -4.0f / scale)
    reflectiveCurveToRelative(-4.0f / scale, 1.79f / scale, -4.0f / scale, 4.0f / scale)
    reflectiveCurveToRelative(1.79f / scale, 4.0f / scale, 4.0f / scale, 4.0f / scale)
    close()
    moveTo(x.toFloat(), y + 2.0F / scale)
    curveToRelative(-2.67f / scale, 0.0f, -8.0f / scale, 1.34f / scale, -8.0f / scale, 4.0f / scale)
    verticalLineToRelative(2.0f / scale)
    horizontalLineToRelative(16.0f / scale)
    verticalLineToRelative(-2.0f / scale)
    curveToRelative(0.0f, -2.66f / scale, -5.33f / scale, -4.0f / scale, -8.0f / scale, -4.0f / scale)
    close()
}

/**
 * an icon with 4 small people in a 2x2 grid
 */
val Icons.Filled.FourPeople: ImageVector by lazy {
    materialIcon("Filled.FourPeople") {
        materialPath {
            person(6, 6, 0.5F)
            person(18, 6, 0.5F)
            person(6, 18, 0.5F)
            person(18, 18, 0.5F)
        }
    }
}