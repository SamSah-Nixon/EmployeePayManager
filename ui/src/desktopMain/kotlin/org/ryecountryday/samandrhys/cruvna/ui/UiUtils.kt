/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.cruvna.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material.icons.materialPath
import androidx.compose.material3.*
import androidx.compose.material3.Tab
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
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
                        imageVector = Icons.Outlined.Clock,
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
    entries: Map<String, @Composable () -> Unit>,
    secondary: Boolean = false,
    defaultTab: Int = 0
) {
    Column(modifier = Modifier.fillMaxSize()) {
        var tab by remember { mutableStateOf(defaultTab) }

        val content = @Composable {
            entries.keys.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title, color = MaterialTheme.colors.onBackground) },
                    selected = tab == index,
                    onClick = { tab = index }
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
 * Utility delegate to construct a Material icon with default size information.
 *
 * @param name the full name of the generated icon
 * @param viewportWith the width of the vector asset's viewport
 * @param viewportHeight the height of the vector asset's viewport
 * @param autoMirror determines if the vector asset should automatically be mirrored for right to
 * left locales
 * @param block builder lambda to add paths to this vector asset
 */
inline fun materialIcon(
    name: String,
    viewportWith: Float = 24f,
    viewportHeight: Float = 24f,
    autoMirror: Boolean = false,
    block: ImageVector.Builder.() -> ImageVector.Builder
): ImageVector = ImageVector.Builder(
    name = name,
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = viewportWith,
    viewportHeight = viewportHeight,
    autoMirror = autoMirror
).block().build()

@Composable
fun Modifier.verticalScroll() = this.verticalScroll(rememberScrollState())

object EmptyShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        return Outline.Generic(Path())
    }
}

/**
 * An icon with 4 small people in a 2x2 grid
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

/**
 * An icon with an open folder
 */
val Icons.Outlined.FolderOpen: ImageVector by lazy {
    materialIcon("Outlined.FolderOpen", viewportHeight = 960f, viewportWith = 960f) {
        materialPath {
            moveTo(160.0f, 800.0f)
            quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
            reflectiveQuadTo(80.0f, 720.0f)
            verticalLineToRelative(-480.0f)
            quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
            reflectiveQuadTo(160.0f, 160.0f)
            horizontalLineToRelative(240.0f)
            lineToRelative(80.0f, 80.0f)
            horizontalLineToRelative(320.0f)
            quadToRelative(33.0f, 0.0f, 56.5f, 23.5f)
            reflectiveQuadTo(880.0f, 320.0f)
            lineTo(447.0f, 320.0f)
            lineToRelative(-80.0f, -80.0f)
            lineTo(160.0f, 240.0f)
            verticalLineToRelative(480.0f)
            lineToRelative(96.0f, -320.0f)
            horizontalLineToRelative(684.0f)
            lineTo(837.0f, 743.0f)
            quadToRelative(-8.0f, 26.0f, -29.5f, 41.5f)
            reflectiveQuadTo(760.0f, 800.0f)
            lineTo(160.0f, 800.0f)
            close()
            moveTo(244.0f, 720.0f)
            horizontalLineToRelative(516.0f)
            lineToRelative(72.0f, -240.0f)
            lineTo(316.0f, 480.0f)
            lineToRelative(-72.0f, 240.0f)
            close()
            moveTo(244.0f, 720.0f)
            lineTo(316.0f, 480.0f)
            lineTo(244.0f, 720.0f)
            close()
            moveTo(160.0f, 320.0f)
            verticalLineToRelative(-80.0f)
            verticalLineToRelative(80.0f)
            close()
        }
    }
}

/**
 * An icon with a clipboard and a clock in the bottom right corner
 */
val Icons.Outlined.ClockWithClipboard: ImageVector by lazy {
    materialIcon("Outlined.ClipboardWithClock", viewportHeight = 960f, viewportWith = 960f) {
        materialPath {
            moveTo(680.0f, 880.0f)
            quadToRelative(-83.0f, 0.0f, -141.5f, -58.5f)
            reflectiveQuadTo(480.0f, 680.0f)
            quadToRelative(0.0f, -83.0f, 58.5f, -141.5f)
            reflectiveQuadTo(680.0f, 480.0f)
            quadToRelative(83.0f, 0.0f, 141.5f, 58.5f)
            reflectiveQuadTo(880.0f, 680.0f)
            quadToRelative(0.0f, 83.0f, -58.5f, 141.5f)
            reflectiveQuadTo(680.0f, 880.0f)
            close()
            moveTo(747.0f, 775.0f)
            lineTo(775.0f, 747.0f)
            lineTo(700.0f, 672.0f)
            verticalLineToRelative(-112.0f)
            horizontalLineToRelative(-40.0f)
            verticalLineToRelative(128.0f)
            lineToRelative(87.0f, 87.0f)
            close()
            moveTo(200.0f, 840.0f)
            quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
            reflectiveQuadTo(120.0f, 760.0f)
            verticalLineToRelative(-560.0f)
            quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
            reflectiveQuadTo(200.0f, 120.0f)
            horizontalLineToRelative(167.0f)
            quadToRelative(11.0f, -35.0f, 43.0f, -57.5f)
            reflectiveQuadToRelative(70.0f, -22.5f)
            quadToRelative(40.0f, 0.0f, 71.5f, 22.5f)
            reflectiveQuadTo(594.0f, 120.0f)
            horizontalLineToRelative(166.0f)
            quadToRelative(33.0f, 0.0f, 56.5f, 23.5f)
            reflectiveQuadTo(840.0f, 200.0f)
            verticalLineToRelative(250.0f)
            quadToRelative(-18.0f, -13.0f, -38.0f, -22.0f)
            reflectiveQuadToRelative(-42.0f, -16.0f)
            verticalLineToRelative(-212.0f)
            horizontalLineToRelative(-80.0f)
            verticalLineToRelative(120.0f)
            lineTo(280.0f, 320.0f)
            verticalLineToRelative(-120.0f)
            horizontalLineToRelative(-80.0f)
            verticalLineToRelative(560.0f)
            horizontalLineToRelative(212.0f)
            quadToRelative(7.0f, 22.0f, 16.0f, 42.0f)
            reflectiveQuadToRelative(22.0f, 38.0f)
            lineTo(200.0f, 840.0f)
            close()
            moveTo(480.0f, 200.0f)
            quadToRelative(17.0f, 0.0f, 28.5f, -11.5f)
            reflectiveQuadTo(520.0f, 160.0f)
            quadToRelative(0.0f, -17.0f, -11.5f, -28.5f)
            reflectiveQuadTo(480.0f, 120.0f)
            quadToRelative(-17.0f, 0.0f, -28.5f, 11.5f)
            reflectiveQuadTo(440.0f, 160.0f)
            quadToRelative(0.0f, 17.0f, 11.5f, 28.5f)
            reflectiveQuadTo(480.0f, 200.0f)
            close()
        }
    }
}

val Icons.Outlined.Clock: ImageVector by lazy {
    materialIcon("Outlined.Clock", viewportHeight = 960f, viewportWith = 960f) {
        materialPath {
            moveToRelative(612.0f, 668.0f)
            lineToRelative(56.0f, -56.0f)
            lineToRelative(-148.0f, -148.0f)
            verticalLineToRelative(-184.0f)
            horizontalLineToRelative(-80.0f)
            verticalLineToRelative(216.0f)
            lineToRelative(172.0f, 172.0f)
            close()
            moveTo(480.0f, 880.0f)
            quadToRelative(-83.0f, 0.0f, -156.0f, -31.5f)
            reflectiveQuadTo(197.0f, 763.0f)
            quadToRelative(-54.0f, -54.0f, -85.5f, -127.0f)
            reflectiveQuadTo(80.0f, 480.0f)
            quadToRelative(0.0f, -83.0f, 31.5f, -156.0f)
            reflectiveQuadTo(197.0f, 197.0f)
            quadToRelative(54.0f, -54.0f, 127.0f, -85.5f)
            reflectiveQuadTo(480.0f, 80.0f)
            quadToRelative(83.0f, 0.0f, 156.0f, 31.5f)
            reflectiveQuadTo(763.0f, 197.0f)
            quadToRelative(54.0f, 54.0f, 85.5f, 127.0f)
            reflectiveQuadTo(880.0f, 480.0f)
            quadToRelative(0.0f, 83.0f, -31.5f, 156.0f)
            reflectiveQuadTo(763.0f, 763.0f)
            quadToRelative(-54.0f, 54.0f, -127.0f, 85.5f)
            reflectiveQuadTo(480.0f, 880.0f)
            close()
            moveTo(480.0f, 480.0f)
            close()
            moveTo(480.0f, 800.0f)
            quadToRelative(133.0f, 0.0f, 226.5f, -93.5f)
            reflectiveQuadTo(800.0f, 480.0f)
            quadToRelative(0.0f, -133.0f, -93.5f, -226.5f)
            reflectiveQuadTo(480.0f, 160.0f)
            quadToRelative(-133.0f, 0.0f, -226.5f, 93.5f)
            reflectiveQuadTo(160.0f, 480.0f)
            quadToRelative(0.0f, 133.0f, 93.5f, 226.5f)
            reflectiveQuadTo(480.0f, 800.0f)
            close()
        }
    }
}

val Icons.Outlined.BriefcaseWithClock: ImageVector by lazy {
    materialIcon("Outlined.BriefcaseWithClock", viewportHeight = 960f, viewportWith = 960f) {
        materialPath {
            moveTo(160.0f, 760.0f)
            verticalLineToRelative(-440.0f)
            verticalLineToRelative(440.0f)
            verticalLineToRelative(-15.0f)
            verticalLineToRelative(15.0f)
            close()
            moveTo(160.0f, 840.0f)
            quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
            reflectiveQuadTo(80.0f, 760.0f)
            verticalLineToRelative(-440.0f)
            quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
            reflectiveQuadTo(160.0f, 240.0f)
            horizontalLineToRelative(160.0f)
            verticalLineToRelative(-80.0f)
            quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
            reflectiveQuadTo(400.0f, 80.0f)
            horizontalLineToRelative(160.0f)
            quadToRelative(33.0f, 0.0f, 56.5f, 23.5f)
            reflectiveQuadTo(640.0f, 160.0f)
            verticalLineToRelative(80.0f)
            horizontalLineToRelative(160.0f)
            quadToRelative(33.0f, 0.0f, 56.5f, 23.5f)
            reflectiveQuadTo(880.0f, 320.0f)
            verticalLineToRelative(171.0f)
            quadToRelative(-18.0f, -13.0f, -38.0f, -22.5f)
            reflectiveQuadTo(800.0f, 452.0f)
            verticalLineToRelative(-132.0f)
            lineTo(160.0f, 320.0f)
            verticalLineToRelative(440.0f)
            horizontalLineToRelative(283.0f)
            quadToRelative(3.0f, 21.0f, 9.0f, 41.0f)
            reflectiveQuadToRelative(15.0f, 39.0f)
            lineTo(160.0f, 840.0f)
            close()
            moveTo(400.0f, 240.0f)
            horizontalLineToRelative(160.0f)
            verticalLineToRelative(-80.0f)
            lineTo(400.0f, 160.0f)
            verticalLineToRelative(80.0f)
            close()
            moveTo(720.0f, 920.0f)
            quadToRelative(-83.0f, 0.0f, -141.5f, -58.5f)
            reflectiveQuadTo(520.0f, 720.0f)
            quadToRelative(0.0f, -83.0f, 58.5f, -141.5f)
            reflectiveQuadTo(720.0f, 520.0f)
            quadToRelative(83.0f, 0.0f, 141.5f, 58.5f)
            reflectiveQuadTo(920.0f, 720.0f)
            quadToRelative(0.0f, 83.0f, -58.5f, 141.5f)
            reflectiveQuadTo(720.0f, 920.0f)
            close()
            moveTo(740.0f, 712.0f)
            verticalLineToRelative(-112.0f)
            horizontalLineToRelative(-40.0f)
            verticalLineToRelative(128.0f)
            lineToRelative(86.0f, 86.0f)
            lineToRelative(28.0f, -28.0f)
            lineToRelative(-74.0f, -74.0f)
            close()
        }
    }
}