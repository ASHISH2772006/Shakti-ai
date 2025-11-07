package com.shakti.ai.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shakti.ai.ai.SwasthyaAI
import com.shakti.ai.models.HealthMetrics
import com.shakti.ai.models.HealthAdvice
import com.shakti.ai.models.HealthCategory
import com.shakti.ai.models.CycleTracking
import com.shakti.ai.models.SymptomAnalysis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class SwasthyaViewModel(application: Application) : AndroidViewModel(application) {

    private val swasthyaAI = SwasthyaAI.getInstance(application)

    private val _healthAdvice = MutableStateFlow<List<HealthAdvice>>(emptyList())
    val healthAdvice: StateFlow<List<HealthAdvice>> = _healthAdvice.asStateFlow()

    private val _wellnessTips = MutableStateFlow<List<String>>(emptyList())
    val wellnessTips: StateFlow<List<String>> = _wellnessTips.asStateFlow()

    private val _emergencyContacts = MutableStateFlow<Map<String, String>>(emptyMap())
    val emergencyContacts: StateFlow<Map<String, String>> = _emergencyContacts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _cycleTracking = MutableStateFlow<CycleTracking?>(null)
    val cycleTracking: StateFlow<CycleTracking?> = _cycleTracking.asStateFlow()

    private val _symptomAnalysis = MutableStateFlow<SymptomAnalysis?>(null)
    val symptomAnalysis: StateFlow<SymptomAnalysis?> = _symptomAnalysis.asStateFlow()

    private val _telemedicineInfo = MutableStateFlow<String>("")
    val telemedicineInfo: StateFlow<String> = _telemedicineInfo.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadEmergencyContacts()
    }

    fun analyzeHealth(metrics: HealthMetrics) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val advice = swasthyaAI.analyzeHealthMetrics(metrics)
                _healthAdvice.value = advice
            } catch (e: Exception) {
                _errorMessage.value = "Failed to analyze health: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getWellnessTips(category: HealthCategory) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val tips = swasthyaAI.getWellnessTips(category)
                _wellnessTips.value = tips
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load wellness tips: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadEmergencyContacts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val contacts = swasthyaAI.getEmergencyContacts()
                _emergencyContacts.value = contacts
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load emergency contacts: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun trackMenstrualCycle(
        lastPeriodDate: LocalDate,
        cycleLength: Int = 28,
        periodDuration: Int = 5
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val tracking = swasthyaAI.trackMenstrualCycle(
                    lastPeriodDate,
                    cycleLength,
                    periodDuration
                )
                _cycleTracking.value = tracking
            } catch (e: Exception) {
                _errorMessage.value = "Failed to track cycle: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun checkSymptoms(symptoms: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val analysis = swasthyaAI.checkSymptoms(symptoms)
                _symptomAnalysis.value = analysis
            } catch (e: Exception) {
                _errorMessage.value = "Failed to check symptoms: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun findTelemedicineServices(location: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val info = swasthyaAI.findTelemedicineServices(location)
                _telemedicineInfo.value = info
            } catch (e: Exception) {
                _errorMessage.value = "Failed to find telemedicine services: ${e.message}"
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
        swasthyaAI.cleanup()
    }
}
