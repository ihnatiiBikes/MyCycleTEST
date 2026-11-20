package com.example.mycycletest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

@Composable
fun AdminPartConfiguratorScreen(
    parts: MutableList<ConfiguratorPart>,
    initialPart: ConfiguratorPart? = null,
    onBack: () -> Unit
) {
    var selectedPartId by remember { mutableStateOf(initialPart?.id) }
    var partType by remember { mutableStateOf(initialPart?.type ?: "Frame") }

    // Hardcoded assets from app/shared/src/assets
    val assetFrames = listOf(
        "assets/frames/factor_frame.png",
        "assets/frames/erasebg-transformed.webp",
        "assets/frames/erasebg-transformed-1.webp"
    )
    val assetFrontWheels = listOf(
        "assets/wheels/front/Black-Inc-62-silver-front-4472x4472.webp",
        "assets/wheels/front/BI-4858-silver-front-180.webp"
    )
    val assetRearWheels = listOf(
        "assets/wheels/rear/Black-Inc-62-silver-rear-4472x4472.webp",
        "assets/wheels/rear/Black-Inc-58Standard-rear-180-4472x4472.webp"
    )

    val initialIsWheels = initialPart?.type == "Wheels"
    val initialWheelNames = if (initialIsWheels) {
        initialPart.name.split(" & ").takeIf { it.size == 2 }
    } else null

    var frameName by remember { mutableStateOf(initialPart?.name ?: "Factor One Frame") }
    var frameImageUrl by remember { mutableStateOf(initialPart?.imageUrl ?: assetFrames.first()) }

    var frontWheelName by remember { mutableStateOf(initialWheelNames?.getOrNull(0) ?: "Front Wheel") }
    var frontWheelUrl by remember { mutableStateOf(if (initialIsWheels) initialPart?.imageUrl ?: assetFrontWheels.first() else assetFrontWheels.first()) }
    var rearWheelName by remember { mutableStateOf(initialWheelNames?.getOrNull(1) ?: "Rear Wheel") }
    var rearWheelUrl by remember { mutableStateOf(if (initialIsWheels) initialPart?.rearImageUrl ?: assetRearWheels.first() else assetRearWheels.first()) }
    var wheelScale by remember { mutableFloatStateOf(initialPart?.wheelScale ?: 0.45f) }

    var jointCoords by remember { 
        mutableStateOf(
            initialPart?.jointCoords?.toMutableList() ?: mutableStateOf(
                mutableListOf(
                    JointCoord(0.2f, 0.7f), // Blue joint (rear)
                    JointCoord(0.8f, 0.7f)  // Green joint (front)
                )
            ).value
        ) 
    }

    var selectedJointIndex by remember { mutableStateOf(0) }
    var stepSize by remember { mutableStateOf(0.01f) } // High precision default step
    var statusMessage by remember { mutableStateOf<String?>(null) }

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
                Text("← Back to Admin Panel")
            }
            Text("Admin Asset Joint & Mount Configurator", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // LEFT PANEL: Asset Selector & Joint Grid
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Select Part Type & Asset", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(
                            onClick = { 
                                partType = "Frame"
                                jointCoords = mutableListOf(JointCoord(0.2f, 0.7f), JointCoord(0.8f, 0.7f))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = if (partType == "Frame") Color(0xFF0071E3) else Color.Gray)
                        ) { Text("Frame (2 Joints)") }
                        Button(
                            onClick = { 
                                partType = "Wheels"
                                jointCoords = mutableListOf(JointCoord(0.5f, 0.5f), JointCoord(0.5f, 0.5f))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = if (partType == "Wheels") Color(0xFF0071E3) else Color.Gray)
                        ) { Text("Wheels (Front & Rear)") }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (partType == "Frame") {
                        OutlinedTextField(
                            value = frameName,
                            onValueChange = { frameName = it },
                            label = { Text("Frame Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Select Asset Frame Image:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        assetFrames.forEach { assetPath ->
                            Button(
                                onClick = { frameImageUrl = assetPath },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (frameImageUrl == assetPath) Color(0xFF27AE60) else Color.LightGray
                                ),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                            ) {
                                Text(assetPath.substringAfterLast("/"), fontSize = 12.sp)
                            }
                        }
                    } else {
                        OutlinedTextField(
                            value = frontWheelName,
                            onValueChange = { frontWheelName = it },
                            label = { Text("Front Wheel Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        assetFrontWheels.forEach { assetPath ->
                            Button(
                                onClick = { frontWheelUrl = assetPath },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (frontWheelUrl == assetPath) Color(0xFF27AE60) else Color.LightGray
                                ),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                            ) {
                                Text("Front: ${assetPath.substringAfterLast("/")}", fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = rearWheelName,
                            onValueChange = { rearWheelName = it },
                            label = { Text("Rear Wheel Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        assetRearWheels.forEach { assetPath ->
                            Button(
                                onClick = { rearWheelUrl = assetPath },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (rearWheelUrl == assetPath) Color(0xFF2980B9) else Color.LightGray
                                ),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                            ) {
                                Text("Rear: ${assetPath.substringAfterLast("/")}", fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Joint Control & Precision Grid", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(4.dp))

                    // Precision Selector & Coordinate Display
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Selected: Joint ${selectedJointIndex + 1} (x: ${((jointCoords.getOrNull(selectedJointIndex)?.x ?: 0f) * 100).toInt()}%, y: ${((jointCoords.getOrNull(selectedJointIndex)?.y ?: 0f) * 100).toInt()}%)",
                            fontSize = 11.sp,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Bold
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf(0.005f to "Ultra", 0.01f to "Fine", 0.05f to "Coarse").forEach { (step, label) ->
                                Button(
                                    onClick = { stepSize = step },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (stepSize == step) Color(0xFF0071E3) else Color.LightGray
                                    ),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text(label, fontSize = 10.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Grid of buttons: [Green] [↑ Up] [Blue] / [← Left] [↓ Down] [→ Right]
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { if (jointCoords.size > 1) selectedJointIndex = 1 },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedJointIndex == 1) Color(0xFF27AE60) else Color(0xFF27AE60).copy(alpha = 0.6f)
                                ),
                                modifier = Modifier.weight(1f).height(45.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) { Text("Green", fontSize = 13.sp) }

                            Button(
                                onClick = {
                                    if (jointCoords.indices.contains(selectedJointIndex)) {
                                        val cur = jointCoords[selectedJointIndex]
                                        jointCoords = jointCoords.toMutableList().apply {
                                            this[selectedJointIndex] = cur.copy(y = (cur.y - stepSize).coerceIn(0f, 1f))
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34495E)),
                                modifier = Modifier.weight(1f).height(45.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) { Text("↑ Up", fontSize = 13.sp) }

                            Button(
                                onClick = { if (jointCoords.isNotEmpty()) selectedJointIndex = 0 },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedJointIndex == 0) Color(0xFF2980B9) else Color(0xFF2980B9).copy(alpha = 0.6f)
                                ),
                                modifier = Modifier.weight(1f).height(45.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) { Text("Blue", fontSize = 13.sp) }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (jointCoords.indices.contains(selectedJointIndex)) {
                                        val cur = jointCoords[selectedJointIndex]
                                        jointCoords = jointCoords.toMutableList().apply {
                                            this[selectedJointIndex] = cur.copy(x = (cur.x - stepSize).coerceIn(0f, 1f))
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34495E)),
                                modifier = Modifier.weight(1f).height(45.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) { Text("← Left", fontSize = 13.sp) }

                            Button(
                                onClick = {
                                    if (jointCoords.indices.contains(selectedJointIndex)) {
                                        val cur = jointCoords[selectedJointIndex]
                                        jointCoords = jointCoords.toMutableList().apply {
                                            this[selectedJointIndex] = cur.copy(y = (cur.y + stepSize).coerceIn(0f, 1f))
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34495E)),
                                modifier = Modifier.weight(1f).height(45.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) { Text("↓ Down", fontSize = 13.sp) }

                            Button(
                                onClick = {
                                    if (jointCoords.indices.contains(selectedJointIndex)) {
                                        val cur = jointCoords[selectedJointIndex]
                                        jointCoords = jointCoords.toMutableList().apply {
                                            this[selectedJointIndex] = cur.copy(x = (cur.x + stepSize).coerceIn(0f, 1f))
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34495E)),
                                modifier = Modifier.weight(1f).height(45.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) { Text("→ Right", fontSize = 13.sp) }
                        }
                    }

                    if (partType == "Wheels") {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Wheel size vs frame height", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("${(wheelScale * 100).toInt()}%", modifier = Modifier.width(48.dp), fontSize = 14.sp)
                            Slider(
                                value = wheelScale,
                                onValueChange = { wheelScale = it },
                                valueRange = 0.1f..1f,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            val partToSave = ConfiguratorPart(
                                id = selectedPartId ?: "part-${kotlin.random.Random.nextInt()}",
                                name = if (partType == "Frame") frameName else "$frontWheelName & $rearWheelName",
                                type = partType,
                                imageUrl = if (partType == "Frame") frameImageUrl else frontWheelUrl,
                                rearImageUrl = if (partType == "Wheels") rearWheelUrl else null,
                                wheelScale = if (partType == "Wheels") wheelScale else 0.45f,
                                jointCoords = jointCoords
                            )
                            if (selectedPartId != null) {
                                val idx = parts.indexOfFirst { it.id == selectedPartId }
                                if (idx >= 0) parts[idx] = partToSave else parts.add(partToSave)
                            } else {
                                parts.add(partToSave)
                                selectedPartId = partToSave.id
                            }
                            statusMessage = "Successfully saved part & joint points for consumer web!"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4D3E)),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("Save & Publish to Consumer Web", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    if (statusMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(statusMessage!!, color = Color(0xFF27AE60), fontWeight = FontWeight.Bold)
                    }
                }
            }

            // RIGHT PANEL: Asset Canvas Preview with Joint Dots aligned to exact picture bounds
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(520.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (partType == "Frame") {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF2C3E50))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFF1A252F), RoundedCornerShape(8.dp))
                        ) {
                            val density = LocalDensity.current
                            val containerWpx = with(density) { maxWidth.toPx() }
                            val containerHpx = with(density) { maxHeight.toPx() }

                            val frameBitmap = rememberImageBitmap(frameImageUrl)
                            if (frameBitmap != null) {
                                val scale = min(containerWpx / frameBitmap.width, containerHpx / frameBitmap.height)
                                val displayedWpx = frameBitmap.width * scale
                                val displayedHpx = frameBitmap.height * scale
                                val pictureTopLeftXpx = (containerWpx - displayedWpx) / 2f
                                val pictureTopLeftYpx = (containerHpx - displayedHpx) / 2f

                                Box(
                                    modifier = Modifier
                                        .offset(
                                            x = with(density) { pictureTopLeftXpx.toDp() },
                                            y = with(density) { pictureTopLeftYpx.toDp() }
                                        )
                                        .size(
                                            with(density) { displayedWpx.toDp() },
                                            with(density) { displayedHpx.toDp() }
                                        )
                                ) {
                                    Image(
                                        painter = remember(frameBitmap) { BitmapPainter(frameBitmap) },
                                        contentDescription = "Frame Asset",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Fit
                                    )
                                }

                                jointCoords.forEachIndexed { idx, coord ->
                                    val isGreen = idx % 2 != 0
                                    val markerRadiusDp = 16.dp
                                    val dotXpx = pictureTopLeftXpx + coord.x * displayedWpx
                                    val dotYpx = pictureTopLeftYpx + coord.y * displayedHpx

                                    Box(
                                        modifier = Modifier.offset(
                                            x = with(density) { dotXpx.toDp() } - markerRadiusDp,
                                            y = with(density) { dotYpx.toDp() } - markerRadiusDp
                                        )
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = if (isGreen) Color(0xFF27AE60) else Color(0xFF2980B9),
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text("${idx + 1}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            }
                                        }
                                    }
                                }
                            } else {
                                AssetImage(
                                    assetPath = frameImageUrl,
                                    contentDescription = "Frame Asset",
                                    modifier = Modifier.fillMaxSize().padding(8.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF2C3E50))
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Front Wheel Box
                        BoxWithConstraints(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(Color(0xFF1A252F), RoundedCornerShape(8.dp))
                        ) {
                            val density = LocalDensity.current
                            val containerWpx = with(density) { maxWidth.toPx() }
                            val containerHpx = with(density) { maxHeight.toPx() }

                            val frontWheelBitmap = rememberImageBitmap(frontWheelUrl)
                            if (frontWheelBitmap != null) {
                                val scale = min(containerWpx / frontWheelBitmap.width, containerHpx / frontWheelBitmap.height)
                                val displayedWpx = frontWheelBitmap.width * scale
                                val displayedHpx = frontWheelBitmap.height * scale
                                val pictureTopLeftXpx = (containerWpx - displayedWpx) / 2f
                                val pictureTopLeftYpx = (containerHpx - displayedHpx) / 2f

                                Box(
                                    modifier = Modifier
                                        .offset(
                                            x = with(density) { pictureTopLeftXpx.toDp() },
                                            y = with(density) { pictureTopLeftYpx.toDp() }
                                        )
                                        .size(
                                            with(density) { displayedWpx.toDp() },
                                            with(density) { displayedHpx.toDp() }
                                        )
                                ) {
                                    Image(
                                        painter = remember(frontWheelBitmap) { BitmapPainter(frontWheelBitmap) },
                                        contentDescription = "Front Wheel Asset",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Fit
                                    )
                                }

                                val coord = jointCoords.getOrNull(1) ?: JointCoord(0.5f, 0.5f)
                                val markerRadiusDp = 16.dp
                                val dotXpx = pictureTopLeftXpx + coord.x * displayedWpx
                                val dotYpx = pictureTopLeftYpx + coord.y * displayedHpx

                                Box(
                                    modifier = Modifier.offset(
                                        x = with(density) { dotXpx.toDp() } - markerRadiusDp,
                                        y = with(density) { dotYpx.toDp() } - markerRadiusDp
                                    )
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF27AE60),
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text("2", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        }
                                    }
                                }
                            } else {
                                AssetImage(
                                    assetPath = frontWheelUrl,
                                    contentDescription = "Front Wheel Asset",
                                    modifier = Modifier.fillMaxSize().padding(8.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }

                        // Rear Wheel Box
                        BoxWithConstraints(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(Color(0xFF1A252F), RoundedCornerShape(8.dp))
                        ) {
                            val density = LocalDensity.current
                            val containerWpx = with(density) { maxWidth.toPx() }
                            val containerHpx = with(density) { maxHeight.toPx() }

                            val rearWheelBitmap = rememberImageBitmap(rearWheelUrl)
                            if (rearWheelBitmap != null) {
                                val scale = min(containerWpx / rearWheelBitmap.width, containerHpx / rearWheelBitmap.height)
                                val displayedWpx = rearWheelBitmap.width * scale
                                val displayedHpx = rearWheelBitmap.height * scale
                                val pictureTopLeftXpx = (containerWpx - displayedWpx) / 2f
                                val pictureTopLeftYpx = (containerHpx - displayedHpx) / 2f

                                Box(
                                    modifier = Modifier
                                        .offset(
                                            x = with(density) { pictureTopLeftXpx.toDp() },
                                            y = with(density) { pictureTopLeftYpx.toDp() }
                                        )
                                        .size(
                                            with(density) { displayedWpx.toDp() },
                                            with(density) { displayedHpx.toDp() }
                                        )
                                ) {
                                    Image(
                                        painter = remember(rearWheelBitmap) { BitmapPainter(rearWheelBitmap) },
                                        contentDescription = "Rear Wheel Asset",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Fit
                                    )
                                }

                                val coord = jointCoords.getOrNull(0) ?: JointCoord(0.5f, 0.5f)
                                val markerRadiusDp = 16.dp
                                val dotXpx = pictureTopLeftXpx + coord.x * displayedWpx
                                val dotYpx = pictureTopLeftYpx + coord.y * displayedHpx

                                Box(
                                    modifier = Modifier.offset(
                                        x = with(density) { dotXpx.toDp() } - markerRadiusDp,
                                        y = with(density) { dotYpx.toDp() } - markerRadiusDp
                                    )
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF2980B9),
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text("1", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        }
                                    }
                                }
                            } else {
                                AssetImage(
                                    assetPath = rearWheelUrl,
                                    contentDescription = "Rear Wheel Asset",
                                    modifier = Modifier.fillMaxSize().padding(8.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
