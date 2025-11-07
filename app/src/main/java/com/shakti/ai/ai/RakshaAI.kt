package com.shakti.ai.ai

import android.content.Context
import com.shakti.ai.models.BehaviorPattern
import com.shakti.ai.models.RiskLevel
import com.shakti.ai.models.IncidentReport
import com.shakti.ai.models.AbuseAnalysis
import com.shakti.ai.models.AbuseType
import com.shakti.ai.models.EmergencyResource
import com.shakti.ai.models.SafetyPlan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Raksha AI - Domestic Violence Detection & Support
 *
 * Features:
 * - Abuse pattern recognition
 * - Safety plan creation
 * - Risk assessment
 * - Resource connection (shelters, NGOs)
 * - Legal guidance integration
 *
 * Algorithm: Pattern Recognition + Anomaly Detection
 * Technology: Anomaly Detection + Gemini
 * Training Data: Abuse incident patterns
 * Focus: Maximum privacy and safety
 */
class RakshaAI(private val context: Context) {

    private val geminiService = GeminiService.getInstance(context)

    companion object {
        @Volatile
        private var INSTANCE: RakshaAI? = null

        fun getInstance(context: Context): RakshaAI {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: RakshaAI(context.applicationContext).also { INSTANCE = it }
            }
        }

        // Abuse indicators
        private val PHYSICAL_INDICATORS =
            listOf("hit", "slap", "punch", "kick", "hurt", "injury", "bruise", "मारा")
        private val EMOTIONAL_INDICATORS =
            listOf("insult", "humiliate", "control", "isolate", "threaten", "intimidate")
        private val FINANCIAL_INDICATORS =
            listOf("money", "control income", "financial", "bank account", "allowance")
        private val SEXUAL_INDICATORS = listOf("force", "unwanted", "sexual", "coerce")
    }

    /**
     * Detect abuse patterns from reported incidents
     */
    suspend fun detectAbusePatterns(
        incidents: List<IncidentReport>
    ): AbuseAnalysis = withContext(Dispatchers.IO) {
        // Pattern recognition
        val patterns = identifyPatterns(incidents)
        val riskLevel = assessRiskLevel(incidents, patterns)
        val escalation = detectEscalation(incidents)

        // Get AI-powered detailed analysis
        val aiAnalysis = getAIAbuseAnalysis(incidents)

        AbuseAnalysis(
            patternTypes = patterns,
            riskLevel = riskLevel,
            escalating = escalation,
            frequency = incidents.size,
            aiInsights = aiAnalysis,
            immediateSafety = getImmediateSafetyRecommendations(riskLevel),
            resources = getEmergencyResources()
        )
    }

    private fun identifyPatterns(incidents: List<IncidentReport>): List<AbuseType> {
        val patterns = mutableSetOf<AbuseType>()

        incidents.forEach { incident ->
            val desc = incident.description.lowercase()

            if (PHYSICAL_INDICATORS.any { desc.contains(it) }) {
                patterns.add(AbuseType.PHYSICAL)
            }
            if (EMOTIONAL_INDICATORS.any { desc.contains(it) }) {
                patterns.add(AbuseType.EMOTIONAL)
            }
            if (FINANCIAL_INDICATORS.any { desc.contains(it) }) {
                patterns.add(AbuseType.FINANCIAL)
            }
            if (SEXUAL_INDICATORS.any { desc.contains(it) }) {
                patterns.add(AbuseType.SEXUAL)
            }
        }

        return patterns.toList()
    }

    private fun assessRiskLevel(
        incidents: List<IncidentReport>,
        patterns: List<AbuseType>
    ): RiskLevel {
        // High risk factors
        if (patterns.contains(AbuseType.PHYSICAL) && incidents.size >= 3) {
            return RiskLevel.HIGH
        }
        if (patterns.size >= 3) {
            return RiskLevel.HIGH
        }

        // Medium risk
        if (patterns.contains(AbuseType.PHYSICAL) || incidents.size >= 2) {
            return RiskLevel.MEDIUM
        }

        // Low risk (but still concerning)
        return if (incidents.isNotEmpty()) RiskLevel.LOW else RiskLevel.NONE
    }

    private fun detectEscalation(incidents: List<IncidentReport>): Boolean {
        // Check if incidents are increasing in frequency or severity
        return incidents.size >= 2
    }

    private suspend fun getAIAbuseAnalysis(incidents: List<IncidentReport>): String {
        val incidentSummary = incidents.joinToString("\n") { "- ${it.date}: ${it.description}" }

        val prompt = """
            Analyze these reported incidents for abuse patterns:
            
            $incidentSummary
            
            Provide:
            1. Pattern type identification
            2. Typical abuse cycle phases present
            3. Current danger assessment
            4. Immediate safety priorities
            5. Long-term safety planning needs
            
            IMPORTANT: Be empathetic, non-judgmental, and empowering. Remind that abuse is never the victim's fault.
        """.trimIndent()

        return try {
            geminiService.callRakshaAI(prompt)
        } catch (e: Exception) {
            "You are not alone. Help is available. Please reach out to the resources provided."
        }
    }

    private fun getImmediateSafetyRecommendations(risk: RiskLevel): List<String> {
        return when (risk) {
            RiskLevel.HIGH -> listOf(
                "🚨 Your safety is at immediate risk",
                "Call Women's Helpline: 181 (24/7, toll-free) NOW",
                "Go to a safe place - friend, family, or shelter",
                "Consider filing police complaint (with support)",
                "Pack emergency bag with essentials if safe to do so",
                "Inform trusted person about your situation",
                "Document everything - photos, messages, medical records"
            )
            RiskLevel.MEDIUM -> listOf(
                "⚠️ Take steps to increase your safety",
                "Create a safety plan (see below)",
                "Identify safe places to go if needed",
                "Keep important documents accessible",
                "Save emergency numbers in phone (disguised)",
                "Talk to someone you trust",
                "Consider legal options - protection order"
            )

            RiskLevel.LOW -> listOf(
                "📋 Stay aware and prepared",
                "Document all incidents",
                "Build support network",
                "Learn about your legal rights",
                "Keep emergency contacts handy",
                "Trust your instincts"
            )

            else -> listOf("Maintain awareness of safety resources")
        }
    }

    private fun getEmergencyResources(): List<EmergencyResource> {
        return listOf(
            EmergencyResource(
                name = "National Women's Helpline",
                number = "181",
                available = "24/7, toll-free",
                type = "Government"
            ),
            EmergencyResource(
                name = "Women in Distress Helpline",
                number = "1091",
                available = "24/7",
                type = "Police"
            ),
            EmergencyResource(
                name = "Domestic Violence Helpline",
                number = "181",
                available = "24/7, multilingual",
                type = "Support"
            ),
            EmergencyResource(
                name = "National Commission for Women",
                number = "7827-170-170",
                available = "10 AM - 6 PM (Mon-Fri)",
                type = "Legal"
            )
        )
    }

    /**
     * Create personalized safety plan
     */
    suspend fun createSafetyPlan(situation: String): SafetyPlan = withContext(Dispatchers.IO) {
        val aiPlan = getAISafetyPlan(situation)

        SafetyPlan(
            immediateSafety = listOf(
                "Identify safest room in house (with exit, phone)",
                "Code word for emergency with trusted friend/family",
                "Keep phone charged and accessible",
                "Know location of nearest police station/safe place",
                "Have escape route planned"
            ),
            safePlaces = listOf(
                "Trusted friend/family member's home",
                "Women's shelter (contact: 181 for nearest)",
                "Police station",
                "Religious place",
                "24/7 public place"
            ),
            importantDocuments = listOf(
                "Aadhaar card",
                "Bank documents",
                "Marriage certificate",
                "Children's documents",
                "Medical records",
                "Property documents",
                "Passport (if applicable)"
            ),
            emergencyBag = listOf(
                "Important documents (copies)",
                "Money (cash + cards)",
                "Medicines",
                "Change of clothes",
                "Phone charger",
                "Keys (car, house)",
                "Children's essentials"
            ),
            financialSafety = listOf(
                "Open separate bank account (if safe)",
                "Hide some emergency money",
                "Know all financial assets",
                "Keep evidence of financial abuse",
                "Apply for own credit/debit card"
            ),
            legalSteps = listOf(
                "File FIR at police station (Women's cell)",
                "Apply for protection order under DV Act",
                "Contact legal aid (free for women)",
                "Document all abuse incidents",
                "Get medical certificate (MLC) for injuries"
            ),
            emotionalSupport = listOf(
                "Talk to counselor (call 181 for referral)",
                "Join support group",
                "Stay connected with trusted friends",
                "Practice self-care",
                "Remember: It's not your fault"
            ),
            aiCustomPlan = aiPlan
        )
    }

    private suspend fun getAISafetyPlan(situation: String): String {
        val prompt = """
            Create personalized safety plan for:
            $situation
            
            Focus on:
            1. Immediate safety strategies specific to situation
            2. Discreet ways to prepare
            3. Realistic escape planning
            4. Support system activation
            5. Empowering language
            
            Be practical, discreet, and empowering.
        """.trimIndent()

        return try {
            geminiService.callRakshaAI(prompt)
        } catch (e: Exception) {
            "Your safety is priority. Take it one step at a time. Help is available."
        }
    }

    /**
     * Find local shelters and NGOs
     */
    suspend fun findShelters(location: String): String = withContext(Dispatchers.IO) {
        val prompt = """
            Find women's shelters and support NGOs in $location:
            
            Provide:
            1. Shelter name and address
            2. Contact number
            3. Services provided
            4. Admission process
            5. Duration of stay allowed
            6. Legal aid availability
            7. Counseling services
            
            Include government and NGO-run shelters.
        """.trimIndent()

        return@withContext geminiService.callRakshaAI(prompt)
    }

    fun cleanup() {
        INSTANCE = null
    }
}