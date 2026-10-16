package com.example.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SkillsHubScreen(
    modifier: Modifier = Modifier,
    onMakeTodayOneThing: (String) -> Unit = {},
    onAddDopamineMinutes: (Int) -> Unit = {},
    onClose: (() -> Unit)? = null
) {
    SkillsScreen(
        modifier = modifier,
        onMakeTodayOneThing = onMakeTodayOneThing,
        onAddDopamineMinutes = onAddDopamineMinutes,
        onClose = onClose
    )
}
