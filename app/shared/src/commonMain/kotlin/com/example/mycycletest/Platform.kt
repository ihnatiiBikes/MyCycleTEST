package com.example.mycycletest

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform