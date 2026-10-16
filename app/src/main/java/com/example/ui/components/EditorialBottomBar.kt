package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenState
import com.example.ui.theme.LocalOneThingColors

enum class NavTab {
    TODAY,
    SHIELD,
    SKILLS,
    PROFILE
}

@Composable
fun EditorialBottomBar(
    currentScreen: ScreenState,
    isFocusActive: Boolean,
    strikeCount: Int = 0,
    onNavigateToHome: () -> Unit,
    onNavigateToShield: () -> Unit,
    onNavigateToSkills: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalOneThingColors.current

    val activeTab = when (currentScreen) {
        ScreenState.HOME_LIST, ScreenState.COMPLETED_LOCKED -> NavTab.TODAY
        ScreenState.DISTRACTION_SHIELD -> NavTab.SHIELD
        ScreenState.SKILL_SETTINGS -> NavTab.SKILLS
        ScreenState.PROFILE_SETTINGS -> NavTab.PROFILE
        else -> NavTab.TODAY
    }

    val barBackground = if (colors.isDark) {
        androidx.compose.ui.graphics.Brush.verticalGradient(
            listOf(Color(0xFF121218).copy(alpha = 0.75f), Color(0xFF121218).copy(alpha = 0.82f))
        )
    } else {
        androidx.compose.ui.graphics.Brush.verticalGradient(
            listOf(Color(0xFFEAE2F8).copy(alpha = 0.75f), Color(0xFFFFF8EC).copy(alpha = 0.82f))
        )
    }
    val topBorderColor = if (colors.isDark) Color(0xFF262632).copy(alpha = 0.60f) else Color(0xFFDCD4E8).copy(alpha = 0.60f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(barBackground)
            .border(
                width = 1.dp,
                color = topBorderColor
            )
            .navigationBarsPadding()
            .height(64.dp)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tab 1: Today
            InstagramNavItem(
                iconType = NavIconType.TODAY,
                isSelected = activeTab == NavTab.TODAY,
                onClick = onNavigateToHome,
                tag = "nav_tab_today"
            )

            // Tab 2: Shield
            InstagramNavItem(
                iconType = NavIconType.SHIELD,
                isSelected = activeTab == NavTab.SHIELD,
                onClick = onNavigateToShield,
                tag = "nav_tab_shield",
                badgeText = when {
                    isFocusActive -> "ON"
                    strikeCount > 0 -> "$strikeCount"
                    else -> null
                },
                badgeColor = if (strikeCount > 0) colors.primaryAccent else colors.secondaryAccent
            )

            // Tab 3: Skills
            InstagramNavItem(
                iconType = NavIconType.SKILLS,
                isSelected = activeTab == NavTab.SKILLS,
                onClick = onNavigateToSkills,
                tag = "nav_tab_skills"
            )

            // Tab 4: Profile / Settings
            InstagramProfileAvatarNavItem(
                isSelected = activeTab == NavTab.PROFILE,
                onClick = onNavigateToProfile,
                tag = "nav_tab_profile"
            )
        }
    }
}

private enum class NavIconType { TODAY, SHIELD, SKILLS }

@Composable
private fun InstagramNavItem(
    iconType: NavIconType,
    isSelected: Boolean,
    onClick: () -> Unit,
    tag: String,
    badgeText: String? = null,
    badgeColor: Color = Color.Unspecified
) {
    val colors = LocalOneThingColors.current
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "nav_scale"
    )

    val iconColor by animateColorAsState(
        targetValue = if (isSelected) colors.primaryText else colors.secondaryText,
        animationSpec = tween(200),
        label = "nav_icon_color"
    )

    LaunchedEffect(isPressed) {
        if (isPressed) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    Column(
        modifier = Modifier
            .testTag(tag)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Canvas(modifier = Modifier.size(26.dp)) {
                when (iconType) {
                    NavIconType.TODAY -> drawTodayBookVector(color = iconColor, isFilled = isSelected)
                    NavIconType.SHIELD -> drawShieldNavVector(color = iconColor, isFilled = isSelected)
                    NavIconType.SKILLS -> drawSkillsBrainSlidersVector(color = iconColor, isFilled = isSelected)
                }
            }

            if (badgeText != null) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(if (badgeColor != Color.Unspecified) badgeColor else colors.primaryAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badgeText,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Active indicator dot
        Box(
            modifier = Modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(if (isSelected) colors.primaryAccent else Color.Transparent)
        )
    }
}

@Composable
private fun InstagramProfileAvatarNavItem(
    isSelected: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    val colors = LocalOneThingColors.current
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "nav_profile_scale"
    )

    LaunchedEffect(isPressed) {
        if (isPressed) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    Column(
        modifier = Modifier
            .testTag(tag)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Circular Instagram-style user avatar
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) colors.primaryAccent else colors.cardBorder,
                    shape = CircleShape
                )
                .background(colors.secondarySurface),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(16.dp)) {
                // Minimalist user avatar silhouette
                val w = size.width
                val h = size.height
                drawCircle(
                    color = if (isSelected) colors.primaryAccent else colors.secondaryText,
                    radius = w * 0.28f,
                    center = Offset(w * 0.5f, h * 0.32f)
                )
                drawArc(
                    color = if (isSelected) colors.primaryAccent else colors.secondaryText,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(w * 0.12f, h * 0.54f),
                    size = Size(w * 0.76f, h * 0.52f)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(if (isSelected) colors.primaryAccent else Color.Transparent)
        )
    }
}

// ==========================================
// CUSTOM VECTOR DRAWING FUNCTIONS
// ==========================================

private fun DrawScope.drawTodayBookVector(color: Color, isFilled: Boolean) {
    val w = size.width
    val h = size.height

    val path = Path().apply {
        moveTo(w * 0.15f, h * 0.25f)
        cubicTo(w * 0.35f, h * 0.2f, w * 0.48f, h * 0.3f, w * 0.5f, h * 0.35f)
        cubicTo(w * 0.52f, h * 0.3f, w * 0.65f, h * 0.2f, w * 0.85f, h * 0.25f)
        lineTo(w * 0.85f, h * 0.85f)
        cubicTo(w * 0.65f, h * 0.8f, w * 0.52f, h * 0.9f, w * 0.5f, h * 0.85f)
        cubicTo(w * 0.48f, h * 0.9f, w * 0.35f, h * 0.8f, w * 0.15f, h * 0.85f)
        close()
    }

    if (isFilled) {
        drawPath(path, color = color)
        drawLine(
            color = Color.White.copy(alpha = 0.5f),
            start = Offset(w * 0.5f, h * 0.35f),
            end = Offset(w * 0.5f, h * 0.85f),
            strokeWidth = 2f
        )
    } else {
        drawPath(path, color = color, style = Stroke(width = 2.2f))
        drawLine(
            color = color,
            start = Offset(w * 0.5f, h * 0.35f),
            end = Offset(w * 0.5f, h * 0.85f),
            strokeWidth = 2.2f
        )
    }
}

private fun DrawScope.drawShieldNavVector(color: Color, isFilled: Boolean) {
    val w = size.width
    val h = size.height

    val path = Path().apply {
        moveTo(w * 0.5f, h * 0.12f)
        lineTo(w * 0.85f, h * 0.28f)
        cubicTo(w * 0.85f, h * 0.65f, w * 0.65f, h * 0.85f, w * 0.5f, h * 0.92f)
        cubicTo(w * 0.35f, h * 0.85f, w * 0.15f, h * 0.65f, w * 0.15f, h * 0.28f)
        close()
    }

    if (isFilled) {
        drawPath(path, color = color)
    } else {
        drawPath(path, color = color, style = Stroke(width = 2.2f))
    }
}

private fun DrawScope.drawSkillsBrainSlidersVector(color: Color, isFilled: Boolean) {
    val w = size.width
    val h = size.height

    // Custom 3 horizontal tuning sliders with active indicator knobs
    val strokeWidth = 2.2f

    // Line 1
    drawLine(color = color, start = Offset(w * 0.15f, h * 0.28f), end = Offset(w * 0.85f, h * 0.28f), strokeWidth = strokeWidth)
    drawCircle(color = color, radius = if (isFilled) 3.5f else 3f, center = Offset(w * 0.38f, h * 0.28f))

    // Line 2
    drawLine(color = color, start = Offset(w * 0.15f, h * 0.52f), end = Offset(w * 0.85f, h * 0.52f), strokeWidth = strokeWidth)
    drawCircle(color = color, radius = if (isFilled) 3.5f else 3f, center = Offset(w * 0.68f, h * 0.52f))

    // Line 3
    drawLine(color = color, start = Offset(w * 0.15f, h * 0.76f), end = Offset(w * 0.85f, h * 0.76f), strokeWidth = strokeWidth)
    drawCircle(color = color, radius = if (isFilled) 3.5f else 3f, center = Offset(w * 0.42f, h * 0.76f))
}
