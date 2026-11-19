package com.example.mycycletest

actual fun updateWebRoute(route: String) {
    try {
        kotlinx.browser.window.location.hash = route
    } catch (_: Exception) {}
}
