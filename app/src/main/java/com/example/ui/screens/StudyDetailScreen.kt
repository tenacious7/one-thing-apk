package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.components.EditorialArtwork
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
 * Focused Learning Screen presenting topic breakdown, why it matters, key principles,
 * 5-minute action prompt, interactive reflection notes, and transition to comprehension quiz.
 */
@Composable
fun StudyDetailScreen(
    topic: LearningTopic,
    initialReflection: String,
    onNavigateBack: () -> Unit,
    onProceedToQuiz: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onNavigateBack()
    }

    var reflectionInput by remember { mutableStateOf(initialReflection) }

    AppBackground(
        variant = BackgroundVariant.SOFT_GLOW,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .testTag("study_detail_screen")
        ) {
        // Top App Bar Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.testTag("back_to_topics_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Topics",
                    tint = InkBlack
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(InkBlack)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = topic.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PaperWhite
                        )
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AcidLime.copy(alpha = 0.25f))
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "${topic.estimatedLearningTimeMinutes}M",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = InkBlack
                        )
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Large Featured Art Banner
            EditorialArtwork(
                shapeType = topic.shapeType,
                primaryColor = topic.primaryColor,
                secondaryColor = topic.secondaryColor,
                height = 140.dp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Topic Headline
            Text(
                text = topic.title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = InkBlack,
                    lineHeight = 36.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = topic.oneLineDescription,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = CharcoalMuted,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 01. Why It Matters Section
            EditorialSectionCard(
                sectionNumber = "01",
                sectionTitle = "WHY IT MATTERS",
                accentColor = topic.primaryColor
            ) {
                Text(
                    text = topic.whyItMatters,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = InkBlack,
                        lineHeight = 24.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 02. Key Concepts & Mental Models Section
            EditorialSectionCard(
                sectionNumber = "02",
                sectionTitle = "KEY CONCEPTS",
                accentColor = ElectricYellow
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    topic.keyConcepts.forEachIndexed { index, concept ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "•",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = topic.primaryColor
                                ),
                                modifier = Modifier.padding(end = 10.dp)
                            )
                            Text(
                                text = concept,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = InkBlack,
                                    lineHeight = 22.sp
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 03. 5-Minute Practical Task Section
            EditorialSectionCard(
                sectionNumber = "03",
                sectionTitle = "TODAY'S 5-MINUTE PRACTICE",
                accentColor = AcidLime
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = InkBlack,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = topic.practicalTaskTitle,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = InkBlack
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = topic.practicalTaskInstruction,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = InkBlack,
                            lineHeight = 22.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Reflection Text Input
                    Text(
                        text = topic.reflectionPrompt,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CharcoalMuted
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = reflectionInput,
                        onValueChange = { reflectionInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reflection_text_input"),
                        placeholder = {
                            Text(
                                text = "Jot down your observation or action here...",
                                style = MaterialTheme.typography.bodyMedium.copy(color = CharcoalMuted)
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InkBlack,
                            unfocusedBorderColor = GreyBorder,
                            focusedContainerColor = PaperWhite,
                            unfocusedContainerColor = PaperWhite,
                            focusedTextColor = InkBlack,
                            unfocusedTextColor = InkBlack
                        ),
                        minLines = 3,
                        maxLines = 6
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Action Button: Take Comprehension Quiz
            Button(
                onClick = { onProceedToQuiz(reflectionInput) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("proceed_to_quiz_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = InkBlack,
                    contentColor = PaperWhite
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                    contentDescription = null,
                    tint = ElectricYellow,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Verify Learning with Quiz",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = PaperWhite
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = PaperWhite,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}
}

@Composable
private fun EditorialSectionCard(
    sectionNumber: String,
    sectionTitle: String,
    accentColor: androidx.compose.ui.graphics.Color,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, GreyBorder, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = WarmCreamSurface,
        tonalElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(accentColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = sectionNumber,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = InkBlack
                        )
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = sectionTitle,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        color = InkBlack
                    )
                )
            }
            content()
        }
    }
}
