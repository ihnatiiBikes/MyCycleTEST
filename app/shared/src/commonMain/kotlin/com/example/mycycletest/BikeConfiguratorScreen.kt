package com.example.mycycletest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

@Composable
fun BikeConfiguratorScreen(parts: MutableList<ConfiguratorPart>, onBack: () -> Unit) {
    // Filter only Frame and Wheels parts
    val frames = parts.filter { it.type == "Frame" }
    val wheels = parts.filter { it.type == "Wheels" }

    var selectedFrameId by remember { mutableStateOf<String?>(null) }
    var selectedWheelsId by remember { mutableStateOf<String?>(null) }

    val selectedFrame = frames.find { it.id == selectedFrameId } ?: frames.firstOrNull()
    val selectedWheels = wheels.find { it.id == selectedWheelsId } ?: wheels.firstOrNull()

    val maybeFrame: ConfiguratorPart? = selectedFrame
    val maybeWheels: ConfiguratorPart? = selectedWheels

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = onBack) {
                Text("← Back to Store")
            }
            Text("Bike Configurator & Setup", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Customer Assembled Bike View", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        // Selection Dropdowns
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                var frameDropdownExpanded by remember { mutableStateOf(false) }
                OutlinedButton(
                    onClick = { frameDropdownExpanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Frame: ${selectedFrame?.name ?: "Select Frame"}")
                }
                DropdownMenu(expanded = frameDropdownExpanded, onDismissRequest = { frameDropdownExpanded = false }) {
                    frames.forEach { frame ->
                        DropdownMenuItem(
                            text = { Text(frame.name) },
                            onClick = {
                                selectedFrameId = frame.id
                                frameDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                var wheelsDropdownExpanded by remember { mutableStateOf(false) }
                OutlinedButton(
                    onClick = { wheelsDropdownExpanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Wheels: ${selectedWheels?.name ?: "Select Wheels"}")
                }
                DropdownMenu(expanded = wheelsDropdownExpanded, onDismissRequest = { wheelsDropdownExpanded = false }) {
                    wheels.forEach { wheel ->
                        DropdownMenuItem(
                            text = { Text(wheel.name) },
                            onClick = {
                                selectedWheelsId = wheel.id
                                wheelsDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ASSEMBLED PREVIEW CARD (Blue Customer View with Layered Alignment)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            BikeCanvas(
                frame = maybeFrame,
                wheels = maybeWheels,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFEBF5FB))
            )
        }
    }
}

@Composable
private fun BikeCanvas(
    frame: ConfiguratorPart?,
    wheels: ConfiguratorPart?,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val containerWpx = with(density) { maxWidth.toPx() }
        val containerHpx = with(density) { maxHeight.toPx() }

        val frameBitmap = rememberImageBitmap(frame?.imageUrl)
        val frontWheelBitmap = rememberImageBitmap(wheels?.imageUrl)
        val rearWheelBitmap = rememberImageBitmap(wheels?.rearImageUrl ?: wheels?.imageUrl)

        val frameScale = if (frameBitmap != null) {
            min(containerWpx / frameBitmap.width, containerHpx / frameBitmap.height)
        } else 0f
        val displayedFrameWpx = (frameBitmap?.width ?: 0) * frameScale
        val displayedFrameHpx = (frameBitmap?.height ?: 0) * frameScale
        val frameTopLeftXpx = (containerWpx - displayedFrameWpx) / 2f
        val frameTopLeftYpx = (containerHpx - displayedFrameHpx) / 2f

        val rearAxle = Offset(
            x = frameTopLeftXpx + (frame?.jointCoords?.getOrNull(0)?.x ?: 0.5f) * displayedFrameWpx,
            y = frameTopLeftYpx + (frame?.jointCoords?.getOrNull(0)?.y ?: 0.5f) * displayedFrameHpx
        )
        val frontAxle = Offset(
            x = frameTopLeftXpx + (frame?.jointCoords?.getOrNull(1)?.x ?: 0.5f) * displayedFrameWpx,
            y = frameTopLeftYpx + (frame?.jointCoords?.getOrNull(1)?.y ?: 0.5f) * displayedFrameHpx
        )

        val wheelScaleValue = wheels?.wheelScale ?: 0.45f
        val wheelSizePx = displayedFrameHpx * wheelScaleValue

        // Rear wheel (behind frame)
        if (rearWheelBitmap != null && wheels != null) {
            val joint = wheels.jointCoords.getOrNull(0) ?: JointCoord(0.5f, 0.5f)
            Box(
                modifier = Modifier
                    .offset(
                        x = with(density) { (rearAxle.x - joint.x * wheelSizePx).toDp() },
                        y = with(density) { (rearAxle.y - joint.y * wheelSizePx).toDp() }
                    )
                    .size(with(density) { wheelSizePx.toDp() })
            ) {
                Image(
                    painter = remember(rearWheelBitmap) { BitmapPainter(rearWheelBitmap) },
                    contentDescription = "Rear Wheel",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }

        // Front wheel (behind frame)
        if (frontWheelBitmap != null && wheels != null) {
            val joint = wheels.jointCoords.getOrNull(1) ?: JointCoord(0.5f, 0.5f)
            Box(
                modifier = Modifier
                    .offset(
                        x = with(density) { (frontAxle.x - joint.x * wheelSizePx).toDp() },
                        y = with(density) { (frontAxle.y - joint.y * wheelSizePx).toDp() }
                    )
                    .size(with(density) { wheelSizePx.toDp() })
            ) {
                Image(
                    painter = remember(frontWheelBitmap) { BitmapPainter(frontWheelBitmap) },
                    contentDescription = "Front Wheel",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }

        // Frame on top
        if (frameBitmap != null && frame != null) {
            Box(
                modifier = Modifier
                    .offset(
                        x = with(density) { frameTopLeftXpx.toDp() },
                        y = with(density) { frameTopLeftYpx.toDp() }
                    )
                    .size(
                        with(density) { displayedFrameWpx.toDp() },
                        with(density) { displayedFrameHpx.toDp() }
                    )
            ) {
                Image(
                    painter = remember(frameBitmap) { BitmapPainter(frameBitmap) },
                    contentDescription = "Frame",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

// Data class for ConfiguratorPart with DB-friendly fields
data class ConfiguratorPart(
    val id: String,
    val name: String,
    val type: String,  // "Frame" or "Wheels"
    val imageUrl: String,  // front/main image URL stored in DB
    val rearImageUrl: String? = null,  // rear wheel image URL (for Wheels parts)
    val wheelScale: Float = 0.45f,     // wheel size as fraction of displayed frame height
    val jointCoords: List<JointCoord> = emptyList()
)

// JointCoord stores x,y as floats
data class JointCoord(
    val x: Float,
    val y: Float
)
