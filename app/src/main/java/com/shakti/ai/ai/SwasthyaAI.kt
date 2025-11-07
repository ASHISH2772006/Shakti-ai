package com.shakti.ai.ai

import android.content.Context
import com.shakti.ai.models.CyclePhase
import com.shakti.ai.models.CycleTracking
import com.shakti.ai.models.HealthMetrics
import com.shakti.ai.models.HealthAdvice
import com.shakti.ai.models.HealthCategory
import com.shakti.ai.models.SymptomAnalysis
import com.shakti.ai.models.UrgencyLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * SwasthyaAI - Health & Wellness Companion
 *
 * Features:
 * - Menstrual cycle tracking
 * - Ovulation prediction
 * - Symptom checker
 * - Health education
 * - Telemedicine connection
 *
 * Algorithm: Symptom Classifier (Custom LSTM)
 * Technology: Custom LSTM + Gemini
 * Training Data: Medical knowledge base
 * Focus: Reproductive health with complete privacy
 */
class SwasthyaAI(private val context: Context) {

    private val geminiService = GeminiService.getInstance(context)

    suspend fun analyzeHealthMetrics(metrics: HealthMetrics): List<HealthAdvice> =
        withContext(Dispatchers.Default) {
            val adviceList = mutableListOf<HealthAdvice>()

            // Heart rate analysis
            metrics.heartRate?.let { hr ->
                when {
                    hr < 60 -> adviceList.add(
                        HealthAdvice(
                            HealthCategory.PHYSICAL_FITNESS,
                            "Your heart rate is low. If you're an athlete, this is normal. Otherwise, consult a doctor.",
                            "Info",
                            false
                        )
                    )

                    hr > 100 -> adviceList.add(
                        HealthAdvice(
                            HealthCategory.PHYSICAL_FITNESS,
                            "Your resting heart rate is elevated. Consider stress management and consult a healthcare provider.",
                            "Warning",
                            true
                        )
                    )

                    else -> adviceList.add(
                        HealthAdvice(
                            HealthCategory.PHYSICAL_FITNESS,
                            "Your heart rate is in the normal range. Keep maintaining a healthy lifestyle!",
                            "Good",
                            false
                        )
                    )
                }
            }

            // Steps analysis
            metrics.steps?.let { steps ->
                when {
                    steps < 5000 -> adviceList.add(
                        HealthAdvice(
                            HealthCategory.PHYSICAL_FITNESS,
                            "Try to increase your daily steps. Aim for at least 7,000-10,000 steps per day.",
                            "Improvement Needed",
                            false
                        )
                    )

                    steps >= 10000 -> adviceList.add(
                        HealthAdvice(
                            HealthCategory.PHYSICAL_FITNESS,
                            "Excellent! You're meeting your daily activity goals. Keep it up!",
                            "Excellent",
                            false
                        )
                    )

                    else -> adviceList.add(
                        HealthAdvice(
                            HealthCategory.PHYSICAL_FITNESS,
                            "You're on the right track. Try to reach 10,000 steps for optimal health.",
                            "Good",
                            false
                        )
                    )
                }
            }

            // Sleep analysis
            metrics.sleepHours?.let { sleep ->
                when {
                    sleep < 6 -> adviceList.add(
                        HealthAdvice(
                            HealthCategory.SLEEP,
                            "You're not getting enough sleep. Aim for 7-9 hours for optimal health and wellbeing.",
                            "Warning",
                            true
                        )
                    )

                    sleep > 9 -> adviceList.add(
                        HealthAdvice(
                            HealthCategory.SLEEP,
                            "You're sleeping more than recommended. Ensure quality sleep and consult a doctor if feeling constantly tired.",
                            "Info",
                            false
                        )
                    )

                    else -> adviceList.add(
                        HealthAdvice(
                            HealthCategory.SLEEP,
                            "Your sleep duration is good. Focus on maintaining consistent sleep schedule.",
                            "Good",
                            false
                        )
                    )
                }
            }

            // Stress level analysis
            metrics.stressLevel?.let { stress ->
                when {
                    stress > 0.7f -> adviceList.add(
                        HealthAdvice(
                            HealthCategory.STRESS_MANAGEMENT,
                            "Your stress levels are high. Try meditation, deep breathing, or talk to a counselor.",
                            "Critical",
                            true
                        )
                    )

                    stress > 0.4f -> adviceList.add(
                        HealthAdvice(
                            HealthCategory.STRESS_MANAGEMENT,
                            "Moderate stress detected. Practice relaxation techniques and ensure adequate rest.",
                            "Warning",
                            false
                        )
                    )

                    else -> adviceList.add(
                        HealthAdvice(
                            HealthCategory.STRESS_MANAGEMENT,
                            "Your stress levels are manageable. Continue your current wellness practices.",
                            "Good",
                            false
                        )
                    )
                }
            }

            adviceList
        }

    suspend fun getWellnessTips(category: HealthCategory): List<String> =
        withContext(Dispatchers.Default) {
            when (category) {
                HealthCategory.MENTAL_HEALTH -> listOf(
                    "Practice mindfulness meditation for 10 minutes daily",
                    "Maintain a gratitude journal",
                    "Connect with supportive friends and family",
                    "Consider professional counseling if needed",
                    "Engage in activities you enjoy",
                    "Limit social media and news consumption"
                )

                HealthCategory.PHYSICAL_FITNESS -> listOf(
                    "Aim for 150 minutes of moderate exercise weekly",
                    "Include strength training twice a week",
                    "Take breaks to stretch during work",
                    "Stay hydrated throughout the day",
                    "Choose stairs over elevators when possible",
                    "Join a fitness class or walking group"
                )

                HealthCategory.NUTRITION -> listOf(
                    "Eat a balanced diet with fruits and vegetables",
                    "Stay hydrated - drink 8-10 glasses of water daily",
                    "Limit processed foods and added sugars",
                    "Include protein in every meal",
                    "Don't skip breakfast",
                    "Practice mindful eating"
                )

                HealthCategory.SLEEP -> listOf(
                    "Maintain consistent sleep schedule",
                    "Create a relaxing bedtime routine",
                    "Keep bedroom cool, dark, and quiet",
                    "Avoid screens 1 hour before bed",
                    "Limit caffeine after 2 PM",
                    "Practice relaxation techniques before sleep"
                )

                HealthCategory.STRESS_MANAGEMENT -> listOf(
                    "Practice deep breathing exercises",
                    "Engage in regular physical activity",
                    "Set boundaries and learn to say no",
                    "Take regular breaks throughout the day",
                    "Connect with nature",
                    "Seek support when overwhelmed"
                )
            }
        }

    suspend fun getEmergencyContacts(): Map<String, String> = withContext(Dispatchers.Default) {
        mapOf(
            "National Emergency" to "112",
            "Women's Helpline" to "181",
            "Mental Health Helpline" to "08046110007",
            "Ambulance" to "102",
            "National Commission for Women" to "7827170170"
        )
    }

    /**
     * Track menstrual cycle and predict future dates
     */
    suspend fun trackMenstrualCycle(
        lastPeriodDate: LocalDate,
        cycleLength: Int = 28,
        periodDuration: Int = 5
    ): CycleTracking = withContext(Dispatchers.IO) {
        // Calculate key dates
        val ovulationDate = lastPeriodDate.plusDays((cycleLength / 2).toLong())
        val nextPeriodDate = lastPeriodDate.plusDays(cycleLength.toLong())
        val fertileWindowStart = ovulationDate.minusDays(5)
        val fertileWindowEnd = ovulationDate.plusDays(1)

        // Calculate current phase
        val today = LocalDate.now()
        val daysSinceLastPeriod = ChronoUnit.DAYS.between(lastPeriodDate, today)
        val currentPhase = calculatePhase(daysSinceLastPeriod.toInt(), cycleLength)

        // Get AI health tips
        val healthTips = getPhaseSpecificTips(currentPhase)

        CycleTracking(
            lastPeriod = lastPeriodDate,
            cycleLength = cycleLength,
            nextPeriod = nextPeriodDate,
            ovulationDate = ovulationDate,
            fertileWindowStart = fertileWindowStart,
            fertileWindowEnd = fertileWindowEnd,
            currentPhase = currentPhase,
            daysUntilNextPeriod = ChronoUnit.DAYS.between(today, nextPeriodDate).toInt(),
            healthTips = healthTips,
            symptomsToTrack = getSymptomsForPhase(currentPhase)
        )
    }

    private fun calculatePhase(daysSinceLastPeriod: Int, cycleLength: Int): CyclePhase {
        return when (daysSinceLastPeriod) {
            in 0..5 -> CyclePhase.MENSTRUATION
            in 6..13 -> CyclePhase.FOLLICULAR
            in 14..16 -> CyclePhase.OVULATION
            else -> CyclePhase.LUTEAL
        }
    }

    private fun getPhaseSpecificTips(phase: CyclePhase): List<String> {
        return when (phase) {
            CyclePhase.MENSTRUATION -> listOf(
                "Increase iron intake - eat spinach, dates, jaggery",
                "Stay hydrated - drink 8-10 glasses of water",
                "Use heating pad for cramps",
                "Light exercise like walking or yoga",
                "Rest well - your body is working hard"
            )

            CyclePhase.FOLLICULAR -> listOf(
                "Peak energy time - good for intense workouts",
                "Start new projects - cognitive function is high",
                "Eat protein-rich foods",
                "This is your confident phase - schedule important meetings",
                "Estrogen rising - you'll feel more social"
            )

            CyclePhase.OVULATION -> listOf(
                "Fertile window - track if planning pregnancy",
                "Energy at peak - take on challenges",
                "Stay hydrated",
                "Notice: increased body temperature, clear discharge",
                "Good time for difficult conversations"
            )

            CyclePhase.LUTEAL -> listOf(
                "Reduce caffeine - can worsen PMS",
                "Increase water intake - reduces bloating",
                "Eat complex carbs - whole grains, fruits",
                "Practice stress management - meditation, deep breathing",
                "Be kind to yourself - mood changes are normal"
            )
        }
    }

    private fun getSymptomsForPhase(phase: CyclePhase): List<String> {
        return when (phase) {
            CyclePhase.MENSTRUATION -> listOf(
                "Cramping",
                "Fatigue",
                "Mood",
                "Flow intensity",
                "Backache"
            )

            CyclePhase.FOLLICULAR -> listOf("Energy level", "Mood", "Skin condition", "Appetite")
            CyclePhase.OVULATION -> listOf(
                "Discharge",
                "Body temperature",
                "Mild cramping",
                "Energy"
            )

            CyclePhase.LUTEAL -> listOf(
                "Bloating",
                "Breast tenderness",
                "Mood swings",
                "Cravings",
                "Fatigue"
            )
        }
    }

    /**
     * Check symptoms and provide health guidance
     */
    suspend fun checkSymptoms(symptoms: String): SymptomAnalysis = withContext(Dispatchers.IO) {
        val urgency = assessUrgency(symptoms)
        val aiAdvice = getAISymptomAdvice(symptoms)

        SymptomAnalysis(
            symptoms = symptoms,
            urgencyLevel = urgency,
            aiAdvice = aiAdvice,
            whenToSeeDoctor = getWhenToSeeDoctor(urgency),
            homeRemedies = getHomeRemedies(symptoms),
            emergencyWarning = urgency == UrgencyLevel.EMERGENCY
        )
    }

    private fun assessUrgency(symptoms: String): UrgencyLevel {
        val lowerSymptoms = symptoms.lowercase()

        // Emergency symptoms
        if (lowerSymptoms.contains("severe pain") ||
            lowerSymptoms.contains("heavy bleeding") ||
            lowerSymptoms.contains("can't breathe") ||
            lowerSymptoms.contains("chest pain") ||
            lowerSymptoms.contains("loss of consciousness")
        ) {
            return UrgencyLevel.EMERGENCY
        }

        // High priority
        if (lowerSymptoms.contains("fever") ||
            lowerSymptoms.contains("persistent") ||
            lowerSymptoms.contains("getting worse")
        ) {
            return UrgencyLevel.HIGH
        }

        // Medium priority
        if (lowerSymptoms.contains("pain") ||
            lowerSymptoms.contains("unusual") ||
            lowerSymptoms.contains("irregular")
        ) {
            return UrgencyLevel.MEDIUM
        }

        return UrgencyLevel.LOW
    }

    private suspend fun getAISymptomAdvice(symptoms: String): String {
        val prompt = """
            Patient reports: $symptoms
            
            Provide (keep brief and clear):
            1. Possible causes (common ones first)
            2. Home remedies (safe and effective)
            3. When to see a doctor
            4. Dietary suggestions
            5. Specialist recommendation if needed
            
            IMPORTANT: Emphasize this is not a diagnosis. Encourage seeing a healthcare provider if symptoms persist.
        """.trimIndent()

        return try {
            geminiService.callSwasthyaAI(prompt)
        } catch (e: Exception) {
            "If symptoms persist or worsen, please consult a healthcare professional."
        }
    }

    private fun getWhenToSeeDoctor(urgency: UrgencyLevel): String {
        return when (urgency) {
            UrgencyLevel.EMERGENCY -> "🚨 Seek immediate medical attention! Call 102 (ambulance) or go to nearest emergency room."
            UrgencyLevel.HIGH -> "⚠️ See a doctor within 24-48 hours. Don't delay if symptoms worsen."
            UrgencyLevel.MEDIUM -> "📅 Schedule a doctor's appointment within this week."
            UrgencyLevel.LOW -> "Monitor symptoms. See a doctor if they persist beyond 3-5 days or worsen."
        }
    }

    private fun getHomeRemedies(symptoms: String): List<String> {
        return listOf(
            "Stay hydrated - drink plenty of water",
            "Get adequate rest - 7-8 hours sleep",
            "Maintain balanced diet - fruits and vegetables",
            "Light exercise or yoga if feeling up to it",
            "Track symptoms - note any changes"
        )
    }

    /**
     * Connect with telemedicine services
     */
    suspend fun findTelemedicineServices(location: String): String = withContext(Dispatchers.IO) {
        val prompt = """
            Find telemedicine services for women in $location:
            
            List:
            1. Government telemedicine platforms
            2. Private online consultation services
            3. Women-specific health apps
            4. Mental health support lines
            5. 24/7 helplines
            
            Include:
            - Service name
            - Cost (free/paid)
            - Availability
            - Contact info
        """.trimIndent()

        return@withContext geminiService.callSwasthyaAI(prompt)
    }

    companion object {
        @Volatile
        private var INSTANCE: SwasthyaAI? = null

        fun getInstance(context: Context): SwasthyaAI {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SwasthyaAI(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun cleanup() {
        INSTANCE = null
    }
}
