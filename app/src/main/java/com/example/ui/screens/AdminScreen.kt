package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.AccountCategory
import com.example.data.GeneratorRepository
import com.example.data.TierType
import com.example.ui.components.RoyalGlassCard
import com.example.ui.components.RoyalGoldButton
import com.example.ui.components.RoyalOutlinedButton
import com.example.ui.components.StockBadge
import com.example.ui.theme.DarkGlassGradient
import com.example.ui.theme.MidnightBlack
import com.example.ui.theme.MidnightBlackDeep
import com.example.ui.theme.MidnightCardBorder
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.MidnightSurfaceElevated
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.RoyalGoldBright
import com.example.ui.theme.RoyalGoldDark
import com.example.ui.theme.RoyalGoldGradient
import com.example.ui.theme.RoyalGoldLight
import com.example.ui.theme.RoyalPurple
import com.example.ui.theme.RoyalSuccess
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AdminScreen(
    repository: GeneratorRepository,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Filling Stock", "Gen Code Tier 2", "Gen Code Tier 3")

    val stocks by repository.stocks.collectAsState()
    val tier2Codes by repository.tier2Codes.collectAsState()
    val tier3Codes by repository.tier3Codes.collectAsState()

    var showAddStockDialogForCategory by remember { mutableStateOf<AccountCategory?>(null) }
    var showStockViewDialogForCategory by remember { mutableStateOf<AccountCategory?>(null) }

    BackHandler {
        onBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Admin Top Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MidnightSurfaceElevated)
                        .border(1.dp, RoyalGold.copy(alpha = 0.3f), CircleShape)
                        .testTag("admin_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = RoyalGoldLight
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = RoyalGoldLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ADMIN DASHBOARD",
                            color = RoyalGoldBright,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = "WAFI OG CONTROL PANEL",
                        color = RoyalGoldDark,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                }

                IconButton(
                    onClick = {
                        repository.resetActivationsForTesting()
                        Toast.makeText(context, "User Tier unlocks reset for re-testing!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MidnightSurfaceElevated)
                        .border(1.dp, RoyalGold.copy(alpha = 0.3f), CircleShape)
                        .testTag("admin_reset_testing_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset Activations",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Tab Navigation
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MidnightSurface,
                contentColor = RoyalGoldLight,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = RoyalGoldLight,
                        height = 3.dp
                    )
                },
                divider = {}
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTabIndex == index) RoyalGoldBright else TextMuted,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier.testTag("admin_tab_$index")
                    )
                }
            }

            // Tab Content
            when (selectedTabIndex) {
                0 -> FillingStockTab(
                    stocks = stocks,
                    onOpenAddStock = { category -> showAddStockDialogForCategory = category },
                    onOpenViewStock = { category -> showStockViewDialogForCategory = category }
                )
                1 -> GenCodeTab(
                    tierType = TierType.TIER_2,
                    codes = tier2Codes,
                    onGenerateCode = { repository.generateActivationCode(TierType.TIER_2) },
                    onDeleteCode = { code -> repository.deleteActivationCode(TierType.TIER_2, code) }
                )
                2 -> GenCodeTab(
                    tierType = TierType.TIER_3,
                    codes = tier3Codes,
                    onGenerateCode = { repository.generateActivationCode(TierType.TIER_3) },
                    onDeleteCode = { code -> repository.deleteActivationCode(TierType.TIER_3, code) }
                )
            }
        }
    }

    // Add Stock Dialog
    showAddStockDialogForCategory?.let { category ->
        AddStockDialog(
            category = category,
            onDismiss = { showAddStockDialogForCategory = null },
            onAdd = { rawInput ->
                val addedCount = repository.addStock(category, rawInput)
                Toast.makeText(context, "Added $addedCount accounts to ${category.shortName}!", Toast.LENGTH_SHORT).show()
                showAddStockDialogForCategory = null
            }
        )
    }

    // View Stock Dialog
    showStockViewDialogForCategory?.let { category ->
        val list = stocks[category] ?: emptyList()
        ViewStockDialog(
            category = category,
            accounts = list,
            onDismiss = { showStockViewDialogForCategory = null },
            onClear = {
                repository.clearStock(category)
                Toast.makeText(context, "Cleared stock for ${category.shortName}", Toast.LENGTH_SHORT).show()
                showStockViewDialogForCategory = null
            }
        )
    }
}

@Composable
private fun FillingStockTab(
    stocks: Map<AccountCategory, List<String>>,
    onOpenAddStock: (AccountCategory) -> Unit,
    onOpenViewStock: (AccountCategory) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "INVENTORY & STOCK FILLING",
            color = RoyalGoldBright,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Text(
            text = "Input and replenish accounts for each category. Changes persist across sessions.",
            color = TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
        )

        // 4 Specific Categories required:
        // Tier 1 Hotmail, Tier 1 Minecraft (MC), Tier 2, Tier 3
        StockCategoryRow(
            category = AccountCategory.TIER_1_HOTMAIL,
            icon = Icons.Default.Email,
            count = stocks[AccountCategory.TIER_1_HOTMAIL]?.size ?: 0,
            onAdd = { onOpenAddStock(AccountCategory.TIER_1_HOTMAIL) },
            onView = { onOpenViewStock(AccountCategory.TIER_1_HOTMAIL) }
        )

        Spacer(modifier = Modifier.height(14.dp))

        StockCategoryRow(
            category = AccountCategory.TIER_1_MC,
            icon = Icons.Default.SportsEsports,
            count = stocks[AccountCategory.TIER_1_MC]?.size ?: 0,
            onAdd = { onOpenAddStock(AccountCategory.TIER_1_MC) },
            onView = { onOpenViewStock(AccountCategory.TIER_1_MC) }
        )

        Spacer(modifier = Modifier.height(14.dp))

        StockCategoryRow(
            category = AccountCategory.TIER_2,
            icon = Icons.Default.RocketLaunch,
            count = stocks[AccountCategory.TIER_2]?.size ?: 0,
            onAdd = { onOpenAddStock(AccountCategory.TIER_2) },
            onView = { onOpenViewStock(AccountCategory.TIER_2) }
        )

        Spacer(modifier = Modifier.height(14.dp))

        StockCategoryRow(
            category = AccountCategory.TIER_3,
            icon = Icons.Default.Diamond,
            count = stocks[AccountCategory.TIER_3]?.size ?: 0,
            onAdd = { onOpenAddStock(AccountCategory.TIER_3) },
            onView = { onOpenViewStock(AccountCategory.TIER_3) }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun StockCategoryRow(
    category: AccountCategory,
    icon: ImageVector,
    count: Int,
    onAdd: () -> Unit,
    onView: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = RoyalGoldLight)
            .testTag("admin_stock_card_${category.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightSurfaceElevated),
        border = BorderStroke(1.2.dp, RoyalGold.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MidnightSurface)
                            .border(1.dp, RoyalGold.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = RoyalGoldLight,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = category.displayName,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = category.description,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }
                }

                StockBadge(count = count)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onView,
                    modifier = Modifier.testTag("view_stock_${category.id}")
                ) {
                    Text(text = "View / Clear", color = TextSecondary, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.width(8.dp))

                RoyalGoldButton(
                    text = "Add Stock",
                    icon = Icons.Default.Add,
                    onClick = onAdd,
                    modifier = Modifier.height(42.dp),
                    testTag = "add_stock_btn_${category.id}"
                )
            }
        }
    }
}

@Composable
private fun GenCodeTab(
    tierType: TierType,
    codes: List<String>,
    onGenerateCode: () -> String,
    onDeleteCode: (String) -> Unit
) {
    val context = LocalContext.current
    var justGeneratedCode by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "ACTIVATION CODE GENERATOR (${tierType.displayName.uppercase()})",
            color = RoyalGoldBright,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Text(
            text = "Generate and distribute unique activation keys to authorized users with one-click copy.",
            color = TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
        )

        // Generate Code Button
        RoyalGoldButton(
            text = "GENERATE ${tierType.displayName.uppercase()} CODE",
            icon = Icons.Default.Key,
            onClick = {
                val newCode = onGenerateCode()
                justGeneratedCode = newCode
                Toast.makeText(context, "Generated code: $newCode", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(),
            testTag = "gen_code_button_${tierType.name}"
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Active Codes List Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Active Codes (${codes.size})",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "One-Click Copy",
                color = RoyalGoldDark,
                fontSize = 11.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (codes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MidnightSurfaceElevated)
                    .border(1.dp, MidnightCardBorder, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No active codes generated yet.",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(codes, key = { it }) { code ->
                    CodeItemRow(
                        code = code,
                        onCopy = {
                            val clipboard =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Activation Code", code)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Code copied: $code", Toast.LENGTH_SHORT).show()
                        },
                        onDelete = { onDeleteCode(code) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CodeItemRow(
    code: String,
    onCopy: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, RoyalGold.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            .testTag("code_item_$code"),
        color = MidnightSurfaceElevated
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Key,
                    contentDescription = null,
                    tint = RoyalGoldLight,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = code,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // One-click copy button
                IconButton(
                    onClick = onCopy,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MidnightSurface)
                        .border(1.dp, RoyalGold.copy(alpha = 0.4f), CircleShape)
                        .testTag("copy_code_$code")
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Code",
                        tint = RoyalGoldLight,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Delete button
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MidnightSurface)
                        .border(1.dp, Color.Red.copy(alpha = 0.3f), CircleShape)
                        .testTag("delete_code_$code")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Revoke Code",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AddStockDialog(
    category: AccountCategory,
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {
    var rawInput by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp)
                .shadow(24.dp, RoundedCornerShape(20.dp), spotColor = RoyalGold),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MidnightBlackDeep),
            border = BorderStroke(1.5.dp, RoyalGoldGradient)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "ADD STOCK: ${category.shortName.uppercase()}",
                    color = RoyalGoldBright,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "Paste combo lines below (one account per line in format user:password or email:password):",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                OutlinedTextField(
                    value = rawInput,
                    onValueChange = { rawInput = it },
                    placeholder = {
                        Text(
                            "player1@mail.com:Password123\nplayer2@mail.com:Pass9928\n...",
                            color = TextMuted,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .testTag("admin_bulk_stock_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoyalGold,
                        unfocusedBorderColor = RoyalGold.copy(alpha = 0.4f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Quick presets
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = {
                            val sample = "combo_demo_${System.currentTimeMillis() % 1000}@og.com:OG#Pass2026\n" +
                                    "vault_account_${(10..99).random()}@mail.com:RoyalCrown#99"
                            rawInput = if (rawInput.isBlank()) sample else "$rawInput\n$sample"
                        }
                    ) {
                        Text("+ Insert Sample Combos", color = RoyalGoldLight, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = TextSecondary)
                    }

                    RoyalGoldButton(
                        text = "ADD TO STOCK",
                        onClick = {
                            if (rawInput.isNotBlank()) {
                                onAdd(rawInput)
                            }
                        },
                        modifier = Modifier.width(160.dp),
                        testTag = "submit_add_stock_btn"
                    )
                }
            }
        }
    }
}

@Composable
private fun ViewStockDialog(
    category: AccountCategory,
    accounts: List<String>,
    onDismiss: () -> Unit,
    onClear: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp)
                .shadow(24.dp, RoundedCornerShape(20.dp), spotColor = RoyalGold),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MidnightBlackDeep),
            border = BorderStroke(1.5.dp, RoyalGoldGradient)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "${category.shortName.uppercase()} INVENTORY",
                            color = RoyalGoldBright,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${accounts.size} accounts in stock",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                    StockBadge(count = accounts.size)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 240.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MidnightSurfaceElevated,
                    border = BorderStroke(1.dp, MidnightCardBorder)
                ) {
                    if (accounts.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(30.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Inventory is currently empty.",
                                color = TextMuted,
                                fontSize = 13.sp
                            )
                        }
                    } else {
                        LazyColumn(modifier = Modifier.padding(12.dp)) {
                            items(accounts) { acc ->
                                Text(
                                    text = acc,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.padding(vertical = 3.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = onClear,
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFEF4444))
                    ) {
                        Text("Clear All Stock", fontSize = 12.sp)
                    }

                    RoyalGoldButton(
                        text = "CLOSE",
                        onClick = onDismiss,
                        modifier = Modifier.width(110.dp)
                    )
                }
            }
        }
    }
}
