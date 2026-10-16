package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DistractionTargetApp
import com.example.ui.components.AppBackground
import com.example.ui.components.BackgroundVariant
import com.example.ui.theme.EditorialBlack
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.GreyBorder
import com.example.ui.theme.MintGreen
import com.example.ui.theme.MustardYellow
import com.example.ui.theme.PaperWhite
import com.example.ui.theme.VibrantOrange
import com.example.ui.theme.WarmPaper

/**
 * Distraction Shield and Focus Timer Screen with Grilli Type editorial styling,
 * real-time YouTube Shorts and Instagram Reels auto-back blocking, 5-strike lockout,
 * and anti-deletion tamper protection.
 */
@Composable
fun DistractionShieldScreen(
    isFocusActive: Boolean,
    focusRemainingSeconds: Int,
    selectedDurationMinutes: Int,
    isRewardActive: Boolean,
    rewardRemainingSeconds: Int,
    targetApps: List<DistractionTargetApp>,
    hasUsageStatsPermission: Boolean,
    hasAccessibilityPermission: Boolean = false,
    allStrikes: Map<String, Int> = emptyMap(),
    hardLockedApps: Set<String> = emptySet(),
    onSetDuration: (Int) -> Unit,
    onStartFocus: () -> Unit,
    onStopFocus: () -> Unit,
    onToggleAppBlocked: (String, Boolean) -> Unit,
    onSelectAllApps: () -> Unit,
    onDeselectAllApps: () -> Unit,
    onAddCustomApp: (packageName: String, appName: String, category: String) -> Unit,
    onRemoveCustomApp: (packageName: String) -> Unit,
    onEmergencyExit: () -> Unit,
    onNavigateBack: () -> Unit,
    onRefreshPermissions: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler {
        onNavigateBack()
    }

    val context = LocalContext.current
    var showCustomDurationDialog by remember { mutableStateOf(false) }
    var customDurationInput by remember { mutableStateOf(selectedDurationMinutes.toString()) }

    var showAddAppSection by remember { mutableStateOf(false) }
    var newPkgInput by remember { mutableStateOf("") }
    var newNameInput by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        onRefreshPermissions()
    }

    // Custom Duration Dialog
    if (showCustomDurationDialog) {
        AlertDialog(
            onDismissRequest = { showCustomDurationDialog = false },
            title = {
                Text(
                    "Set Custom Focus Duration",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = EditorialBlack
                    )
                )
            },
            text = {
                Column {
                    Text(
                        "Enter duration in minutes (e.g. 15, 45, 60):",
                        style = MaterialTheme.typography.bodySmall.copy(color = EditorialBlack.copy(alpha = 0.7f))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customDurationInput,
                        onValueChange = { customDurationInput = it.filter { ch -> ch.isDigit() } },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_duration_input_field"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditorialBlack,
                            unfocusedBorderColor = GreyBorder
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsed = customDurationInput.toIntOrNull()
                        if (parsed != null && parsed > 0) {
                            onSetDuration(parsed)
                        }
                        showCustomDurationDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EditorialBlack),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Save", color = WarmPaper, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomDurationDialog = false }) {
                    Text("Cancel", color = EditorialBlack)
                }
            },
            containerColor = WarmPaper
        )
    }

    AppBackground(
        variant = BackgroundVariant.FLAT,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Masthead Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("shield_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = EditorialBlack
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "DISTRACTION SHIELD",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Black,
                            color = ElectricPurple
                        )
                    )
                    Text(
                        text = "Focus Guard & Short-Form Blocker",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = EditorialBlack
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Live Reward Banner (if unlocked)
            AnimatedVisibility(visible = isRewardActive) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.5.dp, EditorialBlack, RoundedCornerShape(16.dp))
                        .padding(bottom = 16.dp),
                    color = MintGreen
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(EditorialBlack),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = MustardYellow,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "5-MIN REWARD PASS UNLOCKED",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = EditorialBlack
                                )
                            )
                            val m = rewardRemainingSeconds / 60
                            val s = rewardRemainingSeconds % 60
                            Text(
                                text = String.format("%02d:%02d ACCESS REMAINING", m, s),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = EditorialBlack
                                )
                            )
                        }
                    }
                }
            }

            // Focus Timer Control Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.5.dp, EditorialBlack, RoundedCornerShape(18.dp)),
                color = WarmPaper
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Timer,
                                contentDescription = null,
                                tint = VibrantOrange,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Focus Session Timer",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = EditorialBlack
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isFocusActive) VibrantOrange else MintGreen)
                                .border(1.dp, EditorialBlack, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = if (isFocusActive) "ACTIVE" else "IDLE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = if (isFocusActive) WarmPaper else EditorialBlack
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Timer Countdown Display
                    val displaySeconds = if (isFocusActive) focusRemainingSeconds else selectedDurationMinutes * 60
                    val minutes = displaySeconds / 60
                    val seconds = displaySeconds % 60

                    Text(
                        text = String.format("%02d:%02d", minutes, seconds),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 2.sp,
                            color = EditorialBlack
                        ),
                        modifier = Modifier.testTag("focus_timer_countdown_display")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Duration Preset Buttons (only when not active)
                    if (!isFocusActive) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val durations = listOf(25, 30, 45)
                            durations.forEach { dur ->
                                val isSelected = (selectedDurationMinutes == dur)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) ElectricPurple else MintGreen)
                                        .border(1.dp, EditorialBlack, RoundedCornerShape(10.dp))
                                        .clickable { onSetDuration(dur) }
                                        .padding(vertical = 10.dp)
                                        .testTag("duration_preset_${dur}m"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${dur}m",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Black,
                                            color = if (isSelected) WarmPaper else EditorialBlack
                                        )
                                    )
                                }
                            }

                            // Custom Button
                            val isCustom = selectedDurationMinutes !in durations
                            Box(
                                modifier = Modifier
                                    .weight(1.2f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isCustom) ElectricPurple else MintGreen)
                                    .border(1.dp, EditorialBlack, RoundedCornerShape(10.dp))
                                    .clickable {
                                        customDurationInput = selectedDurationMinutes.toString()
                                        showCustomDurationDialog = true
                                    }
                                    .padding(vertical = 10.dp)
                                    .testTag("duration_preset_custom"),
                                    contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isCustom) "Custom (${selectedDurationMinutes}m)" else "Custom",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = if (isCustom) WarmPaper else EditorialBlack
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Main Start/Stop Actions
                    if (!isFocusActive) {
                        Button(
                            onClick = onStartFocus,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("start_focus_timer_button"),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, EditorialBlack),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VibrantOrange,
                                contentColor = WarmPaper
                            )
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Start ${selectedDurationMinutes}-Min Focus Session",
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp
                            )
                        }
                    } else {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = onStopFocus,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("stop_focus_timer_button"),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, EditorialBlack),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = EditorialBlack,
                                    contentColor = WarmPaper
                                )
                            ) {
                                Icon(Icons.Default.Stop, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("End Session", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = onEmergencyExit,
                                modifier = Modifier
                                    .height(48.dp)
                                    .testTag("emergency_exit_button"),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, EditorialBlack),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = VibrantOrange,
                                    contentColor = WarmPaper
                                )
                            ) {
                                Icon(Icons.Default.Emergency, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Emergency Exit", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // --- IN-APP SHORTS/REELS BLOCKER & STRIKE MONITOR ---
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.5.dp, EditorialBlack, RoundedCornerShape(16.dp)),
                color = MintGreen
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = ElectricPurple)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Deep In-App Shield & Strikes",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (hasAccessibilityPermission) ElectricPurple else MustardYellow)
                                .border(1.dp, EditorialBlack, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (hasAccessibilityPermission) "ACTIVE" else "SETUP NEEDED",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = if (hasAccessibilityPermission) WarmPaper else EditorialBlack
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Instantly redirects YouTube Shorts and Instagram Reels back to home feed. Reaching 5 attempts in a session locks out the entire app until the session ends.",
                        style = MaterialTheme.typography.bodySmall.copy(color = EditorialBlack.copy(alpha = 0.8f))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Live Strikes Tracker
                    val ytStrikes = allStrikes["com.google.android.youtube"] ?: 0
                    val instaStrikes = allStrikes["com.instagram.android"] ?: 0
                    val isYtLocked = "com.google.android.youtube" in hardLockedApps
                    val isInstaLocked = "com.instagram.android" in hardLockedApps

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // YouTube Shorts Strike Box
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isYtLocked) VibrantOrange else WarmPaper)
                                .border(1.dp, EditorialBlack, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    "YouTube Shorts",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (isYtLocked) WarmPaper else EditorialBlack
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                if (isYtLocked) {
                                    Text(
                                        "LOCKED (5/5)",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp,
                                        color = WarmPaper
                                    )
                                } else {
                                    Text(
                                        "$ytStrikes / 5 Strikes",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp,
                                        color = if (ytStrikes > 0) VibrantOrange else EditorialBlack
                                    )
                                }
                            }
                        }

                        // Instagram Reels Strike Box
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isInstaLocked) VibrantOrange else WarmPaper)
                                .border(1.dp, EditorialBlack, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    "Instagram Reels",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (isInstaLocked) WarmPaper else EditorialBlack
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                if (isInstaLocked) {
                                    Text(
                                        "LOCKED (5/5)",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp,
                                        color = WarmPaper
                                    )
                                } else {
                                    Text(
                                        "$instaStrikes / 5 Strikes",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp,
                                        color = if (instaStrikes > 0) VibrantOrange else EditorialBlack
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Anti-Deletion / Tamper Notice
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(WarmPaper, RoundedCornerShape(8.dp))
                            .border(1.dp, GreyBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = ElectricPurple,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Anti-Tamper: Uninstallation and App Settings are restricted during active focus.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = EditorialBlack)
                        )
                    }

                    if (!hasAccessibilityPermission) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                try {
                                    context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                                } catch (e: Exception) {
                                    // Fallback
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBlack),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VibrantOrange,
                                contentColor = WarmPaper
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("grant_accessibility_service_button")
                        ) {
                            Text(
                                "Enable ONE THING in Accessibility Settings",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Usage Access Permission Card (if not granted)
            if (!hasUsageStatsPermission) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.5.dp, EditorialBlack, RoundedCornerShape(14.dp)),
                    color = MustardYellow
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = EditorialBlack)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Usage Access Permission",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Required for app-level detection when entertainment apps launch in the foreground.",
                            style = MaterialTheme.typography.bodySmall.copy(color = EditorialBlack.copy(alpha = 0.8f))
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                try {
                                    context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                                } catch (e: Exception) {
                                    // Fallback
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBlack),
                            colors = ButtonDefaults.buttonColors(containerColor = EditorialBlack),
                            modifier = Modifier.testTag("grant_usage_access_button")
                        ) {
                            Text("Grant Usage Access in Settings", color = WarmPaper, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            // Distraction App Targets Header & Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Monitored Distraction Apps",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                    )
                    Text(
                        text = "Restricted in focus • Unlocked in reward pass",
                        style = MaterialTheme.typography.bodySmall.copy(color = EditorialBlack.copy(alpha = 0.6f))
                    )
                }

                Row {
                    OutlinedButton(
                        onClick = onSelectAllApps,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBlack),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("shield_select_all_apps_button")
                    ) {
                        Text("All", color = EditorialBlack, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    OutlinedButton(
                        onClick = onDeselectAllApps,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBlack),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("shield_deselect_all_apps_button")
                    ) {
                        Text("None", color = EditorialBlack, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // App Targets List
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                targetApps.forEach { app ->
                    val isHardLocked = app.packageName in hardLockedApps
                    val strikes = allStrikes[app.packageName] ?: 0

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.5.dp, EditorialBlack, RoundedCornerShape(12.dp)),
                        color = if (isHardLocked) VibrantOrange.copy(alpha = 0.15f) else WarmPaper
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = app.appName,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    if (isHardLocked) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(VibrantOrange)
                                                .padding(horizontal = 5.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                "LOCKED",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = WarmPaper
                                                )
                                            )
                                        }
                                    } else if (strikes > 0) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(MustardYellow)
                                                .padding(horizontal = 5.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                "$strikes STRIKES",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = EditorialBlack
                                                )
                                            )
                                        }
                                    } else if (app.isCustom) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(MintGreen)
                                                .border(1.dp, EditorialBlack, RoundedCornerShape(4.dp))
                                                .padding(horizontal = 5.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                "CUSTOM",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = app.packageName,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = EditorialBlack.copy(alpha = 0.6f),
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Switch(
                                    checked = app.isEnabled,
                                    onCheckedChange = { checked ->
                                        onToggleAppBlocked(app.packageName, checked)
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = WarmPaper,
                                        checkedTrackColor = EditorialBlack,
                                        uncheckedThumbColor = EditorialBlack,
                                        uncheckedTrackColor = MintGreen
                                    ),
                                    modifier = Modifier.testTag("toggle_app_${app.packageName}")
                                )

                                if (app.isCustom) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    IconButton(
                                        onClick = { onRemoveCustomApp(app.packageName) },
                                        modifier = Modifier
                                            .size(28.dp)
                                            .testTag("remove_custom_app_${app.packageName}")
                                    ) {
                                        Icon(
                                            Icons.Default.DeleteOutline,
                                            contentDescription = "Delete",
                                            tint = VibrantOrange,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add Custom Package / App Section
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.5.dp, EditorialBlack, RoundedCornerShape(14.dp)),
                color = WarmPaper
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAddAppSection = !showAddAppSection },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                tint = ElectricPurple,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Add Custom App Package",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Text(
                            text = if (showAddAppSection) "Collapse" else "+ Add",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = ElectricPurple
                            )
                        )
                    }

                    if (showAddAppSection) {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = newPkgInput,
                            onValueChange = { newPkgInput = it },
                            placeholder = { Text("Package Name (e.g. com.snapchat.android)", fontSize = 12.sp) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("custom_app_pkg_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EditorialBlack,
                                unfocusedBorderColor = GreyBorder
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newNameInput,
                            onValueChange = { newNameInput = it },
                            placeholder = { Text("Display Name (e.g. Snapchat)", fontSize = 12.sp) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("custom_app_name_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EditorialBlack,
                                unfocusedBorderColor = GreyBorder
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                if (newPkgInput.isNotBlank()) {
                                    onAddCustomApp(
                                        newPkgInput.trim(),
                                        newNameInput.trim().ifEmpty { newPkgInput.trim() },
                                        "Custom"
                                    )
                                    newPkgInput = ""
                                    newNameInput = ""
                                    showAddAppSection = false
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EditorialBlack),
                            colors = ButtonDefaults.buttonColors(containerColor = EditorialBlack),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("save_custom_app_button")
                        ) {
                            Text("Add to Monitored List", color = WarmPaper, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
