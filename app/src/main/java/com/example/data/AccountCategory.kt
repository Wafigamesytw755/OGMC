package com.example.data

enum class AccountCategory(
    val id: String,
    val displayName: String,
    val shortName: String,
    val description: String
) {
    TIER_1_HOTMAIL(
        id = "tier_1_hotmail",
        displayName = "Tier 1: Hotmail",
        shortName = "Hotmail",
        description = "Secure and legal Hotmail accounts."
    ),
    TIER_1_MC(
        id = "tier_1_mc",
        displayName = "Tier 1: Minecraft (MC)",
        shortName = "MC Standard",
        description = "Standard Minecraft basic accounts."
    ),
    TIER_2(
        id = "tier_2",
        displayName = "Tier 2: Semi-Permanent MC",
        shortName = "Semi-Perm MC",
        description = "Semi-permanent Minecraft accounts for Please Cloud double-boosters."
    ),
    TIER_3(
        id = "tier_3",
        displayName = "Tier 3: Permanent MC (VIP)",
        shortName = "Perm MC VIP",
        description = "Permanent Minecraft accounts for VIP package holders."
    )
}

enum class TierType(val codePrefix: String, val displayName: String) {
    TIER_2("OG-T2", "Tier 2 Boost"),
    TIER_3("OG-VIP", "Tier 3 VIP")
}
