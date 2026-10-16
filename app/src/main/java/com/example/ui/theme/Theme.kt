package com.example.ui.theme

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

@Immutable
data class OneThingColors(
    val background: Color,
    val surface: Color,
    val secondarySurface: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val cardBorder: Color,
    val primaryAccent: Color,
    val secondaryAccent: Color,
    val success: Color,
    val badgePink: Color,
    val isDark: Boolean,
    val archetypeCardBgStart: Color = if (isDark) Color(0xFF2A2736) else Color(0xFFFFFFFF),
    val archetypeCardBgEnd: Color = if (isDark) Color(0xFF1B1924) else Color(0xFFF3EFFA),
    val archetypeCardBorder: Color = if (isDark) Color(0xFF49455E) else Color(0xFFDCD4E8),
    val vibeCardInactiveBg: Color = if (isDark) Color(0xFF181722) else Color(0xFFFFFFFF),
    val vibeCardInactiveBorder: Color = if (isDark) Color(0xFF2E2C40) else Color(0xFFE2DCD0),
    val vibeCardActiveBgStart: Color = if (isDark) Color(0xFF26181A) else Color(0xFFFF7A4D),
    val vibeCardActiveBgEnd: Color = if (isDark) Color(0xFF26181A) else Color(0xFFFF5722),
    val vibeCardActiveBorder: Color = if (isDark) Color(0xFFFF7043) else Color(0xFFFF9E80),
    val mirrorCardBgStart: Color = if (isDark) Color(0xFFEAE4F8) else Color(0xFFF4F0FF),
    val mirrorCardBgEnd: Color = if (isDark) Color(0xFFDCD2F4) else Color(0xFFE9E1FC),
    val mirrorCardBorder: Color = Color(0xFFC7B6F4),
    val vaultCrackingCardBg: Color = Color(0xFFFFE747),
    val vaultCrackingText: Color = Color(0xFF191919),
    val vaultLockedCardBg: Color = if (isDark) Color(0xFF1C1B26) else Color(0xFFECE6F8),
    val vaultLockedBorder: Color = if (isDark) Color(0xFF2E2C3E) else Color(0xFFD5C8F0),
    val bottomBarBg: Color = if (isDark) Color(0xFF121218) else Color(0xFFF7F2E7),
    val bottomBarBorder: Color = if (isDark) Color(0xFF262632) else Color(0xFFDCD4E8)
)

val LightOneThingColors = OneThingColors(
    background = AppColors.Light.base, // #FFF8EC Warm Cream
    surface = Color(0xFFFFFFFF),
    secondarySurface = Color(0xFFECE6F8),
    primaryText = Color(0xFF191919),
    secondaryText = Color(0xFF656059),
    cardBorder = Color(0xFFD8D0C5),
    primaryAccent = Color(0xFFFF7043),
    secondaryAccent = Color(0xFFC7B6F4),
    success = Color(0xFFB9E769),
    badgePink = Color(0xFFF47BC4),
    isDark = false
)

val DarkOneThingColors = OneThingColors(
    background = AppColors.Dark.base, // #0B0B10 Deep Obsidian
    surface = Color(0xFF16161E),
    secondarySurface = Color(0xFF1C1B26),
    primaryText = Color(0xFFFFFFFF),
    secondaryText = Color(0xFFA09EB0),
    cardBorder = Color(0xFF2E2E3E),
    primaryAccent = Color(0xFFFF7043),
    secondaryAccent = Color(0xFF8B5CF6),
    success = Color(0xFFB9E769),
    badgePink = Color(0xFFF47BC4),
    isDark = true
)

val LocalOneThingColors = staticCompositionLocalOf { LightOneThingColors }
val LocalThemeMode = staticCompositionLocalOf { ThemeMode.SYSTEM }

private val LightM3ColorScheme = lightColorScheme(
    primary = Color(0xFFFF7043),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFF1EB),
    onPrimaryContainer = Color(0xFF191919),
    secondary = Color(0xFFC7B6F4),
    onSecondary = Color(0xFF191919),
    secondaryContainer = Color(0xFFF3E8FF),
    onSecondaryContainer = Color(0xFF191919),
    tertiary = Color(0xFFF47BC4),
    onTertiary = Color.White,
    background = AppColors.Light.base,
    onBackground = Color(0xFF191919),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF191919),
    surfaceVariant = Color(0xFFECE6F8),
    onSurfaceVariant = Color(0xFF191919),
    outline = Color(0xFFD8D0C5),
    outlineVariant = Color(0xFFE2DCD0)
)

private val DarkM3ColorScheme = darkColorScheme(
    primary = Color(0xFFFF7043),
    onPrimary = Color(0xFF16161E),
    primaryContainer = Color(0xFF2D1810),
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondary = Color(0xFF8B5CF6),
    onSecondary = Color(0xFF16161E),
    secondaryContainer = Color(0xFF262038),
    onSecondaryContainer = Color(0xFFFFFFFF),
    tertiary = Color(0xFFF47BC4),
    onTertiary = Color(0xFF16161E),
    background = AppColors.Dark.base,
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF16161E),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF1C1B26),
    onSurfaceVariant = Color(0xFFFFFFFF),
    outline = Color(0xFF2E2E3E),
    outlineVariant = Color(0xFF38384A)
)

object ThemePreferences {
    private const val PREFS_NAME = "one_thing_theme_prefs"
    private const val KEY_THEME_MODE = "theme_mode"

    fun getThemeMode(context: Context): ThemeMode {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val modeStr = prefs.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name
        return try {
            ThemeMode.valueOf(modeStr)
        } catch (e: Exception) {
            ThemeMode.SYSTEM
        }
    }

    fun setThemeMode(context: Context, mode: ThemeMode) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_THEME_MODE, mode.name).apply()
    }
}

@Composable
fun MyApplicationTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val oneThingColors = if (isDark) DarkOneThingColors else LightOneThingColors
    val m3Colors = if (isDark) DarkM3ColorScheme else LightM3ColorScheme

    val view = androidx.compose.ui.platform.LocalView.current
    if (!view.isInEditMode) {
        androidx.compose.runtime.SideEffect {
            val window = (view.context as? android.app.Activity)?.window
            if (window != null) {
                val insetsController = androidx.core.view.WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !isDark
                insetsController.isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    CompositionLocalProvider(
        LocalOneThingColors provides oneThingColors,
        LocalThemeMode provides themeMode
    ) {
        MaterialTheme(
            colorScheme = m3Colors,
            typography = Typography,
            content = content
        )
    }
}
