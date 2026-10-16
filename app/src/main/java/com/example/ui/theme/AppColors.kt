package com.example.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Central Theme Color Tokens for ONE THING background gradients, glows, and surfaces.
 * Matches both dark (#0B0B10 Deep Obsidian) and light (#FFF8EC Warm Cream) mockup specifications.
 */
object AppColors {
    // Dark Theme Tokens
    object Dark {
        val base = Color(0xFF0B0B10)
        val flatBase = Color(0xFF111114)
        val surfaceGlowPink = Color(0xFFFF5FA2)
        val surfaceGlowPurple = Color(0xFF8B5CF6)
        val surfaceGlowBlue = Color(0xFF3B82F6)
        val accentOrange = Color(0xFFFF6B2C)
    }

    // Light Theme Tokens
    object Light {
        val base = Color(0xFFFFF8EC)
        val surfaceGlowPink = Color(0xFFF9C6E4)
        val surfaceGlowLavender = Color(0xFFD8C9FF)
        val surfaceGlowSky = Color(0xFFC7DCFF)
        val surfaceGlowPeach = Color(0xFFFFD9B8)
    }

    // Shared Tokens
    val solidCreamTop = Color(0xFFFFF4E6)
    val solidCreamBottom = Color(0xFFFFEBD2)
}
