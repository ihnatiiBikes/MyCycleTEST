package com.example.mycycletest

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("MyCycle Shop SQL & POS Backend is Running!", response.bodyAsText())
    }

    @Test
    fun testProductImportAndImageUrlFormatting() {
        DatabaseManager.init("jdbc:sqlite:test_mycycle.db", "org.sqlite.JDBC")
        val productRepo = ProductRepository()

        val csvRows = listOf(
            mapOf("product_code" to "TREK-DOMANE-2", "in_stock" to "5", "in_shop" to "2", "color" to "Red", "size" to "56", "weight" to "9")
        )
        val imported = productRepo.importProducts(csvRows)
        assertEquals(1, imported)

        val products = productRepo.getAllProducts()
        assertTrue(products.isNotEmpty())
        val p = products.last()
        assertEquals("TREK-DOMANE-2", p["product_code"])
        assertEquals("https://www.mycycle.ie/v/vspfiles/photos/TREK-DOMANE-2-2.jpg", p["image_url"])
    }

    @Test
    fun testCsvMergeImporterByFilename() {
        DatabaseManager.init("jdbc:sqlite:test_csv_merge.db", "org.sqlite.JDBC")
        val productRepo = ProductRepository()

        val csvContent = "DOMANE,10,4,Red,56,9\nEMONDA,8,2,Black,54,8"
        val count = CsvMergeImporter.importCsvFile("trek_catalog.csv", csvContent, productRepo)
        assertEquals(2, count)

        val products = productRepo.getAllProducts()
        assertTrue(products.any { it["product_code"].toString().contains("Trek") })
    }

    @Test
    fun testTaskAndAutoCustomerCreation() {
        DatabaseManager.init("jdbc:sqlite:test_mycycle_task.db", "org.sqlite.JDBC")
        val taskRepo = TaskRepository()
        val customerRepo = CustomerRepository()

        // Test phone number normalization (+ to 00, non-digits removed)
        val phoneRaw = "+353 (87) 123-4567"
        val normalizedPhone = DataTransformUtils.transformPhoneNumber(phoneRaw)
        assertEquals("00353871234567", normalizedPhone)

        // Create task which auto-creates customer if phone doesn't match
        val taskId = taskRepo.createTaskWithCustomerCheck(
            customerName = "Jane Doe",
            eircode = "D02 X285",
            phone = "+353879998888",
            productCode = "SPECIALIZED-ROUBAIX",
            frameNumber = "FM123456789",
            description = "Brake adjustment and gear tune"
        )
        assertTrue(taskId > 0)

        val customers = customerRepo.getAllCustomers()
        assertTrue(customers.isNotEmpty())
    }
}
