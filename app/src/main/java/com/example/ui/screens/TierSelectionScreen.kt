package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.AccountCategory
import com.example.data.GeneratorRepository
import com.example.data.TierType
import com.example.ui.components.ActivationCodeDialog
import com.example.ui.components.RoyalAccountPopup
import com.example.ui.components.RoyalGlassCard
import com.example.ui.components.RoyalGoldButton
import com.example.ui.components.RoyalOutlinedButton
import com.example.ui.components.StockBadge
import com.example.ui.components.TierActivatedSuccessDialog
import com.example.ui.theme.DarkGlassGradient
import com.example.ui.theme.MidnightBlack
import com.example.ui.theme.MidnightBlackDeep
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.MidnightSurfaceElevated
import com.example.ui.theme.PurpleGoldGlow
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.RoyalGoldBright
import com.example.ui.theme.RoyalGoldDark
import com.example.ui.theme.RoyalGoldGradient
import com.example.ui.theme.RoyalGoldLight
import com.example.ui.theme.RoyalPurple
import com.example.ui.theme.RoyalSuccess
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TierSelectionScreen(
    repository: GeneratorRepository,
    onBackToSplash: () -> Unit,
    onOpenAdmin: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // State from repository
    val stocks by repository.stocks.collectAsState()
    val isTier2Unlocked by repository.isTier2Unlocked.collectAsState()
    val isTier3Unlocked by repository.isTier3Unlocked.collectAsState()

    // UI Dialog & Sheet states
    var isTier1SubMenuOpen by remember { mutableStateOf(false) }
    var showActivationDialogForTier by remember { mutableStateOf<TierType?>(null) }
    var showSuccessDialogForTier2 by remember { mutableStateOf(false) }
    var showSuccessDialogForTier3 by remember { mutableStateOf(false) }
    var generatedAccountResult by remember { mutableStateOf<Pair<String, String>?>(null) } // Category Name to Account

    BackHandler {
        onBackToSplash()
    }

    // Helper for pulling an account
    fun handleGenerateAccount(category: AccountCategory) {
        val account = repository.pullAccount(category)
        if (account != null) {
            generatedAccountResult = Pair(category.displayName, account)
        } else {
            Toast.makeText(
                context,
                "Stock depleted for ${category.shortName}! Please check back soon or contact admin.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Royal Top App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBackToSplash,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MidnightSurfaceElevated)
                        .border(1.dp, RoyalGold.copy(alpha = 0.3f), CircleShape)
                        .testTag("tier_screen_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = RoyalGoldLight
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "TIER SELECTION",
                        color = RoyalGoldBright,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "CHOOSE YOUR GENERATOR",
                        color = RoyalGoldDark,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                }

                // Small discreet crown icon
                IconButton(
                    onClick = onOpenAdmin,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MidnightSurfaceElevated)
                        .border(1.dp, RoyalGold.copy(alpha = 0.3f), CircleShape)
                        .testTag("tier_screen_admin_crown_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = "Admin Area",
                        tint = RoyalGoldLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // User Hub Title Banner
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to the Royal Hub",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Select a tier below to generate safe and verified accounts.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                )
            }

            // ==================== TIER 1 (FREE) ====================
            TierCardItem(
                tierBadge = "TIER 1 • FREE ACCESS",
                title = "Hotmail & Minecraft Standard",
                description = "You can generate secure and legal Hotmail accounts.",
                icon = Icons.Default.Email,
                accentColor = RoyalGoldLight,
                borderBrush = RoyalGoldGradient,
                isUnlocked = true,
                liveStockCount = (stocks[AccountCategory.TIER_1_HOTMAIL]?.size ?: 0) +
                        (stocks[AccountCategory.TIER_1_MC]?.size ?: 0),
                actionText = "OPEN TIER 1 GENERATOR",
                onActionClick = {
                    isTier1SubMenuOpen = true
                },
                testTag = "tier_1_card"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ==================== TIER 2 (BOOST) ====================
            val t2Stock = stocks[AccountCategory.TIER_2]?.size ?: 0
            TierCardItem(
                tierBadge = "TIER 2 • PLEASE CLOUD BOOST",
                title = "Semi-Permanent Minecraft",
                description = "If you double-boosted the Please Cloud server, you can generate semi-permanent Minecraft accounts safely and legally.",
                icon = Icons.Default.RocketLaunch,
                accentColor = RoyalGoldBright,
                borderBrush = Brush.linearGradient(
                    listOf(RoyalPurple, RoyalGoldLight, RoyalPurple)
                ),
                isUnlocked = isTier2Unlocked,
                liveStockCount = t2Stock,
                actionText = if (isTier2Unlocked) "GENERATE SEMI-PERM MC" else "ENTER ACTIVATION CODE",
                onActionClick = {
                    if (isTier2Unlocked) {
                        handleGenerateAccount(AccountCategory.TIER_2)
                    } else {
                        showActivationDialogForTier = TierType.TIER_2
                    }
                },
                testTag = "tier_2_card"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ==================== TIER 3 (VIP) ====================
            val t3Stock = stocks[AccountCategory.TIER_3]?.size ?: 0
            TierCardItem(
                tierBadge = "TIER 3 • VIP EXCLUSIVE",
                title = "Permanent Minecraft (VIP)",
                description = "If you purchased Tier 3 packages, you can generate permanent Minecraft accounts safely and legally.",
                icon = Icons.Default.Diamond,
                accentColor = RoyalGoldBright,
                borderBrush = RoyalGoldGradient,
                isUnlocked = isTier3Unlocked,
                liveStockCount = t3Stock,
                actionText = if (isTier3Unlocked) "GENERATE PERMANENT MC" else "ENTER VIP CODE",
                onActionClick = {
                    if (isTier3Unlocked) {
                        handleGenerateAccount(AccountCategory.TIER_3)
                    } else {
                        showActivationDialogForTier = TierType.TIER_3
                    }
                },
                testTag = "tier_3_card"
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // ==================== TIER 1 SUB-MENU DIALOG ====================
    if (isTier1SubMenuOpen) {
        val hotmailStock = stocks[AccountCategory.TIER_1_HOTMAIL]?.size ?: 0
        val mcStock = stocks[AccountCategory.TIER_1_MC]?.size ?: 0

        Dialog(onDismissRequest = { isTier1SubMenuOpen = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .shadow(24.dp, RoundedCornerShape(22.dp), spotColor = RoyalGoldLight),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MidnightBlackDeep),
                border = BorderStroke(1.8.dp, RoyalGoldGradient)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "TIER 1 GENERATOR",
                        color = RoyalGoldBright,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp
                    )

                    Text(
                        text = "Choose your desired account category to generate instantly:",
                        color = TextSecondary,
                        fontSize = 12.5.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 6.dp, bottom = 18.dp)
                    )

                    // Option 1: Generator Hotmail
                    SubMenuOptionCard(
                        title = "Generator Hotmail",
                        subtitle = "Clean, verified Hotmail access",
                        icon = Icons.Default.Email,
                        stockCount = hotmailStock,
                        onGenerate = {
                            isTier1SubMenuOpen = false
                            handleGenerateAccount(AccountCategory.TIER_1_HOTMAIL)
                        },
                        testTag = "generate_hotmail_button"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Option 2: Generator MC
                    SubMenuOptionCard(
                        title = "Generator MC",
                        subtitle = "Standard Minecraft credentials",
                        icon = Icons.Default.Games,
                        stockCount = mcStock,
                        onGenerate = {
                            isTier1SubMenuOpen = false
                            handleGenerateAccount(AccountCategory.TIER_1_MC)
                        },
                        testTag = "generate_mc_button"
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    TextButton(
                        onClick = { isTier1SubMenuOpen = false },
                        modifier = Modifier.testTag("close_tier_1_sub_menu")
                    ) {
                        Text(text = "Close", color = TextSecondary, fontSize = 14.sp)
                    }
                }
            }
        }
    }

    // ==================== ACTIVATION CODE DIALOG ====================
    showActivationDialogForTier?.let { tier ->
        ActivationCodeDialog(
            tierTitle = tier.displayName,
            tierDescription = if (tier == TierType.TIER_2) {
                "Enter your Please Cloud double-boost activation code to unlock Semi-Permanent Minecraft generation."
            } else {
                "Enter your official VIP package authorization code to unlock Permanent Minecraft generation."
            },
            onDismiss = { showActivationDialogForTier = null },
            onRedeem = { code ->
                val success = repository.redeemCode(tier, code)
                if (success) {
                    showActivationDialogForTier = null
                    if (tier == TierType.TIER_2) {
                        showSuccessDialogForTier2 = true
                    } else {
                        showSuccessDialogForTier3 = true
                    }
                }
                success
            }
        )
    }

    // ==================== TIER 2 SUCCESS POPUP ====================
    if (showSuccessDialogForTier2) {
        TierActivatedSuccessDialog(
            arabicMessage = "مبروووووك تم تفعيل التاير ٢",
            englishSubtitle = "Congratulations! Tier 2 Semi-Permanent Minecraft Generator is now fully unlocked.",
            onDismiss = {
                showSuccessDialogForTier2 = false
            }
        )
    }

    // ==================== TIER 3 SUCCESS POPUP ====================
    if (showSuccessDialogForTier3) {
        TierActivatedSuccessDialog(
            arabicMessage = "مبروووووك تم تفعيل VIP تاير ٣",
            englishSubtitle = "Congratulations! Tier 3 Permanent Minecraft Generator is now fully unlocked.",
            onDismiss = {
                showSuccessDialogForTier3 = false
            }
        )
    }

    // ==================== GENERATED ACCOUNT POPUP ====================
    generatedAccountResult?.let { (categoryName, account) ->
        RoyalAccountPopup(
            categoryName = categoryName,
            account = account,
            onDismiss = { generatedAccountResult = null }
        )
    }
}

@Composable
private fun TierCardItem(
    tierBadge: String,
    title: String,
    description: String,
    icon: ImageVector,
    accentColor: Color,
    borderBrush: Brush,
    isUnlocked: Boolean,
    liveStockCount: Int,
    actionText: String,
    onActionClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(16.dp, RoundedCornerShape(20.dp), spotColor = RoyalGoldLight)
            .testTag(testTag),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightSurface.copy(alpha = 0.92f)),
        border = BorderStroke(1.4.dp, borderBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkGlassGradient)
                .padding(20.dp)
        ) {
            // Tier Badge & Stock
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MidnightSurfaceElevated,
                    border = BorderStroke(1.dp, accentColor.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = tierBadge,
                        color = accentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                StockBadge(count = liveStockCount)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title with Icon
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MidnightSurfaceElevated)
                        .border(1.2.dp, accentColor.copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isUnlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isUnlocked) RoyalSuccess else TextMuted,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isUnlocked) "Access Granted" else "Activation Required",
                            color = if (isUnlocked) RoyalSuccess else TextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description required in prompt
            Text(
                text = description,
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.5.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Action Button
            RoyalGoldButton(
                text = actionText,
                icon = if (isUnlocked) Icons.Default.WorkspacePremium else Icons.Default.Key,
                onClick = onActionClick,
                modifier = Modifier.fillMaxWidth(),
                testTag = "${testTag}_button"
            )
        }
    }
}

@Composable
private fun SubMenuOptionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    stockCount: Int,
    onGenerate: () -> Unit,
    testTag: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, RoyalGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
        color = MidnightSurfaceElevated,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = RoyalGoldLight,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = subtitle,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
                StockBadge(count = stockCount)
            }

            Spacer(modifier = Modifier.height(12.dp))

            RoyalGoldButton(
                text = "GENERATE",
                icon = Icons.Default.WorkspacePremium,
                onClick = onGenerate,
                enabled = stockCount > 0,
                modifier = Modifier.fillMaxWidth(),
                testTag = testTag
            )
        }
    }
}
