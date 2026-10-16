package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LearningTopic
import com.example.model.SkillConfig
import com.example.ui.components.TopicCard
import com.example.ui.theme.AcidLime
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.ElectricYellow
import com.example.ui.theme.GreyBorder
import com.example.ui.theme.InkBlack
import com.example.ui.theme.PaperWhite
import com.example.ui.theme.SoftGreyContainer
import com.example.ui.theme.TangerineOrange
import com.example.ui.theme.WarmCream
import com.example.ui.theme.WarmCreamSurface
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Editorial Home Screen with active interests pills, distraction shield bar, live reward countdown,
 * and AI topic generation.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    candidates: List<LearningTopic>,
    streakCount: Int,
    totalCompleted: Int,
    skillConfig: SkillConfig?,
    isGeneratingAi: Boolean,
    aiNoteMessage: String?,
    isFocusActive: Boolean,
    focusRemainingSeconds: Int,
    isRewardActive: Boolean,
    rewardRemainingSeconds: Int,
    onSelectTopic: (LearningTopic) -> Unit,
    onShuffle: () -> Unit,
    onGenerateWithAi: () -> Unit,
    onOpenSkillSettings: () -> Unit,
    onOpenShieldSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedDate = remember {
        val sdf = SimpleDateFormat("EEEE, MMMM d", Locale.US)
        sdf.format(Date()).uppercase()
    }

    com.example.ui.components.AppBackground(
        variant = com.example.ui.components.BackgroundVariant.SOFT_GLOW,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .testTag("home_screen_scroll_list"),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        // Masthead & Header Stats
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 1.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalMuted
                        )
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Shield Status Shortcut
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isFocusActive) TangerineOrange else WarmCreamSurface)
                                .border(1.dp, InkBlack, RoundedCornerShape(20.dp))
                                .clickable { onOpenShieldSettings() }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .testTag("home_shield_shortcut_button")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isFocusActive) Icons.Default.Shield else Icons.Default.Security,
                                    contentDescription = "Shield",
                                    tint = if (isFocusActive) PaperWhite else InkBlack,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isFocusActive) "SHIELD ON" else "SHIELD",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isFocusActive) PaperWhite else InkBlack
                                    )
                                )
                            }
                        }

                        // Streak Badge
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (streakCount > 0) AcidLime else GreyBorder.copy(alpha = 0.5f))
                                .border(1.dp, InkBlack, RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Streak",
                                tint = if (streakCount > 0) InkBlack else CharcoalMuted,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$streakCount DAY${if (streakCount == 1) "" else "S"}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = InkBlack
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "ONE THING",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-1).sp,
                        color = InkBlack
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Become a little better every day.",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = CharcoalMuted,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        // Live 5-Minute Reward Banner (if active)
        if (isRewardActive) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.5.dp, InkBlack, RoundedCornerShape(14.dp)),
                    color = AcidLime
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(InkBlack),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = AcidLime, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("5-MIN REWARD PASS ACTIVE", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black))
                            val m = rewardRemainingSeconds / 60
                            val s = rewardRemainingSeconds % 60
                            Text(
                                text = String.format("%02d:%02d entertainment access remaining", m, s),
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            )
                        }
                    }
                }
            }
        }

        // Active Focus Session Live Banner (if active)
        if (isFocusActive) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, InkBlack, RoundedCornerShape(14.dp)),
                    color = InkBlack
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Timer, contentDescription = null, tint = TangerineOrange, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("FOCUS TIMER RUNNING", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = TangerineOrange))
                                val m = focusRemainingSeconds / 60
                                val s = focusRemainingSeconds % 60
                                Text(
                                    text = String.format("%02d:%02d", m, s),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = PaperWhite, fontFamily = FontFamily.Monospace)
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(WarmCreamSurface)
                                .clickable { onOpenShieldSettings() }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("MANAGE", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = InkBlack))
                        }
                    }
                }
            }
        }

        // Active Learning Interests Pills & Edit Button
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, GreyBorder, RoundedCornerShape(14.dp)),
                color = WarmCreamSurface
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ACTIVE INTERESTS",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = CharcoalMuted, letterSpacing = 1.sp)
                        )
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onOpenSkillSettings() }
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Interests", tint = InkBlack, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit Skills", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val skills = skillConfig?.activeSkillNames?.take(6) ?: emptyList()
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        skills.forEach { skill ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(SoftGreyContainer)
                                    .border(1.dp, GreyBorder, RoundedCornerShape(14.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(text = skill, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, color = InkBlack))
                            }
                        }
                        if (skills.isEmpty()) {
                            Text("Tap Edit Skills to configure your learning curriculum.", style = MaterialTheme.typography.bodySmall.copy(color = CharcoalMuted))
                        }
                    }
                }
            }
        }

        // Heading: What will you learn today?
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(InkBlack)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "What will you learn today?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = PaperWhite
                    )
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(TangerineOrange),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${candidates.size}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = InkBlack
                        )
                    )
                }
            }
        }

        // AI Generation Note / Message (if any)
        if (aiNoteMessage != null) {
            item {
                Text(
                    text = aiNoteMessage,
                    style = MaterialTheme.typography.bodySmall.copy(color = CharcoalMuted),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }

        // 3 Candidate Topic Cards
        items(candidates) { topic ->
            TopicCard(
                topic = topic,
                onSelect = { onSelectTopic(topic) }
            )
        }

        // Action Buttons: AI Topic Generation & Shuffle
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Button 1: AI Topic Generator
                Button(
                    onClick = onGenerateWithAi,
                    enabled = !isGeneratingAi,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("ai_generate_topics_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TangerineOrange,
                        contentColor = PaperWhite
                    )
                ) {
                    if (isGeneratingAi) {
                        CircularProgressIndicator(color = PaperWhite, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generating Personalized Topics...", fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate 3 New Topics with AI", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                    }
                }

                // Button 2: Quick Shuffle
                OutlinedButton(
                    onClick = onShuffle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("pick_new_topics_button"),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.5.dp, InkBlack),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = InkBlack
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Shuffle Topics",
                        modifier = Modifier.size(18.dp),
                        tint = InkBlack
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pick 3 New Topics (Catalog Shuffle)",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Total Lessons Mastered: $totalCompleted • Focus Lock Active Once Selected",
                    style = MaterialTheme.typography.labelSmall.copy(color = CharcoalMuted),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
}
