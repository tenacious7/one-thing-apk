package com.example.data

import com.example.model.ArtShapeType
import com.example.model.ComprehensionQuestion
import com.example.model.LearningTopic
import com.example.ui.theme.AcidLime
import com.example.ui.theme.ElectricYellow
import com.example.ui.theme.HotPink
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.TangerineOrange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiAiService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    /**
     * Extracts skills and mapped categories from natural language user input with minimal token consumption.
     */
    suspend fun extractSkillsFromPrompt(
        apiKey: String,
        userPrompt: String
    ): Result<List<Pair<String, String>>> = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) {
            return@withContext Result.failure(Exception("Gemini API key is required. Please add your key in Skill Settings."))
        }

        val systemPrompt = """
            You are a skill extractor for a daily learning app. The user describes what they want to learn or improve.
            Extract 3 to 6 concise, actionable skills and categorize each into one of these exact 12 categories:
            1. Psychology and Human Behavior
            2. Social Intelligence and Communication
            3. Relationships and Body Language
            4. Fitness, Health, and Human Physiology
            5. Fashion, Grooming, and Personal Presentation
            6. Finance and Money Management
            7. Negotiation and Critical Thinking
            8. Philosophy and Personal Development
            9. Technology, Programming, and AI
            10. Business and Entrepreneurship
            11. Creativity and Design
            12. Practical Life Skills

            Return ONLY a valid JSON array of objects with keys "skill" and "category". Low token output.
            User Input: "$userPrompt"
        """.trimIndent()

        val requestJson = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply { put("text", systemPrompt) })
                    })
                })
            })
            put("generationConfig", JSONObject().apply {
                put("responseMimeType", "application/json")
                put("temperature", 0.4)
                put("maxOutputTokens", 500)
            })
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            val request = Request.Builder()
                .url(url)
                .post(requestJson.toString().toRequestBody(jsonMediaType))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("Gemini API Error (${response.code}): $responseBody"))
            }

            val rootJson = JSONObject(responseBody)
            val candidates = rootJson.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val rawText = parts?.optJSONObject(0)?.optString("text") ?: "[]"

            val jsonArray = JSONArray(rawText)
            val results = mutableListOf<Pair<String, String>>()
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val skill = item.optString("skill", "").trim()
                val cat = item.optString("category", "").trim()
                if (skill.isNotEmpty() && cat.isNotEmpty()) {
                    results.add(Pair(skill, cat))
                }
            }

            Result.success(results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Generates 3 token-efficient, beginner-friendly learning topics with comprehension questions.
     * Enforces:
     * - Never repeating previously completed topics.
     * - If single category specified, all 3 topics come strictly from that category.
     */
    suspend fun generateTopicsWithAi(
        apiKey: String,
        activeSkills: List<String>,
        completedTopicTitles: List<String>,
        singleCategoryOverride: String? = null
    ): Result<List<LearningTopic>> = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) {
            return@withContext Result.failure(Exception("Gemini API key is required. Please enter your API key in Skill Settings."))
        }

        val skillsContext = activeSkills.take(8).joinToString(", ")
        val exclusionContext = if (completedTopicTitles.isNotEmpty()) {
            "STRICT EXCLUSION: Do NOT generate or repeat any of these previously mastered topics: [" +
                    completedTopicTitles.takeLast(20).joinToString("; ") + "]."
        } else ""

        val categoryConstraint = if (!singleCategoryOverride.isNullOrBlank()) {
            "CRITICAL: The user has selected only ONE category ($singleCategoryOverride). All 3 generated topics MUST belong strictly to $singleCategoryOverride."
        } else ""

        val prompt = """
            Generate exactly 3 diverse, actionable, beginner-friendly learning topics for personal growth based on these active user skills: [$skillsContext].
            $categoryConstraint
            $exclusionContext
            Keep descriptions and explanations punchy, structured, and insightful with minimal token usage.

            Return ONLY a valid JSON array of 3 objects with this exact schema:
            [
              {
                "title": "Topic Title",
                "category": "Category Name",
                "short_description": "One sentence summary",
                "why_it_matters": "2-3 sentences on real life value",
                "estimated_learning_time": 10,
                "key_concepts": ["Principle 1", "Principle 2", "Principle 3"],
                "practical_task_title": "Action Title",
                "practical_task_instruction": "Exact 5-minute action to complete today",
                "reflection_prompt": "Thought provoking question",
                "comprehension_question": "Multiple choice question testing understanding",
                "options": ["Option A", "Option B", "Option C", "Option D"],
                "correct_option_index": 1,
                "explanation": "Why this answer is correct"
              }
            ]
        """.trimIndent()

        val requestJson = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply { put("text", prompt) })
                    })
                })
            })
            put("generationConfig", JSONObject().apply {
                put("responseMimeType", "application/json")
                put("temperature", 0.5)
                put("maxOutputTokens", 1200)
            })
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            val request = Request.Builder()
                .url(url)
                .post(requestJson.toString().toRequestBody(jsonMediaType))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("Gemini API Error (${response.code}): $responseBody"))
            }

            val rootJson = JSONObject(responseBody)
            val candidates = rootJson.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val rawText = parts?.optJSONObject(0)?.optString("text") ?: "[]"

            val jsonArray = JSONArray(rawText)
            val colorPalettes = listOf(
                Pair(TangerineOrange, ElectricYellow),
                Pair(HotPink, SoftLavender),
                Pair(AcidLime, TangerineOrange),
                Pair(SoftLavender, ElectricYellow),
                Pair(ElectricYellow, AcidLime)
            )
            val shapes = listOf(
                ArtShapeType.ORGANIC_BLOBS,
                ArtShapeType.SUNBURST_RAYS,
                ArtShapeType.CONCENTRIC_RINGS,
                ArtShapeType.GEOMETRIC_PILL,
                ArtShapeType.RETRO_ARCH
            )

            val topics = mutableListOf<LearningTopic>()
            for (i in 0 until minOf(3, jsonArray.length())) {
                val obj = jsonArray.getJSONObject(i)
                val conceptsArray = obj.optJSONArray("key_concepts")
                val conceptsList = mutableListOf<String>()
                if (conceptsArray != null) {
                    for (c in 0 until conceptsArray.length()) {
                        conceptsList.add(conceptsArray.getString(c))
                    }
                }

                val optionsArray = obj.optJSONArray("options")
                val optionsList = mutableListOf<String>()
                if (optionsArray != null) {
                    for (o in 0 until optionsArray.length()) {
                        optionsList.add(optionsArray.getString(o))
                    }
                }

                val palette = colorPalettes[i % colorPalettes.size]
                val shape = shapes[i % shapes.size]

                topics.add(
                    LearningTopic(
                        id = "ai_" + System.currentTimeMillis() + "_$i",
                        title = obj.optString("title", "Insight $i"),
                        category = obj.optString("category", singleCategoryOverride ?: "Personal Growth"),
                        oneLineDescription = obj.optString("short_description", "Personal growth insight."),
                        whyItMatters = obj.optString("why_it_matters", "Builds intentional awareness and execution."),
                        estimatedLearningTimeMinutes = obj.optInt("estimated_learning_time", 10),
                        keyConcepts = conceptsList.ifEmpty { listOf("Foundational insight", "Practical execution") },
                        practicalTaskTitle = obj.optString("practical_task_title", "Daily Action"),
                        practicalTaskInstruction = obj.optString("practical_task_instruction", "Take one intentional step."),
                        reflectionPrompt = obj.optString("reflection_prompt", "How will you apply this today?"),
                        comprehensionQuestion = ComprehensionQuestion(
                            question = obj.optString("comprehension_question", "What is the key takeaway?"),
                            options = optionsList.ifEmpty { listOf("Option A", "Option B", "Option C", "Option D") },
                            correctOptionIndex = obj.optInt("correct_option_index", 0).coerceIn(0, 3),
                            explanation = obj.optString("explanation", "Core principle of the topic.")
                        ),
                        primaryColor = palette.first,
                        secondaryColor = palette.second,
                        shapeType = shape
                    )
                )
            }

            if (topics.isEmpty()) {
                Result.failure(Exception("AI did not return any topics. Falling back to catalog."))
            } else {
                Result.success(topics)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
