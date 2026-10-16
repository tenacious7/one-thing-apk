package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.model.ArtShapeType
import com.example.ui.theme.AcidLime
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.ElectricYellow
import com.example.ui.theme.HotPink
import com.example.ui.theme.InkBlack
import com.example.ui.theme.PaperWhite
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.TangerineOrange
import com.example.ui.theme.WarmCream

/**
 * Procedural generative editorial artwork canvas inspired by contemporary art magazines and analog risograph prints.
 */
@Composable
fun EditorialArtwork(
    shapeType: ArtShapeType,
    primaryColor: Color,
    secondaryColor: Color,
    modifier: Modifier = Modifier,
    height: Dp = 140.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(14.dp))
            .background(WarmCream)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Rich grainy gradient base
            val gradientBrush = Brush.linearGradient(
                colors = listOf(
                    primaryColor.copy(alpha = 0.95f),
                    secondaryColor.copy(alpha = 0.85f),
                    WarmCream
                ),
                start = Offset(0f, 0f),
                end = Offset(w, h)
            )
            drawRect(brush = gradientBrush)

            // 2. Shape-specific organic / constructivist art composition
            when (shapeType) {
                ArtShapeType.ORGANIC_BLOBS -> {
                    // Fluid flowing Bezier blob 1
                    val blobPath1 = Path().apply {
                        moveTo(w * 0.15f, 0f)
                        cubicTo(w * 0.45f, h * 0.1f, w * 0.35f, h * 0.75f, w * 0.75f, h * 0.65f)
                        cubicTo(w * 0.95f, h * 0.6f, w * 0.9f, h * 0.95f, w, h * 0.9f)
                        lineTo(w, 0f)
                        close()
                    }
                    drawPath(blobPath1, color = primaryColor)

                    // Overlapping secondary blob
                    val blobPath2 = Path().apply {
                        moveTo(0f, h * 0.4f)
                        cubicTo(w * 0.25f, h * 0.3f, w * 0.4f, h * 0.9f, w * 0.2f, h)
                        lineTo(0f, h)
                        close()
                    }
                    drawPath(blobPath2, color = ElectricYellow.copy(alpha = 0.75f))

                    // Floating contrast accent circle
                    drawCircle(
                        color = InkBlack,
                        radius = h * 0.16f,
                        center = Offset(w * 0.78f, h * 0.38f)
                    )
                    drawCircle(
                        color = PaperWhite,
                        radius = h * 0.06f,
                        center = Offset(w * 0.78f, h * 0.38f)
                    )
                }

                ArtShapeType.SUNBURST_RAYS -> {
                    // Geometric radiating lines & semicircles
                    val centerSun = Offset(w * 0.5f, h * 1.05f)
                    val maxRadius = h * 1.1f

                    drawCircle(
                        color = primaryColor,
                        radius = maxRadius,
                        center = centerSun
                    )
                    drawCircle(
                        color = secondaryColor,
                        radius = maxRadius * 0.7f,
                        center = centerSun
                    )
                    drawCircle(
                        color = InkBlack,
                        radius = maxRadius * 0.4f,
                        center = centerSun
                    )
                    drawCircle(
                        color = ElectricYellow,
                        radius = maxRadius * 0.2f,
                        center = centerSun
                    )

                    // Dynamic diagonal ray lines
                    for (i in -4..4) {
                        val angleOffset = i * 22f
                        val rayEnd = Offset(w * 0.5f + angleOffset * 10f, 0f)
                        drawLine(
                            color = PaperWhite.copy(alpha = 0.4f),
                            start = centerSun,
                            end = rayEnd,
                            strokeWidth = 3.dp.toPx()
                        )
                    }
                }

                ArtShapeType.CONCENTRIC_RINGS -> {
                    // Hypnotic concentric rings with offset center
                    val ringCenter = Offset(w * 0.7f, h * 0.5f)
                    val radii = listOf(h * 0.75f, h * 0.55f, h * 0.38f, h * 0.22f, h * 0.1f)
                    val ringColors = listOf(secondaryColor, primaryColor, HotPink, AcidLime, InkBlack)

                    radii.forEachIndexed { index, r ->
                        drawCircle(
                            color = ringColors[index % ringColors.size],
                            radius = r,
                            center = ringCenter,
                            style = Stroke(width = 6.dp.toPx())
                        )
                    }

                    // Contrast blocking strip
                    drawRect(
                        color = TangerineOrange.copy(alpha = 0.85f),
                        topLeft = Offset(0f, h * 0.25f),
                        size = Size(w * 0.45f, h * 0.5f)
                    )
                    drawCircle(
                        color = PaperWhite,
                        radius = h * 0.12f,
                        center = Offset(w * 0.22f, h * 0.5f)
                    )
                }

                ArtShapeType.DIAGONAL_STRIPES -> {
                    // Constructivist high-contrast geometric bands
                    val stripeWidth = w * 0.2f
                    for (i in -2..6) {
                        val startX = i * stripeWidth * 1.2f
                        val stripePath = Path().apply {
                            moveTo(startX, 0f)
                            lineTo(startX + stripeWidth, 0f)
                            lineTo(startX + stripeWidth - h * 0.5f, h)
                            lineTo(startX - h * 0.5f, h)
                            close()
                        }
                        val stripeColor = if (i % 2 == 0) primaryColor else secondaryColor
                        drawPath(stripePath, color = stripeColor.copy(alpha = 0.9f))
                    }

                    // Solid bold editorial disc
                    drawCircle(
                        color = InkBlack,
                        radius = h * 0.25f,
                        center = Offset(w * 0.8f, h * 0.5f)
                    )
                    drawCircle(
                        color = ElectricYellow,
                        radius = h * 0.1f,
                        center = Offset(w * 0.8f, h * 0.5f)
                    )
                }

                ArtShapeType.GEOMETRIC_PILL -> {
                    // Pill badges and organic shapes
                    drawRoundRect(
                        color = primaryColor,
                        topLeft = Offset(w * 0.1f, h * 0.2f),
                        size = Size(w * 0.5f, h * 0.6f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(h * 0.3f, h * 0.3f)
                    )

                    drawCircle(
                        color = secondaryColor,
                        radius = h * 0.35f,
                        center = Offset(w * 0.75f, h * 0.5f)
                    )

                    drawCircle(
                        color = InkBlack,
                        radius = h * 0.15f,
                        center = Offset(w * 0.75f, h * 0.5f)
                    )
                }

                ArtShapeType.RETRO_ARCH -> {
                    // Monumental archway with warm gradient fill
                    val archPath = Path().apply {
                        moveTo(w * 0.3f, h)
                        lineTo(w * 0.3f, h * 0.35f)
                        cubicTo(w * 0.3f, h * 0.05f, w * 0.7f, h * 0.05f, w * 0.7f, h * 0.35f)
                        lineTo(w * 0.7f, h)
                        close()
                    }
                    drawPath(archPath, color = primaryColor)

                    drawCircle(
                        color = ElectricYellow,
                        radius = h * 0.2f,
                        center = Offset(w * 0.5f, h * 0.35f)
                    )
                    drawCircle(
                        color = InkBlack,
                        radius = h * 0.08f,
                        center = Offset(w * 0.5f, h * 0.35f)
                    )
                }
            }

            // 3. Subtle analog print texture dots grid
            val dotSpacing = 28.dp.toPx()
            var x = dotSpacing / 2
            while (x < w) {
                var y = dotSpacing / 2
                while (y < h) {
                    drawCircle(
                        color = DarkCharcoal.copy(alpha = 0.08f),
                        radius = 1.2.dp.toPx(),
                        center = Offset(x, y)
                    )
                    y += dotSpacing
                }
                x += dotSpacing
            }

            // 4. Fine outer graphic frame border
            drawRect(
                color = InkBlack.copy(alpha = 0.15f),
                style = Stroke(width = 1.dp.toPx())
            )
        }
    }
}
