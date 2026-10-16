package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.LearningTopic
import com.example.model.UserProgress
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class UserProgressRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("one_thing_user_data", Context.MODE_PRIVATE)

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    fun getTodayDateString(): String = dateFormat.format(Date())

    fun loadUserProgress(): Pair<UserProgress, List<LearningTopic>> {
        val todayStr = getTodayDateString()
        val savedDate = prefs.getString(KEY_SAVED_DATE, null)
        var streak = prefs.getInt(KEY_STREAK, 0)
        val totalCompleted = prefs.getInt(KEY_TOTAL_COMPLETED, 0)
        val lastCompletedDate = prefs.getString(KEY_LAST_COMPLETED_DATE, null)

        val candidateIds: List<String>
        val selectedId: String?
        val isCompleted: Boolean
        val reflection: String

        if (savedDate == todayStr) {
            // Same day: load existing progress
            val rawCandidates = prefs.getString(KEY_CANDIDATE_IDS, "") ?: ""
            candidateIds = if (rawCandidates.isNotEmpty()) rawCandidates.split(",") else emptyList()
            selectedId = prefs.getString(KEY_SELECTED_ID, null)
            isCompleted = prefs.getBoolean(KEY_IS_COMPLETED, false)
            reflection = prefs.getString(KEY_REFLECTION, "") ?: ""
        } else {
            // New day!
            // Check if streak was broken (if last completed date was not yesterday)
            if (lastCompletedDate != null) {
                val cal = Calendar.getInstance()
                cal.add(Calendar.DAY_OF_YEAR, -1)
                val yesterdayStr = dateFormat.format(cal.time)
                if (lastCompletedDate != yesterdayStr && lastCompletedDate != todayStr) {
                    streak = 0
                    prefs.edit().putInt(KEY_STREAK, streak).apply()
                }
            } else {
                streak = 0
            }

            // Generate 3 fresh candidate topics for today
            val completedIds = getCompletedTopicIds().toList()
            val freshCandidates = TopicCatalog.getDailySelection(
                seed = System.currentTimeMillis(),
                excludedIds = completedIds
            )
            candidateIds = freshCandidates.map { it.id }
            selectedId = null
            isCompleted = false
            reflection = ""

            // Persist reset daily state
            prefs.edit()
                .putString(KEY_SAVED_DATE, todayStr)
                .putString(KEY_CANDIDATE_IDS, candidateIds.joinToString(","))
                .remove(KEY_SELECTED_ID)
                .putBoolean(KEY_IS_COMPLETED, false)
                .putString(KEY_REFLECTION, "")
                .apply()
        }

        // Map candidate IDs to LearningTopic items
        var candidateTopics = candidateIds.mapNotNull { id ->
            TopicCatalog.allTopics.find { it.id == id }
        }

        if (candidateTopics.size < 3) {
            val completedIds = getCompletedTopicIds().toList()
            candidateTopics = TopicCatalog.getDailySelection(
                seed = System.currentTimeMillis(),
                excludedIds = completedIds
            )
            prefs.edit().putString(KEY_CANDIDATE_IDS, candidateTopics.map { it.id }.joinToString(",")).apply()
        }

        val progress = UserProgress(
            todayDateString = todayStr,
            candidateTopicIds = candidateTopics.map { it.id },
            selectedTopicId = selectedId,
            isCompletedToday = isCompleted,
            reflectionText = reflection,
            streakCount = streak,
            totalCompleted = totalCompleted
        )

        return Pair(progress, candidateTopics)
    }

    fun shuffleCandidates(): List<LearningTopic> {
        val completedIds = getCompletedTopicIds().toList()
        val newSelection = TopicCatalog.getDailySelection(
            seed = System.currentTimeMillis() + kotlin.random.Random.nextLong(),
            excludedIds = completedIds
        )
        prefs.edit()
            .putString(KEY_CANDIDATE_IDS, newSelection.map { it.id }.joinToString(","))
            .apply()
        return newSelection
    }

    fun selectTopic(topicId: String) {
        prefs.edit()
            .putString(KEY_SELECTED_ID, topicId)
            .apply()
    }

    fun clearSelection() {
        prefs.edit()
            .remove(KEY_SELECTED_ID)
            .apply()
    }

    fun completeToday(topicId: String, reflection: String): Pair<Int, Int> {
        val todayStr = getTodayDateString()
        val currentStreak = prefs.getInt(KEY_STREAK, 0)
        val currentTotal = prefs.getInt(KEY_TOTAL_COMPLETED, 0)

        val newStreak = currentStreak + 1
        val newTotal = currentTotal + 1

        val completedIds = (prefs.getStringSet(KEY_COMPLETED_TOPIC_IDS, emptySet()) ?: emptySet()).toMutableSet()
        completedIds.add(topicId)

        prefs.edit()
            .putBoolean(KEY_IS_COMPLETED, true)
            .putString(KEY_SELECTED_ID, topicId)
            .putString(KEY_REFLECTION, reflection)
            .putString(KEY_LAST_COMPLETED_DATE, todayStr)
            .putStringSet(KEY_COMPLETED_TOPIC_IDS, completedIds)
            .putInt(KEY_STREAK, newStreak)
            .putInt(KEY_TOTAL_COMPLETED, newTotal)
            .apply()

        return Pair(newStreak, newTotal)
    }

    fun getCompletedTopicIds(): Set<String> {
        return prefs.getStringSet(KEY_COMPLETED_TOPIC_IDS, emptySet()) ?: emptySet()
    }

    companion object {
        private const val KEY_SAVED_DATE = "saved_date"
        private const val KEY_CANDIDATE_IDS = "candidate_ids"
        private const val KEY_SELECTED_ID = "selected_id"
        private const val KEY_IS_COMPLETED = "is_completed"
        private const val KEY_REFLECTION = "reflection"
        private const val KEY_STREAK = "streak"
        private const val KEY_TOTAL_COMPLETED = "total_completed"
        private const val KEY_LAST_COMPLETED_DATE = "last_completed_date"
        private const val KEY_COMPLETED_TOPIC_IDS = "completed_topic_ids"
    }
}
