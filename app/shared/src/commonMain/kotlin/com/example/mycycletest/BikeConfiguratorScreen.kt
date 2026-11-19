package com.example.mycycletest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BikeConfiguratorScreen(parts: List<ConfiguratorPart>, onBack: () -> Unit) {
    // Filter only Frame and Wheels parts (delete groupset)
    val frames = parts.filter { it.type == "Frame" }
    val wheels = parts.filter { it.type == "Wheels" }

    var selectedFrame by remember { mutableStateOf(frames.firstOrNull()) }
    var selectedWheels by remember { mutableStateOf(wheels.firstOrNull()) }
    var zoomScale by remember { mutableStateOf(1f) }

    var frameDropdownExpanded by remember { mutableStateOf(false) }
    var wheelsDropdownExpanded by remember { mutableStateOf(false) }

    // Data model for joint points stored as "x:y" strings in DB
    data class JointCoord(
        val x: Float,
        val y: Float
    )

    // Resolve nullable parts safely
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
            Text("Bike Configurator", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Selection Dropdowns - only Frame and Wheels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpacedBy(24.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
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
                                selectedFrame = frame
                                frameDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Box(modifier = Modifier.weight(1f)) {
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
                                selectedWheels = wheel
                                wheelsDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // SINGLE CARD: Bike image with layered frame on top, wheels behind
        // Frame stores photoUrl + "x:y" joint coordinates as strings
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8F9FA))
                    .padding(0.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Start
                ) {
                    // --- FRAME (top layer) ---
                    // Frame image with name at bottom
                    if (maybeFrame != null && maybeFrame.imageUrl.isNotBlank()) {
                        // Display frame image - full width, name at bottom
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .background(Color(0xFFE0E0E0)),
                            contentAlignment = Alignment.Center
                        ) {
                            // Frame name at BOTTOM of image
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .align(Alignment.BottomCenter),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    maybeFrame.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    background = Color(0xFFFFFFFF).copyAlpha(0.8f)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            // Image placeholder with URL info
                            Text(
                                "Frame Photo URL stored in DB",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                            )
                            // Joint coordinates info
 if (maybeFrame.jointCoords != null && maybeFrame.jointCoords.isNotEmpty()) {
                                Text(
                                    "Joint coords: ${maybeFrame.jointCoords.joinToString(",") { "${it.x}:${it.y}" }}",
                                    fontSize = 11.sp,
                                    color = Color.Blue
                                )
                            }
                        }
                    } else {
                        // Placeholder when no frame selected
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "🖼️ Frame",
                                fontSize = 24.sp,
                                color = Color.White,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .paddingBottom(8.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Add frame via Admin", fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- WHEELS (behind frame layer) ---
                    // Wheels image with name at bottom
                    if (maybeWheels != null && maybeWheels.imageUrl.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(Color(0xFFD0D0D0)),
                            contentAlignment = Alignment.Center
                        ) {
                            // Wheels name at BOTTOM of image
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .align(Alignment.BottomCenter),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    maybeWheels.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    background = Color(0xFFFFFFFF).copyAlpha(0.8f)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            // Image placeholder with URL info
                            Text(
                                "Wheels Photo URL stored in DB",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                            )
                            // Joint coordinates info
 if (maybeWheels.jointCoords != null && maybeWheels.jointCoords.isNotEmpty()) {
                                Text(
                                    "Joint coords: ${maybeWheels.jointCoords.joinToString(",") { "${it.x}:${it.y}" }}",
                                    fontSize = 11.sp,
                                    color = Color.Blue
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "🛞 Wheels",
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .paddingBottom(8.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Select wheels", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Connection info - appears when parts are configured
        if (selectedFrame != null || selectedWheels != null) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Frame joint info
 if (maybeFrame != null && maybeFrame.jointCoords != null && maybeFrame.jointCoords.isNotEmpty()) {
                        Text(
                            "Frame joints: ${maybeFrame.jointCoords.size} points",
                            fontSize = 12.sp,
                            color = Color(0xFF34C759),
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text("Frame joints: none", fontSize = 12.sp, color = Color.Gray)
                    }
                    // Wheels joint info
 if (maybeWheels != null && maybeWheels.jointCoords != null && maybeWheels.jointCoords.isNotEmpty()) {
                        Text(
                            "Wheels joints: ${maybeWheels.jointCoords.size} points",
                            fontSize = 12.sp,
                            color = Color(0xFF34C759),
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text("Wheels joints: none", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

// Data class for ConfiguratorPart with DB-friendly fields
data class ConfiguratorPart(
    val id: String,
    val name: String,
    val type: String,  // "Frame" or "Wheels"
    val imageUrl: String,  // URL stored in DB
    // Joint coordinates stored as "x:y" strings in DB (no larger than 16000x16000)
    val jointCoords: List<JointCoord> = emptyList()  // Empty by default, populated when user adds points
)

// JointCoord stores x,y as floats, will be saved as "x:y" string in DB
data class JointCoord(
    val x: Float,
    val y: Float
)