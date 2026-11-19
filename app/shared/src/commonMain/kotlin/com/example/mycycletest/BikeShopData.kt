package com.example.mycycletest

import androidx.compose.runtime.mutableStateListOf

object BikeShopData {
    val brands = listOf("Specialized", "Trek", "Cannondale", "Giant", "Scott")
    val categories = listOf("Road", "Kids", "Gravel", "Mountain")

    val initialUsers = mutableStateListOf(
        User("1", "admin", "changeme", isAdmin = true),
        User("2", "john_doe", "password123", isAdmin = false),
        User("3", "sarah_ride", "welcome1", isAdmin = false),
        User("4", "alex_mtb", "bike2025", isAdmin = false)
    )

    val bikes = listOf(
        // Christmas Special
        Bike(
            id = "xmas-1",
            name = "Turbo Vado SL 4.0 Holiday Edition",
            brand = "Specialized",
            category = "Mountain",
            price = 3200.0,
            description = "Festive red e-bike engineered for effortless commuting and holiday trail rides with integrated lights and mudguards.",
            imageUrl = "🎄⚡",
            isChristmasSpecial = true,
            inStoreAvailable = true,
            deliveryAvailable = true,
            specs = listOf("E-Bike Motor: Specialized SL 1.1", "Battery: 320Wh", "Weight: 14.9 kg", "Holiday Special Discount: $400 Off")
        ),
        // Main Product (Flagship)
        Bike(
            id = "main-1",
            name = "S-Works Tarmac SL8 - Flagship",
            brand = "Specialized",
            category = "Road",
            price = 12500.0,
            description = "Nothing is faster than the Tarmac SL8 thanks to a combination of aerodynamics, lightweight construction, and ride quality.",
            imageUrl = "🏆🚴‍♂️",
            isMainProduct = true,
            inStoreAvailable = true,
            deliveryAvailable = true,
            specs = listOf("Frameset: FACT 12r Carbon", "Groupset: SRAM RED AXS", "Wheelset: Roval Rapide CLX II", "Aero Optimization")
        ),
        // Road Bikes
        Bike(
            id = "road-1",
            name = "Domane AL 2 Gen 4",
            brand = "Trek",
            category = "Road",
            price = 1199.0,
            description = "Light, fast, and affordable road bike. Perfect for entering the world of road cycling with confidence and comfort.",
            imageUrl = "🛣️🚲",
            inStoreAvailable = true,
            deliveryAvailable = true,
            specs = listOf("Frame: Alpha Aluminum", "Fork: Carbon", "Shifters: Shimano Claris", "Tires: 700x32c")
        ),
        Bike(
            id = "road-2",
            name = "Synapse Carbon 3",
            brand = "Cannondale",
            category = "Road",
            price = 2850.0,
            description = "Smooth, comfortable, and versatile carbon endurance road bike featuring SmartSense integrated safety tech.",
            imageUrl = "🚴‍♀️💨",
            inStoreAvailable = true,
            deliveryAvailable = true,
            specs = listOf("Frame: BallisTec Carbon", "Groupset: Shimano 105", "SmartSense Radar Enabled", "SAVE Micro-Suspension")
        ),
        Bike(
            id = "road-3",
            name = "Contend AR 1",
            brand = "Giant",
            category = "Road",
            price = 1650.0,
            description = "All-rounder road bike designed to handle smooth tarmac, rough roads, and everything in between.",
            imageUrl = "🏁🚴",
            inStoreAvailable = true,
            deliveryAvailable = true,
            specs = listOf("Frame: ALUXX-Grade Aluminum", "Groupset: Shimano 105", "D-Fuse Seatpost", "Tubeless Ready")
        ),
        // Kids Bikes
        Bike(
            id = "kids-1",
            name = "Riprock Coaster 16",
            brand = "Specialized",
            category = "Kids",
            price = 350.0,
            description = "The ultimate bike for kids to learn and build confidence with wide tires and kid-specific geometry.",
            imageUrl = "🧒🚲",
            inStoreAvailable = true,
            deliveryAvailable = true,
            specs = listOf("Wheel Size: 16 inch", "Coaster brake + Hand brake", "Adjustable saddle", "Lightweight Aluminum frame")
        ),
        Bike(
            id = "kids-2",
            name = "Precaliber 20 7-Speed",
            brand = "Trek",
            category = "Kids",
            price = 420.0,
            description = "A versatile kids' multi-speed bike for young adventurers ready to tackle neighborhood hills and dirt paths.",
            imageUrl = "👦🌟",
            inStoreAvailable = true,
            deliveryAvailable = true,
            specs = listOf("Gears: 7-Speed Shimano", "Wheel Size: 20 inch", "Dialed fit components", "Kickstand included")
        ),
        Bike(
            id = "kids-3",
            name = "Animator 16",
            brand = "Giant",
            category = "Kids",
            price = 310.0,
            description = "Fun, durable, and safe starter bike with training wheels included for young riders.",
            imageUrl = "🧒🎈",
            inStoreAvailable = true,
            deliveryAvailable = true,
            specs = listOf("Frame: Lightweight aluminum", "Enclosed chainguard", "Removable training wheels", "V-brakes")
        ),
        // Gravel Bikes
        Bike(
            id = "gravel-1",
            name = "Diverge Expert Carbon",
            brand = "Specialized",
            category = "Gravel",
            price = 4800.0,
            description = "Future Shock 2.0 suspension and carbon frame make this the ultimate gravel explorer for any terrain.",
            imageUrl = "🌲🚵‍♂️",
            inStoreAvailable = true,
            deliveryAvailable = true,
            specs = listOf("Future Shock 2.0", "FACT 8r Carbon Frame", "SRAM Rival eTap AXS", "SWAT Internal Storage")
        ),
        Bike(
            id = "gravel-2",
            name = "Checkpoint SL 5",
            brand = "Trek",
            category = "Gravel",
            price = 3299.0,
            description = "OCLV Carbon gravel bike featuring IsoSpeed comfort decoupling and massive tire clearance.",
            imageUrl = "🏔️🗺️",
            inStoreAvailable = true,
            deliveryAvailable = true,
            specs = listOf("500 Series OCLV Carbon", "Rear IsoSpeed", "Shimano GRX 600/800", "Integrated frame bag mounts")
        ),
        Bike(
            id = "gravel-3",
            name = "Topstone Carbon 2",
            brand = "Cannondale",
            category = "Gravel",
            price = 3750.0,
            description = "Kingpin suspension gravel bike built to float over rough gravel and crush long adventures.",
            imageUrl = "🏜️🚲",
            inStoreAvailable = true,
            deliveryAvailable = true,
            specs = listOf("Kingpin Suspension", "Shimano GRX 800", "700x45mm tire capacity", "Stash storage system")
        ),
        // Mountain Bikes
        Bike(
            id = "mtb-1",
            name = "Spark RC World Cup",
            brand = "Scott",
            category = "Mountain",
            price = 7499.0,
            description = "World Cup winning cross-country full suspension mountain bike with integrated rear shock technology.",
            imageUrl = "⛰️🔥",
            inStoreAvailable = true,
            deliveryAvailable = true,
            specs = listOf("Suspension: 120mm front/rear", "TwinLoc Suspension System", "SRAM X0 Eagle AXS", "Carbon Wheelset")
        ),
        Bike(
            id = "mtb-2",
            name = "Stumpjumper EVO Pro",
            brand = "Specialized",
            category = "Mountain",
            price = 4500.0,
            description = "The ultimate trail ripper with adjustable head angle and unmatched capability on steep descents.",
            imageUrl = "🌲🏔️",
            inStoreAvailable = true,
            deliveryAvailable = true,
            specs = listOf("Travel: 160mm/150mm", "FOX Float Factory suspension", "SRAM Code RSC brakes", "Mullet compatible")
        ),
        Bike(
            id = "mtb-3",
            name = "Fuel EX 8 Gen 6",
            brand = "Trek",
            category = "Mountain",
            price = 3999.0,
            description = "Do-it-all full suspension trail bike ready for technical climbs and rowdy descents.",
            imageUrl = "🚵‍♂️🌲",
            inStoreAvailable = true,
            deliveryAvailable = true,
            specs = listOf("Frame: Alpha Platinum Aluminum", "FOX 36 Rhythm fork", "Shimano SLX/XT drivetrain", "Adjustable geometry")
        )
    )
}
