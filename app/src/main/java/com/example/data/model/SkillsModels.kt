package com.example.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class BrainProfile(
    val level: Int = 4,
    val archetype: String = "The Tactical Empathetic",
    val nextUnlock: String = "The Spotless Boundary",
    val socialLeverageProgress: Float = 0.78f,
    val cognitiveDefenseProgress: Float = 0.55f,
    val dopamineControlProgress: Float = 0.42f
)

@Immutable
data class VibeMode(
    val id: String,
    val title: String,
    val description: String,
    val customAssetResId: Int // Abstract illustration representing State
)

@Immutable
data class MirrorDeckCard(
    val index: Int,
    val total: Int = 50,
    val tag: String,
    val storyQuote: String,
    val hiddenCause: String,
    val targetSkill: String
)

@Immutable
enum class SkillState { MASTERED, CRACKING }

@Immutable
data class MasteredSkill(
    val id: String,
    val title: String,
    val state: SkillState,
    val labelText: String
)
