package com.example.mycycletest.importer

import com.example.mycycletest.ProductRepository

object CsvMergeImporter {
    fun importCsvFile(fileName: String, csvContent: String, productRepo: ProductRepository): Int {
        val lowerName = fileName.lowercase()
        val mapper: VendorMapper = when {
            lowerName.contains("trek") -> TrekMapper()
            lowerName.contains("cube") -> CubeMapper()
            lowerName.contains("brompton") -> BromptonMapper()
            lowerName.contains("madison") -> MadisonMapper()
            lowerName.contains("cyclex") -> CyclexMapper()
            else -> MyCycleMapper()
        }

        val lines = csvContent.lines().filter { it.isNotBlank() }
        val rows = mutableListOf<Map<String, String>>()

        for ((index, line) in lines.withIndex()) {
            if (index == 0 && (line.contains("code") || line.contains("product") || line.contains("sku"))) {
                continue // skip header
            }
            val parts = line.split(",").map { it.trim() }
            if (parts.isNotEmpty()) {
                val code = parts.getOrNull(0) ?: "ITEM-$index"
                val stock = parts.getOrNull(1) ?: "10"
                val shop = parts.getOrNull(2) ?: "3"
                val color = parts.getOrNull(3) ?: "Default"
                val size = parts.getOrNull(4) ?: "Universal"
                val weight = parts.getOrNull(5) ?: "12"
                val image = parts.getOrNull(6)

                val mappedRow = mapper.mapRow(code, stock, shop, color, size, weight, image)
                rows.add(mappedRow)
            }
        }

        return productRepo.importProducts(rows)
    }
}
