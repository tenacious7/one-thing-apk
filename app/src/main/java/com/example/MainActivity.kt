package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.TopicCatalog
import com.example.model.ScreenState
import com.example.ui.components.DistractionReminderOverlay
import com.example.ui.components.EditorialBottomBar
import com.example.ui.screens.CompletedLockScreen
import com.example.ui.screens.DistractionShieldScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileSettingsScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SkillsScreen
import com.example.ui.screens.StudyDetailScreen
import com.example.ui.theme.LocalOneThingColors
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ThemeMode
import com.example.ui.theme.ThemePreferences
import com.example.ui.theme.VibrantOrange
import com.example.viewmodel.OneThingViewModel

/**
 * Main Activity for ONE THING — AI-Powered Personalized Learning & Distraction Shield.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            var themeMode by remember {
                mutableStateOf(ThemePreferences.getThemeMode(context))
            }

            MyApplicationTheme(themeMode = themeMode) {
                OneThingApp(
                    themeMode = themeMode,
                    onThemeChanged = { newMode ->
                        themeMode = newMode
                        ThemePreferences.setThemeMode(context, newMode)
                    }
                )
            }
        }
    }
}

@Composable
fun OneThingApp(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    onThemeChanged: (ThemeMode) -> Unit = {},
    viewModel: OneThingViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = LocalOneThingColors.current

    val showBottomBar = uiState.currentScreen in setOf(
        ScreenState.HOME_LIST,
        ScreenState.COMPLETED_LOCKED,
        ScreenState.DISTRACTION_SHIELD,
        ScreenState.SKILL_SETTINGS,
        ScreenState.PROFILE_SETTINGS
    )
    val totalStrikes = uiState.allStrikes.values.sum()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar && (uiState.candidates.isNotEmpty() || uiState.isCompletedToday)) {
                EditorialBottomBar(
                    currentScreen = uiState.currentScreen,
                    isFocusActive = uiState.isFocusActive,
                    strikeCount = totalStrikes,
                    onNavigateToHome = {
                        viewModel.navigateToHome()
                    },
                    onNavigateToShield = {
                        viewModel.navigateToShield()
                    },
                    onNavigateToSkills = {
                        viewModel.navigateToSettings()
                    },
                    onNavigateToProfile = {
                        viewModel.navigateToProfile()
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            if (uiState.candidates.isEmpty() && !uiState.isGeneratingAiTopics && !uiState.isCompletedToday) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = VibrantOrange)
                }
            } else {
                AnimatedContent(
                    targetState = uiState.currentScreen,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(300)) +
                                scaleIn(initialScale = 0.97f, animationSpec = tween(300)))
                            .togetherWith(
                                fadeOut(animationSpec = tween(250)) +
                                        scaleOut(targetScale = 1.03f, animationSpec = tween(250))
                            )
                    },
                    label = "ScreenTransition"
                ) { screen ->
                    when (screen) {
                        ScreenState.HOME_LIST -> {
                            HomeScreen(
                                candidates = uiState.candidates,
                                streakCount = uiState.streakCount,
                                totalCompleted = uiState.totalCompleted,
                                skillConfig = uiState.skillConfig,
                                isGeneratingAi = uiState.isGeneratingAiTopics,
                                aiNoteMessage = uiState.aiGenerationError,
                                isFocusActive = uiState.isFocusActive,
                                focusRemainingSeconds = uiState.focusRemainingSeconds,
                                isRewardActive = uiState.isRewardActive,
                                rewardRemainingSeconds = uiState.rewardRemainingSeconds,
                                onSelectTopic = { topic ->
                                    viewModel.selectTopic(topic)
                                },
                                onShuffle = {
                                    viewModel.shuffleCandidates()
                                },
                                onGenerateWithAi = {
                                    viewModel.generateTopicsWithAi()
                                },
                                onOpenSkillSettings = {
                                    viewModel.openSkillSettings()
                                },
                                onOpenShieldSettings = {
                                    viewModel.openDistractionShield()
                                }
                            )
                        }

                        ScreenState.STUDY_DETAIL -> {
                            val topic = uiState.selectedTopic ?: uiState.candidates.firstOrNull()
                            if (topic != null) {
                                StudyDetailScreen(
                                    topic = topic,
                                    initialReflection = uiState.reflectionText,
                                    onNavigateBack = {
                                        viewModel.navigateBack()
                                    },
                                    onProceedToQuiz = { reflection ->
                                        viewModel.navigateToQuiz(reflection)
                                    }
                                )
                            } else {
                                viewModel.navigateBack()
                            }
                        }

                        ScreenState.QUIZ_VERIFICATION -> {
                            val topic = uiState.selectedTopic ?: uiState.candidates.firstOrNull()
                            if (topic != null) {
                                QuizScreen(
                                    topic = topic,
                                    onNavigateBack = {
                                        viewModel.navigateBack()
                                    },
                                    onQuizPassed = {
                                        viewModel.passQuizAndComplete(uiState.reflectionText)
                                    }
                                )
                            } else {
                                viewModel.navigateBack()
                            }
                        }

                        ScreenState.COMPLETED_LOCKED -> {
                            val topic = uiState.selectedTopic ?: uiState.candidates.firstOrNull() ?: TopicCatalog.allTopics.first()
                            CompletedLockScreen(
                                completedTopic = topic,
                                streakCount = uiState.streakCount,
                                totalCompleted = uiState.totalCompleted,
                                reflectionText = uiState.reflectionText,
                                isRewardActive = uiState.isRewardActive,
                                rewardRemainingSeconds = uiState.rewardRemainingSeconds,
                                onClaimReward = {
                                    viewModel.claimRewardPass()
                                },
                                onReviewTopic = {
                                    viewModel.reviewTopic()
                                },
                                onOpenShieldSettings = {
                                    viewModel.openDistractionShield()
                                }
                            )
                        }

                        ScreenState.SKILL_SETTINGS -> {
                            var showDetailedSettings by remember { mutableStateOf(false) }
                            if (showDetailedSettings) {
                                SettingsScreen(
                                    currentConfig = uiState.skillConfig ?: com.example.model.SkillConfig(
                                        activeCategoryIds = emptySet(),
                                        activeSkillNames = emptySet(),
                                        customSkills = emptyList(),
                                        aiInterestsPrompt = "",
                                        onboardingMode = com.example.model.OnboardingMode.MANUAL_SELECTION
                                    ),
                                    currentApiKey = uiState.effectiveApiKey,
                                    targetApps = uiState.targetApps,
                                    hardLockedApps = uiState.hardLockedApps,
                                    allStrikes = uiState.allStrikes,
                                    onSaveApiKey = { key ->
                                        viewModel.saveApiKey(key)
                                    },
                                    onSaveConfig = { config ->
                                        viewModel.saveSkillConfig(config)
                                    },
                                    onToggleAppBlocked = { pkg, blocked ->
                                        viewModel.setAppBlocked(pkg, blocked)
                                    },
                                    onNavigateBack = {
                                        showDetailedSettings = false
                                    }
                                )
                            } else {
                                SkillsScreen(
                                    onMakeTodayOneThing = {
                                        viewModel.navigateToHome()
                                    },
                                    onAddDopamineMinutes = { minutes ->
                                        viewModel.startRewardPass(minutes)
                                    },
                                    onClose = {
                                        viewModel.navigateToHome()
                                    }
                                )
                            }
                        }

                        ScreenState.DISTRACTION_SHIELD -> {
                            DistractionShieldScreen(
                                isFocusActive = uiState.isFocusActive,
                                focusRemainingSeconds = uiState.focusRemainingSeconds,
                                selectedDurationMinutes = uiState.focusDurationMinutes,
                                isRewardActive = uiState.isRewardActive,
                                rewardRemainingSeconds = uiState.rewardRemainingSeconds,
                                targetApps = uiState.targetApps,
                                hasUsageStatsPermission = uiState.hasUsageStatsPermission,
                                hasAccessibilityPermission = uiState.hasAccessibilityPermission,
                                allStrikes = uiState.allStrikes,
                                hardLockedApps = uiState.hardLockedApps,
                                onSetDuration = { duration ->
                                    viewModel.setFocusDuration(duration)
                                },
                                onStartFocus = {
                                    viewModel.startFocusTimer()
                                },
                                onStopFocus = {
                                    viewModel.stopFocusTimer()
                                },
                                onToggleAppBlocked = { pkg, blocked ->
                                    viewModel.setAppBlocked(pkg, blocked)
                                },
                                onSelectAllApps = {
                                    viewModel.selectAllApps()
                                },
                                onDeselectAllApps = {
                                    viewModel.deselectAllApps()
                                },
                                onAddCustomApp = { pkg, name, cat ->
                                    viewModel.addCustomApp(pkg, name, cat)
                                },
                                onRemoveCustomApp = { pkg ->
                                    viewModel.removeCustomApp(pkg)
                                },
                                onEmergencyExit = {
                                    viewModel.emergencyExit()
                                },
                                onNavigateBack = {
                                    viewModel.navigateBack()
                                },
                                onRefreshPermissions = {
                                    viewModel.refreshShieldState()
                                }
                            )
                        }

                        ScreenState.PROFILE_SETTINGS -> {
                            ProfileSettingsScreen(
                                currentApiKey = uiState.effectiveApiKey,
                                onSaveApiKey = { key ->
                                    viewModel.saveApiKey(key)
                                },
                                onThemeChanged = onThemeChanged,
                                hasAccessibilityPermission = uiState.hasAccessibilityPermission,
                                totalStrikes = totalStrikes,
                                onResetProgress = {
                                    viewModel.navigateToHome()
                                }
                            )
                        }
                    }
                }

                // Distraction Alert Overlay
                uiState.activeBlockedAlertPackage?.let { blockedPkg ->
                    val appName = uiState.targetApps.find { it.packageName == blockedPkg }?.appName ?: blockedPkg
                    DistractionReminderOverlay(
                        blockedAppName = appName,
                        remainingSeconds = uiState.focusRemainingSeconds,
                        onReturnToApp = {
                            viewModel.dismissShieldAlert()
                        },
                        onEmergencyExit = {
                            viewModel.emergencyExit()
                        }
                    )
                }
            }
        }
    }
}
