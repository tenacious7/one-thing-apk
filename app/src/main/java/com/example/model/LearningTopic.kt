package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AcidLime
import com.example.ui.theme.ElectricYellow
import com.example.ui.theme.HotPink
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.TangerineOrange

enum class ArtShapeType {
    ORGANIC_BLOBS,
    SUNBURST_RAYS,
    CONCENTRIC_RINGS,
    DIAGONAL_STRIPES,
    GEOMETRIC_PILL,
    RETRO_ARCH
}

data class ComprehensionQuestion(
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String
)

data class LearningTopic(
    val id: String,
    val title: String,
    val category: String,
    val oneLineDescription: String,
    val whyItMatters: String,
    val estimatedLearningTimeMinutes: Int = 12,
    val keyConcepts: List<String>,
    val practicalTaskTitle: String,
    val practicalTaskInstruction: String,
    val reflectionPrompt: String,
    val comprehensionQuestion: ComprehensionQuestion,
    val primaryColor: Color = TangerineOrange,
    val secondaryColor: Color = ElectricYellow,
    val shapeType: ArtShapeType = ArtShapeType.ORGANIC_BLOBS
)

data class UserProgress(
    val todayDateString: String,
    val candidateTopicIds: List<String>,
    val selectedTopicId: String?,
    val isCompletedToday: Boolean,
    val reflectionText: String,
    val streakCount: Int,
    val totalCompleted: Int,
    val previouslyCompletedTopicIds: List<String> = emptyList()
)

enum class ScreenState {
    HOME_LIST,
    STUDY_DETAIL,
    QUIZ_VERIFICATION,
    COMPLETED_LOCKED,
    SKILL_SETTINGS,
    DISTRACTION_SHIELD,
    PROFILE_SETTINGS
}
