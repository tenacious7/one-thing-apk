package com.example.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.AppColors
import com.example.ui.theme.LocalOneThingColors
import kotlin.random.Random

/**
 * Standardized background variants matching the ONE THING editorial design system.
 */
enum class BackgroundVariant {
    HEADER_GLOW,
    SOFT_GLOW,
    CENTER_BLOB,
    SOLID_CREAM,
    FLAT
}

@Immutable
private data class NoiseDot(
    val normalizedX: Float,
    val normalizedY: Float,
    val radiusPx: Float,
    val alphaFraction: Float
)

/**
 * Reusable full-screen background wrapper supporting both Light and Dark theme gradient glows,
 * procedural film grain texture, and edge-to-edge display.
 */
@Composable
fun AppBackground(
    variant: BackgroundVariant,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val colors = LocalOneThingColors.current
    val isDark = colors.isDark

    // Procedural cached film-grain noise dots (3-4.5% opacity) to eliminate color banding (Step 7)
    val noiseDots = remember {
        val rand = Random(42)
        List(180) {
            NoiseDot(
                normalizedX = rand.nextFloat(),
                normalizedY = rand.nextFloat(),
                radiusPx = 1.0f + rand.nextFloat() * 1.5f,
                alphaFraction = 0.03f + rand.nextFloat() * 0.015f
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                when (variant) {
                    BackgroundVariant.HEADER_GLOW -> drawHeaderGlowBackground(isDark)
                    BackgroundVariant.SOFT_GLOW -> drawSoftGlowBackground(isDark)
                    BackgroundVariant.CENTER_BLOB -> drawCenterBlobBackground(isDark)
                    BackgroundVariant.SOLID_CREAM -> drawSolidCreamBackground()
                    BackgroundVariant.FLAT -> drawFlatBackground(isDark)
                }

                // Film-grain texture overlay (Step 7)
                drawNoiseOverlay(noiseDots, isDark, variant)
            }
    ) {
        content()
    }
}

/**
 * HEADER_GLOW (Step 2):
 * Used for Skills Hub / Brain Profile.
 * Dark: Base #0B0B10. A vertical gradient covering the top ~35% of the screen:
 * pink #FF5FA2 (top-left) -> purple #8B5CF6 (middle) -> blue #3B82F6 (top-right),
 * at about 85% strength at the very top, fading smoothly to fully transparent at 35% height.
 * Must extend behind the status bar (edge-to-edge).
 * Light: Base #FFF8EC. Same shape, using pastel pink #F9C6E4 -> lavender #D8C9FF -> sky #C7DCFF,
 * fading to the cream base.
 */
private fun DrawScope.drawHeaderGlowBackground(isDark: Boolean) {
    val w = size.width
    val h = size.height
    val topGlowHeight = h * 0.35f

    val base = if (isDark) AppColors.Dark.base else AppColors.Light.base
    val pink = if (isDark) AppColors.Dark.surfaceGlowPink else AppColors.Light.surfaceGlowPink
    val purple = if (isDark) AppColors.Dark.surfaceGlowPurple else AppColors.Light.surfaceGlowLavender
    val blue = if (isDark) AppColors.Dark.surfaceGlowBlue else AppColors.Light.surfaceGlowSky

    // 1. Full-screen base
    drawRect(color = base)

    // 2. Top-edge color glow zones at 85% strength
    // Top-left Pink
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                pink.copy(alpha = 0.85f),
                pink.copy(alpha = 0.45f),
                Color.Transparent
            ),
            center = Offset(0f, 0f),
            radius = w * 0.75f
        )
    )

    // Top-middle Purple
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                purple.copy(alpha = 0.85f),
                purple.copy(alpha = 0.50f),
                Color.Transparent
            ),
            center = Offset(w * 0.50f, 0f),
            radius = w * 0.85f
        )
    )

    // Top-right Blue / Sky
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                blue.copy(alpha = 0.85f),
                blue.copy(alpha = 0.45f),
                Color.Transparent
            ),
            center = Offset(w, 0f),
            radius = w * 0.75f
        )
    )

    // 3. Smooth vertical fade to base color, reaching 100% base at 35% height
    drawRect(
        brush = Brush.verticalGradient(
            0.0f to Color.Transparent,
            0.20f to Color.Transparent,
            0.60f to base.copy(alpha = 0.45f),
            1.0f to base,
            startY = 0f,
            endY = topGlowHeight
        ),
        topLeft = Offset(0f, 0f),
        size = Size(w, topGlowHeight)
    )

    // Below 35% height: ensure 100% clean base
    drawRect(
        color = base,
        topLeft = Offset(0f, topGlowHeight),
        size = Size(w, h - topGlowHeight)
    )
}

/**
 * SOFT_GLOW (Step 3):
 * Used for Discovery Hub / Mirror Deck and Today / ONE THING home.
 * Dark: Base #0B0B10 with one large radial purple glow (#8B5CF6 at ~35% alpha, radius ~70% of
 * screen width) centered behind the main card area, plus a smaller pink radial glow
 * (#FF5FA2 at ~20% alpha) in the top-left corner. Everything else stays dark.
 * Light: Base #FFF8EC with a large radial lavender glow (#D8C9FF at ~60% alpha) behind the main card
 * and a faint peach glow (#FFD9B8 at ~50% alpha) in the top-left corner.
 */
private fun DrawScope.drawSoftGlowBackground(isDark: Boolean) {
    val w = size.width
    val h = size.height

    if (isDark) {
        // Base #0B0B10
        drawRect(color = AppColors.Dark.base)

        // Top-left pink radial glow (#FF5FA2 at ~20% alpha)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    AppColors.Dark.surfaceGlowPink.copy(alpha = 0.20f),
                    AppColors.Dark.surfaceGlowPink.copy(alpha = 0.08f),
                    Color.Transparent
                ),
                center = Offset(w * 0.12f, h * 0.12f),
                radius = w * 0.50f
            )
        )

        // Centered large radial purple glow (#8B5CF6 at ~35% alpha, radius ~70% screen width)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    AppColors.Dark.surfaceGlowPurple.copy(alpha = 0.35f),
                    AppColors.Dark.surfaceGlowPurple.copy(alpha = 0.15f),
                    Color.Transparent
                ),
                center = Offset(w * 0.50f, h * 0.44f),
                radius = w * 0.70f
            )
        )
    } else {
        // Base #FFF8EC
        drawRect(color = AppColors.Light.base)

        // Top-left faint peach glow (#FFD9B8 at ~50% alpha)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    AppColors.Light.surfaceGlowPeach.copy(alpha = 0.50f),
                    AppColors.Light.surfaceGlowPeach.copy(alpha = 0.20f),
                    Color.Transparent
                ),
                center = Offset(w * 0.12f, h * 0.12f),
                radius = w * 0.50f
            )
        )

        // Centered large radial lavender glow (#D8C9FF at ~60% alpha)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    AppColors.Light.surfaceGlowLavender.copy(alpha = 0.60f),
                    AppColors.Light.surfaceGlowLavender.copy(alpha = 0.25f),
                    Color.Transparent
                ),
                center = Offset(w * 0.50f, h * 0.44f),
                radius = w * 0.70f
            )
        )
    }
}

/**
 * CENTER_BLOB (Step 4):
 * Used for the Brain Coach "Custom Skill Forged" overlay.
 * Dark: Dark scrim #0B0B10 at 92% opacity. Behind the center card, a large blurred radial blob:
 * pink -> purple -> transparent, about 80% of screen width, centered slightly above middle.
 * Light: Cream scrim #FFF8EC at 92% opacity. Same blob using pastel pink -> lavender -> peach.
 */
private fun DrawScope.drawCenterBlobBackground(isDark: Boolean) {
    val w = size.width
    val h = size.height

    if (isDark) {
        // Dark scrim #0B0B10 at 92% opacity
        drawRect(color = AppColors.Dark.base.copy(alpha = 0.92f))

        // Large radial blob: pink -> purple -> transparent (~80% screen width, slightly above middle)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    AppColors.Dark.surfaceGlowPink.copy(alpha = 0.65f),
                    AppColors.Dark.surfaceGlowPurple.copy(alpha = 0.45f),
                    Color.Transparent
                ),
                center = Offset(w * 0.50f, h * 0.38f),
                radius = w * 0.80f
            )
        )
    } else {
        // Cream scrim #FFF8EC at 92% opacity
        drawRect(color = AppColors.Light.base.copy(alpha = 0.92f))

        // Pastel pink -> lavender -> peach radial blob
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    AppColors.Light.surfaceGlowPink.copy(alpha = 0.70f),
                    AppColors.Light.surfaceGlowLavender.copy(alpha = 0.48f),
                    AppColors.Light.surfaceGlowPeach.copy(alpha = 0.25f),
                    Color.Transparent
                ),
                center = Offset(w * 0.50f, h * 0.38f),
                radius = w * 0.80f
            )
        )
    }
}

/**
 * SOLID_CREAM (Step 5):
 * Used for the Memory Lock-In / Vault Recall quiz screen.
 * Use warm cream #FFF4E6 in BOTH light and dark themes (the mockup keeps this screen light
 * on purpose so it feels focused). Add a very subtle top-to-bottom gradient to #FFEBD2 at the bottom.
 */
private fun DrawScope.drawSolidCreamBackground() {
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                AppColors.solidCreamTop,    // #FFF4E6
                AppColors.solidCreamBottom  // #FFEBD2
            )
        )
    )
}

/**
 * FLAT (Step 6):
 * Used for Shield / Focus Guard and Dopamine Bank, and Profile / Settings.
 * Dark: Base #111114, plus a very faint pink-purple glow (10-12% alpha) at the top 15% only.
 * Light: Base #FFF8EC, plus a very faint lavender glow (30% alpha) at the top 15% only.
 */
private fun DrawScope.drawFlatBackground(isDark: Boolean) {
    val w = size.width
    val h = size.height

    if (isDark) {
        // Base #111114
        drawRect(color = AppColors.Dark.flatBase)

        // Faint pink-purple glow (10-12% alpha) at top 15% only
        drawRect(
            brush = Brush.verticalGradient(
                0.0f to AppColors.Dark.surfaceGlowPurple.copy(alpha = 0.12f),
                1.0f to Color.Transparent,
                startY = 0f,
                endY = h * 0.15f
            ),
            topLeft = Offset(0f, 0f),
            size = Size(w, h * 0.15f)
        )
    } else {
        // Base #FFF8EC
        drawRect(color = AppColors.Light.base)

        // Faint lavender glow (30% alpha) at top 15% only
        drawRect(
            brush = Brush.verticalGradient(
                0.0f to AppColors.Light.surfaceGlowLavender.copy(alpha = 0.30f),
                1.0f to Color.Transparent,
                startY = 0f,
                endY = h * 0.15f
            ),
            topLeft = Offset(0f, 0f),
            size = Size(w, h * 0.15f)
        )
    }
}

/**
 * Subtle film-grain/noise overlay at 3-4.5% opacity on top of all gradients (Step 7).
 */
private fun DrawScope.drawNoiseOverlay(dots: List<NoiseDot>, isDark: Boolean, variant: BackgroundVariant) {
    val w = size.width
    val h = size.height
    val dotColor = if (isDark && variant != BackgroundVariant.SOLID_CREAM) Color.White else Color(0xFF191919)

    for (dot in dots) {
        drawCircle(
            color = dotColor.copy(alpha = dot.alphaFraction),
            radius = dot.radiusPx,
            center = Offset(dot.normalizedX * w, dot.normalizedY * h)
        )
    }
}
