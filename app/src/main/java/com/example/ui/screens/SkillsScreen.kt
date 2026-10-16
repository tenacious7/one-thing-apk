package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.AcidLime
import com.example.ui.theme.ElectricYellow
import com.example.ui.theme.HotPink
import com.example.ui.theme.InkBlack
import com.example.ui.theme.LocalOneThingColors
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.TangerineOrange
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// ==========================================
// 1. DATA MODELS
// ==========================================

@Immutable
data class BrainProfileState(
    val level: Int = 4,
    val archetypeTitle: String = "The Tactical Empathetic",
    val nextUnlockTitle: String = "The Spotless Boundary",
    val lessonsUntilUnlock: Int = 2,
    val socialLeverage: Float = 0.78f,
    val cognitiveDefense: Float = 0.55f,
    val dopamineControl: Float = 0.42f,
    val isFlipped: Boolean = false,
    val reelsBlockedWeekly: Int = 14,
    val minutesSavedWeekly: Int = 45
)

@Immutable
data class VibeCategory(
    val id: String,
    val title: String,
    val subtitle: String
)

@Immutable
data class MirrorCardItem(
    val id: Int,
    val cardNumber: Int,
    val totalCards: Int = 50,
    val categoryTag: String,
    val relatableQuote: String,
    val hiddenMechanism: String,
    val mappedSkillTitle: String,
    val mappedSkillPromise: String
)

@Immutable
data class VaultSkillItem(
    val id: Int,
    val title: String,
    val isCracking: Boolean,
    val daysAgoLabel: String,
    val quizQuestion: String,
    val quizOptions: List<String>,
    val correctOptionIndex: Int
)

@Immutable
data class ForgedCustomSkill(
    val categoryPill: String,
    val title: String,
    val description: String,
    val readTimeSeconds: Int = 90
)

// ==========================================
// 2. TACTILE BOUNCE MODIFIER
// ==========================================

fun Modifier.tactileBounce(
    scaleDown: Float = 0.94f,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed) scaleDown else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "tactile_bounce_scale"
    )

    LaunchedEffect(isPressed) {
        if (isPressed) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
}

// ==========================================
// 3. MAIN LIVING BRAIN PROFILE SCREEN
// ==========================================

@Composable
fun SkillsScreen(
    modifier: Modifier = Modifier,
    onMakeTodayOneThing: (String) -> Unit = {},
    onAddDopamineMinutes: (Int) -> Unit = {},
    onClose: (() -> Unit)? = null
) {
    val colors = LocalOneThingColors.current
    var profileState by remember { mutableStateOf(BrainProfileState()) }

    val vibes = remember {
        listOf(
            VibeCategory("sharper", "MAKE ME SHARPER", "Cognitive clarity & rapid discernment"),
            VibeCategory("social", "UNFAIR SOCIAL ADVANTAGE", "Tactical empathy & conversational frame"),
            VibeCategory("unmanipulatable", "MAKE ME UNMANIPULATABLE", "Dark psychology counters & boundaries"),
            VibeCategory("focus", "FIX MY BROKEN FOCUS", "Dopamine resets & deep impulse shields")
        )
    }
    var selectedVibeId by remember { mutableStateOf("social") }
    var vibeToastMessage by remember { mutableStateOf<String?>(null) }

    val initialMirrorDeck = remember {
        listOf(
            MirrorCardItem(
                id = 1,
                cardNumber = 14,
                totalCards = 50,
                categoryTag = "SOCIAL RETORT DEFENSE",
                relatableQuote = "\"I always think of the sharpest, most devastating comeback 3 hours AFTER the argument is already over.\"",
                hiddenMechanism = "Hidden Cause: Amygdala Hijack & Staircase Wit",
                mappedSkillTitle = "The 3-Second Delayed Pivot",
                mappedSkillPromise = "Disarm hostile statements before fight-or-flight paralyzes your vocal cortex."
            ),
            MirrorCardItem(
                id = 2,
                cardNumber = 15,
                totalCards = 50,
                categoryTag = "DOPAMINE LEAK CONTROL",
                relatableQuote = "\"I opened my phone just to check the weather, and suddenly 40 minutes evaporated in a trance.\"",
                hiddenMechanism = "Hidden Cause: Variable Reward Slot Machine Loop",
                mappedSkillTitle = "The Friction Wall Ritual",
                mappedSkillPromise = "Place a deliberate 5-breath pause between stimulus and auto-unlock behavior."
            ),
            MirrorCardItem(
                id = 3,
                cardNumber = 16,
                totalCards = 50,
                categoryTag = "COGNITIVE BOUNDARY",
                relatableQuote = "\"I say 'yes' instantly to requests because saying 'no' feels like I am physically burning someone.\"",
                hiddenMechanism = "Hidden Cause: Fawn Response & Approval Conditioning",
                mappedSkillTitle = "The Spotless Boundary",
                mappedSkillPromise = "Decline requests politely with zero justifications or fabricated excuses."
            )
        )
    }

    val mirrorDeck by remember { mutableStateOf(initialMirrorDeck) }
    var currentMirrorIndex by remember { mutableIntStateOf(0) }
    var hasPriorityTodayDot by remember { mutableStateOf(false) }

    var ventInputText by remember { mutableStateOf("") }
    var forgedSkillOverlayItem by remember { mutableStateOf<ForgedCustomSkill?>(null) }

    var vaultSkills by remember {
        mutableStateOf(
            listOf(
                VaultSkillItem(
                    id = 101,
                    title = "The Ben Franklin Effect",
                    isCracking = true,
                    daysAgoLabel = "CRACKING (6d ago)",
                    quizQuestion = "Your neighbor gives you a cold look in the hallway. How do you trigger the Ben Franklin Effect?",
                    quizOptions = listOf(
                        "Give them an unsolicited compliment on their outfit",
                        "Ask to borrow a screwdriver or small tool for 5 minutes",
                        "Ignore them completely to establish emotional dominance"
                    ),
                    correctOptionIndex = 1
                ),
                VaultSkillItem(
                    id = 102,
                    title = "The Labeling Anchor",
                    isCracking = false,
                    daysAgoLabel = "LOCKED (2d ago)",
                    quizQuestion = "When someone is visibly agitated in negotiation, what is the best labeling response?",
                    quizOptions = listOf(
                        "It seems like you feel completely unheard right now.",
                        "Why are you acting so angry about this?",
                        "Calm down and let's look at the hard facts."
                    ),
                    correctOptionIndex = 0
                ),
                VaultSkillItem(
                    id = 103,
                    title = "Micro-Friction Intercept",
                    isCracking = false,
                    daysAgoLabel = "LOCKED (3d ago)",
                    quizQuestion = "What is the primary psychological purpose of a 10-second app launch friction gate?",
                    quizOptions = listOf(
                        "Break the subcortical autopilot habit loop",
                        "Punish yourself for wanting entertainment",
                        "Drain battery so the phone is put away"
                    ),
                    correctOptionIndex = 0
                )
            )
        )
    }

    var activeRecallModalItem by remember { mutableStateOf<VaultSkillItem?>(null) }

    com.example.ui.components.AppBackground(
        variant = com.example.ui.components.BackgroundVariant.HEADER_GLOW,
        modifier = modifier
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            // COMPONENT 1: TOP STATUS HEADER WITH AMBIENT GLOW
            item(key = "section_top_status_header") {
                TopStatusHeader(
                    shieldActive = true,
                    streakDays = 4,
                    onClose = onClose
                )
            }

            // COMPONENT 2: ARCHETYPE HERO CARD
            item(key = "section_archetype_hero_card") {
                ArchetypeHeroCard(
                    profileState = profileState,
                    onToggleFlip = {
                        profileState = profileState.copy(isFlipped = !profileState.isFlipped)
                    }
                )
            }

            // COMPONENT 3: TUNE TODAY'S VIBE 2x2 GRID
            item(key = "section_tune_todays_vibe_grid") {
                TuneTodaysVibeGrid(
                    vibes = vibes,
                    selectedVibeId = selectedVibeId,
                    onSelectVibe = { vibe ->
                        selectedVibeId = vibe.id
                        vibeToastMessage = "Vibe tuned: ${vibe.title}"
                    }
                )
            }

            // COMPONENT 4: VENT TO YOUR BRAIN COACH BAR
            item(key = "section_vent_to_brain_coach") {
                VentToBrainCoachBar(
                    text = ventInputText,
                    onTextChange = { ventInputText = it },
                    onForgeSkill = {
                        val trimmed = ventInputText.trim()
                        val customSkill = if (trimmed.isNotBlank()) {
                            generateCustomSkillFromInput(trimmed)
                        } else {
                            ForgedCustomSkill(
                                categoryPill = "DARK PSYCHOLOGY DEFENSE",
                                title = "The Playful Spotlight Reversal",
                                description = "Force passive-aggressive coworkers to explain their subtle remarks out loud on the spot.",
                                readTimeSeconds = 90
                            )
                        }
                        forgedSkillOverlayItem = customSkill
                    }
                )
            }

            // COMPONENT 5: 3D STACKED MIRROR DECK
            item(key = "section_mirror_deck") {
                val currentCard = mirrorDeck.getOrNull(currentMirrorIndex)
                    ?: mirrorDeck.firstOrNull()

                if (currentCard != null) {
                    TheMirrorDeckSection(
                        currentCard = currentCard,
                        totalCards = 50,
                        hasPriorityTodayDot = hasPriorityTodayDot,
                        onNotMe = {
                            currentMirrorIndex = (currentMirrorIndex + 1) % mirrorDeck.size
                        },
                        onLiterallyMe = {
                            hasPriorityTodayDot = true
                            profileState = profileState.copy(
                                cognitiveDefense = (profileState.cognitiveDefense + 0.05f).coerceAtMost(1.0f)
                            )
                            currentMirrorIndex = (currentMirrorIndex + 1) % mirrorDeck.size
                        }
                    )
                }
            }

            // COMPONENT 6: MASTERED VAULT HORIZONTAL CARDS
            item(key = "section_mastered_vault") {
                MasteredVaultRow(
                    vaultSkills = vaultSkills,
                    onStartRecall = { skill ->
                        activeRecallModalItem = skill
                    }
                )
            }
        }

        // Vibe confirmation feedback toast
        AnimatedVisibility(
            visible = vibeToastMessage != null,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -40 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -40 }),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 12.dp)
        ) {
            vibeToastMessage?.let { msg ->
                Surface(
                    color = InkBlack,
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.5.dp, SoftLavender),
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Canvas(modifier = Modifier.size(8.dp)) {
                            drawCircle(color = AcidLime)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = msg,
                            color = Color.White,
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                }
                LaunchedEffect(msg) {
                    delay(2200)
                    vibeToastMessage = null
                }
            }
        }

        // COMPONENT 7 - OVERLAY 1: CUSTOM SKILL FORGED OVERLAY
        forgedSkillOverlayItem?.let { forged ->
            CustomSkillForgedOverlay(
                skill = forged,
                onDismiss = { forgedSkillOverlayItem = null },
                onMakeTodayOneThing = {
                    onMakeTodayOneThing(forged.title)
                    forgedSkillOverlayItem = null
                },
                onSaveTomorrow = {
                    forgedSkillOverlayItem = null
                }
            )
        }

        // COMPONENT 7 - OVERLAY 2: VAULT RECALL MODAL
        activeRecallModalItem?.let { vaultItem ->
            VaultRecallModal(
                vaultSkill = vaultItem,
                onDismiss = { activeRecallModalItem = null },
                onSuccessLockIn = {
                    vaultSkills = vaultSkills.map {
                        if (it.id == vaultItem.id) it.copy(isCracking = false, daysAgoLabel = "LOCKED (just now)") else it
                    }
                    onAddDopamineMinutes(5)
                    activeRecallModalItem = null
                }
            )
        }
    }
}

// ==========================================
// COMPONENT 1: TOP STATUS HEADER WITH AMBIENT GLOW
// ==========================================

@Composable
private fun TopStatusHeader(
    shieldActive: Boolean,
    streakDays: Int,
    onClose: (() -> Unit)? = null
) {
    val colors = LocalOneThingColors.current

    Column(modifier = Modifier.fillMaxWidth()) {
        // TOP ROW: Date & Pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "WEDNESDAY, OCT 14",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp,
                        color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059),
                        letterSpacing = 0.8.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (shieldActive) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(HotPink)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Canvas(modifier = Modifier.size(6.dp)) {
                                drawCircle(color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "SHIELD ACTIVE",
                                style = TextStyle(
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Dark translucent pill "4 DAYS" with Orange + Yellow flame vector
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (colors.isDark) Color(0x66181722) else Color(0x33191919))
                        .border(
                            1.dp,
                            if (colors.isDark) Color(0xFF2E2C40) else Color(0xFFD8D0C5),
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Canvas(modifier = Modifier.size(14.dp)) {
                            drawMultiColorFlameVector()
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$streakDays DAYS",
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 11.sp,
                                color = if (colors.isDark) Color.White else InkBlack,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                }

                if (onClose != null) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .border(
                                1.5.dp,
                                if (colors.isDark) Color(0xFF2E2E3E) else Color(0xFFD8D0C5),
                                CircleShape
                            )
                            .background(if (colors.isDark) Color(0xFF16161E) else Color.White)
                            .tactileBounce { onClose() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = if (colors.isDark) Color.White else InkBlack,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // SECOND ROW: "BRAIN PROFILE" + 44.dp Circular Stylized Avatar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "BRAIN PROFILE",
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Black,
                    fontSize = 28.sp,
                    color = if (colors.isDark) Color.White else InkBlack,
                    letterSpacing = (-0.8).sp
                )
            )

            // 44.dp Circular Avatar with 2.dp ring border and stylized vector portrait
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .border(
                        2.dp,
                        if (colors.isDark) Color(0xFF8B5CF6) else SoftLavender,
                        CircleShape
                    )
                    .shadow(
                        6.dp,
                        CircleShape,
                        spotColor = if (colors.isDark) Color(0xFF8B5CF6) else SoftLavender
                    )
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFFE8E2F8), Color(0xFFFFF0EB))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(30.dp)) {
                    drawStylizedAvatarVector()
                }
            }
        }
    }
}

// ==========================================
// COMPONENT 2: ARCHETYPE HERO CARD
// ==========================================

@Composable
private fun ArchetypeHeroCard(
    profileState: BrainProfileState,
    onToggleFlip: () -> Unit
) {
    val colors = LocalOneThingColors.current

    val rotationY by animateFloatAsState(
        targetValue = if (profileState.isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "hero_card_flip"
    )

    val cardBg = if (colors.isDark) {
        Brush.linearGradient(listOf(Color(0xFF2A2736), Color(0xFF1B1924)))
    } else {
        Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF3EFFA)))
    }
    val cardBorderColor = if (colors.isDark) Color(0xFF49455E) else Color(0xFFDCD4E8)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.rotationY = rotationY
                cameraDistance = 14f * density
            }
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = if (colors.isDark) Color(0xFF8B5CF6).copy(alpha = 0.25f) else Color(0xFFC7B6F4).copy(alpha = 0.35f)
            )
            .clip(RoundedCornerShape(22.dp))
            .border(1.5.dp, cardBorderColor, RoundedCornerShape(22.dp))
            .background(cardBg)
            .tactileBounce { onToggleFlip() }
    ) {
        if (rotationY <= 90f) {
            // FRONT OF CARD
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CURRENT ARCHETYPE:",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059),
                            letterSpacing = 1.sp
                        )
                    )

                    // Flip Indicator Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (colors.isDark) Color(0xFF383446) else Color(0xFFE8E2F4))
                            .border(1.dp, cardBorderColor, RoundedCornerShape(50))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Flip",
                                tint = if (colors.isDark) Color.White else InkBlack,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "METRICS",
                                style = TextStyle(
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp,
                                    color = if (colors.isDark) Color.White else InkBlack,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = profileState.archetypeTitle,
                    style = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = if (colors.isDark) Color.White else InkBlack
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Progress Bar 1: 78% Social Leverage (AcidLime -> #8CE050)
                ThickDualThemeProgressBar(
                    label = "Social Leverage",
                    progress = profileState.socialLeverage,
                    percentageText = "${(profileState.socialLeverage * 100).roundToInt()}%",
                    useGradient = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Progress Bar 2: 55% Cognitive Defense (AcidLime)
                ThickDualThemeProgressBar(
                    label = "Cognitive Defense",
                    progress = profileState.cognitiveDefense,
                    percentageText = "${(profileState.cognitiveDefense * 100).roundToInt()}%",
                    useGradient = false
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Footer Pill: "2 lessons away from: 'The Spotless Boundary'"
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (colors.isDark) Color(0xFF383446) else Color(0xFFE8E2F4))
                        .border(1.dp, cardBorderColor, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${profileState.lessonsUntilUnlock} lessons away from: \"${profileState.nextUnlockTitle}\"",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = if (colors.isDark) Color.White else InkBlack
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        } else {
            // BACK OF CARD (Flipped 180 deg)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { this.rotationY = 180f }
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "WEEKLY IMPULSE DEFENSE",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059),
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "TAP TO FLIP BACK",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = TangerineOrange
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricHighlightBox(
                        title = "Reels Blocked",
                        value = "${profileState.reelsBlockedWeekly}",
                        modifier = Modifier.weight(1f)
                    )
                    MetricHighlightBox(
                        title = "Time Saved",
                        value = "${profileState.minutesSavedWeekly}m",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (colors.isDark) Color(0xFF383446) else Color(0xFFE8E2F4))
                        .border(1.dp, cardBorderColor, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "PEAK VULNERABILITY WINDOW",
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059),
                                letterSpacing = 0.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "10:30 PM — 11:45 PM (Bedtime Loop)",
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                color = if (colors.isDark) Color.White else InkBlack
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThickDualThemeProgressBar(
    label: String,
    progress: Float,
    percentageText: String,
    useGradient: Boolean
) {
    val colors = LocalOneThingColors.current
    val trackColor = if (colors.isDark) Color(0xFF4A4656) else Color(0xFFD8D2E2)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = if (colors.isDark) Color.White else InkBlack
                )
            )
            Text(
                text = percentageText,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp,
                    color = if (colors.isDark) Color.White else InkBlack
                )
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(11.dp)
                .clip(RoundedCornerShape(50))
                .background(trackColor)
        ) {
            val fillBrush = if (useGradient) {
                Brush.horizontalGradient(listOf(AcidLime, Color(0xFF8CE050)))
            } else {
                Brush.horizontalGradient(listOf(AcidLime, AcidLime))
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                    .height(11.dp)
                    .clip(RoundedCornerShape(50))
                    .background(fillBrush)
            )
        }
    }
}

@Composable
private fun MetricHighlightBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val colors = LocalOneThingColors.current

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (colors.isDark) Color(0xFF1E1C2A) else Color.White)
            .border(
                1.dp,
                if (colors.isDark) Color(0xFF2E2C40) else Color(0xFFDCD4E8),
                RoundedCornerShape(14.dp)
            )
            .padding(14.dp)
    ) {
        Column {
            Text(
                text = title.uppercase(),
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059),
                    letterSpacing = 0.5.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = if (colors.isDark) Color.White else InkBlack
                )
            )
        }
    }
}

// ==========================================
// COMPONENT 3: TUNE TODAY'S VIBE 2x2 GRID (68.dp MULTI-LAYERED GLOWING ILLUSTRATIONS)
// ==========================================

@Composable
private fun TuneTodaysVibeGrid(
    vibes: List<VibeCategory>,
    selectedVibeId: String,
    onSelectVibe: (VibeCategory) -> Unit
) {
    val colors = LocalOneThingColors.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "TUNE TODAY'S VIBE",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059),
                letterSpacing = 1.sp
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VibeTileCard(
                vibe = vibes[0],
                isSelected = selectedVibeId == vibes[0].id,
                iconType = VibeIconType.BRAIN,
                modifier = Modifier.weight(1f),
                onClick = { onSelectVibe(vibes[0]) }
            )
            VibeTileCard(
                vibe = vibes[1],
                isSelected = selectedVibeId == vibes[1].id,
                iconType = VibeIconType.SPEECH,
                modifier = Modifier.weight(1f),
                onClick = { onSelectVibe(vibes[1]) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VibeTileCard(
                vibe = vibes[2],
                isSelected = selectedVibeId == vibes[2].id,
                iconType = VibeIconType.SHIELD,
                modifier = Modifier.weight(1f),
                onClick = { onSelectVibe(vibes[2]) }
            )
            VibeTileCard(
                vibe = vibes[3],
                isSelected = selectedVibeId == vibes[3].id,
                iconType = VibeIconType.FOCUS_LIGHTNING,
                modifier = Modifier.weight(1f),
                onClick = { onSelectVibe(vibes[3]) }
            )
        }
    }
}

private enum class VibeIconType { BRAIN, SPEECH, SHIELD, FOCUS_LIGHTNING }

@Composable
private fun VibeTileCard(
    vibe: VibeCategory,
    isSelected: Boolean,
    iconType: VibeIconType,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = LocalOneThingColors.current

    val cardBgBrush = if (isSelected) {
        if (colors.isDark) {
            Brush.linearGradient(listOf(Color(0xFF26181A), Color(0xFF26181A)))
        } else {
            Brush.linearGradient(listOf(Color(0xFFFF7A4D), Color(0xFFFF5722)))
        }
    } else {
        if (colors.isDark) {
            Brush.linearGradient(listOf(Color(0xFF181722), Color(0xFF181722)))
        } else {
            Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFFFFFF)))
        }
    }

    val cardBorderColor = if (isSelected) {
        if (colors.isDark) Color(0xFFFF7043) else Color(0xFFFF9E80)
    } else {
        if (colors.isDark) Color(0xFF2E2C40) else Color(0xFFE2DCD0)
    }

    val borderWidth = if (isSelected) 2.dp else 1.5.dp

    Box(
        modifier = modifier
            .height(148.dp)
            .shadow(
                elevation = if (isSelected) 8.dp else 6.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = if (isSelected) TangerineOrange.copy(alpha = 0.45f) else Color(0xFF191919).copy(alpha = 0.08f)
            )
            .clip(RoundedCornerShape(20.dp))
            .border(borderWidth, cardBorderColor, RoundedCornerShape(20.dp))
            .background(cardBgBrush)
            .tactileBounce { onClick() }
            .padding(14.dp)
    ) {
        // Inner orange glow if active in dark mode
        if (isSelected && colors.isDark) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFFF7043).copy(alpha = 0.28f), Color.Transparent),
                        center = center,
                        radius = size.width * 0.7f
                    )
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOP/CENTER: Large 68.dp custom multi-layered glowing Canvas illustration
            Box(
                modifier = Modifier.size(68.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(68.dp)) {
                    when (iconType) {
                        VibeIconType.BRAIN -> drawLargeGlowingBrainIllustration(isSelected = isSelected, isDark = colors.isDark)
                        VibeIconType.SPEECH -> drawLargeGlowingSpeechIllustration(isSelected = isSelected, isDark = colors.isDark)
                        VibeIconType.SHIELD -> drawLargeGlowingShieldIllustration(isSelected = isSelected, isDark = colors.isDark)
                        VibeIconType.FOCUS_LIGHTNING -> drawLargeGlowingFocusIllustration(isSelected = isSelected, isDark = colors.isDark)
                    }
                }
            }

            // BOTTOM: Centered bold uppercase two-line text
            val textColor = if (isSelected) {
                Color.White
            } else {
                if (colors.isDark) Color.White else InkBlack
            }

            Text(
                text = vibe.title,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 15.sp,
                    letterSpacing = 0.4.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ==========================================
// COMPONENT 4: VENT TO YOUR BRAIN COACH BAR
// ==========================================

@Composable
private fun VentToBrainCoachBar(
    text: String,
    onTextChange: (String) -> Unit,
    onForgeSkill: () -> Unit
) {
    val colors = LocalOneThingColors.current
    val borderColor = if (colors.isDark) Color(0xFF2E2E3E) else Color(0xFFD8D0C5)
    val cardBg = if (colors.isDark) Color(0xFF16161E) else Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color(0xFF191919).copy(alpha = 0.08f)
            )
            .clip(RoundedCornerShape(20.dp))
            .border(1.5.dp, borderColor, RoundedCornerShape(20.dp))
            .background(cardBg)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "VENT TO YOUR BRAIN COACH",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        color = if (colors.isDark) Color.White else InkBlack,
                        letterSpacing = 0.6.sp
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                BasicTextField(
                    value = text,
                    onValueChange = onTextChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (colors.isDark) Color.White else InkBlack
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onForgeSkill() }),
                    decorationBox = { innerTextField ->
                        if (text.isEmpty()) {
                            Text(
                                text = "My coworker keeps taking credit for my...",
                                style = TextStyle(
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 13.sp,
                                    color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059)
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        innerTextField()
                    }
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Right Button: High-contrast Pill Button HotPink #F47BC4, height 38dp, padding 18dp
            Box(
                modifier = Modifier
                    .height(38.dp)
                    .clip(RoundedCornerShape(50))
                    .background(HotPink)
                    .tactileBounce { onForgeSkill() }
                    .padding(horizontal = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ACTIVE",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
    }
}

// ==========================================
// COMPONENT 5: 3D STACKED MIRROR DECK
// ==========================================

@Composable
private fun TheMirrorDeckSection(
    currentCard: MirrorCardItem,
    totalCards: Int,
    hasPriorityTodayDot: Boolean,
    onNotMe: () -> Unit,
    onLiterallyMe: () -> Unit
) {
    val colors = LocalOneThingColors.current
    var offsetX by remember { mutableFloatStateOf(0f) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "THE MIRROR DECK",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059),
                        letterSpacing = 1.sp
                    )
                )
                if (hasPriorityTodayDot) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Canvas(modifier = Modifier.size(8.dp)) {
                        drawCircle(color = TangerineOrange)
                    }
                }
            }

            Text(
                text = "${currentCard.cardNumber} of $totalCards",
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059)
                )
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3D Stack Wrapper (Box with top padding 24.dp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(310.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Back Card 2: fillMaxWidth(0.84f), offset(y = -18.dp), #C7B6F4 alpha 0.35f
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.84f)
                    .height(260.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = (-18).dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFC7B6F4).copy(alpha = 0.35f))
                    .border(1.dp, Color(0xFFC7B6F4), RoundedCornerShape(24.dp))
            )

            // Back Card 1: fillMaxWidth(0.92f), offset(y = -9.dp), #C7B6F4 alpha 0.60f
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                .height(260.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-9).dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFC7B6F4).copy(alpha = 0.60f))
                .border(1.dp, Color(0xFFC7B6F4), RoundedCornerShape(24.dp))
            )

            // Main Foreground Card with Glowing Lavender Border, Lavender-White surface, Corner Micro-Doodles
            val foregroundGradient = Brush.linearGradient(listOf(Color(0xFFF4F0FF), Color(0xFFE9E1FC)))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(275.dp)
                    .offset { IntOffset(offsetX.roundToInt(), 0) }
                    .graphicsLayer {
                        rotationZ = (offsetX / 60f).coerceIn(-12f, 12f)
                    }
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = Color(0xFF9648FE),
                        spotColor = Color(0xFF9648FE)
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .border(2.dp, SoftLavender, RoundedCornerShape(24.dp))
                    .background(foregroundGradient)
                    .drawBehind {
                        drawCornerMicroDoodles()
                    }
                    .pointerInput(currentCard.id) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                if (offsetX > 150f) {
                                    scope.launch {
                                        offsetX = 600f
                                        delay(150)
                                        offsetX = 0f
                                        onLiterallyMe()
                                    }
                                } else if (offsetX < -150f) {
                                    scope.launch {
                                        offsetX = -600f
                                        delay(150)
                                        offsetX = 0f
                                        onNotMe()
                                    }
                                } else {
                                    offsetX = 0f
                                }
                            },
                            onHorizontalDrag = { _, dragAmount ->
                                offsetX += dragAmount
                            }
                        )
                    }
                    .padding(horizontal = 22.dp, vertical = 20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Card ${currentCard.cardNumber} of $totalCards",
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                fontSize = 11.sp,
                                color = Color(0xFF5A536E)
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "THE MIRROR DECK:",
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                color = InkBlack,
                                letterSpacing = 0.8.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = currentCard.relatableQuote,
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = InkBlack,
                                textAlign = TextAlign.Center,
                                lineHeight = 26.sp
                            ),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }

                    // BOTTOM BUTTON ROW
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Left Pill: Outlined pill "Not Me", height 42dp, border 1.5dp InkBlack
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .clip(RoundedCornerShape(21.dp))
                                .border(1.5.dp, InkBlack, RoundedCornerShape(21.dp))
                                .background(Color.Transparent)
                                .tactileBounce {
                                    scope.launch {
                                        offsetX = -500f
                                        delay(120)
                                        offsetX = 0f
                                        onNotMe()
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Not Me",
                                style = TextStyle(
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = InkBlack
                                )
                            )
                        }

                        // Right Pill: Filled TangerineOrange pill "Literally Me ->", height 42dp
                        Box(
                            modifier = Modifier
                                .weight(1.3f)
                                .height(42.dp)
                                .shadow(8.dp, RoundedCornerShape(21.dp), spotColor = TangerineOrange.copy(alpha = 0.5f))
                                .clip(RoundedCornerShape(21.dp))
                                .background(TangerineOrange)
                                .tactileBounce {
                                    scope.launch {
                                        offsetX = 500f
                                        delay(120)
                                        offsetX = 0f
                                        onLiterallyMe()
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Literally Me ->",
                                    style = TextStyle(
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// COMPONENT 6: MASTERED VAULT HORIZONTAL CARDS
// ==========================================

@Composable
private fun MasteredVaultRow(
    vaultSkills: List<VaultSkillItem>,
    onStartRecall: (VaultSkillItem) -> Unit
) {
    val colors = LocalOneThingColors.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "MASTERED VAULT",
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059),
                    letterSpacing = 1.sp
                )
            )

            Text(
                text = "REVIEW >",
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059)
                )
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(vaultSkills, key = { it.id }) { item ->
                VaultSkillCard(
                    item = item,
                    onStartRecall = { onStartRecall(item) }
                )
            }
        }
    }
}

@Composable
private fun VaultSkillCard(
    item: VaultSkillItem,
    onStartRecall: () -> Unit
) {
    val colors = LocalOneThingColors.current

    if (item.isCracking) {
        // CARD 1: CRACKING STATE (width = 195.dp, height = 195.dp, Solid ElectricYellow #FFE747)
        Box(
            modifier = Modifier
                .width(195.dp)
                .height(195.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(22.dp),
                    spotColor = ElectricYellow.copy(alpha = 0.5f)
                )
                .clip(RoundedCornerShape(22.dp))
                .background(ElectricYellow)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Row: Lock icon on left, 36.dp Shield + Checkmark emblem in center, bookmark icon on right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Canvas(modifier = Modifier.size(16.dp)) {
                        drawLockVector(color = InkBlack)
                    }

                    Canvas(modifier = Modifier.size(36.dp)) {
                        drawShieldCheckmarkEmblem(color = InkBlack)
                    }

                    Canvas(modifier = Modifier.size(16.dp)) {
                        drawBookmarkRibbonVector(color = InkBlack)
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "CRACKING (6d ago)",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp,
                            color = InkBlack,
                            letterSpacing = 0.5.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.title,
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = InkBlack,
                            textAlign = TextAlign.Center,
                            lineHeight = 19.sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Full-width AcidLime #B9E769 pill button (height 38dp, 1dp #8BC34A border)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .clip(RoundedCornerShape(19.dp))
                        .border(1.dp, Color(0xFF8BC34A), RoundedCornerShape(19.dp))
                        .background(AcidLime)
                        .tactileBounce { onStartRecall() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "15s Recall (+5m)",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = InkBlack
                        )
                    )
                }
            }
        }
    } else {
        // CARD 2: LOCKED STATE (width = 195.dp, height = 195.dp)
        val lockedBg = if (colors.isDark) Color(0xFF1C1B26) else Color(0xFFECE6F8)
        val lockedBorder = if (colors.isDark) Color(0xFF2E2C3E) else Color(0xFFD5C8F0)
        val lockedTextColor = if (colors.isDark) Color(0xFFF8FAFC) else InkBlack

        Box(
            modifier = Modifier
                .width(195.dp)
                .height(195.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(22.dp),
                    spotColor = Color(0xFF191919).copy(alpha = 0.08f)
                )
                .clip(RoundedCornerShape(22.dp))
                .border(1.5.dp, lockedBorder, RoundedCornerShape(22.dp))
                .background(lockedBg)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Canvas(modifier = Modifier.size(16.dp)) {
                        drawLockVector(color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059))
                    }

                    Canvas(modifier = Modifier.size(36.dp)) {
                        drawShieldCheckmarkEmblem(color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059))
                    }

                    Canvas(modifier = Modifier.size(16.dp)) {
                        drawBookmarkRibbonVector(color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059))
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "LOCKED (2d ago)",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp,
                            color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059),
                            letterSpacing = 0.5.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.title,
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = lockedTextColor,
                            textAlign = TextAlign.Center,
                            lineHeight = 19.sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .clip(RoundedCornerShape(19.dp))
                        .background(if (colors.isDark) Color(0xFF262434) else Color(0xFFDDD5EE))
                        .tactileBounce { onStartRecall() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "15s Recall (+5m)",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059)
                        )
                    )
                }
            }
        }
    }
}

// ==========================================
// COMPONENT 7: OVERLAYS (SCREEN 3 & SCREEN 4)
// ==========================================

@Composable
private fun CustomSkillForgedOverlay(
    skill: ForgedCustomSkill,
    onDismiss: () -> Unit,
    onMakeTodayOneThing: () -> Unit,
    onSaveTomorrow: () -> Unit
) {
    val colors = LocalOneThingColors.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        com.example.ui.components.AppBackground(variant = com.example.ui.components.BackgroundVariant.CENTER_BLOB) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismiss
                    ),
                contentAlignment = Alignment.Center
            ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .shadow(24.dp, RoundedCornerShape(28.dp), spotColor = Color(0xFF8B5CF6))
                    .clip(RoundedCornerShape(28.dp))
                    .border(
                        1.5.dp,
                        if (colors.isDark) Color(0xFF49455E) else Color(0xFFDCD4E8),
                        RoundedCornerShape(28.dp)
                    )
                    .background(if (colors.isDark) Color(0xFF16161E) else Color.White)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}
                    )
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Top Illustration: 64.dp custom 3-star hand-drawn sparkle cluster (#C7B6F4 & #FFE747)
                    Box(modifier = Modifier.size(64.dp), contentAlignment = Alignment.Center) {
                        Canvas(modifier = Modifier.size(64.dp)) {
                            drawSparkleStarCluster()
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "CUSTOM SKILL FORGED:",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = if (colors.isDark) Color.White else InkBlack,
                            letterSpacing = 1.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Based on what you are dealing with right now:",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059)
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = skill.title,
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 21.sp,
                            color = if (colors.isDark) Color.White else InkBlack,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = skill.description,
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059),
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Primary CTA: TangerineOrange #FF7043 pill button, height 48dp, white bold text
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = TangerineOrange.copy(alpha = 0.5f))
                            .clip(RoundedCornerShape(24.dp))
                            .background(TangerineOrange)
                            .tactileBounce { onMakeTodayOneThing() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Make Today's ONE THING ->",
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Save to Tomorrow's Queue",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (colors.isDark) Color(0xFFA09EB0) else Color(0xFF656059),
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier
                            .clickable { onSaveTomorrow() }
                            .padding(6.dp)
                    )
                }
            }
        }
    }
}
}

@Composable
private fun VaultRecallModal(
    vaultSkill: VaultSkillItem,
    onDismiss: () -> Unit,
    onSuccessLockIn: () -> Unit
) {
    var secondsLeft by remember { mutableIntStateOf(11) }
    var selectedOptionIndex by remember { mutableIntStateOf(1) } // Option B selected by default to match Screen 4 mockup

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft -= 1
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        com.example.ui.components.AppBackground(variant = com.example.ui.components.BackgroundVariant.SOLID_CREAM) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(24.dp), spotColor = Color(0xFF191919).copy(alpha = 0.15f))
                    .clip(RoundedCornerShape(24.dp))
                    .border(2.dp, InkBlack, RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "MEMORY LOCK-IN",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = InkBlack,
                            letterSpacing = 1.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = String.format("0:%02d", secondsLeft),
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Black,
                            fontSize = 36.sp,
                            color = InkBlack
                        )
                    )

                    // Short TangerineOrange underline bar
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(TangerineOrange)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "SKILL TO REPAIR:",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color(0xFF656059)
                        )
                    )

                    Text(
                        text = vaultSkill.title,
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            color = InkBlack,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = vaultSkill.quizQuestion,
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = InkBlack,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    val options = listOf(
                        "A) Give them an unsolicited compliment on their outfit",
                        "B) Ask to borrow a screwdriver for 5 mins",
                        "C) Ignore them completely to establish emotional dominance"
                    )

                    options.forEachIndexed { index, optionText ->
                        val isOptionB = index == 1
                        val isSelected = selectedOptionIndex == index

                        val itemBg = if (isOptionB) AcidLime else Color(0xFFF7F2E7)
                        val itemBorderWidth = if (isOptionB) 2.dp else 1.dp
                        val itemBorderColor = if (isOptionB) InkBlack else Color(0xFFDCD4E8)

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(itemBorderWidth, itemBorderColor, RoundedCornerShape(16.dp))
                                .background(itemBg)
                                .clickable { selectedOptionIndex = index }
                                .padding(horizontal = 14.dp, vertical = 12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = optionText,
                                    style = TextStyle(
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = if (isOptionB) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 12.sp,
                                        color = InkBlack
                                    ),
                                    modifier = Modifier.weight(1f)
                                )

                                if (isOptionB) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(InkBlack),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Correct",
                                            tint = Color.White,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Bottom CTA: Full-width TangerineOrange #FF7043 pill button, height 52dp, white bold text
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .shadow(8.dp, RoundedCornerShape(26.dp), spotColor = TangerineOrange.copy(alpha = 0.5f))
                            .clip(RoundedCornerShape(26.dp))
                            .background(TangerineOrange)
                            .tactileBounce {
                                onSuccessLockIn()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Lock Into Memory (+5m)",
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }
    }
}
}

// ==========================================
// BESPOKE 68.dp MULTI-LAYERED CUSTOM CANVAS ILLUSTRATIONS
// ==========================================

/**
 * 1. MAKE ME SHARPER: Detailed glowing multi-lobed Brain illustration (Pink #F47BC4, Cyan #38BDF8, Yellow #FFE747).
 */
private fun DrawScope.drawLargeGlowingBrainIllustration(isSelected: Boolean, isDark: Boolean) {
    val w = size.width
    val h = size.height

    // Soft radial neon glow behind
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFF47BC4).copy(alpha = if (isSelected) 0.55f else 0.35f),
                Color(0xFF38BDF8).copy(alpha = if (isSelected) 0.35f else 0.15f),
                Color.Transparent
            ),
            center = Offset(w * 0.5f, h * 0.5f),
            radius = w * 0.48f
        )
    )

    // Left hemisphere lobe path (Pink)
    val leftLobe = Path().apply {
        moveTo(w * 0.5f, h * 0.22f)
        cubicTo(w * 0.22f, h * 0.18f, w * 0.12f, h * 0.42f, w * 0.18f, h * 0.65f)
        cubicTo(w * 0.20f, h * 0.82f, w * 0.40f, h * 0.82f, w * 0.5f, h * 0.78f)
        close()
    }
    drawPath(
        path = leftLobe,
        brush = Brush.linearGradient(listOf(Color(0xFFF47BC4), Color(0xFFC084FC)))
    )

    // Right hemisphere lobe path (Cyan / Yellow)
    val rightLobe = Path().apply {
        moveTo(w * 0.5f, h * 0.22f)
        cubicTo(w * 0.78f, h * 0.18f, w * 0.88f, h * 0.42f, w * 0.82f, h * 0.65f)
        cubicTo(w * 0.80f, h * 0.82f, w * 0.60f, h * 0.82f, w * 0.5f, h * 0.78f)
        close()
    }
    drawPath(
        path = rightLobe,
        brush = Brush.linearGradient(listOf(Color(0xFF38BDF8), Color(0xFFFFE747)))
    )

    // Central dividing fissure line
    drawLine(
        color = if (isSelected) Color.White else InkBlack,
        start = Offset(w * 0.5f, h * 0.22f),
        end = Offset(w * 0.5f, h * 0.78f),
        strokeWidth = 2.5f
    )

    // Neural network lines and synaptic nodes
    val strokeColor = if (isSelected) Color.White.copy(alpha = 0.85f) else InkBlack.copy(alpha = 0.75f)
    drawLine(color = strokeColor, start = Offset(w * 0.32f, h * 0.38f), end = Offset(w * 0.42f, h * 0.52f), strokeWidth = 1.8f)
    drawLine(color = strokeColor, start = Offset(w * 0.28f, h * 0.62f), end = Offset(w * 0.42f, h * 0.52f), strokeWidth = 1.8f)
    drawLine(color = strokeColor, start = Offset(w * 0.68f, h * 0.38f), end = Offset(w * 0.58f, h * 0.52f), strokeWidth = 1.8f)
    drawLine(color = strokeColor, start = Offset(w * 0.72f, h * 0.62f), end = Offset(w * 0.58f, h * 0.52f), strokeWidth = 1.8f)

    // Synaptic nodes
    drawCircle(color = Color(0xFFFFE747), radius = 3.5f, center = Offset(w * 0.32f, h * 0.38f))
    drawCircle(color = Color.White, radius = 4f, center = Offset(w * 0.42f, h * 0.52f))
    drawCircle(color = Color(0xFFFFE747), radius = 3.5f, center = Offset(w * 0.28f, h * 0.62f))
    drawCircle(color = Color(0xFF38BDF8), radius = 3.5f, center = Offset(w * 0.68f, h * 0.38f))
    drawCircle(color = Color.White, radius = 4f, center = Offset(w * 0.58f, h * 0.52f))
    drawCircle(color = Color(0xFF38BDF8), radius = 3.5f, center = Offset(w * 0.72f, h * 0.62f))
}

/**
 * 2. UNFAIR SOCIAL ADVANTAGE: Dual overlapping glowing Speech Bubbles with user silhouette and sparkle.
 */
private fun DrawScope.drawLargeGlowingSpeechIllustration(isSelected: Boolean, isDark: Boolean) {
    val w = size.width
    val h = size.height

    // Radial neon glow
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFF8B5CF6).copy(alpha = if (isSelected) 0.55f else 0.35f),
                Color(0xFFFF7043).copy(alpha = if (isSelected) 0.35f else 0.15f),
                Color.Transparent
            ),
            center = Offset(w * 0.5f, h * 0.5f),
            radius = w * 0.48f
        )
    )

    // Back speech bubble (Blue/Purple gradient)
    val backBubble = Path().apply {
        addRoundRect(
            androidx.compose.ui.geometry.RoundRect(
                left = w * 0.16f,
                top = h * 0.18f,
                right = w * 0.68f,
                bottom = h * 0.58f,
                cornerRadius = CornerRadius(14f, 14f)
            )
        )
    }
    drawPath(
        path = backBubble,
        brush = Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)))
    )

    // Front overlapping speech bubble (Orange/Purple gradient)
    val frontBubble = Path().apply {
        addRoundRect(
            androidx.compose.ui.geometry.RoundRect(
                left = w * 0.36f,
                top = h * 0.38f,
                right = w * 0.84f,
                bottom = h * 0.78f,
                cornerRadius = CornerRadius(14f, 14f)
            )
        )
    }
    drawPath(
        path = frontBubble,
        brush = Brush.linearGradient(listOf(Color(0xFFFF7043), Color(0xFFF47BC4)))
    )

    // Front bubble tail
    val tail = Path().apply {
        moveTo(w * 0.44f, h * 0.78f)
        lineTo(w * 0.36f, h * 0.88f)
        lineTo(w * 0.54f, h * 0.78f)
        close()
    }
    drawPath(tail, color = Color(0xFFFF7043))

    // User silhouette inside back bubble
    drawCircle(
        color = Color.White.copy(alpha = 0.9f),
        radius = 5f,
        center = Offset(w * 0.38f, h * 0.32f)
    )
    drawArc(
        color = Color.White.copy(alpha = 0.9f),
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(w * 0.30f, h * 0.40f),
        size = Size(w * 0.16f, h * 0.12f)
    )

    // Sparkle accent on front bubble
    drawSparkleAccent(cx = w * 0.62f, cy = h * 0.58f, sizePx = 16f, color = Color(0xFFFFE747))
}

/**
 * 3. MAKE ME UNMANIPULATABLE: 3D Heraldic Shield divided into 2 halves with vertical armor stripes.
 */
private fun DrawScope.drawLargeGlowingShieldIllustration(isSelected: Boolean, isDark: Boolean) {
    val w = size.width
    val h = size.height

    // Radial neon glow
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFF8B5CF6).copy(alpha = if (isSelected) 0.55f else 0.35f),
                Color(0xFF60A5FA).copy(alpha = if (isSelected) 0.35f else 0.15f),
                Color.Transparent
            ),
            center = Offset(w * 0.5f, h * 0.5f),
            radius = w * 0.48f
        )
    )

    // Left half of shield (Purple/Dark Blue)
    val leftShield = Path().apply {
        moveTo(w * 0.5f, h * 0.16f)
        lineTo(w * 0.20f, h * 0.28f)
        lineTo(w * 0.24f, h * 0.62f)
        lineTo(w * 0.5f, h * 0.84f)
        close()
    }
    drawPath(
        path = leftShield,
        brush = Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFF4F46E5)))
    )

    // Right half of shield (Bright Cyan/Blue)
    val rightShield = Path().apply {
        moveTo(w * 0.5f, h * 0.16f)
        lineTo(w * 0.80f, h * 0.28f)
        lineTo(w * 0.76f, h * 0.62f)
        lineTo(w * 0.5f, h * 0.84f)
        close()
    }
    drawPath(
        path = rightShield,
        brush = Brush.linearGradient(listOf(Color(0xFF60A5FA), Color(0xFF38BDF8)))
    )

    // Center dividing armor seam
    drawLine(
        color = Color.White,
        start = Offset(w * 0.5f, h * 0.16f),
        end = Offset(w * 0.5f, h * 0.84f),
        strokeWidth = 2.5f
    )

    // Vertical heraldic armor stripes
    drawLine(
        color = Color.White.copy(alpha = 0.55f),
        start = Offset(w * 0.34f, h * 0.30f),
        end = Offset(w * 0.36f, h * 0.68f),
        strokeWidth = 2f
    )
    drawLine(
        color = Color.White.copy(alpha = 0.55f),
        start = Offset(w * 0.66f, h * 0.30f),
        end = Offset(w * 0.64f, h * 0.68f),
        strokeWidth = 2f
    )

    // Center golden crest star
    drawSparkleAccent(cx = w * 0.5f, cy = h * 0.46f, sizePx = 18f, color = Color(0xFFFFE747))
}

/**
 * 4. FIX MY BROKEN FOCUS: Detailed Stopwatch with glowing Golden/Orange Lightning Bolt.
 */
private fun DrawScope.drawLargeGlowingFocusIllustration(isSelected: Boolean, isDark: Boolean) {
    val w = size.width
    val h = size.height

    // Radial neon glow
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFF7043).copy(alpha = if (isSelected) 0.55f else 0.35f),
                Color(0xFFFFE747).copy(alpha = if (isSelected) 0.35f else 0.15f),
                Color.Transparent
            ),
            center = Offset(w * 0.5f, h * 0.52f),
            radius = w * 0.48f
        )
    )

    // Outer Stopwatch Ring
    drawCircle(
        brush = Brush.linearGradient(listOf(Color(0xFFFF7043), Color(0xFFFFE747))),
        radius = w * 0.34f,
        center = Offset(w * 0.5f, h * 0.54f),
        style = Stroke(width = 3.5f)
    )

    // Stopwatch top knob
    drawRoundRect(
        color = if (isSelected) Color.White else InkBlack,
        topLeft = Offset(w * 0.44f, h * 0.12f),
        size = Size(w * 0.12f, h * 0.08f),
        cornerRadius = CornerRadius(3f, 3f)
    )
    drawLine(
        color = if (isSelected) Color.White else InkBlack,
        start = Offset(w * 0.5f, h * 0.12f),
        end = Offset(w * 0.5f, h * 0.20f),
        strokeWidth = 3f
    )

    // Dial tick marks
    drawLine(color = Color.White.copy(alpha = 0.7f), start = Offset(w * 0.5f, h * 0.24f), end = Offset(w * 0.5f, h * 0.28f), strokeWidth = 2f)
    drawLine(color = Color.White.copy(alpha = 0.7f), start = Offset(w * 0.5f, h * 0.80f), end = Offset(w * 0.5f, h * 0.84f), strokeWidth = 2f)
    drawLine(color = Color.White.copy(alpha = 0.7f), start = Offset(w * 0.20f, h * 0.54f), end = Offset(w * 0.24f, h * 0.54f), strokeWidth = 2f)
    drawLine(color = Color.White.copy(alpha = 0.7f), start = Offset(w * 0.76f, h * 0.54f), end = Offset(w * 0.80f, h * 0.54f), strokeWidth = 2f)

    // Dynamic sharp lightning bolt striking diagonally through center
    val bolt = Path().apply {
        moveTo(w * 0.56f, h * 0.28f)
        lineTo(w * 0.40f, h * 0.54f)
        lineTo(w * 0.54f, h * 0.54f)
        lineTo(w * 0.44f, h * 0.82f)
        lineTo(w * 0.66f, h * 0.48f)
        lineTo(w * 0.52f, h * 0.48f)
        close()
    }
    drawPath(
        path = bolt,
        brush = Brush.linearGradient(listOf(Color(0xFFFFE747), Color(0xFFFF7043)))
    )
}

// ==========================================
// DRAWING UTILITIES & VECTOR HELPERS
// ==========================================

private fun DrawScope.drawSparkleAccent(cx: Float, cy: Float, sizePx: Float, color: Color) {
    val half = sizePx / 2f
    val path = Path().apply {
        moveTo(cx, cy - half)
        cubicTo(cx + half * 0.15f, cy - half * 0.15f, cx + half * 0.15f, cy - half * 0.15f, cx + half, cy)
        cubicTo(cx + half * 0.15f, cy + half * 0.15f, cx + half * 0.15f, cy + half * 0.15f, cx, cy + half)
        cubicTo(cx - half * 0.15f, cy + half * 0.15f, cx - half * 0.15f, cy + half * 0.15f, cx - half, cy)
        cubicTo(cx - half * 0.15f, cy - half * 0.15f, cx - half * 0.15f, cy - half * 0.15f, cx, cy - half)
        close()
    }
    drawPath(path, color = color)
}

private fun DrawScope.drawStylizedAvatarVector() {
    val w = size.width
    val h = size.height

    // Hair / Headgear (Tangerine / HotPink)
    drawArc(
        brush = Brush.linearGradient(listOf(HotPink, TangerineOrange)),
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(w * 0.22f, h * 0.12f),
        size = Size(w * 0.56f, h * 0.50f)
    )

    // Face / Skin
    drawCircle(
        color = Color(0xFFFCD34D),
        radius = w * 0.22f,
        center = Offset(w * 0.5f, h * 0.44f)
    )

    // Glasses / Eyewear in high contrast
    drawRoundRect(
        color = InkBlack,
        topLeft = Offset(w * 0.32f, h * 0.38f),
        size = Size(w * 0.36f, h * 0.12f),
        cornerRadius = CornerRadius(4f, 4f)
    )

    // Torso / Collar
    val torso = Path().apply {
        moveTo(w * 0.18f, h * 0.98f)
        cubicTo(w * 0.25f, h * 0.68f, w * 0.75f, h * 0.68f, w * 0.82f, h * 0.98f)
        close()
    }
    drawPath(
        path = torso,
        brush = Brush.linearGradient(listOf(Color(0xFF8B5CF6), Color(0xFF38BDF8)))
    )
}

private fun DrawScope.drawMultiColorFlameVector() {
    val w = size.width
    val h = size.height

    // Outer Orange flame
    val outer = Path().apply {
        moveTo(w * 0.5f, 0f)
        cubicTo(w * 0.8f, h * 0.3f, w, h * 0.6f, w * 0.8f, h * 0.9f)
        cubicTo(w * 0.6f, h, w * 0.4f, h, w * 0.2f, h * 0.9f)
        cubicTo(0f, h * 0.6f, w * 0.2f, h * 0.3f, w * 0.5f, 0f)
        close()
    }
    drawPath(outer, color = TangerineOrange)

    // Inner Yellow flame core
    val inner = Path().apply {
        moveTo(w * 0.5f, h * 0.35f)
        cubicTo(w * 0.7f, h * 0.55f, w * 0.75f, h * 0.75f, w * 0.65f, h * 0.9f)
        cubicTo(w * 0.55f, h * 0.98f, w * 0.45f, h * 0.98f, w * 0.35f, h * 0.9f)
        cubicTo(w * 0.25f, h * 0.75f, w * 0.3f, h * 0.55f, w * 0.5f, h * 0.35f)
        close()
    }
    drawPath(inner, color = ElectricYellow)
}

private fun DrawScope.drawCornerMicroDoodles() {
    val w = size.width
    val h = size.height
    val doodleColor = Color(0xFF8B80A8).copy(alpha = 0.45f)

    // Top-left sparkle star & swirl
    drawSparkleAccent(cx = 24f, cy = 24f, sizePx = 14f, color = doodleColor)
    drawLine(color = doodleColor, start = Offset(14f, 38f), end = Offset(34f, 38f), strokeWidth = 1.5f, cap = StrokeCap.Round)

    // Top-right star & dot cluster
    drawSparkleAccent(cx = w - 24f, cy = 24f, sizePx = 14f, color = doodleColor)
    drawCircle(color = doodleColor, radius = 2.5f, center = Offset(w - 38f, 34f))

    // Bottom-left swirl loop
    drawLine(color = doodleColor, start = Offset(20f, h - 28f), end = Offset(40f, h - 28f), strokeWidth = 1.5f, cap = StrokeCap.Round)
    drawCircle(color = doodleColor, radius = 2.5f, center = Offset(30f, h - 38f))

    // Bottom-right star
    drawSparkleAccent(cx = w - 24f, cy = h - 24f, sizePx = 14f, color = doodleColor)
}

private fun DrawScope.drawShieldCheckmarkEmblem(color: Color) {
    val w = size.width
    val h = size.height

    // Shield contour
    val shield = Path().apply {
        moveTo(w * 0.5f, 0f)
        lineTo(w, h * 0.22f)
        lineTo(w * 0.84f, h * 0.72f)
        lineTo(w * 0.5f, h)
        lineTo(w * 0.16f, h * 0.72f)
        lineTo(0f, h * 0.22f)
        close()
    }
    drawPath(shield, color = color, style = Stroke(width = 2.5f))

    // Inner checkmark
    val check = Path().apply {
        moveTo(w * 0.30f, h * 0.50f)
        lineTo(w * 0.45f, h * 0.65f)
        lineTo(w * 0.72f, h * 0.36f)
    }
    drawPath(check, color = color, style = Stroke(width = 2.5f, cap = StrokeCap.Round))
}

private fun DrawScope.drawBookmarkRibbonVector(color: Color) {
    val w = size.width
    val h = size.height
    val path = Path().apply {
        moveTo(0f, 0f)
        lineTo(w, 0f)
        lineTo(w, h)
        lineTo(w * 0.5f, h * 0.7f)
        lineTo(0f, h)
        close()
    }
    drawPath(path, color = color)
}

private fun DrawScope.drawLockVector(color: Color) {
    val w = size.width
    val h = size.height
    drawArc(
        color = color,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(w * 0.25f, 0f),
        size = Size(w * 0.5f, h * 0.55f),
        style = Stroke(width = 2.2f)
    )
    drawRoundRect(
        color = color,
        topLeft = Offset(w * 0.15f, h * 0.45f),
        size = Size(w * 0.7f, h * 0.55f),
        cornerRadius = CornerRadius(4f, 4f)
    )
}

private fun DrawScope.drawSparkleStarCluster() {
    val w = size.width
    val h = size.height

    // Central big 4-point star in ElectricYellow #FFE747
    drawSparkleAccent(cx = w * 0.5f, cy = h * 0.5f, sizePx = 36f, color = ElectricYellow)

    // Top-right medium star in SoftLavender #C7B6F4
    drawSparkleAccent(cx = w * 0.78f, cy = h * 0.25f, sizePx = 20f, color = SoftLavender)

    // Bottom-left small star in SoftLavender #C7B6F4
    drawSparkleAccent(cx = w * 0.22f, cy = h * 0.75f, sizePx = 16f, color = SoftLavender)
}

private fun generateCustomSkillFromInput(input: String): ForgedCustomSkill {
    val lower = input.lowercase()
    return when {
        lower.contains("coworker") || lower.contains("credit") || lower.contains("idea") -> {
            ForgedCustomSkill(
                categoryPill = "DARK PSYCHOLOGY DEFENSE",
                title = "The Playful Spotlight Reversal",
                description = "Force passive-aggressive coworkers to explain their subtle remarks out loud on the spot: 'That sounds like an expansion on what I just shared—walk us through how you see step two working?'",
                readTimeSeconds = 90
            )
        }
        lower.contains("boundary") || lower.contains("no") || lower.contains("people") -> {
            ForgedCustomSkill(
                categoryPill = "RELATIONAL SOVEREIGNTY",
                title = "The Zero-Justification 'No'",
                description = "Refuse requests firmly without offering apologies or excuses: 'I'm unable to take that on, but thank you for thinking of me.'",
                readTimeSeconds = 75
            )
        }
        lower.contains("focus") || lower.contains("procrastinat") || lower.contains("distract") -> {
            ForgedCustomSkill(
                categoryPill = "DOPAMINE RESISTANCE",
                title = "The 5-Minute Gateway Ignition",
                description = "Eliminate startup friction by shrinking task commitment to 300 seconds with zero expectation of completion.",
                readTimeSeconds = 60
            )
        }
        else -> {
            ForgedCustomSkill(
                categoryPill = "TACTICAL COGNITION",
                title = "The Emotional Defusal Anchor",
                description = "Name the underlying emotion before responding to disarm instinctive conflict triggers: 'It seems like there is urgent pressure around this timeline.'",
                readTimeSeconds = 90
            )
        }
    }
}
