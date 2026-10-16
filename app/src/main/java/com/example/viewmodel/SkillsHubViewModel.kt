package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.model.BrainProfile
import com.example.data.model.MasteredSkill
import com.example.data.model.SkillState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SkillsHubViewModel : ViewModel() {
    private val _profile = MutableStateFlow(BrainProfile())
    val profile: StateFlow<BrainProfile> = _profile

    private val _skills = MutableStateFlow(listOf(
        MasteredSkill("1", "Deep Focus", SkillState.MASTERED, "Level 5"),
        MasteredSkill("2", "Cognitive Defense", SkillState.CRACKING, "In Progress"),
        MasteredSkill("3", "Impulse Control", SkillState.MASTERED, "Level 3")
    ))
    val skills: StateFlow<List<MasteredSkill>> = _skills
}
