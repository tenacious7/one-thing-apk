package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ShieldRepository
import com.example.data.TopicCatalog
import com.example.data.UserProgressRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("ONE THING", appName)
    }

    @Test
    fun `single category selection returns all 3 topics from that category`() {
        val selection = TopicCatalog.getDailySelection(
            seed = 12345L,
            count = 3,
            preferredCategories = setOf("cat_psychology")
        )
        assertEquals(3, selection.size)
        assertTrue(selection.all { it.category == "Psychology and Human Behavior" })
    }

    @Test
    fun `excluded topics are never returned in daily selection`() {
        val excludedId = "dark_psych_sunk_cost"
        val selection = TopicCatalog.getDailySelection(
            seed = 54321L,
            count = 3,
            excludedIds = listOf(excludedId)
        )
        assertFalse(selection.any { it.id == excludedId })
    }

    @Test
    fun `shield repository manages custom apps and select all or none`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = ShieldRepository(context)

        repo.addCustomApp("com.custom.game", "Custom Game", "Gaming")
        val appsWithCustom = repo.getTargetApps()
        assertTrue(appsWithCustom.any { it.packageName == "com.custom.game" })

        repo.deselectAllApps()
        val blockedWhenDeselected = repo.getActiveBlockedPackages()
        assertTrue(blockedWhenDeselected.isEmpty())

        repo.selectAllApps()
        val blockedWhenSelected = repo.getActiveBlockedPackages()
        assertTrue(blockedWhenSelected.contains("com.custom.game"))

        repo.removeCustomApp("com.custom.game")
        val appsAfterRemoval = repo.getTargetApps()
        assertFalse(appsAfterRemoval.any { it.packageName == "com.custom.game" })
    }

    @Test
    fun `shield repository increments strikes and hard locks app on fifth strike`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = ShieldRepository(context)
        repo.resetStrikes()

        val yt = "com.google.android.youtube"
        assertEquals(0, repo.getStrikes(yt))
        assertFalse(repo.isAppHardLocked(yt))

        // First 4 strikes
        for (i in 1..4) {
            val strikeCount = repo.incrementStrike(yt)
            assertEquals(i, strikeCount)
            assertFalse(repo.isAppHardLocked(yt))
        }

        // 5th strike triggers hard lock
        val fifthStrike = repo.incrementStrike(yt)
        assertEquals(5, fifthStrike)
        assertTrue(repo.isAppHardLocked(yt))

        // Reset clears strikes and hard lock
        repo.resetStrikes()
        assertEquals(0, repo.getStrikes(yt))
        assertFalse(repo.isAppHardLocked(yt))
    }

    @Test
    fun `user progress completes today and records streak`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = UserProgressRepository(context)

        val (streak, total) = repo.completeToday("dark_psych_sunk_cost", "My test reflection notes.")
        assertTrue(streak >= 1)
        assertTrue(total >= 1)
        assertTrue(repo.getCompletedTopicIds().contains("dark_psych_sunk_cost"))
    }
}
