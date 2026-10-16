package com.example.model

enum class OnboardingMode {
    AI_PERSONALIZATION,
    MANUAL_SELECTION
}

data class SkillCategory(
    val id: String,
    val name: String,
    val description: String,
    val defaultSkills: List<String>
)

data class SkillConfig(
    val activeCategoryIds: Set<String>,
    val activeSkillNames: Set<String>,
    val customSkills: List<String>,
    val aiInterestsPrompt: String,
    val onboardingMode: OnboardingMode
)

object StandardCategories {
    val list: List<SkillCategory> = listOf(
        SkillCategory(
            id = "cat_psychology",
            name = "Psychology and Human Behavior",
            description = "Cognitive biases, mental models, emotional regulation, and behavioral drivers.",
            defaultSkills = listOf("Cognitive Biases", "Sunk Cost Fallacy", "Dopamine & Habit Loops", "Dark Psychology Awareness")
        ),
        SkillCategory(
            id = "cat_communication",
            name = "Social Intelligence and Communication",
            description = "Active listening, conversational rhythm, tactical empathy, and charisma.",
            defaultSkills = listOf("Tactical Empathy", "The 4-Second Pause", "BLUF Executive Messaging", "Conflict De-escalation")
        ),
        SkillCategory(
            id = "cat_relationships",
            name = "Relationships and Body Language",
            description = "Subconscious body cues, personal boundaries, and high-trust rapport.",
            defaultSkills = listOf("Torso & Eye Alignment", "Differentiation of Self", "Micro-Expression Decoupling", "Active Validation")
        ),
        SkillCategory(
            id = "cat_fitness",
            name = "Fitness, Health, and Human Physiology",
            description = "Mitochondrial conditioning, circadian optimization, and muscular strength.",
            defaultSkills = listOf("Zone 2 Aerobic Base", "Circadian Sunlight Anchoring", "Metabolic Flexibility", "Diaphragmatic Breathing")
        ),
        SkillCategory(
            id = "cat_grooming",
            name = "Fashion, Grooming, and Personal Presentation",
            description = "Sartorial proportions, aesthetic silhouette, voice pitch, and personal presence.",
            defaultSkills = listOf("Silhouette & Hem Proportions", "Color Harmony Rule", "Minimalist Wardrobe Curation", "Vocal Resonance")
        ),
        SkillCategory(
            id = "cat_finance",
            name = "Finance and Money Management",
            description = "Compound growth, automated investing, cash flow margin, and debt elimination.",
            defaultSkills = listOf("Opportunity Cost of Capital", "Lifestyle Creep Immunity", "Automated Index Investing", "Asymmetric Risk")
        ),
        SkillCategory(
            id = "cat_negotiation",
            name = "Negotiation and Critical Thinking",
            description = "Calibrated questions, anchor audits, first principles logic, and game theory.",
            defaultSkills = listOf("The Accusation Audit", "The Power of 'No'", "First Principles Reduction", "Second-Order Consequence")
        ),
        SkillCategory(
            id = "cat_philosophy",
            name = "Philosophy and Personal Development",
            description = "Stoic resilience, existential clarity, voluntary discomfort, and agency.",
            defaultSkills = listOf("Stoic Dichotomy of Control", "Radical Ownership", "Memento Mori Perspective", "Negative Visualization")
        ),
        SkillCategory(
            id = "cat_technology",
            name = "Technology, Programming, and AI",
            description = "Prompt engineering, algorithmic awareness, system architecture, and automation.",
            defaultSkills = listOf("Few-Shot Prompt Engineering", "Feed Architecture Auditing", "Local LLM Tooling", "Digital Minimalism")
        ),
        SkillCategory(
            id = "cat_business",
            name = "Business and Entrepreneurship",
            description = "Distribution moats, unit economics, value propositions, and leverage.",
            defaultSkills = listOf("Distribution as the Moat", "Pricing Power & Positioning", "Asymmetric Leverage", "Customer Discovery")
        ),
        SkillCategory(
            id = "cat_creativity",
            name = "Creativity and Design",
            description = "Lateral problem solving, creative constraints, typography, and visual rhythm.",
            defaultSkills = listOf("Oblique Creative Constraints", "Visual Contrast & Hierarchy", "The Subtraction Method", "Idea Cross-Pollination")
        ),
        SkillCategory(
            id = "cat_life_skills",
            name = "Practical Life Skills",
            description = "Deep sleep architecture, crisis calm, negotiation of contracts, and daily order.",
            defaultSkills = listOf("Deep Sleep Protocols", "Emergency Decision Frameworks", "Rapid Information Synthesis", "Personal Ground Truth")
        )
    )
}
