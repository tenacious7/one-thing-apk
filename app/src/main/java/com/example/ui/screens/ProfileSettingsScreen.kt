package com.example.ui.screens

import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppBackground
import com.example.ui.components.BackgroundVariant
import com.example.ui.theme.LocalOneThingColors
import com.example.ui.theme.LocalThemeMode
import com.example.ui.theme.ThemeMode

@Composable
fun ProfileSettingsScreen(
    currentApiKey: String = "",
    onSaveApiKey: (String) -> Unit = {},
    onThemeChanged: (ThemeMode) -> Unit = {},
    hasAccessibilityPermission: Boolean = true,
    totalStrikes: Int = 0,
    onResetProgress: () -> Unit = {}
) {
    val colors = LocalOneThingColors.current
    val currentThemeMode = LocalThemeMode.current
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // API Key State
    var apiKeyInput by remember(currentApiKey) { mutableStateOf(currentApiKey) }
    var isApiKeyMasked by remember { mutableStateOf(true) }
    var apiKeySaveSuccessMessage by remember { mutableStateOf<String?>(null) }

    // AI Model
    var selectedModel by remember { mutableStateOf("Gemini 2.5 Flash (Fast)") }

    // Shield settings
    var strictnessMode by remember { mutableStateOf("5-Strike Tax") }
    var dailyGoal by remember { mutableStateOf("1 Thing / Day") }
    var hapticFeedbackEnabled by remember { mutableStateOf(true) }

    // Dialog state
    var showResetDialog by remember { mutableStateOf(false) }

    AppBackground(
        variant = BackgroundVariant.FLAT
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            // ==========================================
            // SECTION 1: INSTAGRAM PROFILE HEADER
            // ==========================================
            item(key = "section_instagram_profile_header") {
                InstagramProfileHeader(
                    handle = "@focus_architect",
                    level = 4,
                    skillsMastered = 12,
                    currentStreakDays = 4,
                    timeSavedHours = "3.5h",
                    archetypeTitle = "The Tactical Empathetic",
                    bioQuote = "Trading cheap dopamine for daily cognitive leverage."
                )
            }

            // ==========================================
            // SECTION 2: APPEARANCE & THEME SWITCHER
            // ==========================================
            item(key = "section_appearance_theme") {
                SectionHeader(title = "APPEARANCE")
                Spacer(modifier = Modifier.height(10.dp))
                ThemeSwitcherCard(
                    currentMode = currentThemeMode,
                    onModeSelected = { mode ->
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onThemeChanged(mode)
                    }
                )
            }

            // ==========================================
            // SECTION 3: AI BRAIN ENGINE & API CONFIGURATION
            // ==========================================
            item(key = "section_ai_brain_engine") {
                SectionHeader(title = "AI BRAIN ENGINE (GEMINI / LLM API)")
                Spacer(modifier = Modifier.height(10.dp))
                AiEngineConfigCard(
                    apiKey = apiKeyInput,
                    onApiKeyChange = { apiKeyInput = it },
                    isMasked = isApiKeyMasked,
                    onToggleMask = { isApiKeyMasked = !isApiKeyMasked },
                    onSaveApiKey = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSaveApiKey(apiKeyInput)
                        apiKeySaveSuccessMessage = "API Key validated & saved"
                    },
                    selectedModel = selectedModel,
                    onSelectModel = { selectedModel = it },
                    hasKey = apiKeyInput.trim().isNotBlank()
                )
            }

            // ==========================================
            // SECTION 4: SHIELD & PERMISSIONS
            // ==========================================
            item(key = "section_shield_permissions") {
                SectionHeader(title = "SHIELD & PERMISSIONS")
                Spacer(modifier = Modifier.height(10.dp))
                ShieldPermissionsCard(
                    hasAccessibility = hasAccessibilityPermission,
                    onOpenAccessibility = {
                        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                        context.startActivity(intent)
                    },
                    strictnessMode = strictnessMode,
                    onSelectStrictness = { strictnessMode = it },
                    dailyGoal = dailyGoal,
                    onSelectDailyGoal = { dailyGoal = it },
                    hapticsEnabled = hapticFeedbackEnabled,
                    onToggleHaptics = { hapticFeedbackEnabled = it },
                    onPromptReset = { showResetDialog = true }
                )
            }
        }

        // Save confirmation toast
        apiKeySaveSuccessMessage?.let { msg ->
            LaunchedEffect(msg) {
                kotlinx.coroutines.delay(2200)
                apiKeySaveSuccessMessage = null
            }
            Surface(
                color = colors.primaryText,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, colors.cardBorder),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 16.dp),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = colors.success,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = msg,
                        color = colors.background,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Reset Confirmation Dialog
        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = {
                    Text(
                        text = "Reset All Vault Data?",
                        fontWeight = FontWeight.Bold,
                        color = colors.primaryText
                    )
                },
                text = {
                    Text(
                        text = "This will clear your local cached skills, reflection notes, and reset current strike counters. Your streak will be preserved.",
                        color = colors.secondaryText,
                        fontSize = 13.sp
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showResetDialog = false
                            onResetProgress()
                        }
                    ) {
                        Text(
                            text = "Reset Data",
                            color = colors.primaryAccent,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text(text = "Cancel", color = colors.secondaryText)
                    }
                },
                containerColor = colors.surface
            )
        }
    }
}

// ==========================================
// SECTION 1: INSTAGRAM PROFILE HEADER
// ==========================================

@Composable
private fun InstagramProfileHeader(
    handle: String,
    level: Int,
    skillsMastered: Int,
    currentStreakDays: Int,
    timeSavedHours: String,
    archetypeTitle: String,
    bioQuote: String
) {
    val colors = LocalOneThingColors.current

    Column(modifier = Modifier.fillMaxWidth()) {
        // Top handle row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = handle,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = colors.primaryText,
                    letterSpacing = (-0.3).sp
                )
            )

            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(colors.secondarySurface)
                    .border(1.dp, colors.cardBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = colors.primaryText,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Avatar + Stats Columns (Instagram layout)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Avatar with Gradient Story Ring
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                // Gradient Story Ring
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.sweepGradient(
                                colors = listOf(
                                    Color(0xFFFF7043),
                                    Color(0xFFF47BC4),
                                    Color(0xFF8B5CF6),
                                    Color(0xFFFF7043)
                                )
                            )
                        )
                        .padding(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(colors.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(38.dp)) {
                            drawUserHeadshotVector(color = colors.primaryText)
                        }
                    }
                }

                // Anchored LVL 4 badge pill
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 4.dp)
                        .clip(RoundedCornerShape(50))
                        .background(colors.primaryText)
                        .border(1.dp, colors.surface, RoundedCornerShape(50))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "LVL $level",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 9.sp,
                            color = colors.surface
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 3 Stat Columns
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileStatColumn(count = "$skillsMastered", label = "Mastered")
                ProfileStatColumn(count = "${currentStreakDays}d", label = "Streak")
                ProfileStatColumn(count = timeSavedHours, label = "Saved")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bio & Archetype
        Column {
            Text(
                text = archetypeTitle,
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = colors.primaryText
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = bioQuote,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = colors.secondaryText,
                    lineHeight = 16.sp
                )
            )
        }
    }
}

@Composable
private fun ProfileStatColumn(count: String, label: String) {
    val colors = LocalOneThingColors.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = colors.primaryText
            )
        )
        Text(
            text = label,
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                color = colors.secondaryText
            )
        )
    }
}

// ==========================================
// SECTION 2: THEME SWITCHER
// ==========================================

@Composable
private fun ThemeSwitcherCard(
    currentMode: ThemeMode,
    onModeSelected: (ThemeMode) -> Unit
) {
    val colors = LocalOneThingColors.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(colors.surface)
            .border(1.dp, colors.cardBorder, RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(colors.secondarySurface)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ThemeSegmentOption(
                title = "System Default",
                isSelected = currentMode == ThemeMode.SYSTEM,
                modifier = Modifier.weight(1f),
                onClick = { onModeSelected(ThemeMode.SYSTEM) }
            )
            ThemeSegmentOption(
                title = "Light Mode",
                isSelected = currentMode == ThemeMode.LIGHT,
                modifier = Modifier.weight(1f),
                onClick = { onModeSelected(ThemeMode.LIGHT) }
            )
            ThemeSegmentOption(
                title = "Dark Mode",
                isSelected = currentMode == ThemeMode.DARK,
                modifier = Modifier.weight(1f),
                onClick = { onModeSelected(ThemeMode.DARK) }
            )
        }
    }
}

@Composable
private fun ThemeSegmentOption(
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = LocalOneThingColors.current
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) colors.surface else Color.Transparent,
        animationSpec = tween(180),
        label = "segment_bg"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) colors.primaryText else colors.secondaryText,
        animationSpec = tween(180),
        label = "segment_text"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 11.sp,
                color = textColor
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ==========================================
// SECTION 3: AI ENGINE CONFIG CARD
// ==========================================

@Composable
private fun AiEngineConfigCard(
    apiKey: String,
    onApiKeyChange: (String) -> Unit,
    isMasked: Boolean,
    onToggleMask: () -> Unit,
    onSaveApiKey: () -> Unit,
    selectedModel: String,
    onSelectModel: (String) -> Unit,
    hasKey: Boolean
) {
    val colors = LocalOneThingColors.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(colors.surface)
            .border(1.dp, colors.cardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Status Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "API Key Vault",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = colors.primaryText
                    )
                )

                // Live Connection Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (hasKey) colors.success.copy(alpha = 0.2f) else colors.primaryAccent.copy(alpha = 0.15f))
                        .border(
                            1.dp,
                            if (hasKey) colors.success else colors.primaryAccent,
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Canvas(modifier = Modifier.size(6.dp)) {
                            drawCircle(color = if (hasKey) colors.success else colors.primaryAccent)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (hasKey) "Connected" else "Missing Key",
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = if (hasKey) colors.primaryText else colors.primaryAccent
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Inline Input Field with Eye Toggle
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.secondarySurface)
                    .border(1.dp, colors.cardBorder, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        BasicTextField(
                            value = apiKey,
                            onValueChange = onApiKeyChange,
                            singleLine = true,
                            visualTransformation = if (isMasked) PasswordVisualTransformation() else VisualTransformation.None,
                            textStyle = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = colors.primaryText
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { onSaveApiKey() }),
                            decorationBox = { innerTextField ->
                                if (apiKey.isEmpty()) {
                                    Text(
                                        text = "Paste Gemini API Key (AIzaSy...)",
                                        style = TextStyle(
                                            fontFamily = FontFamily.SansSerif,
                                            fontSize = 12.sp,
                                            color = colors.secondaryText
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }

                    IconButton(
                        onClick = onToggleMask,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isMasked) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle mask",
                            tint = colors.secondaryText,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Validate & Save Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.primaryAccent)
                    .clickable(onClick = onSaveApiKey),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Validate & Save Key",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // AI Model Selector
            Text(
                text = "Model Generation Engine",
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = colors.primaryText
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ModelPillOption(
                    title = "Gemini 2.5 Flash (Fast)",
                    isSelected = selectedModel.contains("Flash"),
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectModel("Gemini 2.5 Flash (Fast)") }
                )
                ModelPillOption(
                    title = "Gemini 2.5 Pro (Deep)",
                    isSelected = selectedModel.contains("Pro"),
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectModel("Gemini 2.5 Pro (Deep)") }
                )
            }
        }
    }
}

@Composable
private fun ModelPillOption(
    title: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = LocalOneThingColors.current

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) colors.primaryText else colors.secondarySurface)
            .border(1.dp, if (isSelected) colors.primaryText else colors.cardBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 10.sp,
                color = if (isSelected) colors.surface else colors.secondaryText
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ==========================================
// SECTION 4: SHIELD & PERMISSIONS CARD
// ==========================================

@Composable
private fun ShieldPermissionsCard(
    hasAccessibility: Boolean,
    onOpenAccessibility: () -> Unit,
    strictnessMode: String,
    onSelectStrictness: (String) -> Unit,
    dailyGoal: String,
    onSelectDailyGoal: (String) -> Unit,
    hapticsEnabled: Boolean,
    onToggleHaptics: (Boolean) -> Unit,
    onPromptReset: () -> Unit
) {
    val colors = LocalOneThingColors.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(colors.surface)
            .border(1.dp, colors.cardBorder, RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Accessibility Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onOpenAccessibility),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Accessibility Service Status",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = colors.primaryText
                        )
                    )
                    Text(
                        text = if (hasAccessibility) "Active & Protecting Shorts/Reels" else "Requires Accessibility Permission",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp,
                            color = if (hasAccessibility) colors.success else colors.primaryAccent
                        )
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Open Settings",
                    tint = colors.secondaryText,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(colors.cardBorder))
            Spacer(modifier = Modifier.height(14.dp))

            // Default Strictness Mode
            Text(
                text = "Default Strictness Mode",
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = colors.primaryText
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("Gatekeeper", "5-Strike Tax", "Pomodoro Lock").forEach { mode ->
                    val isSel = strictnessMode.contains(mode.split(" ")[0])
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSel) colors.secondarySurface else Color.Transparent)
                            .border(1.dp, if (isSel) colors.primaryAccent else colors.cardBorder, RoundedCornerShape(10.dp))
                            .clickable { onSelectStrictness(mode) }
                            .padding(vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mode,
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 10.sp,
                                color = if (isSel) colors.primaryAccent else colors.secondaryText
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(colors.cardBorder))
            Spacer(modifier = Modifier.height(14.dp))

            // Daily Lesson Goal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daily Curriculum Commitment",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = colors.primaryText
                    )
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("1 / Day", "2 / Day").forEach { goal ->
                        val isSel = dailyGoal.startsWith(goal.take(1))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) colors.primaryText else colors.secondarySurface)
                                .clickable { onSelectDailyGoal(goal) }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = goal,
                                style = TextStyle(
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = if (isSel) colors.surface else colors.secondaryText
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(colors.cardBorder))
            Spacer(modifier = Modifier.height(14.dp))

            // Haptics & Sound Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Tactile Haptics & Effects",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = colors.primaryText
                        )
                    )
                    Text(
                        text = "Spring bounces and card lock-in vibration",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 11.sp,
                            color = colors.secondaryText
                        )
                    )
                }

                Switch(
                    checked = hapticsEnabled,
                    onCheckedChange = onToggleHaptics,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colors.surface,
                        checkedTrackColor = colors.primaryAccent,
                        uncheckedThumbColor = colors.secondaryText,
                        uncheckedTrackColor = colors.secondarySurface
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(colors.cardBorder))
            Spacer(modifier = Modifier.height(14.dp))

            // Destructive Action: Reset Progress
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onPromptReset),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Reset Progress",
                        tint = colors.primaryAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Reset Cache & Re-seed Curriculum",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = colors.primaryAccent
                        )
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = colors.secondaryText,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    val colors = LocalOneThingColors.current
    Text(
        text = title,
        style = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.sp,
            color = colors.secondaryText,
            letterSpacing = 1.sp
        )
    )
}

private fun DrawScope.drawUserHeadshotVector(color: Color) {
    val w = size.width
    val h = size.height

    drawCircle(
        color = color,
        radius = w * 0.26f,
        center = Offset(w * 0.5f, h * 0.32f)
    )

    drawArc(
        color = color,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(w * 0.1f, h * 0.55f),
        size = Size(w * 0.8f, h * 0.5f)
    )
}
