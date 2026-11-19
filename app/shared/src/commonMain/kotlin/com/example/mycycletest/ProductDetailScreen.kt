package com.example.mycycletest

import androidx.compose.foundation.clickable
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
fun ProductDetailScreen(
    product: UiProduct,
    onBack: () -> Unit,
    onAddToCart: (UiProduct, FulfillmentType) -> Unit
) {
    var fulfillment by remember { mutableStateOf(FulfillmentType.IN_STORE) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp)
    ) {
        OutlinedButton(onClick = onBack) {
            Text("← Back to Store")
        }
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(350.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🚲", fontSize = 96.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Photo URL: ${product.imageUrl}", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(product.brand.uppercase(), fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                Text(product.productCode, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1D1D1F))
                Text("Category: ${product.category}", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text("Price: €${product.price}", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color(0xFF0071E3))

                HorizontalDivider()

                Text("Description:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(product.description, fontSize = 16.sp, color = Color.DarkGray)

                Spacer(modifier = Modifier.height(8.dp))
                Text("Specifications:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("• Color: ${product.color}", fontSize = 15.sp)
                Text("• Size: ${product.size}", fontSize = 15.sp)
                Text("• Weight: ${product.weight} kg", fontSize = 15.sp)
                Text("• In Stock: ${product.inStock} | In Shop: ${product.inShop}", fontSize = 15.sp)

                HorizontalDivider()

                Text("Select Fulfillment Method:", fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { fulfillment = FulfillmentType.IN_STORE }
                    ) {
                        RadioButton(selected = fulfillment == FulfillmentType.IN_STORE, onClick = { fulfillment = FulfillmentType.IN_STORE })
                        Text("In-Store Pickup")
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { fulfillment = FulfillmentType.DELIVERY }
                    ) {
                        RadioButton(selected = fulfillment == FulfillmentType.DELIVERY, onClick = { fulfillment = FulfillmentType.DELIVERY })
                        Text("Home Delivery")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onAddToCart(product, fulfillment) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0071E3)),
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Add to Cart & Checkout", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
