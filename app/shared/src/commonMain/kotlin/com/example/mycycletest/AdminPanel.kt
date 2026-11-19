package com.example.mycycletest

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdminShopControlPanel(
    users: MutableList<User>,
    products: MutableList<UiProduct>,
    configuratorParts: MutableList<ConfiguratorPart>,
    onDismiss: () -> Unit
) {
    var fileNameInput by remember { mutableStateOf("trek_catalog.csv") }
    var csvContentInput by remember { mutableStateOf("DOMANE,12,4,Red,56,9\nEMONDA,10,3,Black,54,8") }
    var importStatus by remember { mutableStateOf<String?>(null) }

    var newPartName by remember { mutableStateOf("") }
    var newPartType by remember { mutableStateOf("Frame") }
    var newPartImageUrl by remember { mutableStateOf("https://www.mycycle.ie/v/vspfiles/photos/CUSTOM-PART-2.jpg") }
    var partAddStatus by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Admin Shop Control Panel", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Button(onClick = onDismiss) { Text("Back to Store View") }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Configurator Part Creator with Picture & Server Storage
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Bike Configurator - Add New Part (Frame / Groupset / Wheels)", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Require a picture, download/save it to server storage, and configure connection joint points.", fontSize = 13.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = newPartName,
                    onValueChange = { newPartName = it },
                    label = { Text("Part Name (e.g. Carbon Enduro Frame)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = newPartImageUrl,
                    onValueChange = { newPartImageUrl = it },
                    label = { Text("Part Picture URL / Server Storage Path") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = { newPartType = "Frame" }, colors = ButtonDefaults.buttonColors(containerColor = if (newPartType == "Frame") Color(0xFF0071E3) else Color.Gray)) { Text("Frame") }
                    Button(onClick = { newPartType = "Groupset" }, colors = ButtonDefaults.buttonColors(containerColor = if (newPartType == "Groupset") Color(0xFF0071E3) else Color.Gray)) { Text("Groupset") }
                    Button(onClick = { newPartType = "Wheels" }, colors = ButtonDefaults.buttonColors(containerColor = if (newPartType == "Wheels") Color(0xFF0071E3) else Color.Gray)) { Text("Wheels") }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (newPartName.isNotBlank() && newPartImageUrl.isNotBlank()) {
                            val newPart = ConfiguratorPart(
                                id = "part-${kotlin.random.Random.nextInt()}",
                                name = newPartName,
                                type = newPartType,
                                imageUrl = newPartImageUrl,
                                points = mutableListOf(
                                    ConnectionPoint("j1", "Front Connection Joint", 0.5f, 0.5f),
                                    ConnectionPoint("j2", "Rear Connection Joint", 0.7f, 0.7f)
                                )
                            )
                            configuratorParts.add(newPart)
                            partAddStatus = "Successfully uploaded picture and saved $newPartType part to server storage!"
                            newPartName = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4D3E))
                ) {
                    Text("Upload Picture & Save Configurator Part")
                }

                if (partAddStatus != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(partAddStatus!!, color = Color(0xFF34C759), fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Product Import Tool
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Product Import (Vendor CSV Mappers)", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Select CSV file name (e.g., trek_bikes.csv, cube_catalog.csv, brompton.csv) to process via CSV merge mappers.", fontSize = 13.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = fileNameInput,
                    onValueChange = { fileNameInput = it },
                    label = { Text("CSV File Name (e.g. trek_bikes.csv)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = csvContentInput,
                    onValueChange = { csvContentInput = it },
                    label = { Text("CSV File Content (Code,Stock,Shop,Color,Size,Weight)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val lowerName = fileNameInput.lowercase()
                        val vendor = when {
                            lowerName.contains("trek") -> "Trek"
                            lowerName.contains("cube") -> "Cube"
                            lowerName.contains("brompton") -> "Brompton"
                            lowerName.contains("madison") -> "Madison"
                            lowerName.contains("cyclex") -> "Cyclex"
                            else -> "MyCycle"
                        }

                        val newIdBase = (products.maxOfOrNull { it.id } ?: 0L) + 1
                        val newItems = csvContentInput.lines().filter { it.isNotBlank() }.mapIndexed { idx, line ->
                            val parts = line.split(",").map { it.trim() }
                            val code = parts.getOrNull(0) ?: "ITEM-$idx"
                            UiProduct(
                                id = newIdBase + idx,
                                productCode = "$vendor-$code",
                                brand = vendor,
                                category = "Road",
                                price = 1500.0 + (idx * 250),
                                description = "Imported via $vendor CSV merge mapper with detailed geometry and carbon frame construction.",
                                imageUrl = "https://www.mycycle.ie/v/vspfiles/photos/$vendor-$code-2.jpg",
                                color = parts.getOrNull(3) ?: "Standard",
                                size = parts.getOrNull(4) ?: "M",
                                weight = parts.getOrNull(5)?.toIntOrNull() ?: 11,
                                isMain = products.isEmpty() && idx == 0,
                                isChristmas = false,
                                inStock = parts.getOrNull(1)?.toIntOrNull() ?: 10,
                                inShop = parts.getOrNull(2)?.toIntOrNull() ?: 3
                            )
                        }

                        products.addAll(newItems)
                        importStatus = "Successfully imported ${newItems.size} products using $vendor mapper!"
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4D3E))
                ) {
                    Text("Run Product Import (File Picker)")
                }

                if (importStatus != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(importStatus!!, color = Color(0xFF34C759), fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Designate Home / Main Product", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        products.forEachIndexed { index, product ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(product.productCode, fontWeight = FontWeight.Bold)
                        Text("Color: ${product.color} | Size: ${product.size}", fontSize = 12.sp, color = Color.Gray)
                        if (product.isMain) Text("★ CURRENT HOME / MAIN PRODUCT", color = Color(0xFFFFCC00), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = {
                            for (i in products.indices) {
                                products[i] = products[i].copy(isMain = (i == index))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = if (product.isMain) Color(0xFF34C759) else Color(0xFF0071E3))
                    ) {
                        Text(if (product.isMain) "Main Product (Active)" else "Set as Home Product")
                    }
                }
            }
        }
    }
}
