package com.example.mycycletest

enum class FulfillmentType {
    IN_STORE,
    DELIVERY
}

data class Bike(
    val id: String,
    val name: String,
    val brand: String,
    val category: String, // "Road", "Kids", "Gravel", "Mountain"
    val price: Double,
    val description: String,
    val imageUrl: String, // placeholder or symbol/emoji description
    val isMainProduct: Boolean = false,
    val isChristmasSpecial: Boolean = false,
    val inStoreAvailable: Boolean = true,
    val deliveryAvailable: Boolean = true,
    val specs: List<String> = emptyList()
)

data class User(
    val id: String,
    val username: String,
    val password: String,
    val isAdmin: Boolean
)

data class CartItem(
    val bike: Bike,
    val fulfillment: FulfillmentType,
    val quantity: Int = 1
)

data class StoreOrder(
    val orderId: String,
    val username: String,
    val items: List<CartItem>,
    val totalAmount: Double,
    val fulfillment: FulfillmentType,
    val details: String // store location or delivery address
)
