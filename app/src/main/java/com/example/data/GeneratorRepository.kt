package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import java.util.UUID

class GeneratorRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("og_generator_storage", Context.MODE_PRIVATE)

    // Flow states
    private val _stocks = MutableStateFlow<Map<AccountCategory, List<String>>>(emptyMap())
    val stocks: StateFlow<Map<AccountCategory, List<String>>> = _stocks.asStateFlow()

    private val _tier2Codes = MutableStateFlow<List<String>>(emptyList())
    val tier2Codes: StateFlow<List<String>> = _tier2Codes.asStateFlow()

    private val _tier3Codes = MutableStateFlow<List<String>>(emptyList())
    val tier3Codes: StateFlow<List<String>> = _tier3Codes.asStateFlow()

    private val _isTier2Unlocked = MutableStateFlow(false)
    val isTier2Unlocked: StateFlow<Boolean> = _isTier2Unlocked.asStateFlow()

    private val _isTier3Unlocked = MutableStateFlow(false)
    val isTier3Unlocked: StateFlow<Boolean> = _isTier3Unlocked.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val isFirstLaunch = !prefs.contains("has_initialized_defaults")

        if (isFirstLaunch) {
            seedInitialData()
            prefs.edit().putBoolean("has_initialized_defaults", true).apply()
        }

        // Load stocks
        val stockMap = mutableMapOf<AccountCategory, List<String>>()
        for (category in AccountCategory.values()) {
            val jsonStr = prefs.getString("stock_${category.id}", null)
            val list = if (jsonStr != null) {
                deserializeList(jsonStr)
            } else {
                emptyList()
            }
            stockMap[category] = list
        }
        _stocks.value = stockMap

        // Load codes
        val t2CodesJson = prefs.getString("codes_tier2", null)
        _tier2Codes.value = if (t2CodesJson != null) deserializeList(t2CodesJson) else emptyList()

        val t3CodesJson = prefs.getString("codes_tier3", null)
        _tier3Codes.value = if (t3CodesJson != null) deserializeList(t3CodesJson) else emptyList()

        // Load unlock states
        _isTier2Unlocked.value = prefs.getBoolean("unlocked_tier2", false)
        _isTier3Unlocked.value = prefs.getBoolean("unlocked_tier3", false)
    }

    private fun seedInitialData() {
        val initialStocks = mapOf(
            AccountCategory.TIER_1_HOTMAIL to listOf(
                "vortex_royal01@hotmail.com:Vort!x9901",
                "shadow_legal42@hotmail.com:Pass#Shadow42",
                "phoenix_secure@hotmail.com:Ph0enix!Gold",
                "nexus_prime77@hotmail.com:Nexus#7788",
                "elite_falcon19@hotmail.com:Falcon!Pass8"
            ),
            AccountCategory.TIER_1_MC to listOf(
                "craft_lord2026@hotmail.com:Mine#CraftKing1",
                "block_runner99@hotmail.com:Diamond!Sword8",
                "nether_walker@hotmail.com:Obsidian#990",
                "ender_knight@hotmail.com:Pearl!Knight77"
            ),
            AccountCategory.TIER_2 to listOf(
                "boost_cloud_mc01@outlook.com:CloudBoost#2026A",
                "double_booster_vip@outlook.com:PleaseCloud#88X",
                "semiperm_steve@outlook.com:SemiPerm!Craft9"
            ),
            AccountCategory.TIER_3 to listOf(
                "vip_sovereign_perm@outlook.com:Permanent#VIP99",
                "royal_mc_lifetime@outlook.com:Lifetime#King2026",
                "emperor_craft_vip@outlook.com:Emperor!Craft4"
            )
        )

        for ((category, list) in initialStocks) {
            prefs.edit().putString("stock_${category.id}", serializeList(list)).apply()
        }

        val initialT2Codes = listOf("OG-T2-CLOUD-8841", "OG-T2-BOOST-3392", "OG-T2-VIP-7715")
        val initialT3Codes = listOf("OG-VIP-PERM-9912", "OG-VIP-ROYAL-5541", "OG-VIP-LIFETIME-007")

        prefs.edit()
            .putString("codes_tier2", serializeList(initialT2Codes))
            .putString("codes_tier3", serializeList(initialT3Codes))
            .apply()
    }

    fun addStock(category: AccountCategory, rawInput: String): Int {
        val lines = rawInput.split("\n", "\r", ",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        if (lines.isEmpty()) return 0

        val currentList = _stocks.value[category]?.toMutableList() ?: mutableListOf()
        currentList.addAll(lines)

        val updatedMap = _stocks.value.toMutableMap()
        updatedMap[category] = currentList
        _stocks.value = updatedMap

        prefs.edit().putString("stock_${category.id}", serializeList(currentList)).apply()
        return lines.size
    }

    fun clearStock(category: AccountCategory) {
        val updatedMap = _stocks.value.toMutableMap()
        updatedMap[category] = emptyList()
        _stocks.value = updatedMap

        prefs.edit().putString("stock_${category.id}", serializeList(emptyList())).apply()
    }

    fun pullAccount(category: AccountCategory): String? {
        val currentList = _stocks.value[category]?.toMutableList() ?: return null
        if (currentList.isEmpty()) return null

        val randomIndex = currentList.indices.random()
        val account = currentList.removeAt(randomIndex)

        val updatedMap = _stocks.value.toMutableMap()
        updatedMap[category] = currentList
        _stocks.value = updatedMap

        prefs.edit().putString("stock_${category.id}", serializeList(currentList)).apply()
        return account
    }

    fun generateActivationCode(tier: TierType): String {
        val randomSuffix = UUID.randomUUID().toString().substring(0, 8).uppercase()
        val code = "${tier.codePrefix}-$randomSuffix"

        if (tier == TierType.TIER_2) {
            val list = _tier2Codes.value.toMutableList()
            list.add(0, code)
            _tier2Codes.value = list
            prefs.edit().putString("codes_tier2", serializeList(list)).apply()
        } else {
            val list = _tier3Codes.value.toMutableList()
            list.add(0, code)
            _tier3Codes.value = list
            prefs.edit().putString("codes_tier3", serializeList(list)).apply()
        }
        return code
    }

    fun deleteActivationCode(tier: TierType, code: String) {
        if (tier == TierType.TIER_2) {
            val list = _tier2Codes.value.filter { it != code }
            _tier2Codes.value = list
            prefs.edit().putString("codes_tier2", serializeList(list)).apply()
        } else {
            val list = _tier3Codes.value.filter { it != code }
            _tier3Codes.value = list
            prefs.edit().putString("codes_tier3", serializeList(list)).apply()
        }
    }

    fun redeemCode(tier: TierType, inputCode: String): Boolean {
        val cleanCode = inputCode.trim()
        if (tier == TierType.TIER_2) {
            val list = _tier2Codes.value
            val match = list.find { it.equals(cleanCode, ignoreCase = true) }
            if (match != null) {
                // Redeem code
                val updated = list.filter { it != match }
                _tier2Codes.value = updated
                prefs.edit().putString("codes_tier2", serializeList(updated)).apply()

                _isTier2Unlocked.value = true
                prefs.edit().putBoolean("unlocked_tier2", true).apply()
                return true
            }
        } else {
            val list = _tier3Codes.value
            val match = list.find { it.equals(cleanCode, ignoreCase = true) }
            if (match != null) {
                // Redeem code
                val updated = list.filter { it != match }
                _tier3Codes.value = updated
                prefs.edit().putString("codes_tier3", serializeList(updated)).apply()

                _isTier3Unlocked.value = true
                prefs.edit().putBoolean("unlocked_tier3", true).apply()
                return true
            }
        }
        return false
    }

    fun resetActivationsForTesting() {
        _isTier2Unlocked.value = false
        _isTier3Unlocked.value = false
        prefs.edit()
            .putBoolean("unlocked_tier2", false)
            .putBoolean("unlocked_tier3", false)
            .apply()
    }

    private fun serializeList(list: List<String>): String {
        val arr = JSONArray()
        list.forEach { arr.put(it) }
        return arr.toString()
    }

    private fun deserializeList(jsonStr: String): List<String> {
        val result = mutableListOf<String>()
        try {
            val arr = JSONArray(jsonStr)
            for (i in 0 until arr.length()) {
                result.add(arr.getString(i))
            }
        } catch (_: Exception) {}
        return result
    }
}
