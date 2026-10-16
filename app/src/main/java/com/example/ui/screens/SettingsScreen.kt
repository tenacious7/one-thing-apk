package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DistractionTargetApp
import com.example.model.OnboardingMode
import com.example.model.SkillConfig
import com.example.model.StandardCategories
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    currentConfig: SkillConfig,
    currentApiKey: String,
    targetApps: List<DistractionTargetApp>,
    hardLockedApps: Set<String>,
    allStrikes: Map<String, Int>,
    onSaveApiKey: (String) -> Unit,
    onSaveConfig: (SkillConfig) -> Unit,
    onToggleAppBlocked: (String, Boolean) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var apiKeyInput by remember { mutableStateOf(currentApiKey) }
    var promptInput by remember { mutableStateOf(currentConfig.aiInterestsPrompt) }
    var activeCategories by remember { mutableStateOf(currentConfig.activeCategoryIds) }
    var activeSkills by remember { mutableStateOf(currentConfig.activeSkillNames) }
    var customSkills by remember { mutableStateOf(currentConfig.customSkills) }
    var showAddAppSection by remember { mutableStateOf(false) }
    var newPkgInput by remember { mutableStateOf("") }
    var newNameInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCream)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .testTag("settings_screen")
    ) {
        // App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = InkBlack)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "SETTINGS",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = InkBlack)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 1. API Configuration Section
        SettingsSection(title = "Gemini AI API", icon = Icons.Default.Key) {
            OutlinedTextField(
                value = apiKeyInput,
                onValueChange = {
                    apiKeyInput = it
                    onSaveApiKey(it)
                },
                placeholder = { Text("Enter your Gemini API Key") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Skill Configuration Section
        SettingsSection(title = "Skills & Interests", icon = Icons.Default.AutoAwesome) {
            Text("Selected Categories", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                StandardCategories.list.forEach { category ->
                    val isChecked = category.id in activeCategories
                    FilterChip(
                        selected = isChecked,
                        onClick = {
                            activeCategories = if (isChecked) activeCategories - category.id else activeCategories + category.id
                        },
                        label = { Text(category.name) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Blocked Apps Section
        SettingsSection(title = "Blocked Distraction Apps", icon = Icons.Default.Shield) {
            targetApps.forEach { app ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(app.appName, style = MaterialTheme.typography.bodyMedium)
                    Switch(
                        checked = app.isEnabled,
                        onCheckedChange = { onToggleAppBlocked(app.packageName, it) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Save Button
        Button(
            onClick = {
                val newConfig = SkillConfig(
                    activeCategoryIds = activeCategories,
                    activeSkillNames = activeSkills,
                    customSkills = customSkills,
                    aiInterestsPrompt = promptInput,
                    onboardingMode = OnboardingMode.MANUAL_SELECTION
                )
                onSaveConfig(newConfig)
                onNavigateBack()
            },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange)
        ) {
            Text("Save Changes", fontWeight = FontWeight.Bold, color = PaperWhite)
        }
        
        Spacer(modifier = Modifier.height(36.dp))
    }
}

@Composable
fun SettingsSection(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, content: @Composable () -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = TangerineOrange, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).border(1.dp, GreyBorder, RoundedCornerShape(14.dp)),
            color = WarmCreamSurface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}
