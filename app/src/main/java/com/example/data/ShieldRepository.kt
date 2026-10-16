package com.example.data

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import com.example.model.DefaultShieldApps
import com.example.model.DistractionTargetApp
import org.json.JSONArray
import org.json.JSONObject

class ShieldRepository(private val context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("one_thing_shield_prefs", Context.MODE_PRIVATE)

    fun getFocusDurationMinutes(): Int {
        return prefs.getInt(KEY_FOCUS_DURATION, 45)
    }

    fun setFocusDurationMinutes(minutes: Int) {
        prefs.edit().putInt(KEY_FOCUS_DURATION, minutes.coerceAtLeast(1)).apply()
    }

    fun getTargetApps(): List<DistractionTargetApp> {
        val disabledList = prefs.getStringSet(KEY_DISABLED_APPS, emptySet()) ?: emptySet()
        val rewardDeniedList = prefs.getStringSet(KEY_REWARD_DENIED_APPS, emptySet()) ?: emptySet()
        val customApps = getCustomAppsInternal()

        val defaultList = DefaultShieldApps.predefinedTargets.map { target ->
            target.copy(
                isEnabled = target.packageName !in disabledList,
                isRewardAllowed = target.packageName !in rewardDeniedList,
                isCustom = false
            )
        }

        val customMapped = customApps.map { custom ->
            custom.copy(
                isEnabled = custom.packageName !in disabledList,
                isRewardAllowed = custom.packageName !in rewardDeniedList,
                isCustom = true
            )
        }

        return defaultList + customMapped
    }

    fun setAppBlocked(packageName: String, blocked: Boolean) {
        val disabled = prefs.getStringSet(KEY_DISABLED_APPS, emptySet())?.toMutableSet() ?: mutableSetOf()
        if (blocked) {
            disabled.remove(packageName)
        } else {
            disabled.add(packageName)
        }
        prefs.edit().putStringSet(KEY_DISABLED_APPS, disabled).apply()
    }

    fun selectAllApps() {
        prefs.edit().remove(KEY_DISABLED_APPS).apply()
    }

    fun deselectAllApps() {
        val allPkgs = getTargetApps().map { it.packageName }.toSet()
        prefs.edit().putStringSet(KEY_DISABLED_APPS, allPkgs).apply()
    }

    fun addCustomApp(packageName: String, appName: String, category: String) {
        val cleanPkg = packageName.trim()
        val cleanName = appName.trim().ifEmpty { cleanPkg }
        val cleanCat = category.trim().ifEmpty { "Custom App" }

        val current = getCustomAppsInternal().toMutableList()
        current.removeAll { it.packageName.equals(cleanPkg, ignoreCase = true) }
        current.add(DistractionTargetApp(cleanPkg, cleanName, cleanCat, isEnabled = true, isCustom = true))
        saveCustomAppsInternal(current)

        // Ensure newly added app is not in disabled set
        val disabled = prefs.getStringSet(KEY_DISABLED_APPS, emptySet())?.toMutableSet() ?: mutableSetOf()
        disabled.remove(cleanPkg)
        prefs.edit().putStringSet(KEY_DISABLED_APPS, disabled).apply()
    }

    fun removeCustomApp(packageName: String) {
        val current = getCustomAppsInternal().toMutableList()
        current.removeAll { it.packageName.equals(packageName, ignoreCase = true) }
        saveCustomAppsInternal(current)

        val disabled = prefs.getStringSet(KEY_DISABLED_APPS, emptySet())?.toMutableSet() ?: mutableSetOf()
        disabled.remove(packageName)
        prefs.edit().putStringSet(KEY_DISABLED_APPS, disabled).apply()
    }

    private fun getCustomAppsInternal(): List<DistractionTargetApp> {
        val raw = prefs.getString(KEY_CUSTOM_APPS_JSON, "[]") ?: "[]"
        val list = mutableListOf<DistractionTargetApp>()
        try {
            val jsonArray = JSONArray(raw)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    DistractionTargetApp(
                        packageName = obj.getString("pkg"),
                        appName = obj.getString("name"),
                        category = obj.optString("cat", "Custom App"),
                        isCustom = true
                    )
                )
            }
        } catch (e: Exception) {
            // Ignore parse errors
        }
        return list
    }

    private fun saveCustomAppsInternal(apps: List<DistractionTargetApp>) {
        val jsonArray = JSONArray()
        for (app in apps) {
            val obj = JSONObject().apply {
                put("pkg", app.packageName)
                put("name", app.appName)
                put("cat", app.category)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString(KEY_CUSTOM_APPS_JSON, jsonArray.toString()).apply()
    }

    fun getActiveBlockedPackages(): Set<String> {
        val disabledList = prefs.getStringSet(KEY_DISABLED_APPS, emptySet()) ?: emptySet()
        return getTargetApps()
            .filter { it.packageName !in disabledList }
            .map { it.packageName }
            .toSet()
    }

    // --- Strike Counter & 5-Attempt Lockout System ---

    fun getStrikes(packageName: String): Int {
        return prefs.getInt(KEY_STRIKE_PREFIX + packageName, 0)
    }

    fun incrementStrike(packageName: String): Int {
        val current = getStrikes(packageName) + 1
        prefs.edit().putInt(KEY_STRIKE_PREFIX + packageName, current).apply()
        if (current >= 5) {
            lockAppForSession(packageName)
        }
        return current
    }

    fun getAllStrikes(): Map<String, Int> {
        val all = mutableMapOf<String, Int>()
        val allKeys = prefs.all
        for ((key, value) in allKeys) {
            if (key.startsWith(KEY_STRIKE_PREFIX) && value is Int) {
                val pkg = key.removePrefix(KEY_STRIKE_PREFIX)
                all[pkg] = value
            }
        }
        return all
    }

    fun resetStrikes() {
        val editor = prefs.edit()
        val allKeys = prefs.all.keys
        for (key in allKeys) {
            if (key.startsWith(KEY_STRIKE_PREFIX)) {
                editor.remove(key)
            }
        }
        editor.remove(KEY_HARD_LOCKED_APPS)
        editor.apply()
    }

    fun getHardLockedApps(): Set<String> {
        return prefs.getStringSet(KEY_HARD_LOCKED_APPS, emptySet()) ?: emptySet()
    }

    fun lockAppForSession(packageName: String) {
        val current = getHardLockedApps().toMutableSet()
        current.add(packageName)
        prefs.edit().putStringSet(KEY_HARD_LOCKED_APPS, current).apply()
    }

    fun isAppHardLocked(packageName: String): Boolean {
        return packageName in getHardLockedApps()
    }

    fun clearHardLockedApps() {
        prefs.edit().remove(KEY_HARD_LOCKED_APPS).apply()
    }

    // --- Reward Pass ---

    fun getRewardExpiryTimestamp(): Long {
        return prefs.getLong(KEY_REWARD_EXPIRY, 0L)
    }

    fun startRewardPass(durationMinutes: Int = 5) {
        val expiry = System.currentTimeMillis() + (durationMinutes * 60 * 1000L)
        prefs.edit().putLong(KEY_REWARD_EXPIRY, expiry).apply()
    }

    fun clearRewardPass() {
        prefs.edit().remove(KEY_REWARD_EXPIRY).apply()
    }

    fun isRewardActive(): Boolean {
        val expiry = getRewardExpiryTimestamp()
        return expiry > System.currentTimeMillis()
    }

    fun getFocusSessionEndTimestamp(): Long {
        return prefs.getLong(KEY_FOCUS_SESSION_END, 0L)
    }

    fun setFocusSessionEndTimestamp(timestamp: Long) {
        prefs.edit().putLong(KEY_FOCUS_SESSION_END, timestamp).apply()
    }

    fun isAccessibilityServiceEnabled(): Boolean {
        return try {
            val enabledServices = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            ) ?: ""
            enabledServices.contains(context.packageName) &&
                enabledServices.contains("OneThingAccessibilityShieldService")
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        private const val KEY_FOCUS_DURATION = "focus_duration"
        private const val KEY_DISABLED_APPS = "disabled_apps"
        private const val KEY_REWARD_DENIED_APPS = "reward_denied_apps"
        private const val KEY_REWARD_EXPIRY = "reward_expiry"
        private const val KEY_FOCUS_SESSION_END = "focus_session_end"
        private const val KEY_CUSTOM_APPS_JSON = "custom_apps_json"
        private const val KEY_STRIKE_PREFIX = "strike_count_"
        private const val KEY_HARD_LOCKED_APPS = "hard_locked_apps_set"
    }
}
