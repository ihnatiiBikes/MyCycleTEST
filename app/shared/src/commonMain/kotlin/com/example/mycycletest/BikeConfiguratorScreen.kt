package com.example.mycycletest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
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

    // Track whether any part has been uploaded/saved (connection joints show only after admin upload)
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
            Text("Interactive Bike Configurator", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { zoomScale = (zoomScale - 0.2f).coerceAtLeast(0.8f) }) { Text("Zoom -") }
                Button(onClick = { zoomScale = (zoomScale + 0.2f).coerceAtMost(2.5f) }) { Text("Zoom +") }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Selection Dropdowns (Frame, Wheels, Groupset)
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

        Spacer(modifier = Modifier.height(24.dp))

        // Visual Assembly Canvas - displays pictures of picked parts
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
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Frame picture display - show image URL or placeholder
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE9ECEF))
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            // Show frame info - no try/catch, no smart cast on delegated property
                            if (maybeFrame != null && maybeFrame.imageUrl.isNotBlank()) {
                                Text("Frame Image URL: ${maybeFrame.imageUrl}", fontSize = 12.sp, color = Color(0xFF666666))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Frame: ${maybeFrame?.name ?: "None"}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            } else {
                                Text("🖼️", fontSize = 48.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Frame Picture: ${selectedFrame?.name ?: "None"}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Add a frame via Admin → Upload Picture & Save", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Picked Wheels & Groupset overlay
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Wheels card
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(110.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE9ECEF))
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                if (maybeWheels != null && maybeWheels.imageUrl.isNotBlank()) {
                                    Text("Wheels Image URL: ${maybeWheels.imageUrl}", fontSize = 11.sp, color = Color(0xFF666666))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Wheels: ${maybeWheels?.name ?: "None"}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Added: selected", fontSize = 10.sp, color = Color.Gray, maxLines = 1)
                                } else {
                                    Text("🛞", fontSize = 28.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Wheels: ${selectedWheels?.name ?: "None"}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("Select wheels to appear here", fontSize = 10.sp, color = Color.Gray, maxLines = 1)
                                }
                            }
                        }

                        // Groupset card
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(110.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE9ECEF))
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                if (maybeGroupset != null && maybeGroupset.imageUrl.isNotBlank()) {
                                    Text("Groupset Image URL: ${maybeGroupset.imageUrl}", fontSize = 11.sp, color = Color(0xFF666666))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Groupset: ${maybeGroupset?.name ?: "None"}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Added: selected", fontSize = 10.sp, color = Color.Gray, maxLines = 1)
                                } else {
                                    Text("⚙️", fontSize = 28.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Groupset: ${selectedGroupset?.name ?: "None"}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("Select groupset to appear here", fontSize = 10.sp, color = Color.Gray, maxLines = 1)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Connection Joints & Contact Points - only appears after parts uploaded/saved in admin
        if (showConnectionJoints || selectedFrame != null || selectedWheels != null || selectedGroupset != null) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(400.dp),
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
                    // Header showing when joints appeared
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
                        // Show count of configured parts
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
                            "No contact points configured. Upload a part via Admin → Add New Part to configure connection joints.",
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
                                    // Checkmark shows joint is linked/connected
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