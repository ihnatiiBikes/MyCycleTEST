package com.example.mycycletest

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseManager {
    fun init(
        jdbcUrl: String = "jdbc:sqlite:mycycle.db",
        driver: String = "org.sqlite.JDBC"
    ) {
        Database.connect(url = jdbcUrl, driver = driver)
        transaction {
            SchemaUtils.createMissingTablesAndColumns(BikesTable, ProductsTable, CustomersTable, TasksTable, ChristmasTable)
        }
    }
}
