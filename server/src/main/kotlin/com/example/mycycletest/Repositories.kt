package com.example.mycycletest

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ProductRepository {
    fun addProduct(
        code: String,
        inStock: Short,
        inShop: Short,
        color: String,
        size: String,
        weight: Short,
        vendorImageUrl: String? = null,
        isMain: Boolean = false,
        isChristmas: Boolean = false
    ): Long = transaction {
        val finalImageUrl = if (!vendorImageUrl.isNullOrBlank()) {
            vendorImageUrl
        } else {
            "https://www.mycycle.ie/v/vspfiles/photos/${code}-2.jpg"
        }

        ProductsTable.insert {
            it[ProductsTable.productCode] = code
            it[ProductsTable.inStock] = inStock
            it[ProductsTable.inShop] = inShop
            it[ProductsTable.color] = color
            it[ProductsTable.size] = size
            it[ProductsTable.weight] = weight
            it[ProductsTable.imageUrl] = finalImageUrl
            it[ProductsTable.isMain] = isMain
            it[ProductsTable.isChristmas] = isChristmas
        }[ProductsTable.id]
    }

    /**
     * Product Import (formerly CSV Merge) processing new items directly into the DB.
     */
    fun importProducts(rows: List<Map<String, String>>): Int = transaction {
        var count = 0
        for (row in rows) {
            val code = row["product_code"] ?: "BIKE-${System.currentTimeMillis()}"
            val inStock = row["in_stock"]?.toShortOrNull() ?: 1
            val inShop = row["in_shop"]?.toShortOrNull() ?: 1
            val color = row["color"] ?: "Black"
            val size = row["size"] ?: "M"
            val weight = row["weight"]?.toShortOrNull() ?: 12
            val vendorLink = row["image_url"]

            addProduct(code, inStock, inShop, color, size, weight, vendorLink)
            count++
        }
        count
    }

    fun setMainProduct(productId: Long) = transaction {
        ProductsTable.update({ ProductsTable.isMain eq true }) {
            it[isMain] = false
        }
        ProductsTable.update({ ProductsTable.id eq productId }) {
            it[isMain] = true
        }
    }

    fun getFilteredProducts(
        color: String? = null,
        size: String? = null,
        limit: Int = 64,
        offset: Long = 0
    ): List<Map<String, Any?>> = transaction {
        var query = ProductsTable.selectAll()
        if (!color.isNullOrBlank()) {
            query = query.andWhere { ProductsTable.color.lowerCase() eq color.lowercase() }
        }
        if (!size.isNullOrBlank()) {
            query = query.andWhere { ProductsTable.size.lowerCase() eq size.lowercase() }
        }
        query.limit(limit).map {
            mapOf(
                "id" to it[ProductsTable.id],
                "product_code" to it[ProductsTable.productCode],
                "in_stock" to it[ProductsTable.inStock],
                "in_shop" to it[ProductsTable.inShop],
                "color" to it[ProductsTable.color],
                "size" to it[ProductsTable.size],
                "weight" to it[ProductsTable.weight],
                "image_url" to it[ProductsTable.imageUrl],
                "is_main" to it[ProductsTable.isMain],
                "is_christmas" to it[ProductsTable.isChristmas]
            )
        }
    }

    fun getAllProducts(): List<Map<String, Any?>> = getFilteredProducts()
}

class BikeRepository {
    fun addBike(productCode: String, frameNumber: String): Long = transaction {
        BikesTable.insert {
            it[BikesTable.productCode] = productCode
            it[BikesTable.frameNumber] = frameNumber
        }[BikesTable.id]
    }

    fun getAllBikes(): List<Map<String, Any>> = transaction {
        BikesTable.selectAll().map {
            mapOf(
                "id" to it[BikesTable.id],
                "product_code" to it[BikesTable.productCode],
                "frame_number" to it[BikesTable.frameNumber]
            )
        }
    }
}

class CustomerRepository {
    fun findOrCreateCustomer(fullName: String, rawEircode: String, rawPhone: String): Long = transaction {
        val normPhone = DataTransformUtils.transformPhoneNumber(rawPhone)
        val normEircode = DataTransformUtils.transformEircode(rawEircode)

        val existing = CustomersTable.selectAll().andWhere { CustomersTable.phoneNumber eq normPhone }.singleOrNull()
        if (existing != null) {
            existing[CustomersTable.id]
        } else {
            CustomersTable.insert {
                it[CustomersTable.fullName] = fullName
                it[CustomersTable.eircode] = normEircode
                it[CustomersTable.phoneNumber] = normPhone
            }[CustomersTable.id]
        }
    }

    fun getAllCustomers(): List<Map<String, Any>> = transaction {
        CustomersTable.selectAll().map {
            mapOf(
                "id" to it[CustomersTable.id],
                "full_name" to it[CustomersTable.fullName],
                "eircode" to it[CustomersTable.eircode],
                "phone_number" to it[CustomersTable.phoneNumber]
            )
        }
    }
}

class TaskRepository {
    fun createTaskWithCustomerCheck(
        customerName: String,
        eircode: String,
        phone: String,
        productCode: String,
        frameNumber: String,
        description: String
    ): Long = transaction {
        val customerRepo = CustomerRepository()
        val customerId = customerRepo.findOrCreateCustomer(customerName, eircode, phone)

        val bikeRepo = BikeRepository()
        val bikeId = bikeRepo.addBike(productCode, frameNumber)

        TasksTable.insert {
            it[TasksTable.bikeId] = bikeId
            it[TasksTable.customerId] = customerId
            it[TasksTable.description] = description
            it[TasksTable.status] = "PENDING"
        }[TasksTable.id]
    }

    fun getAllTasks(): List<Map<String, Any>> = transaction {
        TasksTable.selectAll().map {
            mapOf(
                "id" to it[TasksTable.id],
                "bike_id" to it[TasksTable.bikeId],
                "customer_id" to it[TasksTable.customerId],
                "description" to it[TasksTable.description],
                "status" to it[TasksTable.status]
            )
        }
    }
}

class ChristmasRepository {
    fun addChristmasOrder(customerId: Long, posCode: Int): Long = transaction {
        ChristmasTable.insert {
            it[ChristmasTable.customerId] = customerId
            it[ChristmasTable.posCode] = posCode
        }[ChristmasTable.id]
    }

    fun getAllChristmasOrders(): List<Map<String, Any>> = transaction {
        ChristmasTable.selectAll().map {
            mapOf(
                "id" to it[ChristmasTable.id],
                "customer" to it[ChristmasTable.customerId],
                "pos_code" to it[ChristmasTable.posCode]
            )
        }
    }
}
