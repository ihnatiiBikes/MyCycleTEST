package com.example.mycycletest

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomerPortalScreen(currentUser: User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        Text("Welcome, ${currentUser.username}", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Your Registered Bikes & Service Tasks", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Bike: Turbo Vado SL", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Frame #: FM987654321", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Active Task: Annual Service & Check", fontWeight = FontWeight.Medium)
                Text("Status: PENDING", color = Color(0xFFFF9500), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
