package com.example.ui.screens

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LearningTopic
import com.example.ui.components.AppBackground
import com.example.ui.components.BackgroundVariant
import com.example.ui.theme.AcidLime
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.ElectricYellow
import com.example.ui.theme.GreyBorder
import com.example.ui.theme.InkBlack
import com.example.ui.theme.PaperWhite
import com.example.ui.theme.TangerineOrange
import com.example.ui.theme.WarmCream
import com.example.ui.theme.WarmCreamSurface

/**
 * Comprehension verification quiz screen testing lesson retention before claiming rewards.
 */
@Composable
fun QuizScreen(
    topic: LearningTopic,
    onNavigateBack: () -> Unit,
    onQuizPassed: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onNavigateBack()
    }

    val quiz = topic.comprehensionQuestion
    var selectedOptionIndex by remember { mutableIntStateOf(-1) }
    var hasSubmitted by remember { mutableStateOf(false) }
    val isCorrect = selectedOptionIndex == quiz.correctOptionIndex

    AppBackground(
        variant = BackgroundVariant.SOLID_CREAM,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .testTag("quiz_screen_root")
        ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.testTag("back_to_study_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Study",
                    tint = InkBlack
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(InkBlack)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "COMPREHENSION CHECK",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PaperWhite
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Topic Reference Header
            Text(
                text = topic.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = InkBlack
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Answer correctly to verify your daily learning and unlock your 5-minute reward pass.",
                style = MaterialTheme.typography.bodyMedium.copy(color = CharcoalMuted)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Question Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, GreyBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = WarmCreamSurface
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(ElectricYellow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                                contentDescription = null,
                                tint = InkBlack,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "QUESTION",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = InkBlack,
                                letterSpacing = 1.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = quiz.question,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = InkBlack,
                            lineHeight = 24.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Options List
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                quiz.options.forEachIndexed { index, option ->
                    val isSelected = selectedOptionIndex == index
                    val isOptionCorrect = index == quiz.correctOptionIndex

                    val borderColor = when {
                        hasSubmitted && isOptionCorrect -> AcidLime
                        hasSubmitted && isSelected && !isCorrect -> TangerineOrange
                        isSelected -> InkBlack
                        else -> GreyBorder
                    }

                    val bgColor = when {
                        hasSubmitted && isOptionCorrect -> AcidLime.copy(alpha = 0.2f)
                        hasSubmitted && isSelected && !isCorrect -> TangerineOrange.copy(alpha = 0.15f)
                        isSelected -> SoftGreyContainer()
                        else -> PaperWhite
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .border(if (isSelected || (hasSubmitted && isOptionCorrect)) 2.dp else 1.dp, borderColor, RoundedCornerShape(14.dp))
                            .clickable(enabled = !hasSubmitted) {
                                selectedOptionIndex = index
                            }
                            .testTag("quiz_option_$index"),
                        shape = RoundedCornerShape(14.dp),
                        color = bgColor
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, if (isSelected) InkBlack else GreyBorder, CircleShape)
                                    .background(if (isSelected) InkBlack else PaperWhite),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = PaperWhite,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = InkBlack
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Feedback Card
            AnimatedVisibility(visible = hasSubmitted) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.5.dp, if (isCorrect) AcidLime else TangerineOrange, RoundedCornerShape(14.dp)),
                    color = if (isCorrect) AcidLime.copy(alpha = 0.12f) else TangerineOrange.copy(alpha = 0.12f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (isCorrect) InkBlack else TangerineOrange,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isCorrect) "EXCELLENT WORK!" else "NOT QUITE",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = InkBlack
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = quiz.explanation,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = InkBlack,
                                lineHeight = 22.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Actions
            if (!hasSubmitted) {
                Button(
                    onClick = {
                        if (selectedOptionIndex != -1) {
                            hasSubmitted = true
                        }
                    },
                    enabled = selectedOptionIndex != -1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_quiz_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = InkBlack,
                        contentColor = PaperWhite
                    )
                ) {
                    Text(
                        text = "Verify Answer",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            } else if (isCorrect) {
                Button(
                    onClick = onQuizPassed,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("pass_quiz_continue_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = InkBlack,
                        contentColor = PaperWhite
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Stars,
                        contentDescription = null,
                        tint = AcidLime,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Claim Reward & Complete Session",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            } else {
                Button(
                    onClick = {
                        hasSubmitted = false
                        selectedOptionIndex = -1
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("try_again_quiz_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TangerineOrange,
                        contentColor = PaperWhite
                    )
                ) {
                    Text(
                        text = "Try Again",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}
}

@Composable
private fun SoftGreyContainer(): androidx.compose.ui.graphics.Color {
    return com.example.ui.theme.SoftGreyContainer
}
