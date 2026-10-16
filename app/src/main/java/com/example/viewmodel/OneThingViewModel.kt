package com.example.viewmodel

import android.app.Application
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Process
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GeminiAiService
import com.example.data.ShieldRepository
import com.example.data.SkillRepository
import com.example.data.TopicCatalog
import com.example.data.UserProgressRepository
import com.example.model.DistractionTargetApp
import com.example.model.LearningTopic
import com.example.model.OnboardingMode
import com.example.model.ScreenState
import com.example.model.SkillConfig
import com.example.service.FocusShieldService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OneThingUiState(
    val candidates: List<LearningTopic> = emptyList(),
    val selectedTopic: LearningTopic? = null,
    val isCompletedToday: Boolean = false,
    val reflectionText: String = "",
    val streakCount: Int = 0,
    val totalCompleted: Int = 0,
    val currentScreen: ScreenState = ScreenState.HOME_LIST,

    // AI & Skill Configuration
    val skillConfig: SkillConfig? = null,
    val effectiveApiKey: String = "",
    val isExtractingSkills: Boolean = false,
    val skillExtractionError: String? = null,
    val isGeneratingAiTopics: Boolean = false,
    val aiGenerationError: String? = null,

    // Focus Timer & Shield
    val focusDurationMinutes: Int = 45,
    val focusRemainingSeconds: Int = 45 * 60,
    val isFocusActive: Boolean = false,
    val rewardRemainingSeconds: Int = 0,
    val isRewardActive: Boolean = false,
    val targetApps: List<DistractionTargetApp> = emptyList(),
    val hasUsageStatsPermission: Boolean = false,
    val hasAccessibilityPermission: Boolean = false,
    val allStrikes: Map<String, Int> = emptyMap(),
    val hardLockedApps: Set<String> = emptySet(),
    val activeBlockedAlertPackage: String? = null
)

class OneThingViewModel(application: Application) : AndroidViewModel(application) {
    private val progressRepo = UserProgressRepository(application)
    private val skillRepo = SkillRepository(application)
    private val shieldRepo = ShieldRepository(application)
    private val aiService = GeminiAiService()

    private val _uiState = MutableStateFlow(OneThingUiState())
    val uiState: StateFlow<OneThingUiState> = _uiState.asStateFlow()

    init {
        loadInitialState()
        observeServiceState()
    }

    private fun loadInitialState() {
        val (progress, candidates) = progressRepo.loadUserProgress()
        val skillConfig = skillRepo.loadSkillConfig()
        val apiKey = skillRepo.getEffectiveApiKey()
        val targetApps = shieldRepo.getTargetApps()
        val focusDuration = shieldRepo.getFocusDurationMinutes()
        val hasUsage = checkUsageStatsPermission()
        val hasAccessibility = shieldRepo.isAccessibilityServiceEnabled()
        val allStrikes = shieldRepo.getAllStrikes()
        val hardLockedApps = shieldRepo.getHardLockedApps()

        val selected = progress.selectedTopicId?.let { id ->
            TopicCatalog.allTopics.find { it.id == id } ?: candidates.find { it.id == id }
        }

        val initialScreen = when {
            progress.isCompletedToday -> ScreenState.COMPLETED_LOCKED
            selected != null -> ScreenState.STUDY_DETAIL
            else -> ScreenState.HOME_LIST
        }

        _uiState.update {
            it.copy(
                candidates = candidates,
                selectedTopic = selected,
                isCompletedToday = progress.isCompletedToday,
                reflectionText = progress.reflectionText,
                streakCount = progress.streakCount,
                totalCompleted = progress.totalCompleted,
                currentScreen = initialScreen,
                skillConfig = skillConfig,
                effectiveApiKey = apiKey,
                targetApps = targetApps,
                focusDurationMinutes = focusDuration,
                focusRemainingSeconds = focusDuration * 60,
                hasUsageStatsPermission = hasUsage,
                hasAccessibilityPermission = hasAccessibility,
                allStrikes = allStrikes,
                hardLockedApps = hardLockedApps
            )
        }
    }

    private fun observeServiceState() {
        viewModelScope.launch {
            FocusShieldService.focusRemainingSeconds.collect { sec ->
                _uiState.update { it.copy(focusRemainingSeconds = sec) }
            }
        }
        viewModelScope.launch {
            FocusShieldService.isFocusActive.collect { active ->
                _uiState.update { it.copy(isFocusActive = active) }
            }
        }
        viewModelScope.launch {
            FocusShieldService.rewardRemainingSeconds.collect { sec ->
                _uiState.update { it.copy(rewardRemainingSeconds = sec) }
            }
        }
        viewModelScope.launch {
            FocusShieldService.isRewardActive.collect { active ->
                _uiState.update { it.copy(isRewardActive = active) }
            }
        }
        viewModelScope.launch {
            FocusShieldService.shieldAlertApp.collect { pkg ->
                _uiState.update { it.copy(activeBlockedAlertPackage = pkg) }
            }
        }
    }

    fun checkUsageStatsPermission(): Boolean {
        val appOps = getApplication<Application>().getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager ?: return false
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), getApplication<Application>().packageName)
        } else {
            @Suppress("DEPRECATION")
            appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), getApplication<Application>().packageName)
        }
        val granted = mode == AppOpsManager.MODE_ALLOWED
        _uiState.update { it.copy(hasUsageStatsPermission = granted) }
        return granted
    }

    fun shuffleCandidates() {
        if (_uiState.value.isCompletedToday) return
        val newSelection = progressRepo.shuffleCandidates()
        _uiState.update { it.copy(candidates = newSelection, aiGenerationError = null) }
    }

    fun generateTopicsWithAi() {
        val apiKey = skillRepo.getEffectiveApiKey()
        val skills = _uiState.value.skillConfig?.activeSkillNames?.toList() ?: emptyList()
        val completedIds = progressRepo.getCompletedTopicIds()
        val completedTitles = TopicCatalog.allTopics.filter { it.id in completedIds }.map { it.title } +
                listOfNotNull(_uiState.value.selectedTopic?.title)

        val activeCategories = _uiState.value.skillConfig?.activeCategoryIds ?: emptySet()
        val singleCategoryOverride = if (activeCategories.size == 1) {
            TopicCatalog.getCategoryNameById(activeCategories.first())
        } else null

        viewModelScope.launch {
            _uiState.update { it.copy(isGeneratingAiTopics = true, aiGenerationError = null) }
            val result = aiService.generateTopicsWithAi(apiKey, skills, completedTitles, singleCategoryOverride)
            result.onSuccess { aiTopics ->
                _uiState.update {
                    it.copy(
                        candidates = aiTopics,
                        isGeneratingAiTopics = false,
                        aiGenerationError = null,
                        currentScreen = ScreenState.HOME_LIST
                    )
                }
            }.onFailure { err ->
                // Fallback to local curated selection matching user preferences
                val fallback = TopicCatalog.getDailySelection(
                    seed = System.currentTimeMillis(),
                    count = 3,
                    excludedIds = completedIds.toList(),
                    preferredCategories = activeCategories
                )
                _uiState.update {
                    it.copy(
                        candidates = fallback,
                        isGeneratingAiTopics = false,
                        aiGenerationError = "AI Note: ${err.message}. Displaying tailored curated topics.",
                        currentScreen = ScreenState.HOME_LIST
                    )
                }
            }
        }
    }

    fun extractSkillsFromAi(prompt: String) {
        val apiKey = skillRepo.getEffectiveApiKey()
        viewModelScope.launch {
            _uiState.update { it.copy(isExtractingSkills = true, skillExtractionError = null) }
            val result = aiService.extractSkillsFromPrompt(apiKey, prompt)
            result.onSuccess { extractedPairs ->
                val currentConfig = _uiState.value.skillConfig ?: skillRepo.loadSkillConfig()
                val newSkills = (currentConfig.activeSkillNames + extractedPairs.map { it.first }).toSet()
                val updatedConfig = currentConfig.copy(
                    activeSkillNames = newSkills,
                    aiInterestsPrompt = prompt,
                    onboardingMode = OnboardingMode.AI_PERSONALIZATION
                )
                skillRepo.saveSkillConfig(updatedConfig)
                _uiState.update {
                    it.copy(
                        skillConfig = updatedConfig,
                        isExtractingSkills = false,
                        skillExtractionError = null
                    )
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isExtractingSkills = false,
                        skillExtractionError = err.message ?: "Failed to extract skills."
                    )
                }
            }
        }
    }

    fun saveApiKey(key: String) {
        skillRepo.saveUserApiKey(key)
        _uiState.update { it.copy(effectiveApiKey = key.trim()) }
    }

    fun saveSkillConfig(config: SkillConfig) {
        skillRepo.saveSkillConfig(config)
        _uiState.update { it.copy(skillConfig = config) }
    }

    fun selectTopic(topic: LearningTopic) {
        progressRepo.selectTopic(topic.id)
        _uiState.update { it.copy(selectedTopic = topic, currentScreen = ScreenState.STUDY_DETAIL) }
    }

    fun navigateToQuiz(reflection: String = "") {
        _uiState.update {
            it.copy(
                reflectionText = reflection.ifBlank { it.reflectionText },
                currentScreen = ScreenState.QUIZ_VERIFICATION
            )
        }
    }

    fun passQuizAndComplete(reflection: String) {
        val topic = _uiState.value.selectedTopic ?: return
        val finalReflection = reflection.ifBlank { _uiState.value.reflectionText }
        val (newStreak, newTotal) = progressRepo.completeToday(topic.id, finalReflection)
        _uiState.update {
            it.copy(
                isCompletedToday = true,
                reflectionText = finalReflection,
                streakCount = newStreak,
                totalCompleted = newTotal,
                currentScreen = ScreenState.COMPLETED_LOCKED
            )
        }
    }

    fun claimRewardPass() {
        startRewardPass(5)
    }

    fun startFocusTimer() {
        val duration = _uiState.value.focusDurationMinutes
        val intent = Intent(getApplication(), FocusShieldService::class.java).apply {
            action = FocusShieldService.ACTION_START_FOCUS
            putExtra(FocusShieldService.EXTRA_DURATION_MINUTES, duration)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplication<Application>().startForegroundService(intent)
        } else {
            getApplication<Application>().startService(intent)
        }
    }

    fun stopFocusTimer() {
        val intent = Intent(getApplication(), FocusShieldService::class.java).apply {
            action = FocusShieldService.ACTION_STOP
        }
        getApplication<Application>().startService(intent)
    }

    fun startRewardPass(durationMinutes: Int = 5) {
        val intent = Intent(getApplication(), FocusShieldService::class.java).apply {
            action = FocusShieldService.ACTION_START_REWARD
            putExtra(FocusShieldService.EXTRA_REWARD_MINUTES, durationMinutes)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplication<Application>().startForegroundService(intent)
        } else {
            getApplication<Application>().startService(intent)
        }
    }

    fun emergencyExit() {
        val intent = Intent(getApplication(), FocusShieldService::class.java).apply {
            action = FocusShieldService.ACTION_EMERGENCY_EXIT
        }
        getApplication<Application>().startService(intent)
        FocusShieldService.dismissAlert()
    }

    fun dismissShieldAlert() {
        FocusShieldService.dismissAlert()
    }

    fun setAppBlocked(packageName: String, blocked: Boolean) {
        shieldRepo.setAppBlocked(packageName, blocked)
        _uiState.update { it.copy(targetApps = shieldRepo.getTargetApps()) }
    }

    fun selectAllApps() {
        shieldRepo.selectAllApps()
        _uiState.update { it.copy(targetApps = shieldRepo.getTargetApps()) }
    }

    fun deselectAllApps() {
        shieldRepo.deselectAllApps()
        _uiState.update { it.copy(targetApps = shieldRepo.getTargetApps()) }
    }

    fun addCustomApp(packageName: String, appName: String, category: String) {
        shieldRepo.addCustomApp(packageName, appName, category)
        _uiState.update { it.copy(targetApps = shieldRepo.getTargetApps()) }
    }

    fun removeCustomApp(packageName: String) {
        shieldRepo.removeCustomApp(packageName)
        _uiState.update { it.copy(targetApps = shieldRepo.getTargetApps()) }
    }

    fun setFocusDuration(minutes: Int) {
        shieldRepo.setFocusDurationMinutes(minutes)
        _uiState.update {
            it.copy(
                focusDurationMinutes = minutes,
                focusRemainingSeconds = minutes * 60
            )
        }
    }

    fun navigateBack() {
        when (_uiState.value.currentScreen) {
            ScreenState.STUDY_DETAIL -> {
                if (_uiState.value.isCompletedToday) {
                    _uiState.update { it.copy(currentScreen = ScreenState.COMPLETED_LOCKED) }
                } else {
                    _uiState.update { it.copy(currentScreen = ScreenState.HOME_LIST, selectedTopic = null) }
                }
            }
            ScreenState.QUIZ_VERIFICATION -> {
                _uiState.update { it.copy(currentScreen = ScreenState.STUDY_DETAIL) }
            }
            ScreenState.SKILL_SETTINGS, ScreenState.DISTRACTION_SHIELD, ScreenState.PROFILE_SETTINGS -> {
                _uiState.update { it.copy(currentScreen = ScreenState.HOME_LIST) }
            }
            else -> {}
        }
    }

    fun openSkillSettings() {
        _uiState.update { it.copy(currentScreen = ScreenState.SKILL_SETTINGS) }
    }

    fun openDistractionShield() {
        refreshShieldState()
        _uiState.update { it.copy(currentScreen = ScreenState.DISTRACTION_SHIELD) }
    }

    fun navigateToProfile() {
        _uiState.update { it.copy(currentScreen = ScreenState.PROFILE_SETTINGS) }
    }

    fun refreshShieldState() {
        _uiState.update {
            it.copy(
                hasUsageStatsPermission = checkUsageStatsPermission(),
                hasAccessibilityPermission = shieldRepo.isAccessibilityServiceEnabled(),
                allStrikes = shieldRepo.getAllStrikes(),
                hardLockedApps = shieldRepo.getHardLockedApps(),
                targetApps = shieldRepo.getTargetApps()
            )
        }
    }

    fun navigateToHome() {
        if (_uiState.value.isCompletedToday) {
            _uiState.update { it.copy(currentScreen = ScreenState.COMPLETED_LOCKED) }
        } else {
            _uiState.update { it.copy(currentScreen = ScreenState.HOME_LIST) }
        }
    }

    fun navigateToShield() {
        openDistractionShield()
    }

    fun navigateToSettings() {
        openSkillSettings()
    }

    fun reviewTopic() {
        _uiState.update { it.copy(currentScreen = ScreenState.STUDY_DETAIL) }
    }
}
