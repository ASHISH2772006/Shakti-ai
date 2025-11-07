package com.shakti.ai.models

import java.time.LocalDate

// ============================================================================
// CENTRALIZED DATA MODELS FOR ALL AI MODULES
// These models are now accessible across the entire app
// ============================================================================

// --- DhanShaktiAI Models ---

data class GovernmentScheme(
    val name: String,
    val description: String,
    val eligibility: String,
    val loanAmount: String,
    val interestRate: String,
    val website: String,
    val category: String
)

data class LoanAssessment(
    val eligible: Boolean,
    val eligibilityStatus: String,
    val score: Double,
    val maxLoanAmount: Long,
    val recommendedLoanAmount: Long,
    val interestRate: Double,
    val repaymentPeriod: Int,
    val aiAdvice: String,
    val applicableSchemes: List<GovernmentScheme>
)

data class InvestmentPlan(
    val targetAmount: Long,
    val timeframeMonths: Int,
    val monthlyInvestment: Long,
    val assetAllocation: Map<String, Double>,
    val expectedAnnualReturn: Double,
    val projectedFinalAmount: Long,
    val aiAdvice: String,
    val specificRecommendations: List<String>
)

enum class RiskProfile {
    LOW, MEDIUM, HIGH
}

// --- NyayaAI Models ---

data class LegalSection(
    val sectionNumber: String,
    val title: String,
    val punishment: String,
    val act: String,
    val explanation: String
)

enum class DocumentType {
    RESTRAINING_ORDER,
    LEGAL_NOTICE,
    DIVORCE_PETITION,
    MAINTENANCE_APPLICATION,
    PROTECTION_ORDER,
    COMPLAINT_LETTER
}

// --- RakshaAI Models ---

data class IncidentReport(
    val date: String,
    val description: String,
    val type: String
)

data class AbuseAnalysis(
    val patternTypes: List<AbuseType>,
    val riskLevel: RiskLevel,
    val escalating: Boolean,
    val frequency: Int,
    val aiInsights: String,
    val immediateSafety: List<String>,
    val resources: List<EmergencyResource>
)

enum class AbuseType {
    PHYSICAL,
    EMOTIONAL,
    FINANCIAL,
    SEXUAL,
    DIGITAL
}

data class EmergencyResource(
    val name: String,
    val number: String,
    val available: String,
    val type: String
)

data class SafetyPlan(
    val immediateSafety: List<String>,
    val safePlaces: List<String>,
    val importantDocuments: List<String>,
    val emergencyBag: List<String>,
    val financialSafety: List<String>,
    val legalSteps: List<String>,
    val emotionalSupport: List<String>,
    val aiCustomPlan: String
)

// --- SwasthyaAI Models ---

data class CycleTracking(
    val lastPeriod: LocalDate,
    val cycleLength: Int,
    val nextPeriod: LocalDate,
    val ovulationDate: LocalDate,
    val fertileWindowStart: LocalDate,
    val fertileWindowEnd: LocalDate,
    val currentPhase: CyclePhase,
    val daysUntilNextPeriod: Int,
    val healthTips: List<String>,
    val symptomsToTrack: List<String>
)

enum class CyclePhase {
    MENSTRUATION,      // Day 1-5
    FOLLICULAR,        // Day 6-13
    OVULATION,         // Day 14-16
    LUTEAL             // Day 17-28
}

data class SymptomAnalysis(
    val symptoms: String,
    val urgencyLevel: UrgencyLevel,
    val aiAdvice: String,
    val whenToSeeDoctor: String,
    val homeRemedies: List<String>,
    val emergencyWarning: Boolean
)

enum class UrgencyLevel {
    LOW,        // Home care sufficient
    MEDIUM,     // See doctor within week
    HIGH,       // See doctor within 24-48 hours
    EMERGENCY   // Seek immediate medical attention
}
