package com.shakti.ai.models

import java.util.Date

// ============================================================================
// AI MODEL SPECIFICATIONS - Based on ShaktiAI 3.0 Architecture
// ============================================================================

/**
 * Comprehensive specification for each AI model in the ShaktiAI platform
 */
data class AIModelSpecification(
    val modelName: String,
    val purpose: String,
    val algorithm: String,
    val technology: String,
    val trainingData: String,
    val description: String
)

/**
 * All AI Model Specifications for ShaktiAI 3.0
 */
object AIModelSpecs {
    val SATHI_AI = AIModelSpecification(
        modelName = "Sathi AI",
        purpose = "Mental health support",
        algorithm = "LSTM Seq2Seq",
        technology = "Gemini 2.0 Flash",
        trainingData = "10,000+ counseling chats",
        description = "Emotional support companion providing compassionate mental health assistance using LSTM sequence-to-sequence model trained on thousands of real counseling conversations. Provides culturally sensitive support in multiple Indian languages."
    )

    val GUARDIAN_AI = AIModelSpecification(
        modelName = "Guardian AI",
        purpose = "Threat detection",
        algorithm = "YOLOv5 Audio",
        technology = "TensorFlow Lite",
        trainingData = "5,000+ audio samples",
        description = "Real-time audio threat detection system using YOLOv5 architecture adapted for audio analysis. Detects distress signals, screams, and threatening situations from ambient audio with high accuracy."
    )

    val NYAYA_AI = AIModelSpecification(
        modelName = "Nyaya AI",
        purpose = "Legal advisor",
        algorithm = "NLP Transformer",
        technology = "Gemini 2.0 Flash + IPC",
        trainingData = "Indian legal documents",
        description = "Intelligent legal advisor specialized in Indian laws related to women's rights. Uses NLP transformers to understand legal queries and provide advice based on IPC, Domestic Violence Act, POSH Act, and other relevant laws."
    )

    val DHAN_SHAKTI_AI = AIModelSpecification(
        modelName = "Dhan Shakti AI",
        purpose = "Financial planning",
        algorithm = "XGBoost + Regression",
        technology = "Gemini 2.0 Flash",
        trainingData = "Credit scoring dataset",
        description = "Financial literacy and planning assistant using XGBoost machine learning with regression analysis. Helps women achieve financial independence through personalized budgeting, savings plans, and investment recommendations."
    )

    val SANGAM_AI = AIModelSpecification(
        modelName = "Sangam AI",
        purpose = "Community matching",
        algorithm = "Collaborative Filter",
        technology = "Aptos Smart Contracts",
        trainingData = "User interaction patterns",
        description = "Community connection platform using collaborative filtering algorithms deployed on Aptos blockchain. Safely connects women with similar interests, support groups, and mentorship opportunities while maintaining privacy."
    )

    val GYAAN_AI = AIModelSpecification(
        modelName = "Gyaan AI",
        purpose = "Education guidance",
        algorithm = "Text Classification",
        technology = "Gemini 2.0 Flash",
        trainingData = "Course + skill database",
        description = "Educational advisor using advanced text classification to match users with learning opportunities. Provides personalized course recommendations, scholarship information, and skill development pathways."
    )

    val SWASTHYA_AI = AIModelSpecification(
        modelName = "Swasthya AI",
        purpose = "Health companion",
        algorithm = "Symptom Classifier",
        technology = "Custom LSTM",
        trainingData = "Medical knowledge base",
        description = "Reproductive health and wellness companion using custom LSTM-based symptom classifier. Tracks menstrual cycles, provides health education, and connects users with telemedicine services while maintaining complete privacy."
    )

    val RAKSHA_AI = AIModelSpecification(
        modelName = "Raksha AI",
        purpose = "DV detection",
        algorithm = "Pattern Recognition",
        technology = "Anomaly Detection",
        trainingData = "Abuse incident patterns",
        description = "Domestic violence detection system using pattern recognition and anomaly detection algorithms. Identifies abusive patterns, creates safety plans, and provides discreet emergency resources and support."
    )

    /**
     * Get all AI model specifications as a list
     */
    fun getAllModels(): List<AIModelSpecification> = listOf(
        SATHI_AI,
        GUARDIAN_AI,
        NYAYA_AI,
        DHAN_SHAKTI_AI,
        SANGAM_AI,
        GYAAN_AI,
        SWASTHYA_AI,
        RAKSHA_AI
    )

    /**
     * Get model specification by name
     */
    fun getModelByName(name: String): AIModelSpecification? {
        return getAllModels().find { it.modelName.equals(name, ignoreCase = true) }
    }
}

// ============================================================================
// DATA MODELS FOR EACH AI MODULE
// ============================================================================

// Sathi AI Models - Emotional Support & Mental Health
data class EmotionalState(
    val userId: String,
    val emotion: String,
    val intensity: Float,
    val timestamp: Long = System.currentTimeMillis()
)

data class ConversationMessage(
    val id: String,
    val message: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

// Guardian AI Models - Safety Monitoring
data class AudioAnalysisResult(
    val isDistressDetected: Boolean,
    val confidenceScore: Float,
    val audioType: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class SafetyAlert(
    val alertType: String,
    val severity: AlertSeverity,
    val location: Location?,
    val timestamp: Long = System.currentTimeMillis(),
    val isResolved: Boolean = false
)

enum class AlertSeverity {
    LOW, MEDIUM, HIGH, CRITICAL
}

data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String? = null
)

// Nyaya AI Models - Legal Assistant
data class LegalQuery(
    val query: String,
    val category: LegalCategory,
    val timestamp: Long = System.currentTimeMillis()
)

enum class LegalCategory {
    DOMESTIC_VIOLENCE,
    WORKPLACE_HARASSMENT,
    PROPERTY_RIGHTS,
    FAMILY_LAW,
    GENERAL
}

data class LegalAdvice(
    val query: String,
    val advice: String,
    val relevantLaws: List<String>,
    val suggestedActions: List<String>,
    val confidence: Float
)

// DhanShakti AI Models - Financial Literacy
data class FinancialProfile(
    val userId: String,
    val income: Double,
    val expenses: Double,
    val savings: Double,
    val investments: Double
)

data class FinancialAdvice(
    val category: FinancialCategory,
    val recommendation: String,
    val expectedImpact: String,
    val priority: Int
)

enum class FinancialCategory {
    BUDGETING,
    SAVINGS,
    INVESTMENT,
    DEBT_MANAGEMENT,
    INSURANCE
}

// Sangam AI Models - Community Connections
data class CommunityMember(
    val id: String,
    val name: String,
    val interests: List<String>,
    val location: String,
    val verified: Boolean = false
)

data class CommunityRecommendation(
    val members: List<CommunityMember>,
    val groups: List<CommunityGroup>,
    val events: List<CommunityEvent>
)

data class CommunityGroup(
    val id: String,
    val name: String,
    val description: String,
    val memberCount: Int,
    val category: String
)

data class CommunityEvent(
    val id: String,
    val title: String,
    val description: String,
    val date: Long,
    val location: String
)

// Gyaan AI Models - Educational Content
data class EducationalContent(
    val id: String,
    val title: String,
    val category: EducationCategory,
    val content: String,
    val difficulty: DifficultyLevel,
    val estimatedTime: Int // in minutes
)

enum class EducationCategory {
    HEALTH,
    FINANCE,
    LEGAL_RIGHTS,
    CAREER,
    TECHNOLOGY,
    LIFE_SKILLS
}

enum class DifficultyLevel {
    BEGINNER, INTERMEDIATE, ADVANCED
}

// Swasthya AI Models - Health Monitoring
data class HealthMetrics(
    val userId: String,
    val heartRate: Int?,
    val steps: Int?,
    val sleepHours: Float?,
    val stressLevel: Float?,
    val timestamp: Long = System.currentTimeMillis()
)

data class HealthAdvice(
    val category: HealthCategory,
    val advice: String,
    val severity: String,
    val actionRequired: Boolean
)

enum class HealthCategory {
    MENTAL_HEALTH,
    PHYSICAL_FITNESS,
    NUTRITION,
    SLEEP,
    STRESS_MANAGEMENT
}

// Raksha AI Models - Pattern Recognition
data class BehaviorPattern(
    val patternType: String,
    val frequency: Int,
    val riskLevel: RiskLevel,
    val description: String
)

enum class RiskLevel {
    NONE, LOW, MEDIUM, HIGH
}

// User Profile
data class UserProfile(
    val userId: String,
    val name: String,
    val email: String,
    val phone: String,
    val emergencyContacts: List<EmergencyContact>,
    val preferences: UserPreferences
)

data class EmergencyContact(
    val name: String,
    val phone: String,
    val relationship: String
)

data class UserPreferences(
    val notificationsEnabled: Boolean = true,
    val locationSharingEnabled: Boolean = false,
    val aiAssistanceLevel: AssistanceLevel = AssistanceLevel.BALANCED
)

enum class AssistanceLevel {
    MINIMAL, BALANCED, COMPREHENSIVE
}
