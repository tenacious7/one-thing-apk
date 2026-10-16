package com.example.model

data class DistractionTargetApp(
    val packageName: String,
    val appName: String,
    val category: String,
    val isEnabled: Boolean = true,
    val isRewardAllowed: Boolean = true,
    val isCustom: Boolean = false
)

object DefaultShieldApps {
    val predefinedTargets: List<DistractionTargetApp> = listOf(
        DistractionTargetApp("com.instagram.android", "Instagram", "Social Media"),
        DistractionTargetApp("com.google.android.youtube", "YouTube", "Video & Shorts"),
        DistractionTargetApp("com.zhiliaoapp.musically", "TikTok", "Short Video"),
        DistractionTargetApp("com.twitter.android", "X (Twitter)", "Social Feeds"),
        DistractionTargetApp("com.reddit.frontpage", "Reddit", "Social Feeds"),
        DistractionTargetApp("com.facebook.katana", "Facebook", "Social Media"),
        DistractionTargetApp("com.netflix.mediaclient", "Netflix", "Streaming Video"),
        DistractionTargetApp("tv.twitch.android.app", "Twitch", "Live Streams"),
        DistractionTargetApp("com.snapchat.android", "Snapchat", "Messaging & Feeds")
    )
}

data class FocusTimerState(
    val selectedDurationMinutes: Int = 45,
    val remainingSeconds: Int = 45 * 60,
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val isCompleted: Boolean = false
)

data class RewardTimerState(
    val totalSeconds: Int = 5 * 60,
    val remainingSeconds: Int = 0,
    val isActive: Boolean = false,
    val expiryTimestamp: Long = 0L
)
