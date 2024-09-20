/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.cruvna.ui

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
import androidx.compose.material.LocalTextStyle
import androidx.compose.material3.*
import androidx.compose.material3.Tab
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import org.ryecountryday.samandrhys.cruvna.util.LocalDate
import org.ryecountryday.samandrhys.cruvna.util.toDateString
import java.time.LocalTime

/**
 * An [OutlinedCard] with a label that overlaps the top border. The card's content is defined by [content], and the label
 * is defined by [value].
 */
@Composable
fun LabeledCard(
    value: String,
    modifier: Modifier = Modifier,
    border: BorderStroke = mutedBorder(),
    content: @Composable () -> Unit
) {
    LabeledCardImpl(value, modifier, mutableStateOf(false), border, null, true, content)
}

/**
 * An [OutlinedCard] with a label that overlaps the top border. The card's content is defined by [content], and the label
 * is defined by [value]. When clicked, the card's border changes to the primary color, and [onClick] is called.
 */
@Composable
fun LabeledButton(
    value: String,
    modifier: Modifier = Modifier,
    selected: MutableState<Boolean>? = null,
    border: BorderStroke = selected?.let { maybeSelectedBorder(it.value) } ?: mutedBorder(),
    onClick: () -> Unit,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    LabeledCardImpl(value, modifier, selected, border, onClick, enabled, content)
}

/**
 * Common logic for [LabeledCard] and [LabeledButton].
 */
@Composable
private fun LabeledCardImpl(
    value: String,
    modifier: Modifier = Modifier,
    selected: MutableState<Boolean>? = null,
    border: BorderStroke = selected?.let { maybeSelectedBorder(it.value) } ?: mutedBorder(),
    onClick: (() -> Unit)?,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
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
        if (onClick != null) {
            OutlinedCard(
                onClick = onClick,
                shape = shape,
                colors = colors,
                border = border,
                enabled = enabled,
                content = { content1() },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            OutlinedCard(
                shape = shape,
                colors = colors,
                border = border,
                content = { content1() },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Label text that overlaps with the card's border
        Text(value,
            modifier = Modifier.padding(start = 16.dp)
                .align(Alignment.TopStart).offset(y = (-8).dp)
                .let { // Add a background color to match the parent - gives the appearance of interrupting the border
                    if (!MaterialTheme.colors.isLight) it.background(Color(0xFF1E1E1E))
                    else it.background(MaterialTheme.colors.background)
                }
                .padding(horizontal = 4.dp), // Padding for the text itself
            color = if (selected?.value == true) MaterialTheme.colors.primary
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
    Button(
        onClick = { expanded = true },
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
    Box(modifier = modifier) {
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
                        colors = DatePickerDefaults.colors().copy(
                            dayContentColor = MaterialTheme.colors.onSurface,
                            weekdayContentColor = MaterialTheme.colors.onSurface,
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun InlineTimePicker(
    label: String,
    initialTime: LocalTime = LocalTime.now(),
    onTimeChange: (LocalTime) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showTimePicker by remember { mutableStateOf(false) }

    var state by remember { mutableStateOf(initialTime) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = LocalTime.of(state.hour, state.minute).toString(),
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showTimePicker = !showTimePicker }) {
                    Icon(
                        imageVector = Icons.Default.Clock,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().height(64.dp)
        )

        if (showTimePicker) {
            Popup(
                onDismissRequest = { showTimePicker = false },
                alignment = Alignment.TopStart,
                properties = PopupProperties(focusable = true)
            ) {
                Box(
                    modifier = Modifier
                        .offset(y = 64.dp)
                        .height(200.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colors.surface)
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background)
                        LabeledCard(
                            value = "Hour",
                            border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)),
                            modifier = Modifier.width(100.dp),
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                                Text(
                                    state.hour.toString(),
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(48.dp)) {
                                    Button(onClick = {
                                        state = LocalTime.of((state.hour + 1) % 24, state.minute)
                                        onTimeChange(state)
                                    }, colors = colors, content = {})
                                    Button(onClick = {
                                        state = LocalTime.of((state.hour - 1) % 24, state.minute)
                                        onTimeChange(state)
                                    }, colors = colors, content = {})
                                }
                            }
                        }
                        Text(":", style = MaterialTheme.typography.h5, modifier = Modifier.padding(horizontal = 8.dp))
                        LabeledCard(
                            value = "Minute",
                            border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)),
                            modifier = Modifier.width(100.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                                Text(
                                    state.minute.toString(),
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(48.dp)) {
                                    Button(onClick = {
                                        state = LocalTime.of(state.hour, (state.minute + 1) % 60)
                                        onTimeChange(state)
                                    }, colors = colors, content = {})
                                    Button(onClick = {
                                        state = LocalTime.of(state.hour, (state.minute - 1) % 60)
                                        onTimeChange(state)
                                    }, colors = colors, content = {})
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CoolTabRow(
    entries: Map<Pair<String, ImageVector?>, @Composable () -> Unit>,
    secondary: Boolean = false,
    defaultTab: Int = 0
) {
    Column(modifier = Modifier.fillMaxSize()) {
        var tab by remember { mutableStateOf(defaultTab) }

        val content = @Composable {
            entries.keys.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title.first, color = MaterialTheme.colors.onBackground) },
                    selected = tab == index,
                    onClick = { tab = index },
                    icon = title.second?.let {{ Icon(
                        imageVector = it,
                        contentDescription = title.first,
                        tint = MaterialTheme.colors.onBackground
                    ) }}
                )
            }
        }

        if(secondary) {
            SecondaryTabRow(
                selectedTabIndex = tab,
                containerColor = MaterialTheme.colors.background,
                tabs = content
            )
        } else {
            PrimaryTabRow(
                selectedTabIndex = tab,
                containerColor = MaterialTheme.colors.background,
                tabs = content
            )
        }

        Box { // use Box to reset the alignments in the Column
            entries.values.elementAt(tab).invoke()
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
    return if (selected)
        BorderStroke(TextFieldDefaults.FocusedBorderThickness, MaterialTheme.colors.primary)
    else
        BorderStroke(
            TextFieldDefaults.UnfocusedBorderThickness,
            MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)
        )
}

@Composable
fun TextWithIcon(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onBackground,
    textStyle: TextStyle = LocalTextStyle.current,
    spacerWidth: Number = 8
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text, color = color, style = textStyle, modifier = Modifier.padding(end = spacerWidth.toDouble().dp))
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = color
        )
    }
}