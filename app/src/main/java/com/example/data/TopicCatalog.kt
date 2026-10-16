package com.example.data

import com.example.model.ArtShapeType
import com.example.model.ComprehensionQuestion
import com.example.model.LearningTopic
import com.example.model.StandardCategories
import com.example.ui.theme.AcidLime
import com.example.ui.theme.ElectricYellow
import com.example.ui.theme.HotPink
import com.example.ui.theme.InkBlack
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.TangerineOrange

object TopicCatalog {
    val allTopics: List<LearningTopic> = listOf(
        // 1. Psychology and Human Behavior
        LearningTopic(
            id = "dark_psych_sunk_cost",
            title = "The Sunk Cost Trap",
            category = "Psychology and Human Behavior",
            oneLineDescription = "Recognize when past investments of time or emotion blind you from quitting a losing battle.",
            whyItMatters = "Manipulators exploit our instinct to justify past pain. True maturity is realizing that past investments are gone forever; only future value matters.",
            estimatedLearningTimeMinutes = 10,
            keyConcepts = listOf(
                "Sunk costs cannot be recovered regardless of future outcome.",
                "Emotional escalation of commitment often masks the fear of admitting a mistake.",
                "The rational question is never 'How much have I put in?' but 'If I started today, would I enter?'"
            ),
            practicalTaskTitle = "Audit an Open Commitment",
            practicalTaskInstruction = "Identify one project, relationship habit, or subscription you maintain only because of past effort. Write down the hard truth: if you began fresh today, would you choose it?",
            reflectionPrompt = "What project or habit are you holding onto simply because of past investment?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "When evaluating whether to continue a failing project, which factor should you strictly IGNORE?",
                options = listOf(
                    "Future potential returns",
                    "Time, money, and effort already spent in the past",
                    "Current ongoing maintenance costs",
                    "Alternative opportunities available today"
                ),
                correctOptionIndex = 1,
                explanation = "Sunk costs are unrecoverable; rational decisions must only consider prospective future costs and benefits."
            ),
            primaryColor = TangerineOrange,
            secondaryColor = ElectricYellow,
            shapeType = ArtShapeType.ORGANIC_BLOBS
        ),
        LearningTopic(
            id = "dark_psych_gaslighting",
            title = "Reality Anchoring Against Gaslighting",
            category = "Psychology and Human Behavior",
            oneLineDescription = "Defend your cognitive autonomy by documenting facts and establishing personal ground truth.",
            whyItMatters = "Gaslighting relies on wearing down your confidence in your own memory. Grounding yourself with written timestamps and verified benchmarks makes manipulation impossible.",
            estimatedLearningTimeMinutes = 12,
            keyConcepts = listOf(
                "Gaslighters shift blame by questioning your sanity, recall, or emotional stability.",
                "External documentation (notes, messages, facts) prevents narrative rewriting.",
                "Refuse to debate basic sensory reality: 'I know what I experienced, and that is not up for debate.'"
            ),
            practicalTaskTitle = "Create an Objective Memory Log",
            practicalTaskInstruction = "In your personal notes, jot down one crucial decision or conversation from this week with exact quotes and outcomes before memory distorts it.",
            reflectionPrompt = "How do you protect your personal conviction when someone challenges your perception?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "What is the primary psychological vulnerability that gaslighting exploits?",
                options = listOf(
                    "Physical exhaustion",
                    "Self-doubt regarding one's own perception and memory",
                    "Desire for financial gain",
                    "Lack of technical expertise"
                ),
                correctOptionIndex = 1,
                explanation = "Gaslighting systematically undermines the target's trust in their own senses and memory to create psychological dependency."
            ),
            primaryColor = HotPink,
            secondaryColor = SoftLavender,
            shapeType = ArtShapeType.CONCENTRIC_RINGS
        ),
        LearningTopic(
            id = "psych_halo_effect",
            title = "The Halo Effect & Perception Bias",
            category = "Psychology and Human Behavior",
            oneLineDescription = "Disentangle superficial charisma from genuine competence and moral integrity.",
            whyItMatters = "When we see someone who is articulate or attractive, our brain automatically assumes they are also honest, intelligent, and capable. Learning to decouple these traits prevents costly errors in judgment.",
            estimatedLearningTimeMinutes = 9,
            keyConcepts = listOf(
                "A positive impression in one area unfairly influences opinion in other unrelated areas.",
                "Separate delivery style from evidentiary substance when evaluating claims.",
                "Look for documented track records instead of persuasive charm."
            ),
            practicalTaskTitle = "Perform a Decoupled Evaluation",
            practicalTaskInstruction = "Think of someone you admire. List one technical flaw or blind spot they have to restore realistic balance to your perception.",
            reflectionPrompt = "Where has someone's charisma or appearance misled your evaluation of their competence?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "What cognitive error defines the Halo Effect?",
                options = listOf(
                    "Assuming negative intentions in everyone you meet",
                    "Allowing one positive trait to bias your entire perception of a person's character",
                    "Remembering only the beginning of a presentation",
                    "Overestimating the rarity of everyday events"
                ),
                correctOptionIndex = 1,
                explanation = "The Halo Effect occurs when a single prominent positive trait causes observers to assume all other traits are equally admirable."
            ),
            primaryColor = ElectricYellow,
            secondaryColor = AcidLime,
            shapeType = ArtShapeType.SUNBURST_RAYS
        ),

        // 2. Social Intelligence and Communication
        LearningTopic(
            id = "comm_tactical_empathy",
            title = "Tactical Empathy & Labeling",
            category = "Social Intelligence and Communication",
            oneLineDescription = "Disarm interpersonal friction by neutralizing unstated emotional barriers before making requests.",
            whyItMatters = "People rarely listen until they feel deeply heard. Naming someone's underlying emotional state disarms their amygdala and builds instant trust.",
            estimatedLearningTimeMinutes = 10,
            keyConcepts = listOf(
                "Use neutral exploratory openers: 'It seems like...', 'It feels like...', 'It sounds like...'",
                "Never say 'I understand how you feel'—it sounds patronizing and draws focus back to you.",
                "After applying a label, stay silent for 4 full seconds to allow them to elaborate."
            ),
            practicalTaskTitle = "Practice The 4-Second Label",
            practicalTaskInstruction = "In your next conversation, listen for an underlying feeling. State: 'It sounds like [emotion] is creating friction,' then remain completely silent.",
            reflectionPrompt = "What emotion did you observe in someone today that was left unsaid?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "Why is following an emotional label with 4 seconds of silence critical?",
                options = listOf(
                    "It shows you have lost interest in the topic",
                    "It creates emotional space that invites the other person to confirm, correct, or expand",
                    "It gives you time to check your phone",
                    "It intimidates the speaker into submission"
                ),
                correctOptionIndex = 1,
                explanation = "Dynamic silence forces the other person's brain to process the label and voluntarily share more underlying truth."
            ),
            primaryColor = ElectricYellow,
            secondaryColor = TangerineOrange,
            shapeType = ArtShapeType.GEOMETRIC_PILL
        ),
        LearningTopic(
            id = "comm_bottom_line_up_front",
            title = "BLUF: Bottom Line Up Front",
            category = "Social Intelligence and Communication",
            oneLineDescription = "Communicate complex ideas with military precision and senior executive brevity.",
            whyItMatters = "Rambling before getting to the point signals insecurity and drains your listener's attention span. Senior leaders value clarity above all else.",
            estimatedLearningTimeMinutes = 8,
            keyConcepts = listOf(
                "State the conclusion, decision, or request in the very first sentence.",
                "Follow with 2-3 supporting bullet points only if necessary.",
                "Provide context only after the objective is crystal clear."
            ),
            practicalTaskTitle = "Condense an Email or Message",
            practicalTaskInstruction = "Draft an upcoming email or message. Cut the first two introductory sentences and put your direct call to action on line 1.",
            reflectionPrompt = "How did putting the bottom line first change the recipient's response or your own clarity?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "In the BLUF communication framework, where should your key conclusion or request appear?",
                options = listOf(
                    "In the closing signature paragraph",
                    "In the very first sentence",
                    "Hidden in an attached document",
                    "Only after establishing 5 minutes of historical background"
                ),
                correctOptionIndex = 1,
                explanation = "BLUF demands that the core decision or request is stated immediately in line 1 so readers grasp intent without cognitive friction."
            ),
            primaryColor = AcidLime,
            secondaryColor = SoftLavender,
            shapeType = ArtShapeType.DIAGONAL_STRIPES
        ),
        LearningTopic(
            id = "comm_ben_franklin_effect",
            title = "The Ben Franklin Reciprocity Effect",
            category = "Social Intelligence and Communication",
            oneLineDescription = "Turn skeptical rivals into allies by asking for a small, effortless favor.",
            whyItMatters = "Cognitive dissonance dictates that when someone does you a favor, their subconscious rationalizes that they must like or respect you to have done so.",
            estimatedLearningTimeMinutes = 9,
            keyConcepts = listOf(
                "Ask for minor assistance: borrow a book, ask for a book recommendation, or request a quick opinion.",
                "Thank them warmly without appearing indebted.",
                "Their mind resolves the action by attributing positive regard toward you."
            ),
            practicalTaskTitle = "Request a Small Micro-Favor",
            practicalTaskInstruction = "Ask an acquaintance or guarded coworker for a low-stakes piece of advice or quick book recommendation today.",
            reflectionPrompt = "How did asking for a favor shift the dynamic between you and someone you wanted to connect with?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "Why does asking someone for a small favor make them like you more?",
                options = listOf(
                    "They feel superior and pity you",
                    "Their brain justifies the helpful act by concluding you are worth helping",
                    "They hope you will pay them cash",
                    "It proves that you are incompetent"
                ),
                correctOptionIndex = 1,
                explanation = "The Ben Franklin effect relies on cognitive dissonance: people align their attitudes to match their helpful actions."
            ),
            primaryColor = HotPink,
            secondaryColor = ElectricYellow,
            shapeType = ArtShapeType.ORGANIC_BLOBS
        ),

        // 3. Relationships and Body Language
        LearningTopic(
            id = "body_power_pause",
            title = "The Power Pause & Eye Gaze",
            category = "Relationships and Body Language",
            oneLineDescription = "Command authority and calm charisma by mastering silence before you speak.",
            whyItMatters = "Insecure communicators rush to fill silence with filler words. Confident communicators embrace pregnant pauses to formulate high-impact thoughts.",
            estimatedLearningTimeMinutes = 10,
            keyConcepts = listOf(
                "When asked an important question, pause for 2 full seconds while maintaining calm eye contact.",
                "Eliminate fillers like 'um', 'like', and 'basically'; silence projects gravitas.",
                "Slow down your blink rate; rapid blinking signals stress and duress."
            ),
            practicalTaskTitle = "Execute Three Conscious Pauses",
            practicalTaskInstruction = "Today, whenever someone finishes a statement, count 'one-one-thousand, two-one-thousand' before you open your mouth to reply.",
            reflectionPrompt = "How did embracing silence feel in your interactions today?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "What message does pausing 2 seconds before answering a question project?",
                options = listOf(
                    "You forgot the answer",
                    "Calm deliberateness, authority, and emotional composure",
                    "Confusion and hesitation",
                    "Aggressive disinterest"
                ),
                correctOptionIndex = 1,
                explanation = "Taking a conscious pause demonstrates that you are not reactive and possess the gravitas to think before offering value."
            ),
            primaryColor = TangerineOrange,
            secondaryColor = AcidLime,
            shapeType = ArtShapeType.SUNBURST_RAYS
        ),
        LearningTopic(
            id = "body_navel_rule",
            title = "The Navel Rule & Torso Alignment",
            category = "Relationships and Body Language",
            oneLineDescription = "Signal total presence and uncover hidden disinterest by observing bellybutton orientation.",
            whyItMatters = "People can fake eye contact and smiles, but their feet and torso subconsciously point toward where they actually want to be.",
            estimatedLearningTimeMinutes = 8,
            keyConcepts = listOf(
                "Bellybutton pointing directly at someone signals genuine attention and high engagement.",
                "Torso angled away or feet pointing toward the exit indicates mental checkout.",
                "Align your own torso square to the listener to immediately project confidence."
            ),
            practicalTaskTitle = "Observe Physical Orientation",
            practicalTaskInstruction = "In your next conversation, observe where the other person's feet and torso are pointed without being awkward. Align your own torso directly toward them.",
            reflectionPrompt = "What subtle body language cues did you notice during social interactions today?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "In nonverbal body language analysis, what does a person's torso pointing toward the exit indicate?",
                options = listOf(
                    "Extreme romantic interest",
                    "A subconscious desire to conclude the interaction and leave",
                    "Superior intellectual agreement",
                    "Physical exhaustion from exercise"
                ),
                correctOptionIndex = 1,
                explanation = "The ventral (torso) direction reveals true subconscious engagement; orienting away from the speaker signals an urge to disengage."
            ),
            primaryColor = SoftLavender,
            secondaryColor = ElectricYellow,
            shapeType = ArtShapeType.GEOMETRIC_PILL
        ),
        LearningTopic(
            id = "body_active_boundaries",
            title = "Differentiation of Self in Relationships",
            category = "Relationships and Body Language",
            oneLineDescription = "Maintain deep intimate connection without absorbing other people's emotional reactivity.",
            whyItMatters = "Low differentiation leads to codependency, where another person's bad mood ruins your day. High differentiation lets you support others with calm empathy.",
            estimatedLearningTimeMinutes = 11,
            keyConcepts = listOf(
                "You are responsible for your emotional regulation, not for fixing someone else's storm.",
                "Respond with calm presence rather than frantic problem-solving or defensive withdrawal.",
                "Healthy love exists between two whole individuals, not two halves seeking completion."
            ),
            practicalTaskTitle = "Practice The Non-Anxious Presence",
            practicalTaskInstruction = "When someone expresses frustration or anxiety today, resist the urge to give advice or take it personally. Simply listen with calm steady breathing.",
            reflectionPrompt = "Whose emotional states do you tend to absorb as your own responsibility?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "What is the hallmark of high differentiation of self?",
                options = listOf(
                    "Completely isolating yourself from all human relationships",
                    "Remaining emotionally connected while maintaining separate personal emotional sovereignty",
                    "Fixing everyone's problems immediately",
                    "Agreeing with everything your partner says"
                ),
                correctOptionIndex = 1,
                explanation = "Differentiation is the ability to stay close to others without losing your own sense of self or adopting their emotional reactivity."
            ),
            primaryColor = HotPink,
            secondaryColor = TangerineOrange,
            shapeType = ArtShapeType.RETRO_ARCH
        ),

        // 4. Fitness, Health, and Human Physiology
        LearningTopic(
            id = "fit_zone_two",
            title = "Zone 2 Cardiovascular Base",
            category = "Fitness, Health, and Human Physiology",
            oneLineDescription = "Build elite mitochondrial density and metabolic longevity through conversational-pace cardio.",
            whyItMatters = "High-intensity intervals get hype, but Zone 2 aerobic base builds cellular engine efficiency, insulin sensitivity, and long-term stamina.",
            estimatedLearningTimeMinutes = 12,
            keyConcepts = listOf(
                "Zone 2 is a steady pace where you can still hold a conversation, but with effort.",
                "It trains mitochondria to burn fatty acids efficiently without producing excessive lactate.",
                "Aim for 150-180 minutes per week broken into 45-minute blocks."
            ),
            practicalTaskTitle = "Check Your Nasal Breathing Pace",
            practicalTaskInstruction = "Next time you walk briskly or jog, breathe exclusively through your nose. If you must open your mouth to gasp, slow down.",
            reflectionPrompt = "Are you balancing your high-stress outputs with restorative aerobic base work?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "What is the primary cellular benefit of Zone 2 training?",
                options = listOf(
                    "Maximum lactic acid production in muscles",
                    "Increased mitochondrial efficiency and density for burning fat",
                    "Rapid depletion of muscle glycogen",
                    "Immediate maximum heart rate spike"
                ),
                correctOptionIndex = 1,
                explanation = "Zone 2 specifically trains mitochondria in slow-twitch fibers to clear lactate and maximize aerobic lipid oxidation."
            ),
            primaryColor = AcidLime,
            secondaryColor = ElectricYellow,
            shapeType = ArtShapeType.ORGANIC_BLOBS
        ),
        LearningTopic(
            id = "fit_circadian_sunlight",
            title = "Circadian Sunlight Anchoring",
            category = "Fitness, Health, and Human Physiology",
            oneLineDescription = "Reset your sleep-wake clock and optimize cortisol-melatonin timing with morning photon exposure.",
            whyItMatters = "Morning photons hitting retinal ganglion cells stimulate dopamine release, trigger alertness, and set a 14-hour countdown timer for natural melatonin release at night.",
            estimatedLearningTimeMinutes = 8,
            keyConcepts = listOf(
                "View sunlight within 30-60 minutes of waking for 10-15 minutes (or 20-30 min on cloudy days).",
                "Do not view through windows or sunglasses; glass filters out crucial lux and UV wavelengths.",
                "This anchors your suprachiasmatic nucleus (master biological clock) for deep night sleep."
            ),
            practicalTaskTitle = "Step Outside for 10 Minutes",
            practicalTaskInstruction = "Within an hour of sunrise tomorrow, step outside into natural ambient light without sunglasses for at least 10 minutes.",
            reflectionPrompt = "How does morning natural light affect your energy compared to scrolling a screen in bed?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "Why should morning sunlight be viewed directly rather than through window glass?",
                options = listOf(
                    "Window glass blocks the specific lux intensity and wavelengths needed to trigger retinal receptors",
                    "Windows make the sun too bright to look at",
                    "Glass turns sunlight into dangerous radiation",
                    "There is no difference between looking through glass or outdoors"
                ),
                correctOptionIndex = 0,
                explanation = "Window glass filters out critical lux levels and photon frequencies required to stimulate melanopsin-containing retinal ganglion cells."
            ),
            primaryColor = TangerineOrange,
            secondaryColor = ElectricYellow,
            shapeType = ArtShapeType.SUNBURST_RAYS
        ),
        LearningTopic(
            id = "fit_physiological_sigh",
            title = "The Physiological Sigh for Instant Calm",
            category = "Fitness, Health, and Human Physiology",
            oneLineDescription = "Reset autonomic nervous system arousal in under 30 seconds using double inhalations.",
            whyItMatters = "When stressed, pulmonary alveoli in the lungs collapse and CO2 accumulates in the blood. The physiological sigh re-inflates alveoli and triggers vagal nerve slowing of heart rate.",
            estimatedLearningTimeMinutes = 7,
            keyConcepts = listOf(
                "Two inhales through the nose: one deep inhale followed immediately by a sharp second 'top-off' inhale.",
                "Follow with a long, slow, unforced exhalation through the mouth.",
                "Repeating this 2-3 times dramatically lowers heart rate and autonomic anxiety."
            ),
            practicalTaskTitle = "Execute Three Physiological Sighs",
            practicalTaskInstruction = "Right now: take two quick inhales through your nose, then a long slow sigh out through your mouth. Repeat twice and note the muscular release.",
            reflectionPrompt = "How quickly did physical breathwork shift your mental tension today?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "What physiological event happens during the double-inhalation physiological sigh?",
                options = listOf(
                    "It causes sudden hyperventilation",
                    "It re-inflates collapsed pulmonary alveoli and increases oxygen-carbon dioxide exchange efficiency",
                    "It shuts off the parasympathetic nervous system",
                    "It raises blood pressure to maximum levels"
                ),
                correctOptionIndex = 1,
                explanation = "The second sharp inhale pops open collapsed lung alveoli, allowing efficient offloading of carbon dioxide and rapid vagal deceleration."
            ),
            primaryColor = SoftLavender,
            secondaryColor = AcidLime,
            shapeType = ArtShapeType.CONCENTRIC_RINGS
        ),

        // 5. Fashion, Grooming, and Personal Presentation
        LearningTopic(
            id = "groom_proportions",
            title = "Sartorial Proportions & Silhouette",
            category = "Fashion, Grooming, and Personal Presentation",
            oneLineDescription = "Elevate personal presence by prioritizing silhouette, hem drape, and shoulder fit over brand logos.",
            whyItMatters = "True visual elegance lies in geometry and proportion. Expensive clothes that fit poorly look sloppy; well-tailored basics command instant respect.",
            estimatedLearningTimeMinutes = 8,
            keyConcepts = listOf(
                "Shoulder seams must align precisely with the clavicle-acromion bone intersection.",
                "Trousers should break cleanly at the top of the shoe without messy stacking.",
                "Color harmony: adhere to a 3-color rule (two neutrals, one restrained accent)."
            ),
            practicalTaskTitle = "The Mirror Silhouette Audit",
            practicalTaskInstruction = "Look in a full-length mirror wearing your favorite outfit. Step back 6 feet. Ignore brands and evaluate only the lines, shoulder fit, and hem lengths.",
            reflectionPrompt = "What small adjustment in tailoring or silhouette could elevate your everyday presence?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "In tailoring, where should a jacket or shirt shoulder seam sit?",
                options = listOf(
                    "Drooping 2 inches down the bicep",
                    "Directly at the natural edge of your shoulder bone",
                    "Pulled tight against the neck",
                    "It does not matter if the brand is expensive"
                ),
                correctOptionIndex = 1,
                explanation = "A correct shoulder seam sits right at the acromion bone point, framing the upper body in clean athletic proportion."
            ),
            primaryColor = SoftLavender,
            secondaryColor = TangerineOrange,
            shapeType = ArtShapeType.RETRO_ARCH
        ),
        LearningTopic(
            id = "groom_color_harmony",
            title = "The 3-Color Wardrobe Discipline",
            category = "Fashion, Grooming, and Personal Presentation",
            oneLineDescription = "Eliminate visual clutter by limiting daily attire to two core neutrals and one focal accent.",
            whyItMatters = "Visual sophistication is defined by subtraction. Dressing in balanced tonal neutrals with a single deliberate focal point makes everyday outfits look effortless.",
            estimatedLearningTimeMinutes = 8,
            keyConcepts = listOf(
                "Foundational neutrals: navy, charcoal, black, olive, cream, camel, and crisp white.",
                "Never wear more than three distinct colors in a single outfit (including shoes and outerwear).",
                "Texture contrast (cashmere, brushed wool, denim, matte leather) adds depth without loud colors."
            ),
            practicalTaskTitle = "Audit an Outfit's Palette",
            practicalTaskInstruction = "Count the colors in what you are wearing right now. If there are 4 or more, remove or replace one piece to restore clean balance.",
            reflectionPrompt = "How does visual restraint in clothing free up mental focus during your day?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "What is the recommended ratio in the 3-color wardrobe discipline?",
                options = listOf(
                    "Three bright fluorescent neon colors",
                    "Two grounding neutral tones and at most one restrained accent color",
                    "All different shades of tie-dye",
                    "Five colors matching every accessory"
                ),
                correctOptionIndex = 1,
                explanation = "Grounding an outfit in two neutrals prevents visual chaos and allows one subtle accent to create clean contrast."
            ),
            primaryColor = HotPink,
            secondaryColor = AcidLime,
            shapeType = ArtShapeType.GEOMETRIC_PILL
        ),
        LearningTopic(
            id = "groom_vocal_resonance",
            title = "Vocal Resonance & Chest Projection",
            category = "Fashion, Grooming, and Personal Presentation",
            oneLineDescription = "Project authority and warmth by lowering vocal pitch and speaking from the chest cavity.",
            whyItMatters = "Speaking from the throat or nasal passage produces thin, anxious, strained tones. Anchoring resonance in the lower chest conveys grounded competence and trustworthiness.",
            estimatedLearningTimeMinutes = 9,
            keyConcepts = listOf(
                "Place your hand flat on your sternum while speaking; feel the vibration.",
                "Inhale deeply through the diaphragm rather than lifting the shoulders.",
                "Drop your pitch slightly at the end of statements instead of using upward questioning inflection."
            ),
            practicalTaskTitle = "The Chest Humming Drill",
            practicalTaskInstruction = "Place your palm on your chest. Hum on a comfortable low pitch until you feel strong vibration under your hand. Speak five words in that same resonant register.",
            reflectionPrompt = "Do you speak with an upward questioning inflection when you should be making definitive statements?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "Why should definitive statements avoid upward vocal inflection at the end of sentences?",
                options = listOf(
                    "Upward inflection signals hesitation, insecurity, or subconscious pleading for approval",
                    "It makes you speak too quietly",
                    "It damages the vocal cords permanently",
                    "It sounds too authoritative"
                ),
                correctOptionIndex = 0,
                explanation = "Up-talk turns statements into questions, signaling to the listener that the speaker lacks conviction in their own words."
            ),
            primaryColor = ElectricYellow,
            secondaryColor = InkBlack,
            shapeType = ArtShapeType.DIAGONAL_STRIPES
        ),

        // 6. Finance and Money Management
        LearningTopic(
            id = "fin_lifestyle_creep",
            title = "Taming Lifestyle Creep",
            category = "Finance and Money Management",
            oneLineDescription = "Protect your financial freedom by freezing fixed baseline expenses as income grows.",
            whyItMatters = "High earners often stay broke because spending expands to meet cash flow. Wealth is not what you spend, but what you keep and compound.",
            estimatedLearningTimeMinutes = 10,
            keyConcepts = listOf(
                "Allocate at least 60% of all future raises, bonuses, and windfalls directly to investments.",
                "Distinguish between comforts that save meaningful time vs luxuries that require maintenance.",
                "Financial peace comes from a wide margin between your income and your living costs."
            ),
            practicalTaskTitle = "Audit Recurring Bleeds",
            practicalTaskInstruction = "Review your bank statements from the last 30 days. Cancel one recurring subscription or membership you have not used this month.",
            reflectionPrompt = "What expense did you adopt in the past year that added zero lasting happiness?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "What is the defining symptom of lifestyle creep?",
                options = listOf(
                    "Spending drops as your savings increase",
                    "Fixed living expenses automatically inflate in lockstep with income gains",
                    "Investing 80% of any unexpected bonus",
                    "Refusing to buy depreciating assets"
                ),
                correctOptionIndex = 1,
                explanation = "Lifestyle creep happens when discretionary luxuries become perceived necessities as cash flow increases, keeping net worth flat."
            ),
            primaryColor = AcidLime,
            secondaryColor = TangerineOrange,
            shapeType = ArtShapeType.GEOMETRIC_PILL
        ),
        LearningTopic(
            id = "fin_rule_of_72",
            title = "The Rule of 72 & Compound Friction",
            category = "Finance and Money Management",
            oneLineDescription = "Calculate compounding timelines instantly and understand the devastating drag of hidden fees.",
            whyItMatters = "Divide 72 by your annual interest rate to find out how many years it takes your money to double. A 1% management fee costs you over 30% of your total lifetime wealth.",
            estimatedLearningTimeMinutes = 10,
            keyConcepts = listOf(
                "72 / 8% return = 9 years to double your capital.",
                "72 / 12% return = 6 years to double your capital.",
                "Hidden fund fees (1-2% expense ratios) quietly cannibalize multiple doubling cycles over 30 years."
            ),
            practicalTaskTitle = "Calculate Your Doubling Time",
            practicalTaskInstruction = "Look up the approximate return of your primary savings or index fund. Divide 72 by that rate to know when your balance will double.",
            reflectionPrompt = "Are you paying unnecessary advisory fees on investments that could be in low-cost index funds?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "Using the Rule of 72, approximately how many years will it take for money growing at 6% annually to double?",
                options = listOf("6 years", "12 years", "18 years", "72 years"),
                correctOptionIndex = 1,
                explanation = "72 divided by 6 equals 12 years to double the initial principal."
            ),
            primaryColor = ElectricYellow,
            secondaryColor = SoftLavender,
            shapeType = ArtShapeType.SUNBURST_RAYS
        ),
        LearningTopic(
            id = "fin_asymmetric_risk",
            title = "Asymmetric Bets vs The Risk of Ruin",
            category = "Finance and Money Management",
            oneLineDescription = "Structure investments with limited capped downside and uncapped exponential upside.",
            whyItMatters = "Never risk what you need to gain what you simply want. The primary goal of financial survival is avoiding the mathematical certainty of the risk of ruin.",
            estimatedLearningTimeMinutes = 11,
            keyConcepts = listOf(
                "Avoid leverage that can force liquidation during temporary market drawdowns.",
                "An asymmetric bet has a known loss of 1x, but potential upside of 10x to 50x.",
                "If you lose 50%, you need a 100% gain just to get back to even."
            ),
            practicalTaskTitle = "Audit Downside Vulnerabilities",
            practicalTaskInstruction = "Identify any investment or debt where an extreme market shock could wipe out your entire emergency cushion. Formulate a risk reduction plan.",
            reflectionPrompt = "What single vulnerability in your finances could trigger irreversible ruin?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "If an investment loses 50% of its value, what percentage gain is required to return to the original break-even point?",
                options = listOf("50%", "75%", "100%", "200%"),
                correctOptionIndex = 2,
                explanation = "A drop from $100 to $50 is a 50% loss; climbing from $50 back to $100 requires a 100% gain."
            ),
            primaryColor = TangerineOrange,
            secondaryColor = HotPink,
            shapeType = ArtShapeType.CONCENTRIC_RINGS
        ),

        // 7. Negotiation and Critical Thinking
        LearningTopic(
            id = "neg_accusation_audit",
            title = "The Accusation Audit",
            category = "Negotiation and Critical Thinking",
            oneLineDescription = "Neutralize the other party's negative assumptions before they can even voice them.",
            whyItMatters = "When entering a difficult discussion, list every terrible thing the other person might think about you. Bringing their fears into the light disarms them completely.",
            estimatedLearningTimeMinutes = 10,
            keyConcepts = listOf(
                "Open with humility: 'You probably think I'm being demanding, selfish, or unprepared...'",
                "By stating their criticisms first, you deny them the leverage of springing it on you.",
                "It shifts their posture from attack to reassurance."
            ),
            practicalTaskTitle = "Draft an Accusation Audit",
            practicalTaskInstruction = "Think of a tough conversation you need to have. Write down the 3 worst things they could possibly say about your stance.",
            reflectionPrompt = "What unvoiced objection is holding back your next crucial deal or conversation?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "How does voicing the other party's potential criticisms first disarm them?",
                options = listOf(
                    "It admits you are guilty and terminates the negotiation",
                    "It exposes and diffuses negative emotions, shifting them from defensive to empathetic",
                    "It confuses them with excessive legal jargon",
                    "It proves that you do not care about their feelings"
                ),
                correctOptionIndex = 1,
                explanation = "Proactively articulating negative thoughts removes their weaponized shock value and encourages the listener to reassure you."
            ),
            primaryColor = HotPink,
            secondaryColor = SoftLavender,
            shapeType = ArtShapeType.ORGANIC_BLOBS
        ),
        LearningTopic(
            id = "neg_first_principles",
            title = "First Principles Deconstruction",
            category = "Negotiation and Critical Thinking",
            oneLineDescription = "Boil problems down to their most fundamental truths instead of reasoning by analogy.",
            whyItMatters = "Reasoning by analogy means copying what others do with slight tweaks. First principles thinking strips away assumptions to discover what is physically and logically possible.",
            estimatedLearningTimeMinutes = 11,
            keyConcepts = listOf(
                "Identify assumptions: 'Why do we do it this way? Because everyone else does.'",
                "Deconstruct to raw physics, cost of materials, and axiomatic truths.",
                "Reconstruct a solution from the ground up without legacy dogma."
            ),
            practicalTaskTitle = "Deconstruct One Dogma",
            practicalTaskInstruction = "Pick one process in your work that feels slow. Ask 'Why?' 5 times until you reach the fundamental physical constraint.",
            reflectionPrompt = "What standard assumption in your industry or life is simply unexamined tradition?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "How does first principles reasoning differ from reasoning by analogy?",
                options = listOf(
                    "Reasoning by analogy is based on mathematics, while first principles is guessing",
                    "First principles deconstructs to foundational physical truths rather than copying existing conventions",
                    "First principles relies on popular consensus and peer review",
                    "Reasoning by analogy is faster and always produces breakthroughs"
                ),
                correctOptionIndex = 1,
                explanation = "First principles reasoning breaks problems down to fundamental truths that cannot be deduced any further, avoiding copying errors."
            ),
            primaryColor = AcidLime,
            secondaryColor = ElectricYellow,
            shapeType = ArtShapeType.DIAGONAL_STRIPES
        ),
        LearningTopic(
            id = "neg_power_of_no",
            title = "The Power of 'No' in Negotiation",
            category = "Negotiation and Critical Thinking",
            oneLineDescription = "Give the other party control by encouraging them to say 'no' instead of hunting for fake 'yeses'.",
            whyItMatters = "People feel trapped and defensive when pushed for a 'yes'. When they say 'no', they feel safe, autonomous, and in total control, opening real dialogue.",
            estimatedLearningTimeMinutes = 10,
            keyConcepts = listOf(
                "Flip questions: Instead of 'Do you agree?', ask 'Is this totally off track?'",
                "Instead of 'Do you have a few minutes?', ask 'Is now a bad time to talk?'",
                "A 'no' marks the beginning of the real negotiation, not the end."
            ),
            practicalTaskTitle = "Flip a 'Yes' Question to a 'No'",
            practicalTaskInstruction = "Reframe your next scheduling message from 'Do you have time?' to 'Would it be unreasonable to connect for 5 minutes today?'",
            reflectionPrompt = "How does giving someone permission to say 'no' change their defensiveness?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "Why is asking a 'no-oriented' question more effective than pushing for an immediate 'yes'?",
                options = listOf(
                    "Saying 'no' gives people a sense of safety, autonomy, and control",
                    "It confuses the counterparty into making concessions",
                    "It insults them so they give up",
                    "It shows you do not actually want to reach an agreement"
                ),
                correctOptionIndex = 0,
                explanation = "Saying 'no' preserves the respondent's psychological autonomy, lowering their defenses and creating an honest space for exploration."
            ),
            primaryColor = TangerineOrange,
            secondaryColor = ElectricYellow,
            shapeType = ArtShapeType.GEOMETRIC_PILL
        ),

        // 8. Philosophy and Personal Development
        LearningTopic(
            id = "phil_stoic_dichotomy",
            title = "The Stoic Dichotomy of Control",
            category = "Philosophy and Personal Development",
            oneLineDescription = "Sever anxiety by strictly distinguishing between what you control and what you cannot.",
            whyItMatters = "Suffering stems from tying peace of mind to external outcomes. You control your intentions, actions, and judgments; everything else is indifferent.",
            estimatedLearningTimeMinutes = 10,
            keyConcepts = listOf(
                "Internal: Your judgment, effort, preparation, response, integrity.",
                "External: Other people's opinions, market moves, weather, the past, the future.",
                "Focus 100% of your energy on the internal circle; let go of the external."
            ),
            practicalTaskTitle = "The Two-Column Triage",
            practicalTaskInstruction = "Draw a line down a piece of paper. On the left: things you worry about that you cannot control. Cross them out. On the right: actions you can take today.",
            reflectionPrompt = "What outside factor are you stressing over that is fundamentally beyond your control?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "According to Epictetus, which of the following is completely under your direct control?",
                options = listOf(
                    "Your reputation and public standing",
                    "Your personal judgments, desires, and choices",
                    "The state of the global economy",
                    "Whether someone accepts your proposal"
                ),
                correctOptionIndex = 1,
                explanation = "Stoicism holds that only your inner judgments, desires, and conscious choices are fully within your sovereign control."
            ),
            primaryColor = AcidLime,
            secondaryColor = HotPink,
            shapeType = ArtShapeType.CONCENTRIC_RINGS
        ),
        LearningTopic(
            id = "phil_memento_mori",
            title = "Memento Mori & Radical Urgency",
            category = "Philosophy and Personal Development",
            oneLineDescription = "Clarify daily priorities and banish trivial drama by remembering your mortality.",
            whyItMatters = "You could leave life right now; let that determine what you do and say and think. Remembering that our days are finite cuts through petty vanity and procrastination.",
            estimatedLearningTimeMinutes = 9,
            keyConcepts = listOf(
                "Death is not in the distant future; the time already passed belongs to death.",
                "Trivial annoyances evaporate when held against the certainty of mortality.",
                "Spend today's hours on what truly matters to you and your loved ones."
            ),
            practicalTaskTitle = "The Mortality Priority Filter",
            practicalTaskInstruction = "Take your daily to-do list. Ask yourself: 'If this was my final month, would I waste my life on this petty grievance?' Drop the fluff.",
            reflectionPrompt = "What trivial worry or grudge will mean nothing in five years?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "What is the primary psychological purpose of the Stoic practice of Memento Mori?",
                options = listOf(
                    "To generate morbid depression and nihilism",
                    "To create clarifying urgency and banish trivial distractions from your life",
                    "To obsess over funeral planning",
                    "To avoid making long-term plans"
                ),
                correctOptionIndex = 1,
                explanation = "Memento Mori uses the reality of mortality as an active lens to strip away superficial noise and prioritize meaningful action."
            ),
            primaryColor = TangerineOrange,
            secondaryColor = SoftLavender,
            shapeType = ArtShapeType.SUNBURST_RAYS
        ),
        LearningTopic(
            id = "phil_premeditatio",
            title = "Premeditatio Malorum (Negative Visualization)",
            category = "Philosophy and Personal Development",
            oneLineDescription = "Inoculate your mind against panic by mentally preparing for worst-case obstacles.",
            whyItMatters = "Optimism blinds us to real risks. By calmly premeditating potential setbacks in advance, nothing catches you off guard, and you develop deep gratitude for current peace.",
            estimatedLearningTimeMinutes = 10,
            keyConcepts = listOf(
                "Visualize a project going wrong: delays, lost clients, system failures.",
                "Formulate calm contingency protocols while you are still emotionally composed.",
                "Appreciate the things you currently take for granted before they are removed."
            ),
            practicalTaskTitle = "Run a Pre-Mortem on Your Next Goal",
            practicalTaskInstruction = "Imagine it is 6 months from now and your main goal has failed. Write down the 3 reasons why it failed, and install safeguards today.",
            reflectionPrompt = "What potential obstacle have you been avoiding thinking about out of wishful optimism?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "How does Premeditatio Malorum build psychological resilience?",
                options = listOf(
                    "It ensures that bad things will never happen",
                    "It mentally inoculates against shock and prepares practical contingency responses",
                    "It invites bad luck into your life through negative thinking",
                    "It prevents you from starting any new projects"
                ),
                correctOptionIndex = 1,
                explanation = "Anticipating potential adversity removes the paralyzing shock of unexpected setbacks and strengthens emotional composure."
            ),
            primaryColor = ElectricYellow,
            secondaryColor = AcidLime,
            shapeType = ArtShapeType.ORGANIC_BLOBS
        ),

        // 9. Technology, Programming, and AI
        LearningTopic(
            id = "tech_prompt_few_shot",
            title = "Few-Shot Prompt Engineering",
            category = "Technology, Programming, and AI",
            oneLineDescription = "Direct large language models with extreme accuracy using structured input-output exemplars.",
            whyItMatters = "Vague prompts produce generic hallucinations. Providing 2-3 precise examples in your prompt steers the model's latent space directly toward your desired format.",
            estimatedLearningTimeMinutes = 10,
            keyConcepts = listOf(
                "Show, don't just tell: supply clear Input -> Output pairs in the system prompt.",
                "Define the persona, output schema (e.g. JSON), and negative constraints explicitly.",
                "Chain-of-thought: instruct the model to 'think step by step before answering'."
            ),
            practicalTaskTitle = "Craft a Few-Shot Template",
            practicalTaskInstruction = "Write a reusable AI prompt for a repetitive task you do. Include exactly two high-quality example inputs and expected outputs before your prompt text.",
            reflectionPrompt = "How did providing clear examples improve the precision of your AI tool outputs?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "Why is providing 2-3 input/output examples (few-shot) superior to raw zero-shot prompting?",
                options = listOf(
                    "It consumes more memory and makes the server crash",
                    "It constrains pattern generation by clearly modeling desired tone, formatting, and syntax",
                    "It disables the AI model's safety filters",
                    "It forces the model to search the web"
                ),
                correctOptionIndex = 1,
                explanation = "In-context exemplars anchor the language model's probability distribution to the exact structural format you require."
            ),
            primaryColor = TangerineOrange,
            secondaryColor = SoftLavender,
            shapeType = ArtShapeType.GEOMETRIC_PILL
        ),
        LearningTopic(
            id = "tech_feed_architecture",
            title = "Feed Architecture & Attention Sovereignty",
            category = "Technology, Programming, and AI",
            oneLineDescription = "Understand recommendation engine mechanics to reclaim sovereign control over your attention.",
            whyItMatters = "Algorithmic recommendation engines are tuned to maximize watch time through outrage, envy, and variable rewards. If you do not curate your inputs, algorithms dictate your identity.",
            estimatedLearningTimeMinutes = 9,
            keyConcepts = listOf(
                "Algorithms optimize for retention, not wisdom, truth, or well-being.",
                "Switch feeds from algorithmic discovery to chronological subscriptions wherever possible.",
                "Actively train your algorithm: aggressive 'Not Interested' clicks prune toxic feed recommendations."
            ),
            practicalTaskTitle = "Purge Five Algorithmic Triggers",
            practicalTaskInstruction = "Open your primary social feed. Tap 'Not Interested' or unfollow five accounts that trigger outrage, vanity, or FOMO.",
            reflectionPrompt = "What content inputs are consuming your subconscious thoughts without your consent?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "What is the primary metric optimized by modern social media recommendation algorithms?",
                options = listOf(
                    "User long-term wisdom and happiness",
                    "Time spent, user retention, and engagement velocity",
                    "Scientific accuracy and factual balance",
                    "Reducing digital screen time"
                ),
                correctOptionIndex = 1,
                explanation = "Social media algorithms optimize for attention capture and time spent on platform to maximize ad impressions."
            ),
            primaryColor = HotPink,
            secondaryColor = ElectricYellow,
            shapeType = ArtShapeType.CONCENTRIC_RINGS
        ),
        LearningTopic(
            id = "tech_local_llm_quant",
            title = "Local LLMs & Weight Quantization",
            category = "Technology, Programming, and AI",
            oneLineDescription = "Run private, offline artificial intelligence on consumer hardware using quantized model weights.",
            whyItMatters = "Sending confidential personal thoughts or client data to cloud APIs carries privacy and subscription risks. Quantized 4-bit and 8-bit models let you run AI directly on your laptop or phone.",
            estimatedLearningTimeMinutes = 11,
            keyConcepts = listOf(
                "Quantization reduces 16-bit floating-point weights to 4-bit or 8-bit integers with minimal accuracy loss.",
                "Enables 7B to 14B parameter models to fit inside 8GB to 16GB of unified memory.",
                "Complete privacy: zero internet connectivity required for inference."
            ),
            practicalTaskTitle = "Explore Local Model Options",
            practicalTaskInstruction = "Search for an open-source local model runner (like Ollama or llama.cpp) and note how many gigabytes of RAM your device has available for local weights.",
            reflectionPrompt = "What personal or business tasks would benefit from 100% private, on-device AI?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "What does model weight quantization achieve in modern AI?",
                options = listOf(
                    "It rewrites the code in Python",
                    "It compresses numeric precision (e.g. from 16-bit to 4-bit) to drastically reduce RAM usage while preserving most model capability",
                    "It connects the model to live cloud servers",
                    "It trains the model on new data"
                ),
                correctOptionIndex = 1,
                explanation = "Quantization reduces the memory footprint and compute demands of model weights, allowing modern LLMs to run on edge and consumer devices."
            ),
            primaryColor = AcidLime,
            secondaryColor = TangerineOrange,
            shapeType = ArtShapeType.SUNBURST_RAYS
        ),

        // 10. Business and Entrepreneurship
        LearningTopic(
            id = "biz_distribution_advantage",
            title = "Distribution as the Moat",
            category = "Business and Entrepreneurship",
            oneLineDescription = "Understand why superior distribution consistently outperforms superior product features.",
            whyItMatters = "First-time founders obsess over product; second-time founders obsess over distribution. If you build it, they will not come unless you own the rails to reach them.",
            estimatedLearningTimeMinutes = 12,
            keyConcepts = listOf(
                "Build distribution channels before you finish building the product.",
                "High switching costs and network effects protect margins better than features.",
                "Own your customer relationship (email lists, direct access) rather than renting it on third-party algorithms."
            ),
            practicalTaskTitle = "Map Your Distribution Channel",
            practicalTaskInstruction = "For any skill or project you want to launch, write down how the first 100 people will discover it without paid ads.",
            reflectionPrompt = "Where do your prospective customers or collaborators gather right now?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "According to venture capitalist Peter Thiel, why do most failed startups fail?",
                options = listOf(
                    "Their product features were insufficient",
                    "Failure in distribution and customer acquisition, not product technology",
                    "Their office space was too small",
                    "They hired too many engineers"
                ),
                correctOptionIndex = 1,
                explanation = "Poor distribution is the number one cause of startup failure; even great products fail if customers cannot be economically reached."
            ),
            primaryColor = HotPink,
            secondaryColor = ElectricYellow,
            shapeType = ArtShapeType.SUNBURST_RAYS
        ),
        LearningTopic(
            id = "biz_value_ladder",
            title = "The Value Ladder & Customer Lifetime Value",
            category = "Business and Entrepreneurship",
            oneLineDescription = "Ascend customers through low-friction entry points to high-trust premium partnerships.",
            whyItMatters = "Selling a high-ticket service to a cold stranger is nearly impossible. A value ladder builds trust step-by-step with low-cost entry points that prove competence.",
            estimatedLearningTimeMinutes = 10,
            keyConcepts = listOf(
                "Lead magnet: high-value free insight that solves one acute micro-problem.",
                "Tripwire: low-cost entry product that turns viewers into paying customers.",
                "Core offer & backend retainer: high-touch transformation for dedicated clients."
            ),
            practicalTaskTitle = "Sketch a 3-Step Value Ladder",
            practicalTaskInstruction = "Draft three tiers for an offering: a free resource, a $20 product, and a $500 service that naturally solve progressive problems.",
            reflectionPrompt = "What low-friction entry point can you create to let people test your expertise?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "What is the primary purpose of a 'tripwire' offer in a business value ladder?",
                options = listOf(
                    "To generate maximum quarterly profit immediately",
                    "To transition a casual audience member into a paying customer with minimal financial hesitation",
                    "To trick people into recurring subscriptions",
                    "To replace your core product line"
                ),
                correctOptionIndex = 1,
                explanation = "A low-friction entry product breaks the psychological barrier of payment, establishing a transaction relationship."
            ),
            primaryColor = AcidLime,
            secondaryColor = SoftLavender,
            shapeType = ArtShapeType.GEOMETRIC_PILL
        ),
        LearningTopic(
            id = "biz_pricing_power",
            title = "Pricing Power & Positioning",
            category = "Business and Entrepreneurship",
            oneLineDescription = "Escape commoditization by charging based on client value delivered rather than hours worked.",
            whyItMatters = "Warren Buffett calls pricing power the single most important metric of a great business. If you compete on price, you race to the bottom; if you compete on unique value, you command premiums.",
            estimatedLearningTimeMinutes = 11,
            keyConcepts = listOf(
                "Cost-plus pricing charges for your time; value pricing charges for the ROI generated for the client.",
                "If your work saves a company $100,000, charging $20,000 is an extraordinary bargain.",
                "Raise prices until you lose 15-20% of tire-kickers and keep high-quality partners."
            ),
            practicalTaskTitle = "Reframe a Quote as Client ROI",
            practicalTaskInstruction = "Write down how an upcoming skill or project of yours makes money or saves time for someone else. Quantify the dollar value of that result.",
            reflectionPrompt = "Are you pricing yourself like a disposable commodity or an essential strategic partner?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "What is the key advantage of value-based pricing over hourly billing?",
                options = listOf(
                    "It decouples your income from the clock and captures a fraction of the value created for the client",
                    "It guarantees you can work 80 hours every week",
                    "It ensures you are always the cheapest option in town",
                    "It makes contracts harder to understand"
                ),
                correctOptionIndex = 0,
                explanation = "Value pricing charges based on the outcome and impact delivered to the buyer rather than capping income to hours spent."
            ),
            primaryColor = TangerineOrange,
            secondaryColor = AcidLime,
            shapeType = ArtShapeType.ORGANIC_BLOBS
        ),

        // 11. Creativity and Design
        LearningTopic(
            id = "creat_subtraction_method",
            title = "The Subtraction Method in Minimalist Design",
            category = "Creativity and Design",
            oneLineDescription = "Achieve timeless elegance by stripping away every element that does not serve an essential purpose.",
            whyItMatters = "Perfection is achieved not when there is nothing more to add, but when there is nothing left to take away. Removing noise clarifies the signal.",
            estimatedLearningTimeMinutes = 9,
            keyConcepts = listOf(
                "Identify the single focal point of the composition.",
                "Every container, border, gradient, or icon must justify its presence or be removed.",
                "Generous whitespace is not empty room; it is breathing room for user comprehension."
            ),
            practicalTaskTitle = "Subtract Three Elements",
            practicalTaskInstruction = "Take a slide, document, or screen you designed. Remove three non-essential decorative lines, badges, or borders.",
            reflectionPrompt = "Where in your workspace or work output is visual clutter hiding your core message?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "According to designer Antoine de Saint-Exupéry, when is design perfection reached?",
                options = listOf(
                    "When you have added every possible feature",
                    "When there is nothing left that can be taken away without breaking function",
                    "When you use at least five different vibrant colors",
                    "When the file size is as large as possible"
                ),
                correctOptionIndex = 1,
                explanation = "Subtraction strips away decorative excess so the essential functionality and visual hierarchy stand in sharp relief."
            ),
            primaryColor = SoftLavender,
            secondaryColor = TangerineOrange,
            shapeType = ArtShapeType.RETRO_ARCH
        ),
        LearningTopic(
            id = "creat_oblique_constraints",
            title = "Oblique Creative Constraints",
            category = "Creativity and Design",
            oneLineDescription = "Defeat creative block by deliberately imposing severe arbitrary limitations.",
            whyItMatters = "Total freedom creates paralyzing blank-page anxiety. Restricting yourself to one font, two colors, or a 50-word limit forces radical ingenuity and unique solutions.",
            estimatedLearningTimeMinutes = 8,
            keyConcepts = listOf(
                "Constraints eliminate infinite options and channel focus into deep problem solving.",
                "Dr. Seuss wrote 'Green Eggs and Ham' on a bet that he could not write a book with only 50 distinct words.",
                "When stuck, invent a constraint: half the time, zero budget, or one tool."
            ),
            practicalTaskTitle = "Create Under a Strict Constraint",
            practicalTaskInstruction = "Draft an explanation of your core skill in exactly 25 words—no more, no less.",
            reflectionPrompt = "What artificial restriction could unlock your current creative roadblock?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "Why do strict creative constraints accelerate innovative output?",
                options = listOf(
                    "They allow you to copy someone else's work legally",
                    "They eliminate the paralysis of infinite choice and force novel problem-solving within defined boundaries",
                    "They make the work less interesting to others",
                    "They eliminate the need for editing"
                ),
                correctOptionIndex = 1,
                explanation = "Constraints narrow the decision space, triggering subconscious lateral thinking and innovative workarounds."
            ),
            primaryColor = ElectricYellow,
            secondaryColor = HotPink,
            shapeType = ArtShapeType.SUNBURST_RAYS
        ),
        LearningTopic(
            id = "creat_typographic_scale",
            title = "Typographic Scale & Visual Rhythm",
            category = "Creativity and Design",
            oneLineDescription = "Guide the reader's eye effortlessly using deliberate proportional font sizing.",
            whyItMatters = "Amateur designs look messy because font sizes are chosen randomly. A disciplined modular type scale (like 1.25 Major Third or 1.33 Perfect Fourth) creates instant editorial harmony.",
            estimatedLearningTimeMinutes = 10,
            keyConcepts = listOf(
                "Use a mathematical multiplier across hierarchy: 12sp -> 16sp -> 20sp -> 28sp -> 40sp.",
                "Pair one strong editorial Display typeface with a highly readable neutral Body typeface.",
                "Letter-spacing: tighten large titles (-0.5sp) and track out small uppercase labels (+1.5sp)."
            ),
            practicalTaskTitle = "Audit a Typographic Hierarchy",
            practicalTaskInstruction = "Examine a favorite magazine or app. Identify how many font sizes they use on a single page (rarely more than 4).",
            reflectionPrompt = "How does consistent typographic scale improve readability on complex screens?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "How should letter-spacing (tracking) generally be handled for small uppercase label tags?",
                options = listOf(
                    "Squished as close together as possible",
                    "Expanded with generous letter spacing to maximize legibility and editorial rhythm",
                    "Letter-spacing does not matter for small text",
                    "Randomized for visual variety"
                ),
                correctOptionIndex = 1,
                explanation = "Small uppercase typography requires wider letter-spacing to prevent characters from blurring together at small optical sizes."
            ),
            primaryColor = TangerineOrange,
            secondaryColor = AcidLime,
            shapeType = ArtShapeType.DIAGONAL_STRIPES
        ),

        // 12. Practical Life Skills
        LearningTopic(
            id = "life_sleep_architecture",
            title = "Deep Sleep Architecture & Protocols",
            category = "Practical Life Skills",
            oneLineDescription = "Rebuild cognitive horsepower and emotional stamina with cold, dark, consistent sleep hygiene.",
            whyItMatters = "Sleep is your superpower. During slow-wave deep sleep, your brain clears metabolic waste and consolidates daytime learning. Depriving sleep impairs memory and cognitive executive function.",
            estimatedLearningTimeMinutes = 10,
            keyConcepts = listOf(
                "Keep bedroom temperature cool (65°F / 18°C) to allow core body temperature to drop.",
                "Zero caffeine within 10 hours of bed; caffeine half-life blocks adenosine sleep pressure.",
                "Darkness: blackout shades and no overhead blue light 2 hours before sleep."
            ),
            practicalTaskTitle = "Set a Digital Sunset Tonight",
            practicalTaskInstruction = "Set an alarm for 60 minutes before your bedtime. When it rings, dim all overhead house lights and put your phone in another room.",
            reflectionPrompt = "How does sleep deprivation manifest in your emotional patience and focus?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "Why should caffeine be avoided within 8 to 10 hours of sleep?",
                options = listOf(
                    "It causes sudden dehydration",
                    "Its long half-life blocks adenosine receptors, preventing natural sleep pressure and reducing deep sleep architecture",
                    "It permanently damages taste buds",
                    "It turns into sugar in the bloodstream"
                ),
                correctOptionIndex = 1,
                explanation = "Caffeine competitively binds to adenosine receptors for 5-8 hours, inhibiting the biological signal for restorative slow-wave sleep."
            ),
            primaryColor = AcidLime,
            secondaryColor = SoftLavender,
            shapeType = ArtShapeType.CONCENTRIC_RINGS
        ),
        LearningTopic(
            id = "life_emergency_triage",
            title = "Crisis Calm & The OODA Loop",
            category = "Practical Life Skills",
            oneLineDescription = "Make decisive, calm moves under acute pressure by executing the Observe-Orient-Decide-Act loop.",
            whyItMatters = "When chaos hits, undisciplined people freeze or panic. The OODA loop resets your orientation and allows you to out-cycle fast-moving complications.",
            estimatedLearningTimeMinutes = 11,
            keyConcepts = listOf(
                "Observe: Gather raw unvarnished facts without emotional embellishment.",
                "Orient: Check mental biases, physical resources, and environmental constraints.",
                "Decide: Pick a viable hypothesis; speed of iteration beats waiting for perfect data.",
                "Act: Execute decisively, observe the new baseline, and repeat."
            ),
            practicalTaskTitle = "Run an OODA Triage on an Active Stressor",
            practicalTaskInstruction = "For one problem causing stress today: write down the raw facts (Observe), the real constraint (Orient), one decision (Decide), and take one action right now (Act).",
            reflectionPrompt = "When unexpected stress hits, do you freeze or cycle through systematic observation?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "What does the OODA decision framework stand for?",
                options = listOf(
                    "Operate, Overcome, Defend, Advance",
                    "Observe, Orient, Decide, Act",
                    "Organize, Order, Deliver, Audit",
                    "Outline, Obtain, Draft, Apply"
                ),
                correctOptionIndex = 1,
                explanation = "Developed by military strategist John Boyd, OODA stands for Observe, Orient, Decide, Act."
            ),
            primaryColor = TangerineOrange,
            secondaryColor = ElectricYellow,
            shapeType = ArtShapeType.ORGANIC_BLOBS
        ),
        LearningTopic(
            id = "life_info_synthesis",
            title = "High-Yield Information Synthesis",
            category = "Practical Life Skills",
            oneLineDescription = "Convert torrential digital reading into lasting actionable wisdom using progressive summarization.",
            whyItMatters = "Reading 50 books a year is useless if you remember none of them. Synthesizing insights into your own words creates a searchable personal knowledge bank.",
            estimatedLearningTimeMinutes = 10,
            keyConcepts = listOf(
                "Capture: Save compelling excerpts and quotes in one central note.",
                "Bold: Highlight the core 10% on a second pass.",
                "Executive Summary: Write a 2-sentence summary in your own voice answering 'How does this change my behavior?'"
            ),
            practicalTaskTitle = "Summarize One Article in Two Sentences",
            practicalTaskInstruction = "Take an insightful article or book chapter you read recently. Write a two-sentence summary of the single biggest action it inspires.",
            reflectionPrompt = "Are you consuming more information than you can realistically synthesize and apply?",
            comprehensionQuestion = ComprehensionQuestion(
                question = "What is the critical final step of progressive summarization?",
                options = listOf(
                    "Reading the entire text out loud three times",
                    "Writing a concise executive summary in your own words focusing on practical application",
                    "Copying the whole text into a new notebook",
                    "Memorizing every single date and footnote"
                ),
                correctOptionIndex = 1,
                explanation = "Translating insights into your own words forces neural reconsolidation and translates knowledge into usable behavioral models."
            ),
            primaryColor = HotPink,
            secondaryColor = AcidLime,
            shapeType = ArtShapeType.GEOMETRIC_PILL
        )
    )

    /**
     * Maps category ID (e.g. "cat_psychology") to standard display name ("Psychology and Human Behavior")
     */
    fun getCategoryNameById(id: String): String {
        return StandardCategories.list.find { it.id.equals(id, ignoreCase = true) }?.name ?: id
    }

    /**
     * Generates a 3-topic selection.
     * Guaranteed behavior:
     * - If only 1 category is selected, all 3 topics come strictly from that single category.
     * - Never repeats completed topics in excludedIds.
     * - Guarantees zero duplicate topic IDs.
     */
    fun getDailySelection(
        seed: Long,
        count: Int = 3,
        excludedIds: List<String> = emptyList(),
        preferredCategories: Set<String> = emptySet()
    ): List<LearningTopic> {
        val eligible = allTopics.filter { it.id !in excludedIds }.ifEmpty { allTopics }

        // Normalize preferred category names
        val normalizedCategoryNames = preferredCategories.map { pref ->
            getCategoryNameById(pref)
        }.toSet()

        // Single Category Selection Check (Requirement: if only one category is selected, generate all 3 from that category)
        if (normalizedCategoryNames.size == 1) {
            val singleCategoryName = normalizedCategoryNames.first()
            val singleCategoryTopics = eligible.filter { it.category.equals(singleCategoryName, ignoreCase = true) }
            if (singleCategoryTopics.size >= count) {
                return singleCategoryTopics.shuffled(kotlin.random.Random(seed)).take(count)
            } else if (singleCategoryTopics.isNotEmpty()) {
                val moreFromCat = allTopics.filter { it.category.equals(singleCategoryName, ignoreCase = true) }
                return moreFromCat.shuffled(kotlin.random.Random(seed)).take(count)
            }
        }

        val prioritized = if (normalizedCategoryNames.isNotEmpty()) {
            val matching = eligible.filter { topic ->
                normalizedCategoryNames.any { it.equals(topic.category, ignoreCase = true) }
            }
            if (matching.size >= count) matching else eligible
        } else {
            eligible
        }

        val shuffled = prioritized.shuffled(kotlin.random.Random(seed))
        val selected = mutableListOf<LearningTopic>()

        // Pass 1: Try diverse categories
        for (topic in shuffled) {
            if (selected.size >= count) break
            if (selected.none { it.category == topic.category }) {
                selected.add(topic)
            }
        }

        // Pass 2: Fill remaining slots
        if (selected.size < count) {
            for (topic in shuffled) {
                if (selected.size >= count) break
                if (selected.none { it.id == topic.id }) {
                    selected.add(topic)
                }
            }
        }

        // Fallback if still under count
        if (selected.size < count) {
            for (topic in allTopics.shuffled(kotlin.random.Random(seed))) {
                if (selected.size >= count) break
                if (selected.none { it.id == topic.id }) {
                    selected.add(topic)
                }
            }
        }

        return selected
    }
}
