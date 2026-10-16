package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.data.ShieldRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class FocusShieldService : Service() {
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private lateinit var shieldRepository: ShieldRepository

    override fun onCreate() {
        super.onCreate()
        shieldRepository = ShieldRepository(applicationContext)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action ?: ACTION_START_FOCUS
        when (action) {
            ACTION_START_FOCUS -> {
                val durationMinutes = intent?.getIntExtra(EXTRA_DURATION_MINUTES, 45) ?: 45
                startFocusSession(durationMinutes)
            }
            ACTION_START_REWARD -> {
                val rewardMinutes = intent?.getIntExtra(EXTRA_REWARD_MINUTES, 5) ?: 5
                startRewardSession(rewardMinutes)
            }
            ACTION_STOP -> {
                stopFocusSession()
                stopSelf()
            }
            ACTION_EMERGENCY_EXIT -> {
                _shieldAlertApp.value = null
                stopFocusSession()
                stopSelf()
            }
        }
        return START_STICKY
    }

    private fun startFocusSession(durationMinutes: Int) {
        val totalSec = durationMinutes * 60
        _focusRemainingSeconds.value = totalSec
        _isFocusActive.value = true
        _isRewardActive.value = false
        _shieldAlertApp.value = null
        shieldRepository.resetStrikes()
        shieldRepository.clearHardLockedApps()

        startForeground(NOTIFICATION_ID, buildFocusNotification(totalSec))

        serviceScope.launch {
            while (isActive && _isFocusActive.value) {
                delay(1000L)
                val currentSec = _focusRemainingSeconds.value - 1
                if (currentSec <= 0) {
                    _focusRemainingSeconds.value = 0
                    _isFocusActive.value = false
                    updateNotification("Focus Session Complete", "Great job! Time to claim your 5-minute reward.")
                    break
                }
                _focusRemainingSeconds.value = currentSec

                // Update notification periodically
                if (currentSec % 30 == 0) {
                    val m = currentSec / 60
                    val s = currentSec % 60
                    updateNotification("Focus Shield Active", String.format("%02d:%02d remaining", m, s))
                }

                // Check foreground app for distraction monitoring
                checkForegroundApp()
            }
        }
    }

    private fun startRewardSession(rewardMinutes: Int) {
        shieldRepository.startRewardPass(rewardMinutes)
        val totalSec = rewardMinutes * 60
        _rewardRemainingSeconds.value = totalSec
        _isRewardActive.value = true
        _isFocusActive.value = false
        _shieldAlertApp.value = null

        startForeground(NOTIFICATION_ID, buildRewardNotification(totalSec))

        serviceScope.launch {
            while (isActive && _isRewardActive.value) {
                delay(1000L)
                val currentSec = _rewardRemainingSeconds.value - 1
                if (currentSec <= 0) {
                    _rewardRemainingSeconds.value = 0
                    _isRewardActive.value = false
                    shieldRepository.clearRewardPass()
                    updateNotification("Reward Pass Expired", "Entertainment time is over. Focus shield restored.")
                    stopSelf()
                    break
                }
                _rewardRemainingSeconds.value = currentSec

                if (currentSec % 15 == 0) {
                    val m = currentSec / 60
                    val s = currentSec % 60
                    updateNotification("5-Min Reward Pass Active", String.format("%02d:%02d entertainment access remaining", m, s))
                }
            }
        }
    }

    private fun checkForegroundApp() {
        if (!_isFocusActive.value || _isRewardActive.value) return

        try {
            val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager ?: return
            val time = System.currentTimeMillis()
            val stats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                time - 10000,
                time
            )

            if (!stats.isNullOrEmpty()) {
                val sortedStats = stats.sortedByDescending { it.lastTimeUsed }
                val topPackage = sortedStats.firstOrNull()?.packageName

                if (topPackage != null && topPackage != packageName) {
                    val blockedPackages = shieldRepository.getActiveBlockedPackages()
                    if (topPackage in blockedPackages) {
                        _shieldAlertApp.value = topPackage
                    } else if (_shieldAlertApp.value != null && topPackage == packageName) {
                        // User returned to ONE THING, clear alert
                        _shieldAlertApp.value = null
                    }
                }
            }
        } catch (e: Exception) {
            // Ignore background query exceptions
        }
    }

    private fun stopFocusSession() {
        _isFocusActive.value = false
        _isRewardActive.value = false
        _shieldAlertApp.value = null
        shieldRepository.resetStrikes()
        shieldRepository.clearHardLockedApps()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "ONE THING Focus Shield",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows focus session and entertainment reward countdowns"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun buildFocusNotification(remainingSec: Int): Notification {
        val m = remainingSec / 60
        val s = remainingSec % 60
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("ONE THING • Focus Shield Active")
            .setContentText(String.format("%02d:%02d remaining in focus session", m, s))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun buildRewardNotification(remainingSec: Int): Notification {
        val m = remainingSec / 60
        val s = remainingSec % 60
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("ONE THING • Reward Pass Active")
            .setContentText(String.format("%02d:%02d entertainment access remaining", m, s))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification(title: String, text: String) {
        val manager = getSystemService(NotificationManager::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .build()
        manager?.notify(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val CHANNEL_ID = "one_thing_focus_channel"
        const val NOTIFICATION_ID = 1001

        const val ACTION_START_FOCUS = "com.example.action.START_FOCUS"
        const val ACTION_START_REWARD = "com.example.action.START_REWARD"
        const val ACTION_STOP = "com.example.action.STOP"
        const val ACTION_EMERGENCY_EXIT = "com.example.action.EMERGENCY_EXIT"

        const val EXTRA_DURATION_MINUTES = "extra_duration_minutes"
        const val EXTRA_REWARD_MINUTES = "extra_reward_minutes"

        private val _focusRemainingSeconds = MutableStateFlow(45 * 60)
        val focusRemainingSeconds: StateFlow<Int> = _focusRemainingSeconds.asStateFlow()

        private val _isFocusActive = MutableStateFlow(false)
        val isFocusActive: StateFlow<Boolean> = _isFocusActive.asStateFlow()

        private val _rewardRemainingSeconds = MutableStateFlow(0)
        val rewardRemainingSeconds: StateFlow<Int> = _rewardRemainingSeconds.asStateFlow()

        private val _isRewardActive = MutableStateFlow(false)
        val isRewardActive: StateFlow<Boolean> = _isRewardActive.asStateFlow()

        private val _shieldAlertApp = MutableStateFlow<String?>(null)
        val shieldAlertApp: StateFlow<String?> = _shieldAlertApp.asStateFlow()

        fun dismissAlert() {
            _shieldAlertApp.value = null
        }
    }
}
