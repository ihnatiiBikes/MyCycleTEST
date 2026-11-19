package com.example.mycycletest

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BikeConfiguratorScreen(parts: List<ConfiguratorPart>, onBack: () -> Unit) {
    val frames = parts.filter { it.type == "Frame" }
    val groupsets = parts.filter { it.type == "Groupset" }
    val wheels = parts.filter { it.type == "Wheels" }

    var selectedFrame by remember { mutableStateOf(frames.firstOrNull()) }
    var selectedGroupset by remember { mutableStateOf(groupsets.firstOrNull()) }
    var selectedWheels by remember { mutableStateOf(wheels.firstOrNull()) }
    var zoomScale by remember { mutableStateOf(1f) }

    var frameDropdownExpanded by remember { mutableStateOf(false) }
    var wheelsDropdownExpanded by remember { mutableStateOf(false) }
    var groupsetDropdownExpanded by remember { mutableStateOf(false) }

    var showConnectionJoints by remember { mutableStateOf(parts.isNotEmpty()) }

    // Resolve nullable parts safely - avoid smart cast on delegated properties
    val maybeFrame: ConfiguratorPart? = selectedFrame
    val maybeWheels: ConfiguratorPart? = selectedWheels
    val maybeGroupset: ConfiguratorPart? = selectedGroupset

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

        // Selection Dropdowns
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
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

            Box(modifier = Modifier.weight(1f)) {
                OutlinedButton(
                    onClick = { groupsetDropdownExpanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Groupset: ${selectedGroupset?.name ?: "Select Groupset"}")
                }
                DropdownMenu(expanded = groupsetDropdownExpanded, onDismissRequest = { groupsetDropdownExpanded = false }) {
                    groupsets.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(group.name) },
                            onClick = {
                                selectedGroupset = group
                                groupsetDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Visual Assembly Canvas - layered: frame on top, wheels under, groupset at bottom
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(560.dp),
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
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .background(Color(0xFFE0E0E0)),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
                    ) {
                        // Frame image - full bleed, name at bottom
                        if (maybeFrame != null && maybeFrame.imageUrl.isNotBlank()) {
                            // Show frame image with URL
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Gray),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Frame: ${maybeFrame.name}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    // Background bar at bottom for name
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .paddingBottom(8.dp),
                                    background = Color(0xFFFFFFFF).copyAlpha(0.8f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Image: ${maybeFrame.imageUrl.substring(0, if (maybeFrame.imageUrl.length > 50) 50 else maybeFrame.imageUrl.length)}...",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        } else {
                            // Placeholder
                            Box(
                                modifier = Modifier.fillMaxSize().background(Color.Gray),
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
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- WHEELS (middle layer) ---
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(Color(0xFFD0D0D0)),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFD0D0D0))
                    ) {
                        // Wheels image - full bleed, name at bottom
                        if (maybeWheels != null && maybeWheels.imageUrl.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Gray),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Wheels: ${maybeWheels.name}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .paddingBottom(8.dp),
                                    background = Color(0xFFFFFFFF).copyAlpha(0.8f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Image: ${maybeWheels.imageUrl.substring(0, if (maybeWheels.imageUrl.length > 50) 50 else maybeWheels.imageUrl.length)}...",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize().background(Color.Gray),
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

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- GROUpsET (bottom layer) ---
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(Color(0xFFC0C0C0)),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFC0C0C0))
                    ) {
                        // Groupset image - full bleed, name at bottom
                        if (maybeGroupset != null && maybeGroupset.imageUrl.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Gray),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Groupset: ${maybeGroupset.name}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .paddingBottom(8.dp),
                                    background = Color(0xFFFFFFFF).copyAlpha(0.8f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Image: ${maybeGroupset.imageUrl.substring(0, if (maybeGroupset.imageUrl.length > 50) 50 else maybeGroupset.imageUrl.length)}...",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize().background(Color.Gray),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "⚙️ Groupset",
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .paddingBottom(8.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Select groupset", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Connection Joints panel - appears after parts configured
        if (showConnectionJoints || selectedFrame != null || selectedWheels != null || selectedGroupset != null) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(300.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Connection Joints & Contact Points",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (parts.isNotEmpty()) {
                            Text(
                                "• ${parts.size} part(s) configured",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    val allPoints = mutableListOf<ConnectionPoint>()
                    selectedFrame?.let { allPoints.addAll(it.points) }
                    selectedWheels?.let { allPoints.addAll(it.points) }
                    selectedGroupset?.let { allPoints.addAll(it.points) }

                    if (allPoints.isEmpty()) {
                        Text(
                            "No contact points configured. Add parts via Admin to configure connection joints.",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    } else {
                        allPoints.forEach { point ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(point.name, fontWeight = FontWeight.Bold)
                                        Text(
                                            "Position: X ${(point.x * 100).toInt()}% | Y ${(point.y * 100).toInt()}%",
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    Text(
                                        "✓ Connected",
                                        color = Color(0xFF34C759),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}