package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.data.GeneratorRepository
import com.example.ui.components.AdminPasscodeDialog
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.TierSelectionScreen
import com.example.ui.theme.MidnightBlack

enum class AppDestination {
    SPLASH,
    TIER_SELECTION,
    ADMIN
}

@Composable
fun OGGeneratorApp(
    repository: GeneratorRepository
) {
    var currentDestination by remember { mutableStateOf(AppDestination.SPLASH) }
    var showAdminPasscodeDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MidnightBlack
    ) {
        AnimatedContent(
            targetState = currentDestination,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "app_navigation"
        ) { destination ->
            when (destination) {
                AppDestination.SPLASH -> {
                    SplashScreen(
                        onStartGenerator = {
                            currentDestination = AppDestination.TIER_SELECTION
                        },
                        onOpenAdminAuth = {
                            showAdminPasscodeDialog = true
                        }
                    )
                }
                AppDestination.TIER_SELECTION -> {
                    TierSelectionScreen(
                        repository = repository,
                        onBackToSplash = {
                            currentDestination = AppDestination.SPLASH
                        },
                        onOpenAdmin = {
                            showAdminPasscodeDialog = true
                        }
                    )
                }
                AppDestination.ADMIN -> {
                    AdminScreen(
                        repository = repository,
                        onBack = {
                            currentDestination = AppDestination.TIER_SELECTION
                        }
                    )
                }
            }
        }

        if (showAdminPasscodeDialog) {
            AdminPasscodeDialog(
                onDismiss = { showAdminPasscodeDialog = false },
                onSuccess = {
                    showAdminPasscodeDialog = false
                    currentDestination = AppDestination.ADMIN
                }
            )
        }
    }
}
