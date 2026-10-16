package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LearningTopic
import com.example.ui.components.AppBackground
import com.example.ui.components.BackgroundVariant
import com.example.ui.components.EditorialArtwork
import com.example.ui.theme.AcidLime
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GreyBorder
import com.example.ui.theme.InkBlack
import com.example.ui.theme.PaperWhite
import com.example.ui.theme.TangerineOrange
import com.example.ui.theme.WarmCream
import com.example.ui.theme.WarmCreamSurface

/**
 * Editorial Locked Screen with 5-minute reward pass redemption, streak counter, and lesson review.
 */
@Composable
fun CompletedLockScreen(
    completedTopic: LearningTopic,
    streakCount: Int,
    totalCompleted: Int,
    reflectionText: String,
    isRewardActive: Boolean,
    rewardRemainingSeconds: Int,
    onClaimReward: () -> Unit,
    onReviewTopic: () -> Unit,
    onOpenShieldSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    AppBackground(
        variant = BackgroundVariant.SOFT_GLOW,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
                .testTag("completed_lock_screen"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            // Lock Badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(InkBlack)
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = AcidLime,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "LOCKED UNTIL TOMORROW",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        color = PaperWhite
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Generative Celebration Artwork
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
            ) {
                EditorialArtwork(
                    shapeType = completedTopic.shapeType,
                    primaryColor = AcidLime,
                    secondaryColor = TangerineOrange,
                    height = 150.dp
                )

                // Large Checkmark Stamp
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(InkBlack)
                        .border(2.dp, PaperWhite, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completed",
                        tint = AcidLime,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Today's ONE THING is Complete.",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Black,
                    color = InkBlack,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "You showed up for your growth today. Let this lesson digest in your subconscious.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = CharcoalMuted,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Reward Pass Banner / Trigger
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.5.dp, if (isRewardActive) InkBlack else TangerineOrange, RoundedCornerShape(16.dp)),
                color = if (isRewardActive) AcidLime else WarmCreamSurface
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = InkBlack, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isRewardActive) "5-MIN REWARD ACTIVE" else "5-MINUTE ENTERTAINMENT REWARD",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    if (isRewardActive) {
                        val m = rewardRemainingSeconds / 60
                        val s = rewardRemainingSeconds % 60
                        Text(
                            text = String.format("%02d:%02d REMAINING", m, s),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = InkBlack
                            )
                        )
                        Text(
                            text = "Selected apps (Instagram, YouTube, etc.) are unblocked.",
                            style = MaterialTheme.typography.bodySmall.copy(color = DarkCharcoal)
                        )
                    } else {
                        Text(
                            text = "You earned 5 minutes of unrestricted entertainment access for passing today's lesson.",
                            style = MaterialTheme.typography.bodySmall.copy(color = CharcoalMuted, textAlign = TextAlign.Center)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = onClaimReward,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = InkBlack, contentColor = PaperWhite),
                            modifier = Modifier.testTag("claim_5min_reward_button")
                        ) {
                            Text("Activate 5-Minute Pass", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Streak & Progress Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, GreyBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = WarmCreamSurface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = TangerineOrange,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$streakCount",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = InkBlack
                                )
                            )
                        }
                        Text(
                            text = "DAY STREAK",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = CharcoalMuted
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(GreyBorder)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$totalCompleted",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = InkBlack
                            )
                        )
                        Text(
                            text = "LESSONS MASTERED",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = CharcoalMuted
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Completed Topic Summary Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, GreyBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = WarmCreamSurface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "TODAY'S TOPIC",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = CharcoalMuted,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = completedTopic.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = InkBlack
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = completedTopic.oneLineDescription,
                        style = MaterialTheme.typography.bodySmall.copy(color = CharcoalMuted)
                    )

                    if (reflectionText.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(WarmCream)
                                .padding(10.dp)
                        ) {
                            Column {
                                Text(
                                    text = "YOUR REFLECTION:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = CharcoalMuted
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "\"$reflectionText\"",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = InkBlack,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Actions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onReviewTopic,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("review_topic_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = InkBlack)
            ) {
                Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Review Lesson & Key Concepts", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onOpenShieldSettings,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("open_shield_from_lock_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = InkBlack)
            ) {
                Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Distraction Shield & Focus Timer", fontWeight = FontWeight.Bold)
            }
        }
    }
}
}
