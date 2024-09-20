package org.ryecountryday.samandrhys.cruvna.ui

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.unit.dp

// These were automatically generated from svg files from https://fonts.google.com/icons

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

@Composable
fun Modifier.verticalScroll() = this.verticalScroll(rememberScrollState())

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
val Icons.Filled.ClockWithClipboard: ImageVector by lazy {
    materialIcon("Filled.ClipboardWithClock", viewportHeight = 960f, viewportWith = 960f) {
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

val Icons.Filled.Clock: ImageVector by lazy {
    materialIcon("Filled.Clock", viewportHeight = 960f, viewportWith = 960f) {
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

/**
 * An icon with a briefcase and a clock in the bottom right corner
 */
val Icons.Filled.BriefcaseWithClock: ImageVector by lazy {
    materialIcon("Filled.BriefcaseWithClock", viewportHeight = 960f, viewportWith = 960f) {
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

/**
 * An icon with a file cabinet looking thingy
 */
val Icons.Filled.Storage: ImageVector by lazy {
    materialIcon("Filled.Storage", viewportHeight = 960f, viewportWith = 960f) {
        materialPath {
            moveToRelative(200.0f, 840.0f)
            lineToRelative(-80.0f, -480.0f)
            horizontalLineToRelative(720.0f)
            lineToRelative(-80.0f, 480.0f)
            lineTo(200.0f, 840.0f)
            close()
            moveTo(267.0f, 760.0f)
            horizontalLineToRelative(426.0f)
            lineToRelative(51.0f, -320.0f)
            lineTo(216.0f, 440.0f)
            lineToRelative(51.0f, 320.0f)
            close()
            moveTo(400.0f, 600.0f)
            horizontalLineToRelative(160.0f)
            quadToRelative(17.0f, 0.0f, 28.5f, -11.5f)
            reflectiveQuadTo(600.0f, 560.0f)
            quadToRelative(0.0f, -17.0f, -11.5f, -28.5f)
            reflectiveQuadTo(560.0f, 520.0f)
            lineTo(400.0f, 520.0f)
            quadToRelative(-17.0f, 0.0f, -28.5f, 11.5f)
            reflectiveQuadTo(360.0f, 560.0f)
            quadToRelative(0.0f, 17.0f, 11.5f, 28.5f)
            reflectiveQuadTo(400.0f, 600.0f)
            close()
            moveTo(240.0f, 320.0f)
            quadToRelative(-17.0f, 0.0f, -28.5f, -11.5f)
            reflectiveQuadTo(200.0f, 280.0f)
            quadToRelative(0.0f, -17.0f, 11.5f, -28.5f)
            reflectiveQuadTo(240.0f, 240.0f)
            horizontalLineToRelative(480.0f)
            quadToRelative(17.0f, 0.0f, 28.5f, 11.5f)
            reflectiveQuadTo(760.0f, 280.0f)
            quadToRelative(0.0f, 17.0f, -11.5f, 28.5f)
            reflectiveQuadTo(720.0f, 320.0f)
            lineTo(240.0f, 320.0f)
            close()
            moveTo(320.0f, 200.0f)
            quadToRelative(-17.0f, 0.0f, -28.5f, -11.5f)
            reflectiveQuadTo(280.0f, 160.0f)
            quadToRelative(0.0f, -17.0f, 11.5f, -28.5f)
            reflectiveQuadTo(320.0f, 120.0f)
            horizontalLineToRelative(320.0f)
            quadToRelative(17.0f, 0.0f, 28.5f, 11.5f)
            reflectiveQuadTo(680.0f, 160.0f)
            quadToRelative(0.0f, 17.0f, -11.5f, 28.5f)
            reflectiveQuadTo(640.0f, 200.0f)
            lineTo(320.0f, 200.0f)
            close()
            moveTo(267.0f, 760.0f)
            horizontalLineToRelative(426.0f)
            horizontalLineToRelative(-426.0f)
            close()
        }
    }
}

/**
 * An icon with two bills
 */
val Icons.Filled.Money: ImageVector by lazy {
    materialIcon("Filled.Money", viewportHeight = 960f, viewportWith = 960f) {
        materialPath {
            moveTo(560.0f, 520.0f)
            quadToRelative(-50.0f, 0.0f, -85.0f, -35.0f)
            reflectiveQuadToRelative(-35.0f, -85.0f)
            quadToRelative(0.0f, -50.0f, 35.0f, -85.0f)
            reflectiveQuadToRelative(85.0f, -35.0f)
            quadToRelative(50.0f, 0.0f, 85.0f, 35.0f)
            reflectiveQuadToRelative(35.0f, 85.0f)
            quadToRelative(0.0f, 50.0f, -35.0f, 85.0f)
            reflectiveQuadToRelative(-85.0f, 35.0f)
            close()
            moveTo(280.0f, 640.0f)
            quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
            reflectiveQuadTo(200.0f, 560.0f)
            verticalLineToRelative(-320.0f)
            quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
            reflectiveQuadTo(280.0f, 160.0f)
            horizontalLineToRelative(560.0f)
            quadToRelative(33.0f, 0.0f, 56.5f, 23.5f)
            reflectiveQuadTo(920.0f, 240.0f)
            verticalLineToRelative(320.0f)
            quadToRelative(0.0f, 33.0f, -23.5f, 56.5f)
            reflectiveQuadTo(840.0f, 640.0f)
            lineTo(280.0f, 640.0f)
            close()
            moveTo(360.0f, 560.0f)
            horizontalLineToRelative(400.0f)
            quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
            reflectiveQuadTo(840.0f, 480.0f)
            verticalLineToRelative(-160.0f)
            quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
            reflectiveQuadTo(760.0f, 240.0f)
            lineTo(360.0f, 240.0f)
            quadToRelative(0.0f, 33.0f, -23.5f, 56.5f)
            reflectiveQuadTo(280.0f, 320.0f)
            verticalLineToRelative(160.0f)
            quadToRelative(33.0f, 0.0f, 56.5f, 23.5f)
            reflectiveQuadTo(360.0f, 560.0f)
            close()
            moveTo(800.0f, 800.0f)
            lineTo(120.0f, 800.0f)
            quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
            reflectiveQuadTo(40.0f, 720.0f)
            verticalLineToRelative(-440.0f)
            horizontalLineToRelative(80.0f)
            verticalLineToRelative(440.0f)
            horizontalLineToRelative(680.0f)
            verticalLineToRelative(80.0f)
            close()
            moveTo(280.0f, 560.0f)
            verticalLineToRelative(-320.0f)
            verticalLineToRelative(320.0f)
            close()
        }
    }
}

/**
 * An icon with a clock, which has an outline that's a circular arrow pointing counter-clockwise
 * It also has a gear in the bottom right corner
 */
val Icons.Filled.HistorySettings: ImageVector by lazy {
    materialIcon("Filled.HistorySettings", viewportHeight = 960f, viewportWith = 960f) {
        materialPath {
            moveTo(120.0f, 400.0f)
            verticalLineToRelative(-240.0f)
            horizontalLineToRelative(80.0f)
            verticalLineToRelative(94.0f)
            quadToRelative(51.0f, -64.0f, 124.5f, -99.0f)
            reflectiveQuadTo(480.0f, 120.0f)
            quadToRelative(150.0f, 0.0f, 255.0f, 105.0f)
            reflectiveQuadToRelative(105.0f, 255.0f)
            horizontalLineToRelative(-80.0f)
            quadToRelative(0.0f, -117.0f, -81.5f, -198.5f)
            reflectiveQuadTo(480.0f, 200.0f)
            quadToRelative(-69.0f, 0.0f, -129.0f, 32.0f)
            reflectiveQuadToRelative(-101.0f, 88.0f)
            horizontalLineToRelative(110.0f)
            verticalLineToRelative(80.0f)
            lineTo(120.0f, 400.0f)
            close()
            moveTo(122.0f, 520.0f)
            horizontalLineToRelative(82.0f)
            quadToRelative(12.0f, 93.0f, 76.5f, 157.5f)
            reflectiveQuadTo(435.0f, 756.0f)
            lineToRelative(48.0f, 84.0f)
            quadToRelative(-138.0f, 0.0f, -242.0f, -91.5f)
            reflectiveQuadTo(122.0f, 520.0f)
            close()
            moveTo(534.0f, 590.0f)
            lineTo(440.0f, 496.0f)
            verticalLineToRelative(-216.0f)
            horizontalLineToRelative(80.0f)
            verticalLineToRelative(184.0f)
            lineToRelative(56.0f, 56.0f)
            lineToRelative(-42.0f, 70.0f)
            close()
            moveTo(719.0f, 960.0f)
            lineToRelative(-12.0f, -60.0f)
            quadToRelative(-12.0f, -5.0f, -22.5f, -10.5f)
            reflectiveQuadTo(663.0f, 876.0f)
            lineToRelative(-58.0f, 18.0f)
            lineToRelative(-40.0f, -68.0f)
            lineToRelative(46.0f, -40.0f)
            quadToRelative(-2.0f, -13.0f, -2.0f, -26.0f)
            reflectiveQuadToRelative(2.0f, -26.0f)
            lineToRelative(-46.0f, -40.0f)
            lineToRelative(40.0f, -68.0f)
            lineToRelative(58.0f, 18.0f)
            quadToRelative(11.0f, -8.0f, 21.5f, -13.5f)
            reflectiveQuadTo(707.0f, 620.0f)
            lineToRelative(12.0f, -60.0f)
            horizontalLineToRelative(80.0f)
            lineToRelative(12.0f, 60.0f)
            quadToRelative(12.0f, 5.0f, 23.0f, 11.5f)
            reflectiveQuadToRelative(21.0f, 14.5f)
            lineToRelative(58.0f, -20.0f)
            lineToRelative(40.0f, 70.0f)
            lineToRelative(-46.0f, 40.0f)
            quadToRelative(2.0f, 13.0f, 2.0f, 25.0f)
            reflectiveQuadToRelative(-2.0f, 25.0f)
            lineToRelative(46.0f, 40.0f)
            lineToRelative(-40.0f, 68.0f)
            lineToRelative(-58.0f, -18.0f)
            quadToRelative(-11.0f, 8.0f, -21.5f, 13.5f)
            reflectiveQuadTo(811.0f, 900.0f)
            lineTo(799.0f, 960.0f)
            horizontalLineToRelative(-80.0f)
            close()
            moveTo(759.0f, 840.0f)
            quadToRelative(33.0f, 0.0f, 56.5f, -23.5f)
            reflectiveQuadTo(839.0f, 760.0f)
            quadToRelative(0.0f, -33.0f, -23.5f, -56.5f)
            reflectiveQuadTo(759.0f, 680.0f)
            quadToRelative(-33.0f, 0.0f, -56.5f, 23.5f)
            reflectiveQuadTo(679.0f, 760.0f)
            quadToRelative(0.0f, 33.0f, 23.5f, 56.5f)
            reflectiveQuadTo(759.0f, 840.0f)
            close()
        }
    }
}

/**
 * An icon with a 3x3 grid of buttons, and a pencil in the bottom right corner
 */
val Icons.Filled.ButtonPencil: ImageVector by lazy {
    materialIcon("Filled.ButtonPencil", viewportHeight = 960f, viewportWith = 960f) {
        materialPath {
            moveTo(240.0f, 800.0f)
            quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
            reflectiveQuadTo(160.0f, 720.0f)
            quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
            reflectiveQuadTo(240.0f, 640.0f)
            quadToRelative(33.0f, 0.0f, 56.5f, 23.5f)
            reflectiveQuadTo(320.0f, 720.0f)
            quadToRelative(0.0f, 33.0f, -23.5f, 56.5f)
            reflectiveQuadTo(240.0f, 800.0f)
            close()
            moveTo(240.0f, 560.0f)
            quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
            reflectiveQuadTo(160.0f, 480.0f)
            quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
            reflectiveQuadTo(240.0f, 400.0f)
            quadToRelative(33.0f, 0.0f, 56.5f, 23.5f)
            reflectiveQuadTo(320.0f, 480.0f)
            quadToRelative(0.0f, 33.0f, -23.5f, 56.5f)
            reflectiveQuadTo(240.0f, 560.0f)
            close()
            moveTo(240.0f, 320.0f)
            quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
            reflectiveQuadTo(160.0f, 240.0f)
            quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
            reflectiveQuadTo(240.0f, 160.0f)
            quadToRelative(33.0f, 0.0f, 56.5f, 23.5f)
            reflectiveQuadTo(320.0f, 240.0f)
            quadToRelative(0.0f, 33.0f, -23.5f, 56.5f)
            reflectiveQuadTo(240.0f, 320.0f)
            close()
            moveTo(480.0f, 320.0f)
            quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
            reflectiveQuadTo(400.0f, 240.0f)
            quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
            reflectiveQuadTo(480.0f, 160.0f)
            quadToRelative(33.0f, 0.0f, 56.5f, 23.5f)
            reflectiveQuadTo(560.0f, 240.0f)
            quadToRelative(0.0f, 33.0f, -23.5f, 56.5f)
            reflectiveQuadTo(480.0f, 320.0f)
            close()
            moveTo(720.0f, 320.0f)
            quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
            reflectiveQuadTo(640.0f, 240.0f)
            quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
            reflectiveQuadTo(720.0f, 160.0f)
            quadToRelative(33.0f, 0.0f, 56.5f, 23.5f)
            reflectiveQuadTo(800.0f, 240.0f)
            quadToRelative(0.0f, 33.0f, -23.5f, 56.5f)
            reflectiveQuadTo(720.0f, 320.0f)
            close()
            moveTo(480.0f, 560.0f)
            quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
            reflectiveQuadTo(400.0f, 480.0f)
            quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
            reflectiveQuadTo(480.0f, 400.0f)
            quadToRelative(33.0f, 0.0f, 56.5f, 23.5f)
            reflectiveQuadTo(560.0f, 480.0f)
            quadToRelative(0.0f, 33.0f, -23.5f, 56.5f)
            reflectiveQuadTo(480.0f, 560.0f)
            close()
            moveTo(520.0f, 800.0f)
            verticalLineToRelative(-123.0f)
            lineToRelative(221.0f, -220.0f)
            quadToRelative(9.0f, -9.0f, 20.0f, -13.0f)
            reflectiveQuadToRelative(22.0f, -4.0f)
            quadToRelative(12.0f, 0.0f, 23.0f, 4.5f)
            reflectiveQuadToRelative(20.0f, 13.5f)
            lineToRelative(37.0f, 37.0f)
            quadToRelative(8.0f, 9.0f, 12.5f, 20.0f)
            reflectiveQuadToRelative(4.5f, 22.0f)
            quadToRelative(0.0f, 11.0f, -4.0f, 22.5f)
            reflectiveQuadTo(863.0f, 580.0f)
            lineTo(643.0f, 800.0f)
            lineTo(520.0f, 800.0f)
            close()
            moveTo(820.0f, 537.0f)
            lineTo(783.0f, 500.0f)
            lineTo(820.0f, 537.0f)
            close()
            moveTo(580.0f, 740.0f)
            horizontalLineToRelative(38.0f)
            lineToRelative(121.0f, -122.0f)
            lineToRelative(-18.0f, -19.0f)
            lineToRelative(-19.0f, -18.0f)
            lineToRelative(-122.0f, 121.0f)
            verticalLineToRelative(38.0f)
            close()
            moveTo(721.0f, 599.0f)
            lineTo(702.0f, 581.0f)
            lineTo(739.0f, 618.0f)
            lineTo(721.0f, 599.0f)
            close()
        }
    }
}

/**
 * Person with a plus on the right
 */
val Icons.Filled.PersonAdd: ImageVector by lazy {
    materialIcon("Filled.PersonAdd", viewportHeight = 960f, viewportWith = 960f) {
        materialPath {
            moveTo(720.0f, 560.0f)
            verticalLineToRelative(-120.0f)
            lineTo(600.0f, 440.0f)
            verticalLineToRelative(-80.0f)
            horizontalLineToRelative(120.0f)
            verticalLineToRelative(-120.0f)
            horizontalLineToRelative(80.0f)
            verticalLineToRelative(120.0f)
            horizontalLineToRelative(120.0f)
            verticalLineToRelative(80.0f)
            lineTo(800.0f, 440.0f)
            verticalLineToRelative(120.0f)
            horizontalLineToRelative(-80.0f)
            close()
            moveTo(360.0f, 480.0f)
            quadToRelative(-66.0f, 0.0f, -113.0f, -47.0f)
            reflectiveQuadToRelative(-47.0f, -113.0f)
            quadToRelative(0.0f, -66.0f, 47.0f, -113.0f)
            reflectiveQuadToRelative(113.0f, -47.0f)
            quadToRelative(66.0f, 0.0f, 113.0f, 47.0f)
            reflectiveQuadToRelative(47.0f, 113.0f)
            quadToRelative(0.0f, 66.0f, -47.0f, 113.0f)
            reflectiveQuadToRelative(-113.0f, 47.0f)
            close()
            moveTo(40.0f, 800.0f)
            verticalLineToRelative(-112.0f)
            quadToRelative(0.0f, -34.0f, 17.5f, -62.5f)
            reflectiveQuadTo(104.0f, 582.0f)
            quadToRelative(62.0f, -31.0f, 126.0f, -46.5f)
            reflectiveQuadTo(360.0f, 520.0f)
            quadToRelative(66.0f, 0.0f, 130.0f, 15.5f)
            reflectiveQuadTo(616.0f, 582.0f)
            quadToRelative(29.0f, 15.0f, 46.5f, 43.5f)
            reflectiveQuadTo(680.0f, 688.0f)
            verticalLineToRelative(112.0f)
            lineTo(40.0f, 800.0f)
            close()
            moveTo(120.0f, 720.0f)
            horizontalLineToRelative(480.0f)
            verticalLineToRelative(-32.0f)
            quadToRelative(0.0f, -11.0f, -5.5f, -20.0f)
            reflectiveQuadTo(580.0f, 654.0f)
            quadToRelative(-54.0f, -27.0f, -109.0f, -40.5f)
            reflectiveQuadTo(360.0f, 600.0f)
            quadToRelative(-56.0f, 0.0f, -111.0f, 13.5f)
            reflectiveQuadTo(140.0f, 654.0f)
            quadToRelative(-9.0f, 5.0f, -14.5f, 14.0f)
            reflectiveQuadToRelative(-5.5f, 20.0f)
            verticalLineToRelative(32.0f)
            close()
            moveTo(360.0f, 400.0f)
            quadToRelative(33.0f, 0.0f, 56.5f, -23.5f)
            reflectiveQuadTo(440.0f, 320.0f)
            quadToRelative(0.0f, -33.0f, -23.5f, -56.5f)
            reflectiveQuadTo(360.0f, 240.0f)
            quadToRelative(-33.0f, 0.0f, -56.5f, 23.5f)
            reflectiveQuadTo(280.0f, 320.0f)
            quadToRelative(0.0f, 33.0f, 23.5f, 56.5f)
            reflectiveQuadTo(360.0f, 400.0f)
            close()
            moveTo(360.0f, 320.0f)
            close()
            moveTo(360.0f, 720.0f)
            close()
        }
    }
}