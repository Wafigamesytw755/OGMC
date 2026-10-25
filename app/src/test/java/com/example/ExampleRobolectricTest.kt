package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AccountCategory
import com.example.data.GeneratorRepository
import com.example.data.TierType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var context: Context
    private lateinit var repository: GeneratorRepository

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext<Context>()
        repository = GeneratorRepository(context)
    }

    @Test
    fun testAppNameString() {
        val appName = context.getString(R.string.app_name)
        assertEquals("OG Generator", appName)
    }

    @Test
    fun testInitialStocksSeeded() {
        val hotmailStock = repository.stocks.value[AccountCategory.TIER_1_HOTMAIL]
        assertNotNull(hotmailStock)
        assertTrue((hotmailStock?.size ?: 0) > 0)

        val mcStock = repository.stocks.value[AccountCategory.TIER_1_MC]
        assertNotNull(mcStock)
        assertTrue((mcStock?.size ?: 0) > 0)
    }

    @Test
    fun testAddAndPullStock() {
        val initialCount = repository.stocks.value[AccountCategory.TIER_1_HOTMAIL]?.size ?: 0
        val added = repository.addStock(
            AccountCategory.TIER_1_HOTMAIL,
            "test_user_1@hotmail.com:Pass1\ntest_user_2@hotmail.com:Pass2"
        )
        assertEquals(2, added)

        val updatedCount = repository.stocks.value[AccountCategory.TIER_1_HOTMAIL]?.size ?: 0
        assertEquals(initialCount + 2, updatedCount)

        val pulledAccount = repository.pullAccount(AccountCategory.TIER_1_HOTMAIL)
        assertNotNull(pulledAccount)
        val finalCount = repository.stocks.value[AccountCategory.TIER_1_HOTMAIL]?.size ?: 0
        assertEquals(updatedCount - 1, finalCount)
    }

    @Test
    fun testTier2CodeGenerationAndRedemption() {
        val newCode = repository.generateActivationCode(TierType.TIER_2)
        assertTrue(newCode.startsWith("OG-T2-"))
        assertTrue(repository.tier2Codes.value.contains(newCode))

        val invalidRedeem = repository.redeemCode(TierType.TIER_2, "INVALID-CODE")
        assertFalse(invalidRedeem)

        val validRedeem = repository.redeemCode(TierType.TIER_2, newCode)
        assertTrue(validRedeem)
        assertTrue(repository.isTier2Unlocked.value)
    }

    @Test
    fun testTier3CodeGenerationAndRedemption() {
        val newCode = repository.generateActivationCode(TierType.TIER_3)
        assertTrue(newCode.startsWith("OG-VIP-"))
        assertTrue(repository.tier3Codes.value.contains(newCode))

        val validRedeem = repository.redeemCode(TierType.TIER_3, newCode)
        assertTrue(validRedeem)
        assertTrue(repository.isTier3Unlocked.value)
    }
}
