package com.example.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.example.MainActivity
import com.example.data.ShieldRepository

class OneThingAccessibilityShieldService : AccessibilityService() {
    private lateinit var shieldRepository: ShieldRepository
    private val mainHandler = Handler(Looper.getMainLooper())
    private var lastInterceptionTime = 0L
    private var lastContentScanTime = 0L
    private var lastToastTime = 0L

    override fun onServiceConnected() {
        super.onServiceConnected()
        shieldRepository = ShieldRepository(applicationContext)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        // Only enforce when focus mode is currently running and reward pass is NOT active
        val isFocusActive = FocusShieldService.isFocusActive.value
        val isRewardActive = shieldRepository.isRewardActive()
        if (!isFocusActive || isRewardActive) return

        val pkgName = event.packageName?.toString() ?: return
        val now = System.currentTimeMillis()

        // Debounce: If we just performed an action within the last 1000ms, the system is actively
        // animating window transitions. Do not query mutating nodes during this transition window.
        if (now - lastInterceptionTime < 1000L) {
            return
        }

        // Throttle high-frequency content changes so we don't query IPC nodes during rapid frame redraws
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            if (now - lastContentScanTime < 250L) {
                return
            }
            lastContentScanTime = now
        }

        // 1. Anti-Deletion & Settings Tamper Protection
        if (pkgName == "com.android.settings" ||
            pkgName == "com.android.packageinstaller" ||
            pkgName == "com.google.android.packageinstaller"
        ) {
            val isTampering = rootInActiveWindow.use { root ->
                isTamperingWithOneThing(root)
            } ?: false

            if (isTampering) {
                lastInterceptionTime = now
                performGlobalAction(GLOBAL_ACTION_HOME)
                showNotice("ONE THING is in active focus mode. App changes and uninstallation are restricted.")
                bringOneThingToFront()
                return
            }
            return
        }

        // 2. YouTube Shorts & Instagram Reels Interception
        if (pkgName == YOUTUBE_PKG || pkgName == INSTAGRAM_PKG) {
            // Check if app is already hard-locked (5 strikes reached in this session)
            if (shieldRepository.isAppHardLocked(pkgName)) {
                lastInterceptionTime = now
                performGlobalAction(GLOBAL_ACTION_HOME)
                val appName = if (pkgName == YOUTUBE_PKG) "YouTube" else "Instagram"
                showNotice("5 strikes reached. $appName is locked for the remainder of this focus session.")
                bringOneThingToFront()
                return
            }

            // Inspect if user is inside Shorts or Reels
            val isBlockedFeed = rootInActiveWindow.use { root ->
                if (pkgName == YOUTUBE_PKG) {
                    isViewingYouTubeShorts(root, event)
                } else {
                    isViewingInstagramReels(root, event)
                }
            } ?: false

            if (isBlockedFeed) {
                lastInterceptionTime = now

                // 1. Execute immediate Back action to return user to the home screen
                performGlobalAction(GLOBAL_ACTION_BACK)

                // 2. Increment strike count for this session
                val strikes = shieldRepository.incrementStrike(pkgName)
                val appName = if (pkgName == YOUTUBE_PKG) "YouTube Shorts" else "Instagram Reels"

                if (strikes < 5) {
                    showNotice("🛡️ $appName blocked! Strike $strikes of 5. Return to your goal.")
                } else {
                    // 5 Strikes reached -> Lock the entire app for this session!
                    shieldRepository.lockAppForSession(pkgName)
                    performGlobalAction(GLOBAL_ACTION_HOME)
                    val parentApp = if (pkgName == YOUTUBE_PKG) "YouTube" else "Instagram"
                    showNotice("⛔ 5 strikes reached! $parentApp is now completely locked for this session.")
                    bringOneThingToFront()
                }
                return
            }
            return
        }

        // 3. General Blocked Apps Enforcement
        val activeBlocked = shieldRepository.getActiveBlockedPackages()
        if (pkgName in activeBlocked && pkgName != YOUTUBE_PKG && pkgName != INSTAGRAM_PKG) {
            if (now - lastInterceptionTime > 1500L) {
                lastInterceptionTime = now
                performGlobalAction(GLOBAL_ACTION_HOME)
                showNotice("Stay focused on ONE THING! Entertainment apps are shielded.")
                bringOneThingToFront()
            }
        }
    }

    private fun isViewingYouTubeShorts(root: AccessibilityNodeInfo?, event: AccessibilityEvent): Boolean {
        val className = event.className?.toString() ?: ""
        if (className.contains("Shorts", ignoreCase = true) || className.contains("Reel", ignoreCase = true)) {
            return true
        }

        if (root == null) return false

        // Check view IDs common in YouTube Shorts player
        if (hasAnyViewId(
                root,
                "com.google.android.youtube:id/shorts_player",
                "com.google.android.youtube:id/reel_player",
                "com.google.android.youtube:id/shorts_container"
            )
        ) {
            return true
        }

        // Check text content in active window
        return hasMatchingText(root, "Shorts") { node ->
            val text = node.text?.toString() ?: ""
            val desc = node.contentDescription?.toString() ?: ""
            val isShortsLabel = text.equals("Shorts", ignoreCase = true) || desc.equals("Shorts", ignoreCase = true)
            isShortsLabel && (node.isSelected || node.isFocused || node.className?.contains("Button") == true)
        }
    }

    private fun isViewingInstagramReels(root: AccessibilityNodeInfo?, event: AccessibilityEvent): Boolean {
        val className = event.className?.toString() ?: ""
        if (className.contains("Clips", ignoreCase = true) || className.contains("Reel", ignoreCase = true)) {
            return true
        }

        if (root == null) return false

        if (hasAnyViewId(
                root,
                "com.instagram.android:id/clips_viewer_view_pager",
                "com.instagram.android:id/clips_video_container",
                "com.instagram.android:id/reel_viewer_root"
            )
        ) {
            return true
        }

        return hasMatchingText(root, "Reels") { node ->
            val text = node.text?.toString() ?: ""
            val desc = node.contentDescription?.toString() ?: ""
            val isReelsLabel = text.equals("Reels", ignoreCase = true) || desc.equals("Reels", ignoreCase = true)
            isReelsLabel && (node.isSelected || node.isFocused)
        }
    }

    private fun isTamperingWithOneThing(root: AccessibilityNodeInfo?): Boolean {
        if (root == null) return false
        val hasOneThing = hasMatchingText(root, "ONE THING")
        if (hasOneThing) {
            return hasMatchingText(root, "Uninstall") ||
                hasMatchingText(root, "Force stop") ||
                hasMatchingText(root, "Clear storage")
        }
        return false
    }

    private fun hasAnyViewId(root: AccessibilityNodeInfo, vararg viewIds: String): Boolean {
        for (id in viewIds) {
            val nodes = root.findAccessibilityNodeInfosByViewId(id)
            val found = nodes.isNotEmpty()
            recycleNodes(nodes)
            if (found) return true
        }
        return false
    }

    private fun hasMatchingText(
        root: AccessibilityNodeInfo,
        textQuery: String,
        matcher: ((AccessibilityNodeInfo) -> Boolean)? = null
    ): Boolean {
        val nodes = root.findAccessibilityNodeInfosByText(textQuery)
        var matched = false
        try {
            if (matcher == null) {
                matched = nodes.isNotEmpty()
            } else {
                for (node in nodes) {
                    if (matcher(node)) {
                        matched = true
                        break
                    }
                }
            }
        } finally {
            recycleNodes(nodes)
        }
        return matched
    }

    private inline fun <T> AccessibilityNodeInfo?.use(block: (AccessibilityNodeInfo) -> T): T? {
        if (this == null) return null
        return try {
            block(this)
        } finally {
            recycleNode(this)
        }
    }

    private fun recycleNode(node: AccessibilityNodeInfo?) {
        if (node == null) return
        try {
            @Suppress("DEPRECATION")
            node.recycle()
        } catch (_: Exception) {
            // Safe ignore
        }
    }

    private fun recycleNodes(nodes: List<AccessibilityNodeInfo>?) {
        if (nodes == null) return
        for (node in nodes) {
            recycleNode(node)
        }
    }

    private fun bringOneThingToFront() {
        try {
            val launchIntent = Intent(applicationContext, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(launchIntent)
        } catch (_: Exception) {
            // Ignore background launch errors
        }
    }

    private fun showNotice(message: String) {
        val now = System.currentTimeMillis()
        if (now - lastToastTime > 2000L) {
            lastToastTime = now
            mainHandler.post {
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onInterrupt() {
        // Accessibility service interrupted
    }

    companion object {
        const val YOUTUBE_PKG = "com.google.android.youtube"
        const val INSTAGRAM_PKG = "com.instagram.android"
    }
}
