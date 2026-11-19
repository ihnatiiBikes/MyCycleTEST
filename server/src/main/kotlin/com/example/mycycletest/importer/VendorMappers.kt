package com.example.mycycletest.importer

interface VendorMapper {
    val vendorName: String
    fun mapRow(rawCode: String, rawStock: String, rawShop: String, rawColor: String, rawSize: String, rawWeight: String, rawImage: String?): Map<String, String>
}

class TrekMapper : VendorMapper {
    override val vendorName = "Trek"
    override fun mapRow(rawCode: String, rawStock: String, rawShop: String, rawColor: String, rawSize: String, rawWeight: String, rawImage: String?): Map<String, String> {
        return mapOf(
            "product_code" to "TREK-$rawCode",
            "in_stock" to (rawStock.toShortOrNull()?.toString() ?: "10"),
            "in_shop" to (rawShop.toShortOrNull()?.toString() ?: "3"),
            "color" to rawColor.ifBlank { "Trek Black" },
            "size" to rawSize.ifBlank { "56" },
            "weight" to (rawWeight.toShortOrNull()?.toString() ?: "9"),
            "image_url" to (rawImage ?: "https://www.mycycle.ie/v/vspfiles/photos/TREK-$rawCode-2.jpg")
        )
    }
}

class CubeMapper : VendorMapper {
    override val vendorName = "Cube"
    override fun mapRow(rawCode: String, rawStock: String, rawShop: String, rawColor: String, rawSize: String, rawWeight: String, rawImage: String?): Map<String, String> {
        return mapOf(
            "product_code" to "CUBE-$rawCode",
            "in_stock" to (rawStock.toShortOrNull()?.toString() ?: "8"),
            "in_shop" to (rawShop.toShortOrNull()?.toString() ?: "2"),
            "color" to if (rawColor.isBlank()) "Cube Green" else rawColor,
            "size" to if (rawSize.isBlank()) "M" else rawSize,
            "weight" to (rawWeight.toShortOrNull()?.toString() ?: "12"),
            "image_url" to (rawImage ?: "https://www.mycycle.ie/v/vspfiles/photos/CUBE-$rawCode-2.jpg")
        )
    }
}

class BromptonMapper : VendorMapper {
    override val vendorName = "Brompton"
    override fun mapRow(rawCode: String, rawStock: String, rawShop: String, rawColor: String, rawSize: String, rawWeight: String, rawImage: String?): Map<String, String> {
        return mapOf(
            "product_code" to "BROMPTON-$rawCode",
            "in_stock" to (rawStock.toShortOrNull()?.toString() ?: "5"),
            "in_shop" to (rawShop.toShortOrNull()?.toString() ?: "1"),
            "color" to if (rawColor.isBlank()) "Racing Green" else rawColor,
            "size" to if (rawSize.isBlank()) "Standard" else rawSize,
            "weight" to (rawWeight.toShortOrNull()?.toString() ?: "11"),
            "image_url" to (rawImage ?: "https://www.mycycle.ie/v/vspfiles/photos/BROMPTON-$rawCode-2.jpg")
        )
    }
}

class MadisonMapper : VendorMapper {
    override val vendorName = "Madison"
    override fun mapRow(rawCode: String, rawStock: String, rawShop: String, rawColor: String, rawSize: String, rawWeight: String, rawImage: String?): Map<String, String> {
        return mapOf(
            "product_code" to "MADISON-$rawCode",
            "in_stock" to (rawStock.toShortOrNull()?.toString() ?: "20"),
            "in_shop" to (rawShop.toShortOrNull()?.toString() ?: "5"),
            "color" to if (rawColor.isBlank()) "Matte Gray" else rawColor,
            "size" to if (rawSize.isBlank()) "L" else rawSize,
            "weight" to (rawWeight.toShortOrNull()?.toString() ?: "10"),
            "image_url" to (rawImage ?: "https://www.mycycle.ie/v/vspfiles/photos/MADISON-$rawCode-2.jpg")
        )
    }
}

class CyclexMapper : VendorMapper {
    override val vendorName = "Cyclex"
    override fun mapRow(rawCode: String, rawStock: String, rawShop: String, rawColor: String, rawSize: String, rawWeight: String, rawImage: String?): Map<String, String> {
        return mapOf(
            "product_code" to "CYCLEX-$rawCode",
            "in_stock" to (rawStock.toShortOrNull()?.toString() ?: "12"),
            "in_shop" to (rawShop.toShortOrNull()?.toString() ?: "4"),
            "color" to if (rawColor.isBlank()) "Blue" else rawColor,
            "size" to if (rawSize.isBlank()) "S" else rawSize,
            "weight" to (rawWeight.toShortOrNull()?.toString() ?: "13"),
            "image_url" to (rawImage ?: "https://www.mycycle.ie/v/vspfiles/photos/CYCLEX-$rawCode-2.jpg")
        )
    }
}

class MyCycleMapper : VendorMapper {
    override val vendorName = "MyCycle"
    override fun mapRow(rawCode: String, rawStock: String, rawShop: String, rawColor: String, rawSize: String, rawWeight: String, rawImage: String?): Map<String, String> {
        return mapOf(
            "product_code" to rawCode,
            "in_stock" to (rawStock.toShortOrNull()?.toString() ?: "10"),
            "in_shop" to (rawShop.toShortOrNull()?.toString() ?: "3"),
            "color" to if (rawColor.isBlank()) "Black" else rawColor,
            "size" to if (rawSize.isBlank()) "M" else rawSize,
            "weight" to (rawWeight.toShortOrNull()?.toString() ?: "11"),
            "image_url" to (rawImage ?: "https://www.mycycle.ie/v/vspfiles/photos/$rawCode-2.jpg")
        )
    }
}
