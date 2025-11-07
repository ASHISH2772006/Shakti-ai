package com.shakti.ai.ai

import android.content.Context
import com.shakti.ai.models.EducationalContent
import com.shakti.ai.models.EducationCategory
import com.shakti.ai.models.DifficultyLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * GyaanAI - Educational Guidance & Skill Development
 *
 * Features:
 * - Scholarship finder
 * - Course recommendations
 * - Career pathway planning
 * - Skill gap analysis
 * - Vocational training matching
 *
 * Algorithm: Text Classification
 * Technology: Gemini 2.0 Flash
 * Training Data: Course + skill database
 * Focus: Women-centric education programs
 */
class GyaanAI(private val context: Context) {

    private val educationalDatabase = listOf(
        EducationalContent(
            "edu1",
            "Understanding Your Legal Rights",
            EducationCategory.LEGAL_RIGHTS,
            "Learn about fundamental rights, property rights, and protection laws.",
            DifficultyLevel.BEGINNER,
            15
        ),
        EducationalContent(
            "edu2",
            "Financial Planning 101",
            EducationCategory.FINANCE,
            "Basics of budgeting, saving, and investing for beginners.",
            DifficultyLevel.BEGINNER,
            20
        ),
        EducationalContent(
            "edu3",
            "Women's Health Essentials",
            EducationCategory.HEALTH,
            "Important health screenings, nutrition, and wellness tips.",
            DifficultyLevel.BEGINNER,
            25
        ),
        EducationalContent(
            "edu4",
            "Career Development Strategies",
            EducationCategory.CAREER,
            "Building your career: skills, networking, and growth.",
            DifficultyLevel.INTERMEDIATE,
            30
        ),
        EducationalContent(
            "edu5",
            "Digital Literacy & Online Safety",
            EducationCategory.TECHNOLOGY,
            "Navigate the digital world safely and effectively.",
            DifficultyLevel.BEGINNER,
            15
        ),
        EducationalContent(
            "edu6",
            "Effective Communication Skills",
            EducationCategory.LIFE_SKILLS,
            "Master assertive communication and conflict resolution.",
            DifficultyLevel.INTERMEDIATE,
            20
        ),
        EducationalContent(
            "edu7",
            "Advanced Investment Strategies",
            EducationCategory.FINANCE,
            "Stocks, mutual funds, and portfolio diversification.",
            DifficultyLevel.ADVANCED,
            45
        ),
        EducationalContent(
            "edu8",
            "Mental Health & Wellbeing",
            EducationCategory.HEALTH,
            "Understanding stress, anxiety, and self-care practices.",
            DifficultyLevel.BEGINNER,
            20
        ),
        EducationalContent(
            "edu9",
            "Workplace Rights & Harassment",
            EducationCategory.LEGAL_RIGHTS,
            "Know your rights at workplace and how to report harassment.",
            DifficultyLevel.INTERMEDIATE,
            25
        ),
        EducationalContent(
            "edu10",
            "Leadership & Management",
            EducationCategory.CAREER,
            "Develop leadership skills and team management abilities.",
            DifficultyLevel.ADVANCED,
            40
        )
    )

    private val geminiService = GeminiService.getInstance(context)

    suspend fun getRecommendedContent(
        category: EducationCategory? = null,
        difficulty: DifficultyLevel? = null
    ): List<EducationalContent> = withContext(Dispatchers.Default) {
        educationalDatabase.filter { content ->
            (category == null || content.category == category) &&
                    (difficulty == null || content.difficulty == difficulty)
        }
    }

    suspend fun searchContent(query: String): List<EducationalContent> =
        withContext(Dispatchers.Default) {
            educationalDatabase.filter { content ->
                content.title.contains(query, ignoreCase = true) ||
                        content.content.contains(query, ignoreCase = true) ||
                        content.category.name.contains(query, ignoreCase = true)
            }
        }

    suspend fun getContentByCategory(category: EducationCategory): List<EducationalContent> =
        withContext(Dispatchers.Default) {
            educationalDatabase.filter { it.category == category }
        }

    suspend fun getLearningPath(startLevel: DifficultyLevel): List<EducationalContent> =
        withContext(Dispatchers.Default) {
            when (startLevel) {
                DifficultyLevel.BEGINNER -> {
                    educationalDatabase.filter {
                        it.difficulty == DifficultyLevel.BEGINNER
                    }.take(5)
                }

                DifficultyLevel.INTERMEDIATE -> {
                    val beginner = educationalDatabase.filter {
                        it.difficulty == DifficultyLevel.BEGINNER
                    }.take(2)
                    val intermediate = educationalDatabase.filter {
                        it.difficulty == DifficultyLevel.INTERMEDIATE
                    }.take(3)
                    beginner + intermediate
                }

                DifficultyLevel.ADVANCED -> {
                    educationalDatabase.filter {
                        it.difficulty != DifficultyLevel.BEGINNER
                    }.take(5)
                }
            }
        }

    suspend fun getTotalLearningTime(contents: List<EducationalContent>): Int =
        withContext(Dispatchers.Default) {
            contents.sumOf { it.estimatedTime }
        }

    suspend fun categorizeQuery(query: String): EducationCategory =
        withContext(Dispatchers.Default) {
            val lowerQuery = query.lowercase()

            when {
                lowerQuery.contains("health") || lowerQuery.contains("fitness") ||
                        lowerQuery.contains("wellness") || lowerQuery.contains("medical") ->
                    EducationCategory.HEALTH

                lowerQuery.contains("money") || lowerQuery.contains("finance") ||
                        lowerQuery.contains("investment") || lowerQuery.contains("budget") ->
                    EducationCategory.FINANCE

                lowerQuery.contains("legal") || lowerQuery.contains("law") ||
                        lowerQuery.contains("rights") || lowerQuery.contains("court") ->
                    EducationCategory.LEGAL_RIGHTS

                lowerQuery.contains("career") || lowerQuery.contains("job") ||
                        lowerQuery.contains("work") || lowerQuery.contains("profession") ->
                    EducationCategory.CAREER

                lowerQuery.contains("technology") || lowerQuery.contains("computer") ||
                        lowerQuery.contains("digital") || lowerQuery.contains("tech") ->
                    EducationCategory.TECHNOLOGY

                else -> EducationCategory.LIFE_SKILLS
            }
        }

    suspend fun findScholarships(
        education: String,
        income: Long,
        category: String = "All"
    ): String = withContext(Dispatchers.IO) {
        val prompt = """
            Find scholarships for women in India:
            - Education Level: $education
            - Family Income: ₹${income / 100000} lakhs per year
            - Category: $category
            
            Provide TOP 10 scholarships:
            
            For each scholarship include:
            1. Scholarship name
            2. Eligibility criteria
            3. Award amount (₹)
            4. Application deadline
            5. Required documents
            6. Success rate/competitiveness
            7. Application link
            8. Special benefits (if any)
            
            Prioritize:
            - Women-specific scholarships
            - Merit-cum-means based
            - Government schemes
            - Corporate CSR scholarships
            - International opportunities
        """.trimIndent()

        return@withContext try {
            geminiService.callGyaanAI(prompt)
        } catch (e: Exception) {
            """
                Visit National Scholarship Portal: https://scholarships.gov.in/
                
                Popular scholarships for women:
                1. Central Sector Scheme of Scholarship for College and University Students
                2. PM Scholarship Scheme for Central Armed Police Forces
                3. AICTE Pragati Scholarship for Girls
                4. Inspire Scholarship by DST
                5. National Means cum Merit Scholarship
            """.trimIndent()
        }
    }

    suspend fun recommendCourses(
        currentSkills: List<String>,
        careerGoal: String,
        budget: Long
    ): String = withContext(Dispatchers.IO) {
        val prompt = """
            Recommend courses for career transition:
            - Current Skills: ${currentSkills.joinToString(", ")}
            - Career Goal: $careerGoal
            - Budget: ₹$budget
            
            Suggest:
            1. Top 5 courses (online + offline)
            2. Duration and time commitment
            3. Cost breakdown
            4. Certification value
            5. Job prospects after completion
            6. Free alternatives
            7. Scholarship opportunities
            
            Include:
            - Coursera, Udemy, edX courses
            - Government skill programs
            - Industry certifications
            - Vocational training
        """.trimIndent()

        return@withContext geminiService.callGyaanAI(prompt)
    }

    suspend fun analyzeSkillGap(
        currentRole: String,
        targetRole: String
    ): String = withContext(Dispatchers.IO) {
        val prompt = """
            Skill gap analysis:
            - Current Role: $currentRole
            - Target Role: $targetRole
            
            Provide:
            1. Required skills for target role
            2. Your existing transferable skills
            3. Skills to acquire (prioritized)
            4. Learning path with timeline
            5. Certification recommendations
            6. Practice projects/portfolio ideas
            7. Networking opportunities
        """.trimIndent()

        return@withContext geminiService.callGyaanAI(prompt)
    }

    suspend fun findVocationalTraining(
        interest: String,
        location: String
    ): String = withContext(Dispatchers.IO) {
        val prompt = """
            Find vocational training for women:
            - Interest Area: $interest
            - Location: $location
            
            List programs offering:
            1. Program name and institute
            2. Duration and fees
            3. Certification
            4. Placement assistance
            5. Government subsidies available
            6. Success rate
            7. Contact details
            
            Include:
            - Industrial Training Institutes (ITI)
            - National Skill Development Corporation (NSDC)
            - State skill missions
            - Private vocational centers
        """.trimIndent()

        return@withContext geminiService.callGyaanAI(prompt)
    }

    companion object {
        @Volatile
        private var INSTANCE: GyaanAI? = null

        fun getInstance(context: Context): GyaanAI {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: GyaanAI(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun cleanup() {
        INSTANCE = null
    }
}
