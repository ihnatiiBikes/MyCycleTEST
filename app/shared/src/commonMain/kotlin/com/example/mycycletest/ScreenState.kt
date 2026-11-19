package com.example.mycycletest

data class UiProduct(
    val id: Long,
    val productCode: String,
    val brand: String,
    val category: String,
    val price: Double,
    val description: String,
    val imageUrl: String,
    val color: String,
    val size: String,
    val weight: Int,
    val isMain: Boolean,
    val isChristmas: Boolean,
    val inStock: Int,
    val inShop: Int
)

data class ConnectionPoint(
    val id: String,
    val name: String,
    var x: Float = 0.5f,
    var y: Float = 0.5f
)



sealed class ShopScreen {
    object Home : ShopScreen()
    data class ProductDetail(val product: UiProduct) : ShopScreen()
    object AdminPanel : ShopScreen()
    object CustomerPortal : ShopScreen()
    object BikeConfigurator : ShopScreen()
}
