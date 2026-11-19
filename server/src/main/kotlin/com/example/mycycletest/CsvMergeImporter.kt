package com.example.mycycletest

object CsvMergeImporter {
    /**
     * Processes a CSV file import according to the file name, mimicking the C++ csvMerge project mappers
     * (Trek, Cube, Brompton, Madison, Cyclex, MyCycle).
     */
    fun importCsvFile(fileName: String, csvContent: String, productRepo: ProductRepository): Int {
        val lowerName = fileName.lowercase()
        val vendor = when {
            lowerName.contains("trek") -> "Trek"
            lowerName.contains("cube") -> "Cube"
            lowerName.contains("brompton") -> "Brompton"
            lowerName.contains("madison") -> "Madison"
            lowerName.contains("cyclex") -> "Cyclex"
            else -> "MyCycle"
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
                val inStock = parts.getOrNull(1)?.toShortOrNull() ?: 10
                val inShop = parts.getOrNull(2)?.toShortOrNull() ?: 3
                val color = parts.getOrNull(3) ?: "Default"
                val size = parts.getOrNull(4) ?: "Universal"
                val weight = parts.getOrNull(5)?.toShortOrNull() ?: 12
                val imageUrl = parts.getOrNull(6)

                rows.add(
                    mapOf(
                        "product_code" to "$vendor-$code",
                        "in_stock" to inStock.toString(),
                        "in_shop" to inShop.toString(),
                        "color" to color,
                        "size" to size,
                        "weight" to weight.toString(),
                        "image_url" to (imageUrl ?: "")
                    )
                )
            }
        }

        return productRepo.importProducts(rows)
    }
}
