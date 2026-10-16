package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.example.data.model.MasteredSkill
import com.example.ui.theme.InkBlack
import com.example.ui.theme.PaperWhite

@Composable
fun SkillCard(skill: MasteredSkill, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current
    
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.94f else 1f, label = "scale")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                }
            ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, InkBlack),
        colors = CardDefaults.cardColors(containerColor = PaperWhite)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = skill.title, style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
            Text(text = skill.labelText, style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
        }
    }
}
