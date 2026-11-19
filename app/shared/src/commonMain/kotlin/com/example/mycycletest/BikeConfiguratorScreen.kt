package com.example.mycycletest

import androidx.compose.foundation.Background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.GraphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.input.pointerInput
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawImage
import androidx.compose.ui.graphics.image.AsyncImage
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.animateFloatAsState

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

    // Joint points state for selected frame/wheels - stores (x,y) coords and whether active
    var framePoints by remember { mutableStateOf(mutableListOf<JointPoint>() ) }
    var wheelsPoints by remember { mutableStateOf(mutableListOf<JointPoint>() ) }

    // Resolve nullable parts safely - avoid smart cast on delegated properties
    val maybeFrame: ConfiguratorPart? = selectedFrame
    val maybeWheels: ConfiguratorPart? = selectedWheels
    val maybeGroupset: ConfiguratorPart? = selectedGroupset

    // Track if we're in admin add-part mode
    var adminAddMode by remember { mutableStateOf(false) }
    var adminPartType by remember { mutableStateOf("Frame") }

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
                                // Initialize joint points for this frame
                                framePoints = mutableListOf(
                                    JointPoint("p1", "Front Derailleur", 0.45f, 0.6f),
                                    JointPoint("p2", "Rear Derailleur", 0.7f, 0.75f)
                                )
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
                                wheelsPoints = mutableListOf(
                                    JointPoint("w1", "Hub Axle", 0.5f, 0.5f)
                                )
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

        // --- Admin Add Part Screen ---
        if (adminAddMode) {
            AdminAddPartScreen(
                partType = adminPartType,
                onSave = { part ->
                    when (adminPartType) {
                        "Frame" -> {
                            val newPart = ConfiguratorPart(
                                id = "part-${System.currentTimeMillis()}",
                                name = part.name,
                                type = "Frame",
                                imageUrl = part.imageUrl,
                                points = part.points
                            )
                            parts.add(newPart)
                            // Initialize joint points
                            framePoints = mutableListOf(newPart.points)
                            adminAddMode = false
                        }
                        "Wheels" -> {
                            val newPart = ConfiguratorPart(
                                id = "part-${System.currentTimeMillis()}",
                                name = part.name,
                                type = "Wheels",
                                imageUrl = part.imageUrl,
                                points = part.points
                            )
                            parts.add(newPart)
                            wheelsPoints = mutableListOf(newPart.points)
                            adminAddMode = false
                        }
                    }
                },
                onCancel = { adminAddMode = false }
            )
            // Don't show the normal configurator UI while in admin add mode
            Spacer(modifier = Modifier.height(24.dp))
            // Back button inside admin screen will handle cancellation
            return@Composable BikeConfiguratorScreen(parts, onBack)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Visual Assembly Canvas ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8F9FA))
                    .padding(0.dp),  // No padding - let images fill space
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
                            .height(300.dp)
                            .background(Color(0xFFE0E0E0)),  // Light gray placeholder
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
                    ) {
                        // Frame image with joint points overlay
 FrameImageWithPoints(
                            imageUrl = maybeFrame?.imageUrl,
                            points = framePoints,
                            partName = maybeFrame?.name,
                            onPointAdd = { point ->
                                // Add joint point at click location
                                if (maybeFrame != null) {
                                    val updated = mutableListOf(framePoints)
                                    updated.add(point)
                                    framePoints = updated
                                }
                            }
                        )
                        // Frame name at BOTTOM of image
                        if (maybeFrame != null && maybeFrame.name.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.BottomCenter),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    maybeFrame.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF000000),
                                    background = Color(0xFFFFFFFF).copyAlpha(0.8f),
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- WHEELS (middle layer) ---
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color(0xFFD0D0D0)),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFD0D0D0))
                    ) {
                        // Wheels image with joint points overlay
                        WheelImageWithPoints(
                            imageUrl = maybeWheels?.imageUrl,
                            points = wheelsPoints,
                            partName = maybeWheels?.name,
                            onPointAdd = { point ->
                                if (maybeWheels != null) {
                                    val updated = mutableListOf(wheelsPoints)
                                    updated.add(point)
                                    wheelsPoints = updated
                                }
                            }
                        )
                        // Wheels name at BOTTOM of image
                        if (maybeWheels != null && maybeWheels.name.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.BottomCenter),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    maybeWheels.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF000000),
                                    background = Color(0xFFFFFFFF).copyAlpha(0.8f),
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- GROUpsET (bottom layer) ---
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(Color(0xFFC0C0C0)),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFC0C0C0))
                    ) {
                        // Groupset image with joint points overlay
                        GroupImageWithPoints(
                            imageUrl = maybeGroupset?.imageUrl,
                            points = mutableListOf<JointPoint>(),  // Groupsets have separate points
                            partName = maybeGroupset?.name,
                            onPointAdd = { }
                        )
                        // Groupset name at BOTTOM of image
                        if (maybeGroupset != null && maybeGroupset.name.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.BottomCenter),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    maybeGroupset.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF000000),
                                    background = Color(0xFFFFFFFF).copyAlpha(0.8f),
                                )
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
                    .height(350.dp),
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

// --- Data model for joint points ---

data class JointPoint(
    val id: String,
    val name: String,
    val x: Float,
    val y: Float
)

// --- Composable: Frame image with joint points overlay ---

@Composable
fun FrameImageWithPoints(
    imageUrl: String?,
    points: MutableList<JointPoint>,
    partName: String?,
    onPointAdd: (JointPoint) -> Unit
) {
    // Load image asynchronously
    var showPlaceholder by remember { mutableStateOf(true) }

    AsyncImage(
        model = if (imageUrl != null && imageUrl.isNotBlank()) { imageUrl } else { "" },
        placeholder = painterResource(id = androidx.compose.R.drawable.ic_launcher_foreground),
        contentDescription = "Frame configurator image",
        modifier = Modifier.fillMaxSize(),
        // Load callback
        contentScale = ContentScale.Crop
    ) { painter ->
        // Draw the image
        if (showPlaceholder) {
            // Show placeholder initially
            Box(modifier = Modifier.fillMaxSize()) {
                Text("🖼️", fontSize = 48.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Frame: ${partName ?: "None"}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        } else {
            // Draw the actual image with joint points overlay
            Image(
                painter = painter,
                contentDescription = "Frame",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            ) {
                // Draw joint points as red dots on top of the image
                val paint = Paint()
                paint.color = Color(0xFFFF0000)  // Red color
                paint.style = Paint.Style.Stroke
                paint.strokeWidth = 8f
                paint.isAntiAlias = true

                // Draw each joint point
                points.forEach { point ->
                    // Calculate position based on image dimensions
                    val imgWidth = 300.dp.toFloat()  // Approximate
                    val imgHeight = 200.dp.toFloat()
                    val x = (point.x * imgWidth).toInt()
                    val y = (point.y * imgHeight).toInt()

                    // Draw circle at joint point
                    Canvas(modifier = Modifier.offset(x = x - 16, y = y - 16)) {
                        drawCircle(
                            color = Color(0xFFFF0000),
                            radius = 12f,
                            style = StrokeStrokeStyle
                        )
                        // Draw label
                        drawText(
                            text = point.name,
                            x = 28f,
                            y = 28f,
                            textSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// --- Composable: Wheel image with joint points overlay ---

@Composable
fun WheelImageWithPoints(
    imageUrl: String?,
    points: MutableList<JointPoint>,
    partName: String?,
    onPointAdd: (JointPoint) -> Unit
) {
    var showPlaceholder by remember { mutableStateOf(true) }

    AsyncImage(
        model = if (imageUrl != null && imageUrl.isNotBlank()) { imageUrl } else { "" },
        placeholder = painterResource(id = androidx.compose.R.drawable.ic_launcher_foreground),
        contentDescription = "Wheel configurator image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    ) { painter ->
        if (showPlaceholder) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text("🛞", fontSize = 48.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Wheels: ${partName ?: "None"}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        } else {
            Image(
                painter = painter,
                contentDescription = "Wheels",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            ) {
                // Draw joint points as red dots
                val paint = Paint()
                paint.color = Color(0xFFFF0000)
                paint.style = Paint.Style.Stroke
                paint.strokeWidth = 6f
                paint.isAntiAlias = true

                points.forEach { point ->
                    val imgWidth = 200.dp.toFloat()
                    val imgHeight = 150.dp.toFloat()
                    val x = (point.x * imgWidth).toInt()
                    val y = (point.y * imgHeight).toInt()

                    Canvas(modifier = Modifier.offset(x = x - 12, y = y - 12)) {
                        drawCircle(
                            color = Color(0xFFFF0000),
                            radius = 10f,
                            style = StrokeStrokeStyle
                        )
                        drawText(
                            text = point.name,
                            x = 22f,
                            y = 22f,
                            textSize = 11.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// --- Composable: Groupset image with joint points overlay ---

@Composable
fun GroupImageWithPoints(
    imageUrl: String?,
    points: MutableList<JointPoint>,
    partName: String?,
    onPointAdd: (JointPoint) -> Unit
) {
    var showPlaceholder by remember { mutableStateOf(true) }

    AsyncImage(
        model = if (imageUrl != null && imageUrl.isNotBlank()) { imageUrl } else { "" },
        placeholder = painterResource(id = androidx.compose.R.drawable.ic_launcher_foreground),
        contentDescription = "Groupset configurator image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    ) { painter ->
        if (showPlaceholder) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text("⚙️", fontSize = 48.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Groupset: ${partName ?: "None"}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        } else {
            Image(
                painter = painter,
                contentDescription = "Groupset",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            ) {
                // Draw joint points
                val paint = Paint()
                paint.color = Color(0xFFFF0000)
                paint.style = Paint.Style.Stroke
                paint.strokeWidth = 6f
                paint.isAntiAlias = true

                points.forEach { point ->
                    val imgWidth = 150.dp.toFloat()
                    val imgHeight = 100.dp.toFloat()
                    val x = (point.x * imgWidth).toInt()
                    val y = (point.y * imgHeight).toInt()

                    Canvas(modifier = Modifier.offset(x = x - 12, y = y - 12)) {
                        drawCircle(
                            color = Color(0xFFFF0000),
                            radius = 10f,
                            style = StrokeStrokeStyle
                        )
                        drawText(
                            text = point.name,
                            x = 22f,
                            y = 22f,
                            textSize = 11.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// --- Composable: Admin Add Part Screen ---

@Composable
fun AdminAddPartScreen(
    partType: String,
    onSave: (ConfiguratorPart) -> Unit,
    onCancel: () -> Unit
) {
    var partName by remember { mutableStateOf("") }
    var partImageUrl by remember { mutableStateOf("https://www.mycycle.ie/v/vspfiles/photos/CUSTOM-PART-2.jpg") }

    Column(
        modifier = Modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Admin: Add New Configurator Part",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = partName,
            onValueChange = { partName = it },
            label = { Text("Part Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = partImageUrl,
            onValueChange = { partImageUrl = it },
            label = { Text("Picture URL") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spaceEvenly) {
            Button(
                onClick = {
                    val newPart = ConfiguratorPart(
                        id = "part-new",
                        name = partName,
                        type = partType,
                        imageUrl = partImageUrl,
                        points = if (partType == "Frame") {
                            mutableListOf(
                                JointPoint("p1", "Front Joint", 0.5f, 0.5f),
                                JointPoint("p2", "Rear Joint", 0.7f, 0.7f)
                            )
                        } else {
                            mutableListOf(
                                JointPoint("w1", "Hub Joint", 0.5f, 0.5f)
                            )
                        }
                    )
                    onSave(newPart)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4D3E))
            ) {
                Text("Save Part", fontSize = 16.sp)
            }
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(containerColor = Color.GRAY)
            ) {
                Text("Cancel", fontSize = 16.sp)
            }
        }
    }
}