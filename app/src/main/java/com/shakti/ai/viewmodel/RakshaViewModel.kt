package com.shakti.ai.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shakti.ai.ai.RakshaAI
import com.shakti.ai.models.AbuseAnalysis
import com.shakti.ai.models.EmergencyResource
import com.shakti.ai.models.IncidentReport
import com.shakti.ai.models.SafetyPlan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RakshaViewModel(application: Application) : AndroidViewModel(application) {

    private val rakshaAI = RakshaAI.getInstance(application)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _abuseAnalysis = MutableStateFlow<AbuseAnalysis?>(null)
    val abuseAnalysis: StateFlow<AbuseAnalysis?> = _abuseAnalysis.asStateFlow()

    private val _safetyPlan = MutableStateFlow<SafetyPlan?>(null)
    val safetyPlan: StateFlow<SafetyPlan?> = _safetyPlan.asStateFlow()

    private val _shelterInfo = MutableStateFlow<String>("")
    val shelterInfo: StateFlow<String> = _shelterInfo.asStateFlow()

    private val _emergencyResources =
        MutableStateFlow<List<EmergencyResource>>(emptyList())
    val emergencyResources: StateFlow<List<EmergencyResource>> =
        _emergencyResources.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun detectAbusePatterns(incidents: List<IncidentReport>) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val analysis = rakshaAI.detectAbusePatterns(incidents)
                _abuseAnalysis.value = analysis
                _emergencyResources.value = analysis.resources
            } catch (e: Exception) {
                _errorMessage.value = "Failed to analyze patterns: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createSafetyPlan(situation: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val plan = rakshaAI.createSafetyPlan(situation)
                _safetyPlan.value = plan
            } catch (e: Exception) {
                _errorMessage.value = "Failed to create safety plan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun findShelters(location: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val info = rakshaAI.findShelters(location)
                _shelterInfo.value = info
            } catch (e: Exception) {
                _errorMessage.value = "Failed to find shelters: ${e.message}"
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
        rakshaAI.cleanup()
    }
}
