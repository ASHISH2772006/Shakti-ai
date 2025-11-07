package com.shakti.ai.models

// Generic API Response
data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String?,
    val error: String?
)

// AI Service Responses
data class AIAnalysisResponse(
    val result: String,
    val confidence: Float,
    val metadata: Map<String, Any> = emptyMap()
)

// Gemini AI Response
data class GeminiResponse(
    val text: String,
    val finishReason: String?,
    val safetyRatings: List<SafetyRating>?
)

data class SafetyRating(
    val category: String,
    val probability: String
)

// Blockchain Responses
data class TransactionResponse(
    val transactionHash: String,
    val status: TransactionStatus,
    val blockNumber: Long?,
    val gasUsed: String?
)

enum class TransactionStatus {
    PENDING, CONFIRMED, FAILED
}

data class BlockchainRecord(
    val recordId: String,
    val userId: String,
    val recordType: RecordType,
    val data: String,
    val transactionHash: String,
    val timestamp: Long
)

enum class RecordType {
    SAFETY_ALERT,
    HEALTH_DATA,
    EDUCATION_CERTIFICATE,
    COMMUNITY_VERIFICATION
}

// ML Model Responses
data class ModelPrediction(
    val label: String,
    val confidence: Float,
    val allPredictions: Map<String, Float> = emptyMap()
)

data class LSTMPrediction(
    val prediction: FloatArray,
    val sequenceLength: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LSTMPrediction

        if (!prediction.contentEquals(other.prediction)) return false
        if (sequenceLength != other.sequenceLength) return false

        return true
    }

    override fun hashCode(): Int {
        var result = prediction.contentHashCode()
        result = 31 * result + sequenceLength
        return result
    }
}

// Error Response
data class ErrorResponse(
    val code: Int,
    val message: String,
    val details: String? = null
)
