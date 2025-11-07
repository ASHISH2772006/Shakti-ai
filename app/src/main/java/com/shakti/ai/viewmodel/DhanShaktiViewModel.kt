package com.shakti.ai.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shakti.ai.ai.DhanShaktiAI
import com.shakti.ai.models.GovernmentScheme
import com.shakti.ai.models.LoanAssessment
import com.shakti.ai.models.InvestmentPlan
import com.shakti.ai.models.RiskProfile
import com.shakti.ai.models.FinancialProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DhanShaktiViewModel(application: Application) : AndroidViewModel(application) {

    private val dhanShaktiAI = DhanShaktiAI.getInstance(application)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _loanAssessment = MutableStateFlow<LoanAssessment?>(null)
    val loanAssessment: StateFlow<LoanAssessment?> = _loanAssessment.asStateFlow()

    private val _governmentSchemes = MutableStateFlow<List<GovernmentScheme>>(emptyList())
    val governmentSchemes: StateFlow<List<GovernmentScheme>> =
        _governmentSchemes.asStateFlow()

    private val _investmentPlan = MutableStateFlow<InvestmentPlan?>(null)
    val investmentPlan: StateFlow<InvestmentPlan?> = _investmentPlan.asStateFlow()

    private val _businessIdeas = MutableStateFlow<String>("")
    val businessIdeas: StateFlow<String> = _businessIdeas.asStateFlow()

    private val _budgetPlan = MutableStateFlow<String>("")
    val budgetPlan: StateFlow<String> = _budgetPlan.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadAllSchemes()
    }

    fun assessLoanEligibility(
        income: Long,
        age: Int,
        businessType: String,
        existingLoans: Long = 0,
        creditScore: Int = 650
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val assessment = dhanShaktiAI.assessLoanEligibility(
                    income = income,
                    age = age,
                    businessType = businessType,
                    existingLoans = existingLoans,
                    creditScore = creditScore
                )
                _loanAssessment.value = assessment
            } catch (e: Exception) {
                _errorMessage.value = "Failed to assess loan eligibility: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadAllSchemes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val schemes = dhanShaktiAI.getAllSchemes()
                _governmentSchemes.value = schemes
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load schemes: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun suggestGovernmentSchemes(age: Int, businessType: String, loanRequired: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val schemes = dhanShaktiAI.suggestGovernmentSchemes(age, businessType, loanRequired)
                _governmentSchemes.value = schemes
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load schemes: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createInvestmentPlan(
        targetAmount: Long,
        timeframeMonths: Int,
        riskAppetite: RiskProfile,
        currentSavings: Long = 0
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val plan = dhanShaktiAI.createInvestmentPlan(
                    targetAmount = targetAmount,
                    timeframeMonths = timeframeMonths,
                    riskAppetite = riskAppetite,
                    currentSavings = currentSavings
                )
                _investmentPlan.value = plan
            } catch (e: Exception) {
                _errorMessage.value = "Failed to create investment plan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun suggestBusinessIdeas(skills: String, budget: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val ideas = dhanShaktiAI.suggestBusinessIdeas(skills, budget)
                _businessIdeas.value = ideas
            } catch (e: Exception) {
                _errorMessage.value = "Failed to generate business ideas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createBudgetPlan(income: Double, expenses: Double, savings: Double, investments: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val profile = FinancialProfile(
                    userId = "user",
                    income = income,
                    expenses = expenses,
                    savings = savings,
                    investments = investments
                )
                val budget = dhanShaktiAI.createBudgetPlan(profile)
                _budgetPlan.value = budget
            } catch (e: Exception) {
                _errorMessage.value = "Failed to create budget plan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        dhanShaktiAI.cleanup()
    }
}
