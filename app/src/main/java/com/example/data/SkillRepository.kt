package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.BuildConfig
import com.example.model.OnboardingMode
import com.example.model.SkillConfig
import com.example.model.StandardCategories

class SkillRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("one_thing_skills_prefs", Context.MODE_PRIVATE)

    fun getEffectiveApiKey(): String {
        val userKey = prefs.getString(KEY_USER_API_KEY, "")?.trim() ?: ""
        if (userKey.isNotEmpty()) return userKey
        return try {
            val buildKey = BuildConfig.GEMINI_API_KEY
            if (buildKey.isNotEmpty() && !buildKey.contains("MY_GEMINI_API_KEY")) buildKey else ""
        } catch (e: Exception) {
            ""
        }
    }

    fun saveUserApiKey(key: String) {
        prefs.edit().putString(KEY_USER_API_KEY, key.trim()).apply()
    }

    fun loadSkillConfig(): SkillConfig {
        val allCategoryIds = StandardCategories.list.map { it.id }.toSet()
        val allSkillNames = StandardCategories.list.flatMap { it.defaultSkills }.toSet()

        val savedCategories = prefs.getStringSet(KEY_ACTIVE_CATEGORIES, allCategoryIds) ?: allCategoryIds
        val savedSkills = prefs.getStringSet(KEY_ACTIVE_SKILLS, allSkillNames) ?: allSkillNames

        val rawCustomSkills = prefs.getString(KEY_CUSTOM_SKILLS, "") ?: ""
        val customSkills = if (rawCustomSkills.isNotBlank()) rawCustomSkills.split("|") else emptyList()

        val aiPrompt = prefs.getString(KEY_AI_PROMPT, "") ?: ""
        val modeStr = prefs.getString(KEY_MODE, OnboardingMode.MANUAL_SELECTION.name)
        val mode = try {
            OnboardingMode.valueOf(modeStr ?: OnboardingMode.MANUAL_SELECTION.name)
        } catch (e: Exception) {
            OnboardingMode.MANUAL_SELECTION
        }

        return SkillConfig(
            activeCategoryIds = savedCategories,
            activeSkillNames = savedSkills,
            customSkills = customSkills,
            aiInterestsPrompt = aiPrompt,
            onboardingMode = mode
        )
    }

    fun saveSkillConfig(config: SkillConfig) {
        prefs.edit()
            .putStringSet(KEY_ACTIVE_CATEGORIES, config.activeCategoryIds)
            .putStringSet(KEY_ACTIVE_SKILLS, config.activeSkillNames)
            .putString(KEY_CUSTOM_SKILLS, config.customSkills.joinToString("|"))
            .putString(KEY_AI_PROMPT, config.aiInterestsPrompt)
            .putString(KEY_MODE, config.onboardingMode.name)
            .apply()
    }

    fun addCustomSkill(skillName: String) {
        val current = loadSkillConfig()
        val updatedCustom = (current.customSkills + skillName.trim()).distinct()
        val updatedActive = current.activeSkillNames + skillName.trim()
        saveSkillConfig(current.copy(customSkills = updatedCustom, activeSkillNames = updatedActive))
    }

    fun removeSkill(skillName: String) {
        val current = loadSkillConfig()
        val updatedCustom = current.customSkills.filter { it != skillName }
        val updatedActive = current.activeSkillNames.filter { it != skillName }.toSet()
        saveSkillConfig(current.copy(customSkills = updatedCustom, activeSkillNames = updatedActive))
    }

    fun selectAll() {
        val allCategoryIds = StandardCategories.list.map { it.id }.toSet()
        val allSkillNames = StandardCategories.list.flatMap { it.defaultSkills }.toSet()
        val current = loadSkillConfig()
        saveSkillConfig(current.copy(
            activeCategoryIds = allCategoryIds,
            activeSkillNames = allSkillNames + current.customSkills
        ))
    }

    fun uncheckAll() {
        val current = loadSkillConfig()
        saveSkillConfig(current.copy(
            activeCategoryIds = emptySet(),
            activeSkillNames = emptySet()
        ))
    }

    fun resetToDefault() {
        val allCategoryIds = StandardCategories.list.map { it.id }.toSet()
        val allSkillNames = StandardCategories.list.flatMap { it.defaultSkills }.toSet()
        saveSkillConfig(SkillConfig(
            activeCategoryIds = allCategoryIds,
            activeSkillNames = allSkillNames,
            customSkills = emptyList(),
            aiInterestsPrompt = "",
            onboardingMode = OnboardingMode.MANUAL_SELECTION
        ))
    }

    companion object {
        private const val KEY_USER_API_KEY = "user_api_key"
        private const val KEY_ACTIVE_CATEGORIES = "active_categories"
        private const val KEY_ACTIVE_SKILLS = "active_skills"
        private const val KEY_CUSTOM_SKILLS = "custom_skills"
        private const val KEY_AI_PROMPT = "ai_prompt"
        private const val KEY_MODE = "onboarding_mode"
    }
}
