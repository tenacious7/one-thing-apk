package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.LearningTopic
import com.example.ui.theme.AcidLime
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GreyBorder
import com.example.ui.theme.InkBlack
import com.example.ui.theme.PaperWhite
import com.example.ui.theme.TangerineOrange
import com.example.ui.theme.WarmCreamSurface

/**
 * Editorial Topic Card with distinctive procedural art header, category badge, and selection CTA.
 */
@Composable
fun TopicCard(
    topic: LearningTopic,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, GreyBorder, RoundedCornerShape(16.dp))
            .testTag("topic_card_${topic.id}"),
        shape = RoundedCornerShape(16.dp),
        color = WarmCreamSurface,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Editorial Abstract Artwork Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                EditorialArtwork(
                    shapeType = topic.shapeType,
                    primaryColor = topic.primaryColor,
                    secondaryColor = topic.secondaryColor,
                    height = 120.dp
                )

                // High-contrast category pill
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(InkBlack)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = topic.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = PaperWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Topic Title
            Text(
                text = topic.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = InkBlack
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Topic Description
            Text(
                text = topic.oneLineDescription,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = CharcoalMuted,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2f
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action Button
            val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val scale by androidx.compose.animation.core.animateFloatAsState(
                targetValue = if (isPressed) 0.96f else 1.0f,
                animationSpec = androidx.compose.animation.core.spring(dampingRatio = 0.5f, stiffness = 400f),
                label = "button_scale"
            )

            Button(
                onClick = onSelect,
                interactionSource = interactionSource,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .graphicsLayer(scaleX = scale, scaleY = scale)
                    .testTag("select_topic_button_${topic.id}"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = InkBlack,
                    contentColor = PaperWhite
                )
            ) {
                Text(
                    text = "I'll Learn This",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = topic.primaryColor
                )
            }
        }
    }
}
