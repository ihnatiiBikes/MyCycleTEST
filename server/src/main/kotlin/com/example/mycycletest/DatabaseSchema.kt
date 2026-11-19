package com.example.mycycletest

import org.jetbrains.exposed.sql.Table

object BikesTable : Table("bikes") {
    val id = long("id").autoIncrement()
    val productCode = varchar("product_code", 64)
    val frameNumber = varchar("frame_number", 64)

    override val primaryKey = PrimaryKey(id)
}

object ProductsTable : Table("products") {
    val id = long("id").autoIncrement()
    val productCode = varchar("product_code", 64)
    val inStock = short("in_stock")
    val inShop = short("in_shop")
    val color = varchar("color", 32)
    val size = varchar("size", 32)
    val weight = short("weight")
    val imageUrl = varchar("image_url", 256).nullable()
    val isMain = bool("is_main").default(false)
    val isChristmas = bool("is_christmas").default(false)

    override val primaryKey = PrimaryKey(id)
}

object CustomersTable : Table("customers") {
    val id = long("id").autoIncrement()
    val fullName = varchar("full_name", 128)
    val eircode = varchar("eircode", 32)
    val phoneNumber = varchar("phone_number", 32)

    override val primaryKey = PrimaryKey(id)
}

object TasksTable : Table("shop_tasks") {
    val id = long("id").autoIncrement()
    val bikeId = long("bike_id").references(BikesTable.id)
    val customerId = long("customer_id").references(CustomersTable.id)
    val description = varchar("description", 256)
    val status = varchar("status", 32)

    override val primaryKey = PrimaryKey(id)
}

object ChristmasTable : Table("christmas") {
    val id = long("id").autoIncrement()
    val customerId = long("customer").references(CustomersTable.id)
    val posCode = integer("pos_code")

    override val primaryKey = PrimaryKey(id)
}
