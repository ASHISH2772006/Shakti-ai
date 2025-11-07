package com.shakti.ai.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shakti.ai.ai.NyayaAI
import com.shakti.ai.models.DocumentType
import com.shakti.ai.models.LegalAdvice
import com.shakti.ai.models.LegalCategory
import com.shakti.ai.models.LegalQuery
import com.shakti.ai.models.LegalSection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NyayaViewModel(application: Application) : AndroidViewModel(application) {

    private val nyayaAI = NyayaAI.getInstance(application)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _firDocument = MutableStateFlow<String>("")
    val firDocument: StateFlow<String> = _firDocument.asStateFlow()

    private val _legalAdvice = MutableStateFlow<LegalAdvice?>(null)
    val legalAdvice: StateFlow<LegalAdvice?> = _legalAdvice.asStateFlow()

    private val _legalRightsExplanation = MutableStateFlow<String>("")
    val legalRightsExplanation: StateFlow<String> = _legalRightsExplanation.asStateFlow()

    private val _legalDocument = MutableStateFlow<String>("")
    val legalDocument: StateFlow<String> = _legalDocument.asStateFlow()

    private val _lawyerInfo = MutableStateFlow<String>("")
    val lawyerInfo: StateFlow<String> = _lawyerInfo.asStateFlow()

    private val _ipcSections = MutableStateFlow<List<LegalSection>>(emptyList())
    val ipcSections: StateFlow<List<LegalSection>> = _ipcSections.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadIPCSections()
    }

    fun generateFIR(complaint: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val fir = nyayaAI.generateFIRFromComplaint(complaint)
                _firDocument.value = fir
            } catch (e: Exception) {
                _errorMessage.value = "Failed to generate FIR: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun explainLegalRights(topic: String, category: LegalCategory) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val explanation = nyayaAI.explainLegalRights(topic, category)
                _legalRightsExplanation.value = explanation
            } catch (e: Exception) {
                _errorMessage.value = "Failed to get legal explanation: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun draftLegalDocument(documentType: DocumentType, details: Map<String, String>) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val document = nyayaAI.draftLegalDocument(documentType, details)
                _legalDocument.value = document
            } catch (e: Exception) {
                _errorMessage.value = "Failed to draft document: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun matchWithLawyer(caseType: String, location: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val info = nyayaAI.matchWithLawyer(caseType, location)
                _lawyerInfo.value = info
            } catch (e: Exception) {
                _errorMessage.value = "Failed to find lawyer: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun analyzeLegalQuery(query: String, category: LegalCategory) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val legalQuery = LegalQuery(
                    query = query,
                    category = category,
                    timestamp = System.currentTimeMillis()
                )
                val advice = nyayaAI.analyzeLegalQuery(legalQuery)
                _legalAdvice.value = advice
            } catch (e: Exception) {
                _errorMessage.value = "Failed to analyze query: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadIPCSections() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val sections = nyayaAI.getAllIPCSections()
                _ipcSections.value = sections
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load IPC sections: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getSectionDetails(sectionNumber: String): LegalSection? {
        return nyayaAI.getSectionDetails(sectionNumber)
    }

    fun clearError() {
        _errorMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        nyayaAI.cleanup()
    }
}
