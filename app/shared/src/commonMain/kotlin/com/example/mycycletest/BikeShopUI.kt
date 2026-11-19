package com.example.mycycletest

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BikeShopApp() {
    val users = BikeShopData.initialUsers
    val dbProducts = remember { mutableStateListOf<UiProduct>() }
    val configuratorParts = remember {
        mutableStateListOf(
            ConfiguratorPart(
                id = "f-1",
                name = "Aero Carbon Frame Pro",
                type = "Frame",
                imageUrl = "https://www.mycycle.ie/v/vspfiles/photos/FRAME-CARBON-2.jpg",
                points = mutableListOf(
                    ConnectionPoint("p1", "Front Derailleur", 0.45f, 0.6f),
                    ConnectionPoint("p2", "Rear Derailleur", 0.7f, 0.75f),
                    ConnectionPoint("p3", "Front Brake Caliper", 0.25f, 0.4f),
                    ConnectionPoint("p4", "Rear Brake Caliper", 0.75f, 0.65f),
                    ConnectionPoint("p5", "Chainwheel", 0.5f, 0.65f),
                    ConnectionPoint("p6", "Levers / Cockpit", 0.25f, 0.25f)
                )
            ),
            ConfiguratorPart(
                id = "w-1",
                name = "Carbon Disc Wheels 50mm",
                type = "Wheels",
                imageUrl = "https://www.mycycle.ie/v/vspfiles/photos/WHEELS-CARBON-2.jpg",
                points = mutableListOf(
                    ConnectionPoint("w1", "Hub Axle Joint", 0.5f, 0.5f)
                )
            )
        )
    }

    var currentScreen by remember { mutableStateOf<ShopScreen>(ShopScreen.Home) }
    var currentUser by remember { mutableStateOf<User?>(null) }
    var selectedTab by remember { mutableStateOf("All") }
    var selectedBrandFilter by remember { mutableStateOf<String?>(null) }
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }
    var colorFilterInput by remember { mutableStateOf("") }
    var sizeFilterInput by remember { mutableStateOf("") }
    var currentPage by remember { mutableStateOf(0) }
    val pageSize = 64

    val cartItems = remember { mutableStateListOf<CartItem>() }
    var showCartDialog by remember { mutableStateOf(false) }
    var showLoginDialog by remember { mutableStateOf(false) }

    // Apple style hover flyout states
    var hoveredNavCategory by remember { mutableStateOf<String?>(null) }

    val appleBlack = Color(0xFF161617)
    val primaryDark = Color(0xFF1D1D1F)
    val accentRed = Color(0xFFD70015)
    val holidayGreen = Color(0xFF1B4D3E)

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = appleBlack,
            secondary = accentRed,
            background = Color(0xFFF5F5F7),
            surface = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Sleek Black Apple-Style Global Nav Bar with Hover Flyouts
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = appleBlack,
                tonalElevation = 4.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Logo
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                currentScreen = ShopScreen.Home
                                updateWebRoute("#/")
                                selectedTab = "All"
                                selectedBrandFilter = null
                                selectedCategoryFilter = null
                                colorFilterInput = ""
                                sizeFilterInput = ""
                            }
                        ) {
                            Text("🚲", fontSize = 26.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "MyCycle",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }

                        // Nav Categories with Hover Flyout (`globalnav-flyout-content globalnav-submenu-content`)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(28.dp)
                        ) {
                            // Brands Flyout Item
                            Box(
                                modifier = Modifier.pointerInput(Unit) {
                                    awaitPointerEventScope {
                                        while (true) {
                                            val event = awaitPointerEvent()
                                            if (event.type == PointerEventType.Enter) {
                                                hoveredNavCategory = "Brands"
                                            }
                                        }
                                    }
                                }
                            ) {
                                TextButton(onClick = { hoveredNavCategory = if (hoveredNavCategory == "Brands") null else "Brands" }) {
                                    Text("Brands", fontWeight = FontWeight.Normal, color = Color.LightGray, fontSize = 14.sp)
                                }
                            }

                            // Road Flyout Item
                            Box(
                                modifier = Modifier.pointerInput(Unit) {
                                    awaitPointerEventScope {
                                        while (true) {
                                            val event = awaitPointerEvent()
                                            if (event.type == PointerEventType.Enter) {
                                                hoveredNavCategory = "Road"
                                            }
                                        }
                                    }
                                }
                            ) {
                                TextButton(onClick = { hoveredNavCategory = if (hoveredNavCategory == "Road") null else "Road" }) {
                                    Text("Road", fontWeight = FontWeight.Normal, color = Color.LightGray, fontSize = 14.sp)
                                }
                            }

                            BikeShopData.categories.filter { it != "Road" }.forEach { category ->
                                TextButton(
                                    onClick = {
                                        selectedCategoryFilter = category
                                        selectedBrandFilter = null
                                        selectedTab = "All"
                                        currentScreen = ShopScreen.Home
                                        updateWebRoute("#/")
                                        hoveredNavCategory = null
                                    }
                                ) {
                                    Text(
                                        category,
                                        fontWeight = FontWeight.Normal,
                                        color = if (selectedCategoryFilter == category) Color.White else Color.LightGray,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        // Right Actions
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (currentUser?.isAdmin == true) {
                                Button(
                                    onClick = {
                                        if (currentScreen is ShopScreen.AdminPanel) {
                                            currentScreen = ShopScreen.Home
                                            updateWebRoute("#/")
                                        } else {
                                            currentScreen = ShopScreen.AdminPanel
                                            updateWebRoute("#/admin")
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = holidayGreen)
                                ) {
                                    Text(if (currentScreen is ShopScreen.AdminPanel) "View Store" else "Admin Shop Control", color = Color.White)
                                }
                            } else if (currentUser != null) {
                                Button(
                                    onClick = {
                                        if (currentScreen is ShopScreen.CustomerPortal) {
                                            currentScreen = ShopScreen.Home
                                            updateWebRoute("#/")
                                        } else {
                                            currentScreen = ShopScreen.CustomerPortal
                                            updateWebRoute("#/customer")
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0071E3))
                                ) {
                                    Text(if (currentScreen is ShopScreen.CustomerPortal) "View Store" else "My Bikes & Tasks", color = Color.White)
                                }
                            }

                            if (currentUser != null) {
                                Text("Hi, ${currentUser!!.username}", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                                TextButton(onClick = { currentUser = null; currentScreen = ShopScreen.Home; updateWebRoute("#/") }) {
                                    Text("Logout", color = Color.LightGray, fontSize = 14.sp)
                                }
                            } else {
                                TextButton(onClick = { showLoginDialog = true }) {
                                    Text("Sign In", color = Color.LightGray, fontSize = 14.sp)
                                }
                            }

                            BadgedBox(
                                badge = {
                                    if (cartItems.isNotEmpty()) {
                                        Badge { Text("${cartItems.sumOf { it.quantity }}") }
                                    }
                                }
                            ) {
                                IconButton(onClick = { showCartDialog = true }) {
                                    Text("🛒", fontSize = 20.sp)
                                }
                            }
                        }
                    }

                    // Apple Flyout Submenu Content (`globalnav-flyout-content globalnav-submenu-content`)
                    if (hoveredNavCategory != null) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(Unit) {
                                    awaitPointerEventScope {
                                        while (true) {
                                            val event = awaitPointerEvent()
                                            if (event.type == PointerEventType.Exit) {
                                                hoveredNavCategory = null
                                            }
                                        }
                                    }
                                },
                            color = Color(0xFF222223),
                            tonalElevation = 8.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 64.dp, vertical = 24.dp),
                                horizontalArrangement = Arrangement.spacedBy(64.dp)
                            ) {
                                if (hoveredNavCategory == "Brands") {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text("Explore Brands", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        BikeShopData.brands.forEach { brand ->
                                            TextButton(onClick = {
                                                selectedBrandFilter = brand
                                                selectedCategoryFilter = null
                                                selectedTab = "All"
                                                currentScreen = ShopScreen.Home
                                                updateWebRoute("#/")
                                                hoveredNavCategory = null
                                            }) {
                                                Text(brand, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                            }
                                        }
                                    }
                                } else if (hoveredNavCategory == "Road") {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text("Road Categories", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        TextButton(onClick = {
                                            selectedCategoryFilter = "Road"
                                            selectedBrandFilter = null
                                            selectedTab = "All"
                                            currentScreen = ShopScreen.Home
                                            updateWebRoute("#/")
                                            hoveredNavCategory = null
                                        }) {
                                            Text("Road Bikes", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                                        }
                                        TextButton(onClick = {
                                            currentScreen = ShopScreen.BikeConfigurator
                                            updateWebRoute("#/configurator")
                                            hoveredNavCategory = null
                                        }) {
                                            Text("⚙️ Bike Configurator", color = Color(0xFF0071E3), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Main Screen Routing Switcher
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                when (val screen = currentScreen) {
                    is ShopScreen.AdminPanel -> {
                        AdminShopControlPanel(
                            users = users,
                            products = dbProducts,
                            configuratorParts = configuratorParts,
                            onDismiss = { currentScreen = ShopScreen.Home; updateWebRoute("#/") }
                        )
                    }
                    is ShopScreen.CustomerPortal -> {
                        CustomerPortalScreen(currentUser = currentUser!!)
                    }
                    is ShopScreen.BikeConfigurator -> {
                        BikeConfiguratorScreen(
                            parts = configuratorParts,
                            onBack = { currentScreen = ShopScreen.Home; updateWebRoute("#/") }
                        )
                    }
                    is ShopScreen.ProductDetail -> {
                        ProductDetailScreen(
                            product = screen.product,
                            onBack = { currentScreen = ShopScreen.Home; updateWebRoute("#/") },
                            onAddToCart = { prod, fulfillment ->
                                val bikeModel = Bike(
                                    id = prod.id.toString(),
                                    name = prod.productCode,
                                    brand = prod.brand,
                                    category = prod.category,
                                    price = prod.price,
                                    description = prod.description,
                                    imageUrl = "🚲"
                                )
                                cartItems.add(CartItem(bikeModel, fulfillment, 1))
                                showCartDialog = true
                            }
                        )
                    }
                    is ShopScreen.Home -> {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (dbProducts.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(96.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("🚲", fontSize = 64.sp)
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text("No products in store database.", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Log in as admin (admin / changeme) and use Product Import to select a vendor CSV file.", color = Color.Gray, fontSize = 14.sp)
                                    }
                                }
                            } else {
                                val mainProduct = dbProducts.firstOrNull { it.isMain } ?: dbProducts.firstOrNull()
                                if (mainProduct != null && selectedBrandFilter == null && selectedCategoryFilter == null && colorFilterInput.isBlank() && sizeFilterInput.isBlank() && selectedTab == "All") {
                                    MainProductHero(
                                        product = mainProduct,
                                        onProductClick = {
                                            currentScreen = ShopScreen.ProductDetail(mainProduct)
                                            updateWebRoute("#/product/${mainProduct.id}")
                                        }
                                    )
                                }

                                BikeConfiguratorBanner(
                                    onOpenConfigurator = {
                                        currentScreen = ShopScreen.BikeConfigurator
                                        updateWebRoute("#/configurator")
                                    }
                                )

                                val xmasProduct = dbProducts.firstOrNull { it.isChristmas }
                                if (xmasProduct != null && selectedBrandFilter == null && selectedCategoryFilter == null && colorFilterInput.isBlank() && sizeFilterInput.isBlank() && selectedTab == "All") {
                                    ChristmasHeroBanner(
                                        product = xmasProduct,
                                        onProductClick = {
                                            currentScreen = ShopScreen.ProductDetail(xmasProduct)
                                            updateWebRoute("#/product/${xmasProduct.id}")
                                        }
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 32.dp, vertical = 24.dp)
                                ) {
                                    Text(
                                        "Bike Catalog Storage (Max 64 per page)",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = primaryDark
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = colorFilterInput,
                                            onValueChange = { colorFilterInput = it; currentPage = 0 },
                                            label = { Text("Filter by Color (e.g. Red, Black)") },
                                            modifier = Modifier.weight(1f)
                                        )
                                        OutlinedTextField(
                                            value = sizeFilterInput,
                                            onValueChange = { sizeFilterInput = it; currentPage = 0 },
                                            label = { Text("Filter by Size (e.g. 56, M, L)") },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    val tabs = listOf("All", "Kids", "Road", "Mountain", "Gravel")
                                    Row(
                                        modifier = Modifier
                                            .background(Color(0xFFE5E5EA), RoundedCornerShape(24.dp))
                                            .padding(4.dp),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        tabs.forEach { tab ->
                                            val isSelected = selectedTab == tab
                                            Surface(
                                                modifier = Modifier.clickable { selectedTab = tab; currentPage = 0 },
                                                shape = RoundedCornerShape(20.dp),
                                                color = if (isSelected) Color.White else Color.Transparent,
                                                shadowElevation = if (isSelected) 2.dp else 0.dp
                                            ) {
                                                Text(
                                                    text = tab,
                                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSelected) primaryDark else Color.Gray
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(24.dp))

                                    val filteredProducts = dbProducts.filter { product ->
                                        val matchesTab = selectedTab == "All" || product.category.equals(selectedTab, ignoreCase = true)
                                        val matchesBrand = selectedBrandFilter == null || product.brand.equals(selectedBrandFilter, ignoreCase = true)
                                        val matchesCategory = selectedCategoryFilter == null || product.category.equals(selectedCategoryFilter, ignoreCase = true)
                                        val matchesColor = colorFilterInput.isBlank() || product.color.contains(colorFilterInput, ignoreCase = true)
                                        val matchesSize = sizeFilterInput.isBlank() || product.size.contains(sizeFilterInput, ignoreCase = true)
                                        matchesTab && matchesBrand && matchesCategory && matchesColor && matchesSize
                                    }

                                    val pagedProducts = filteredProducts.drop(currentPage * pageSize).take(pageSize)

                                    if (filteredProducts.isEmpty()) {
                                        Text("No products found matching filters.", color = Color.Gray, fontSize = 16.sp)
                                    } else {
                                        ProductGrid(
                                            products = pagedProducts,
                                            onProductClick = { prod ->
                                                currentScreen = ShopScreen.ProductDetail(prod)
                                                updateWebRoute("#/product/${prod.id}")
                                            },
                                            onAddToCart = { product ->
                                                val bikeModel = Bike(
                                                    id = product.id.toString(),
                                                    name = product.productCode,
                                                    brand = product.brand,
                                                    category = product.category,
                                                    price = product.price,
                                                    description = product.description,
                                                    imageUrl = "🚲"
                                                )
                                                cartItems.add(CartItem(bikeModel, FulfillmentType.DELIVERY, 1))
                                            }
                                        )

                                        if (filteredProducts.size > pageSize) {
                                            Spacer(modifier = Modifier.height(24.dp))
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.Center,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Button(
                                                    onClick = { if (currentPage > 0) currentPage-- },
                                                    enabled = currentPage > 0
                                                ) {
                                                    Text("Previous Page")
                                                }
                                                Spacer(modifier = Modifier.width(16.dp))
                                                Text("Page ${currentPage + 1} of ${(filteredProducts.size + pageSize - 1) / pageSize}")
                                                Spacer(modifier = Modifier.width(16.dp))
                                                Button(
                                                    onClick = { if ((currentPage + 1) * pageSize < filteredProducts.size) currentPage++ },
                                                    enabled = (currentPage + 1) * pageSize < filteredProducts.size
                                                ) {
                                                    Text("Next Page")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showCartDialog) {
            CartDialog(
                cartItems = cartItems,
                onDismiss = { showCartDialog = false },
                onCheckout = {
                    cartItems.clear()
                    showCartDialog = false
                }
            )
        }

        if (showLoginDialog) {
            LoginDialog(
                users = users,
                onDismiss = { showLoginDialog = false },
                onLoginSuccess = { user ->
                    currentUser = user
                    showLoginDialog = false
                }
            )
        }
    }
}
