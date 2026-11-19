package com.example.mycycletest

import kotlinx.browser.window

actual fun updateWebRoute(route: String) {
    try {
        window.location.hash = route
    } catch (_: Exception) {}
}
