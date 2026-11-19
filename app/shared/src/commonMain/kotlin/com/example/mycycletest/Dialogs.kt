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
fun CartDialog(cartItems: MutableList<CartItem>, onDismiss: () -> Unit, onCheckout: () -> Unit) {
    var orderPlaced by remember { mutableStateOf(false) }
    val total = cartItems.sumOf { it.bike.price * it.quantity }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Your Shopping Cart", fontWeight = FontWeight.Bold) },
        text = {
            if (orderPlaced) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(24.dp)
                ) {
                    Text("🎉", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Order Placed Successfully!", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            } else if (cartItems.isEmpty()) {
                Text("Your cart is empty.")
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    cartItems.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(item.bike.name, fontWeight = FontWeight.Bold)
                            Text("€${item.bike.price}")
                        }
                        HorizontalDivider()
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total:", fontWeight = FontWeight.Bold)
                        Text("€$total", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color(0xFF0071E3))
                    }
                }
            }
        },
        confirmButton = {
            if (!orderPlaced && cartItems.isNotEmpty()) {
                Button(
                    onClick = { orderPlaced = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34C759))
                ) {
                    Text("Checkout")
                }
            } else {
                Button(onClick = onCheckout) { Text("Close") }
            }
        },
        dismissButton = {
            if (!orderPlaced) {
                TextButton(onClick = onDismiss) { Text("Back") }
            }
        }
    )
}

@Composable
fun LoginDialog(users: List<User>, onDismiss: () -> Unit, onLoginSuccess: (User) -> Unit) {
    var usernameInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current

    val doLogin = {
        val found = users.find { it.username == usernameInput && it.password == passwordInput }
        if (found != null) {
            onLoginSuccess(found)
        } else {
            errorMessage = "Invalid credentials. Try admin / changeme"
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Login") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = usernameInput,
                    onValueChange = { usernameInput = it },
                    label = { Text("Username") },
                    singleLine = true,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Next),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                        onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = { passwordInput = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Done),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                        onDone = { doLogin() }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                if (errorMessage != null) {
                    Text(errorMessage!!, color = Color.Red, fontSize = 14.sp)
                }
                Text("Admin login: admin / changeme", fontSize = 12.sp, color = Color.Gray)
            }
        },
        confirmButton = {
            Button(onClick = doLogin) { Text("Login") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
