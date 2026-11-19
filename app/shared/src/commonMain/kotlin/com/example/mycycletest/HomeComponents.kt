package com.example.mycycletest

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BikeConfiguratorBanner(onOpenConfigurator: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0047AB))
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("⚙️ CUSTOM BIKE CONFIGURATOR", color = Color(0xFFFFD700), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Build Your Dream Bike", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Assemble frames, groupsets, and wheels with precise connection joint points.", color = Color.LightGray, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onOpenConfigurator,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
            ) {
                Text("Launch Configurator", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ChristmasHeroBanner(product: UiProduct, onProductClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF8B0000))
            .padding(48.dp)
            .clickable { onProductClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🎄 CHRISTMAS SPECIAL 🎄", color = Color(0xFFFFD700), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(product.productCode, color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Color: ${product.color} | Size: ${product.size}", color = Color.LightGray, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onProductClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700), contentColor = Color.Black)
            ) {
                Text("View Deal — €${product.price}", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun MainProductHero(product: UiProduct, onProductClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1D1D1F))
            .padding(64.dp)
            .clickable { onProductClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("HOME / FLAGSHIP PRODUCT (Admin Designated)", color = Color(0xFF86868B), fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(product.productCode, color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Color: ${product.color} | Size: ${product.size} | Weight: ${product.weight}kg", color = Color(0xFF86868B), fontSize = 18.sp)
            Spacer(modifier = Modifier.height(28.dp))
            Button(
                onClick = onProductClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0071E3), contentColor = Color.White),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("View Product — €${product.price}", modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
            }
        }
    }
}

@Composable
fun ProductGrid(products: List<UiProduct>, onProductClick: (UiProduct) -> Unit, onAddToCart: (UiProduct) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        products.chunked(3).forEach { rowProducts ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                rowProducts.forEach { product ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onProductClick(product) },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text("🚲", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(product.brand.uppercase(), fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                            Text(product.productCode, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1D1D1F))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Color: ${product.color} | Size: ${product.size}", fontSize = 14.sp, color = Color.DarkGray)
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("€${product.price}", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1D1D1F))
                                Button(
                                    onClick = { onAddToCart(product) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D1D1F))
                                ) {
                                    Text("Add to Cart")
                                }
                            }
                        }
                    }
                }
                repeat(3 - rowProducts.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
