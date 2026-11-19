package com.example.mycycletest

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // Initialize Database (easily swappable for POS / PostgreSQL / MySQL)
    DatabaseManager.init()

    val productRepo = ProductRepository()
    val bikeRepo = BikeRepository()
    val customerRepo = CustomerRepository()
    val christmasRepo = ChristmasRepository()

    routing {
        get("/") {
            call.respondText("MyCycle Shop SQL & POS Backend is Running!")
        }

        get("/api/products") {
            call.respond(productRepo.getAllProducts().toString())
        }

        get("/api/bikes") {
            call.respond(bikeRepo.getAllBikes().toString())
        }

        get("/api/customers") {
            call.respond(customerRepo.getAllCustomers().toString())
        }

        get("/api/christmas") {
            call.respond(christmasRepo.getAllChristmasOrders().toString())
        }
    }
}
