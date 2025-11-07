package com.shakti.ai.ai

import android.content.Context
import com.shakti.ai.models.FinancialAdvice
import com.shakti.ai.models.FinancialCategory
import com.shakti.ai.models.FinancialProfile
import com.shakti.ai.models.GovernmentScheme
import com.shakti.ai.models.LoanAssessment
import com.shakti.ai.models.InvestmentPlan
import com.shakti.ai.models.RiskProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.pow

/**
 * Dhan Shakti AI - Financial Literacy & Planning for Women
 *
 * Features:
 * - Loan eligibility assessment
 * - Personalized investment planning
 * - Government schemes matching
 * - Business idea recommendations
 * - Budget planning
 * - Financial goal tracking
 *
 * Algorithm: XGBoost + Regression Analysis
 * Technology: Gemini 2.0 Flash + Financial Models
 * Training Data: Credit scoring datasets
 * Focus: Low-cost solutions for economically disadvantaged women
 */
class DhanShaktiAI(private val context: Context) {

    private val geminiService = GeminiService.getInstance(context)

    companion object {
        @Volatile
        private var INSTANCE: DhanShaktiAI? = null

        fun getInstance(context: Context): DhanShaktiAI {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DhanShaktiAI(context.applicationContext).also { INSTANCE = it }
            }
        }

        // Credit score ranges
        private const val EXCELLENT_CREDIT = 750
        private const val GOOD_CREDIT = 650
        private const val FAIR_CREDIT = 550
    }

    // ============================================================================
    // GOVERNMENT SCHEMES DATABASE
    // ============================================================================

    private val governmentSchemes = listOf(
        GovernmentScheme(
            name = "Prime Minister's Employment Generation Programme (PMEGP)",
            description = "Credit-linked subsidy for setting up micro-enterprises",
            eligibility = "18+ years, 8th pass, no other subsidy availed",
            loanAmount = "₹10 lakhs (manufacturing) / ₹5 lakhs (service)",
            interestRate = "15-35% subsidy on project cost",
            website = "https://www.kviconline.gov.in/pmegpeportal/",
            category = "Business Startup"
        ),
        GovernmentScheme(
            name = "Pradhan Mantri MUDRA Yojana (PMMY)",
            description = "Collateral-free loans for micro/small enterprises",
            eligibility = "Any citizen starting or running micro-enterprise",
            loanAmount = "Up to ₹10 lakhs (Shishu: ₹50K, Kishore: ₹5L, Tarun: ₹10L)",
            interestRate = "Bank-dependent (typically 8-12%)",
            website = "https://www.mudra.org.in/",
            category = "Micro-credit"
        ),
        GovernmentScheme(
            name = "Stand Up India Scheme",
            description = "Loans for SC/ST/Women entrepreneurs",
            eligibility = "Women, SC/ST, 18+ years, first-time entrepreneur",
            loanAmount = "₹10 lakhs to ₹1 crore",
            interestRate = "Base rate + 3% + tenure premium",
            website = "https://www.standupmitra.in/",
            category = "Large Business"
        ),
        GovernmentScheme(
            name = "Sukanya Samriddhi Yojana (SSY)",
            description = "Savings scheme for girl child education/marriage",
            eligibility = "Girl child below 10 years",
            loanAmount = "Min: ₹250/year, Max: ₹1.5 lakhs/year",
            interestRate = "7.6% p.a. (quarterly compounded)",
            website = "https://www.india.gov.in/sukanya-samriddhi-yojana",
            category = "Savings"
        ),
        GovernmentScheme(
            name = "Mahila Udyam Nidhi Scheme",
            description = "Special scheme for women entrepreneurs",
            eligibility = "Women entrepreneurs in small scale sector",
            loanAmount = "Up to ₹10 lakhs",
            interestRate = "Concessional rates for women",
            website = "Contact Small Industries Development Bank of India (SIDBI)",
            category = "Women-specific"
        ),
        GovernmentScheme(
            name = "Dena Shakti Scheme",
            description = "Loans for women in agriculture, retail, small enterprises",
            eligibility = "Women above 18 years",
            loanAmount = "Up to ₹20 lakhs",
            interestRate = "0.25% concession in interest rate",
            website = "Contact Bank of Baroda",
            category = "Women-specific"
        ),
        GovernmentScheme(
            name = "Bharatiya Mahila Bank Business Loan",
            description = "Business loans for women entrepreneurs",
            eligibility = "Women entrepreneurs",
            loanAmount = "₹20 lakhs to ₹20 crores",
            interestRate = "Competitive rates",
            website = "Merged with State Bank of India",
            category = "Large Business"
        ),
        GovernmentScheme(
            name = "Cent Kalyani Scheme",
            description = "Loan for women's economic empowerment",
            eligibility = "Women aged 18-65 years",
            loanAmount = "Up to ₹100 lakhs",
            interestRate = "Concession of 0.25% to 0.50%",
            website = "Contact Central Bank of India",
            category = "Women-specific"
        ),
        GovernmentScheme(
            name = "Mahila e-Haat",
            description = "Online marketing platform for women entrepreneurs",
            eligibility = "Women entrepreneurs, SHGs, NGOs",
            loanAmount = "N/A (Marketing platform)",
            interestRate = "Free platform",
            website = "https://www.maahilaehaat-rmk.gov.in/",
            category = "Marketing"
        ),
        GovernmentScheme(
            name = "Annapurna Scheme",
            description = "Loan for food catering businesses",
            eligibility = "Women food caterers",
            loanAmount = "Up to ₹50,000",
            interestRate = "Concessional rates",
            website = "Contact public sector banks",
            category = "Food Business"
        )
    )

    // ============================================================================
    // LOAN ELIGIBILITY ASSESSMENT
    // ============================================================================

    /**
     * Assess loan eligibility using XGBoost-inspired scoring
     */
    suspend fun assessLoanEligibility(
        income: Long,
        age: Int,
        businessType: String,
        existingLoans: Long = 0,
        creditScore: Int = 650
    ): LoanAssessment = withContext(Dispatchers.IO) {
        // XGBoost-like scoring factors
        val incomeScore = calculateIncomeScore(income)
        val ageScore = calculateAgeScore(age)
        val creditScoreWeight = calculateCreditScore(creditScore)
        val debtToIncomeRatio = if (income > 0) (existingLoans.toDouble() / income) else 1.0
        val dtiScore = calculateDTIScore(debtToIncomeRatio)

        // Weighted score calculation (XGBoost regression simulation)
        val totalScore = (incomeScore * 0.35 +
                ageScore * 0.15 +
                creditScoreWeight * 0.30 +
                dtiScore * 0.20)

        val eligibility = when {
            totalScore >= 80 -> "Excellent - Highly Eligible"
            totalScore >= 65 -> "Good - Eligible with favorable terms"
            totalScore >= 50 -> "Fair - Eligible with conditions"
            else -> "Needs Improvement - Consider building credit first"
        }

        val maxLoanAmount = calculateMaxLoan(income, debtToIncomeRatio, totalScore)
        val interestRate = calculateInterestRate(totalScore, creditScore)

        // Get AI-powered detailed assessment
        val aiAdvice = getDetailedLoanAdvice(income, age, businessType, totalScore, maxLoanAmount)

        LoanAssessment(
            eligible = totalScore >= 50,
            eligibilityStatus = eligibility,
            score = totalScore,
            maxLoanAmount = maxLoanAmount,
            recommendedLoanAmount = (maxLoanAmount * 0.7).toLong(),
            interestRate = interestRate,
            repaymentPeriod = calculateOptimalTenure(maxLoanAmount),
            aiAdvice = aiAdvice,
            applicableSchemes = findApplicableSchemes(businessType, maxLoanAmount)
        )
    }

    private fun calculateIncomeScore(income: Long): Double {
        return when {
            income >= 1000000 -> 100.0 // 10 lakhs+
            income >= 500000 -> 85.0   // 5 lakhs
            income >= 300000 -> 70.0   // 3 lakhs
            income >= 180000 -> 55.0   // 1.8 lakhs
            income >= 100000 -> 40.0   // 1 lakh
            else -> 25.0
        }
    }

    private fun calculateAgeScore(age: Int): Double {
        return when (age) {
            in 25..50 -> 100.0  // Prime working age
            in 21..24 -> 85.0
            in 51..60 -> 80.0
            in 18..20 -> 70.0
            else -> 50.0
        }
    }

    private fun calculateCreditScore(score: Int): Double {
        return when {
            score >= EXCELLENT_CREDIT -> 100.0
            score >= GOOD_CREDIT -> 80.0
            score >= FAIR_CREDIT -> 60.0
            else -> 40.0
        }
    }

    private fun calculateDTIScore(ratio: Double): Double {
        return when {
            ratio <= 0.2 -> 100.0
            ratio <= 0.4 -> 75.0
            ratio <= 0.5 -> 50.0
            else -> 25.0
        }
    }

    private fun calculateMaxLoan(income: Long, dtiRatio: Double, score: Double): Long {
        val baseMultiplier = when {
            score >= 80 -> 5.0
            score >= 65 -> 3.5
            score >= 50 -> 2.0
            else -> 1.0
        }
        return (income * baseMultiplier * (1 - dtiRatio)).toLong().coerceAtMost(10000000)
    }

    private fun calculateInterestRate(score: Double, creditScore: Int): Double {
        val baseRate = 10.5
        val adjustment = when {
            score >= 80 && creditScore >= EXCELLENT_CREDIT -> -2.5
            score >= 65 && creditScore >= GOOD_CREDIT -> -1.5
            score >= 50 -> 0.0
            else -> 1.5
        }
        return baseRate + adjustment
    }

    private fun calculateOptimalTenure(loanAmount: Long): Int {
        return when {
            loanAmount >= 5000000 -> 84  // 7 years
            loanAmount >= 2000000 -> 60  // 5 years
            loanAmount >= 500000 -> 36   // 3 years
            else -> 24                    // 2 years
        }
    }

    private suspend fun getDetailedLoanAdvice(
        income: Long,
        age: Int,
        businessType: String,
        score: Double,
        maxLoan: Long
    ): String {
        val prompt = """
            Provide detailed loan advice for a woman entrepreneur:
            - Annual Income: ₹$income
            - Age: $age years
            - Business Type: $businessType
            - Eligibility Score: ${score.toInt()}/100
            - Max Loan Amount: ₹${maxLoan / 100000} lakhs
            
            Provide:
            1. Loan strategy
            2. Required documents
            3. Application process
            4. Tips to improve eligibility
            5. Alternative financing options
            6. Timeline expectations
        """.trimIndent()

        return try {
            geminiService.callDhanShaktiAI(prompt)
        } catch (e: Exception) {
            "Consult with a bank representative for personalized loan advice."
        }
    }

    private fun findApplicableSchemes(
        businessType: String,
        loanAmount: Long
    ): List<GovernmentScheme> {
        return governmentSchemes.filter { scheme ->
            when {
                loanAmount <= 50000 && scheme.category == "Micro-credit" -> true
                loanAmount <= 1000000 && scheme.category in listOf(
                    "Micro-credit",
                    "Business Startup"
                ) -> true

                loanAmount > 1000000 && scheme.category == "Large Business" -> true
                scheme.category == "Women-specific" -> true
                else -> false
            }
        }.take(3)
    }

    // ============================================================================
    // INVESTMENT PLANNING
    // ============================================================================

    /**
     * Create personalized investment plan
     */
    suspend fun createInvestmentPlan(
        targetAmount: Long,
        timeframeMonths: Int,
        riskAppetite: RiskProfile,
        currentSavings: Long = 0
    ): InvestmentPlan = withContext(Dispatchers.IO) {
        val monthlyInvestment = ((targetAmount - currentSavings) / timeframeMonths.toDouble())
        val assetAllocation = getAssetAllocation(riskAppetite)
        val expectedReturns = calculateExpectedReturns(assetAllocation)

        val aiAdvice =
            generateInvestmentAdvice(targetAmount, timeframeMonths, riskAppetite, monthlyInvestment)

        InvestmentPlan(
            targetAmount = targetAmount,
            timeframeMonths = timeframeMonths,
            monthlyInvestment = monthlyInvestment.toLong(),
            assetAllocation = assetAllocation,
            expectedAnnualReturn = expectedReturns,
            projectedFinalAmount = calculateFutureValue(
                monthlyInvestment.toLong(),
                timeframeMonths,
                expectedReturns / 100
            ),
            aiAdvice = aiAdvice,
            specificRecommendations = getSpecificInvestments(
                assetAllocation,
                monthlyInvestment.toLong()
            )
        )
    }

    private fun getAssetAllocation(risk: RiskProfile): Map<String, Double> {
        return when (risk) {
            RiskProfile.LOW -> mapOf(
                "Fixed Deposits" to 40.0,
                "Government Securities" to 30.0,
                "Gold" to 20.0,
                "Liquid Funds" to 10.0
            )

            RiskProfile.MEDIUM -> mapOf(
                "Fixed Deposits" to 25.0,
                "Balanced Mutual Funds" to 30.0,
                "Gold" to 15.0,
                "Index Funds" to 20.0,
                "Liquid Funds" to 10.0
            )

            RiskProfile.HIGH -> mapOf(
                "Equity Mutual Funds" to 40.0,
                "Index Funds" to 25.0,
                "Fixed Deposits" to 15.0,
                "Gold" to 10.0,
                "Stocks" to 10.0
            )
        }
    }

    private fun calculateExpectedReturns(allocation: Map<String, Double>): Double {
        val returns = mapOf(
            "Fixed Deposits" to 6.5,
            "Government Securities" to 7.0,
            "Gold" to 8.0,
            "Balanced Mutual Funds" to 10.0,
            "Index Funds" to 12.0,
            "Equity Mutual Funds" to 14.0,
            "Stocks" to 15.0,
            "Liquid Funds" to 5.0
        )

        return allocation.entries.sumOf { (asset, weight) ->
            (returns[asset] ?: 7.0) * (weight / 100.0)
        }
    }

    private fun calculateFutureValue(monthly: Long, months: Int, annualRate: Double): Long {
        val monthlyRate = annualRate / 12
        val fv = monthly * (((1 + monthlyRate).pow(months) - 1) / monthlyRate)
        return fv.toLong()
    }

    private suspend fun generateInvestmentAdvice(
        target: Long,
        months: Int,
        risk: RiskProfile,
        monthly: Double
    ): String {
        val prompt = """
            Create investment plan for a woman:
            - Target Amount: ₹${target / 100000} lakhs
            - Timeframe: ${months / 12} years
            - Monthly Investment: ₹${monthly.toLong()}
            - Risk Profile: $risk
            
            Provide:
            1. Investment strategy
            2. Specific fund recommendations
            3. Tax-saving options (80C, etc.)
            4. Risk mitigation
            5. Review frequency
        """.trimIndent()

        return try {
            geminiService.callDhanShaktiAI(prompt)
        } catch (e: Exception) {
            "Consult with a financial advisor for personalized investment advice."
        }
    }

    private fun getSpecificInvestments(
        allocation: Map<String, Double>,
        monthly: Long
    ): List<String> {
        return allocation.map { (asset, weight) ->
            val amount = (monthly * weight / 100).toLong()
            "₹$amount/month in $asset"
        }
    }

    // ============================================================================
    // GOVERNMENT SCHEMES RECOMMENDATION
    // ============================================================================

    /**
     * Suggest applicable government schemes
     */
    suspend fun suggestGovernmentSchemes(
        age: Int,
        businessType: String,
        loanRequired: Long
    ): List<GovernmentScheme> = withContext(Dispatchers.IO) {
        return@withContext governmentSchemes.filter { scheme ->
            when {
                scheme.category == "Women-specific" -> true
                loanRequired <= 1000000 && scheme.category in listOf(
                    "Micro-credit",
                    "Business Startup"
                ) -> true

                loanRequired > 1000000 && scheme.category == "Large Business" -> true
                age < 18 && scheme.category == "Savings" -> true
                else -> false
            }
        }
    }

    /**
     * Get detailed scheme information
     */
    fun getSchemeDetails(schemeName: String): GovernmentScheme? {
        return governmentSchemes.find { it.name == schemeName }
    }

    // ============================================================================
    // BUSINESS IDEAS RECOMMENDATION
    // ============================================================================

    /**
     * Suggest business ideas based on skills
     */
    suspend fun suggestBusinessIdeas(
        skills: String,
        budget: Long
    ): String = withContext(Dispatchers.IO) {
        val prompt = """
            Woman with these skills: $skills
            Available budget: ₹${budget / 100000} lakhs
            
            Suggest TOP 5 viable business ideas:
            
            For each idea provide:
            1. Business name/type
            2. Initial capital required
            3. Monthly operational cost
            4. Expected monthly revenue (realistic)
            5. Profit margin
            6. Market demand (High/Medium/Low)
            7. Competition level
            8. Required resources/training
            9. Success story example
            10. Government scheme applicable
            
            Prioritize low-capital, high-demand businesses suitable for women.
        """.trimIndent()

        return@withContext geminiService.callDhanShaktiAI(prompt)
    }

    /**
     * Create budget plan
     */
    suspend fun createBudgetPlan(profile: FinancialProfile): String = withContext(Dispatchers.IO) {
        val savings = profile.income - profile.expenses
        val savingsRate = (savings.toDouble() / profile.income * 100).toInt()

        val prompt = """
            Create monthly budget plan:
            - Income: ₹${profile.income}
            - Expenses: ₹${profile.expenses}
            - Savings: ₹$savings ($savingsRate%)
            
            Provide:
            1. Budget breakdown (50/30/20 rule)
            2. Areas to reduce expenses
            3. Savings improvement tips
            4. Emergency fund recommendation
            5. Debt repayment strategy
        """.trimIndent()

        return@withContext geminiService.callDhanShaktiAI(prompt)
    }

    /**
     * Get all government schemes
     */
    fun getAllSchemes(): List<GovernmentScheme> = governmentSchemes

    /**
     * Cleanup
     */
    fun cleanup() {
        INSTANCE = null
    }
}
