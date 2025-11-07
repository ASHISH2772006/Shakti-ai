package com.shakti.ai.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shakti.ai.ai.GyaanAI
import com.shakti.ai.models.EducationalContent
import com.shakti.ai.models.EducationCategory
import com.shakti.ai.models.DifficultyLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GyaanViewModel(application: Application) : AndroidViewModel(application) {

    private val gyaanAI = GyaanAI.getInstance(application)

    private val _content = MutableStateFlow<List<EducationalContent>>(emptyList())
    val content: StateFlow<List<EducationalContent>> = _content.asStateFlow()

    private val _learningPath = MutableStateFlow<List<EducationalContent>>(emptyList())
    val learningPath: StateFlow<List<EducationalContent>> = _learningPath.asStateFlow()

    private val _totalTime = MutableStateFlow(0)
    val totalTime: StateFlow<Int> = _totalTime.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _scholarships = MutableStateFlow<String>("")
    val scholarships: StateFlow<String> = _scholarships.asStateFlow()

    private val _courseRecommendations = MutableStateFlow<String>("")
    val courseRecommendations: StateFlow<String> = _courseRecommendations.asStateFlow()

    private val _skillGapAnalysis = MutableStateFlow<String>("")
    val skillGapAnalysis: StateFlow<String> = _skillGapAnalysis.asStateFlow()

    private val _vocationalTraining = MutableStateFlow<String>("")
    val vocationalTraining: StateFlow<String> = _vocationalTraining.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadRecommendedContent(
        category: EducationCategory? = null,
        difficulty: DifficultyLevel? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val contents = gyaanAI.getRecommendedContent(category, difficulty)
                _content.value = contents
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load content: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchContent(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val results = gyaanAI.searchContent(query)
                _content.value = results
            } catch (e: Exception) {
                _errorMessage.value = "Failed to search content: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getLearningPath(startLevel: DifficultyLevel) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val path = gyaanAI.getLearningPath(startLevel)
                _learningPath.value = path
                val time = gyaanAI.getTotalLearningTime(path)
                _totalTime.value = time
            } catch (e: Exception) {
                _errorMessage.value = "Failed to generate learning path: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getContentByCategory(category: EducationCategory) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val contents = gyaanAI.getContentByCategory(category)
                _content.value = contents
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load category content: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun findScholarships(education: String, income: Long, category: String = "All") {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = gyaanAI.findScholarships(education, income, category)
                _scholarships.value = result
            } catch (e: Exception) {
                _errorMessage.value = "Failed to find scholarships: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun recommendCourses(currentSkills: List<String>, careerGoal: String, budget: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = gyaanAI.recommendCourses(currentSkills, careerGoal, budget)
                _courseRecommendations.value = result
            } catch (e: Exception) {
                _errorMessage.value = "Failed to recommend courses: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun analyzeSkillGap(currentRole: String, targetRole: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = gyaanAI.analyzeSkillGap(currentRole, targetRole)
                _skillGapAnalysis.value = result
            } catch (e: Exception) {
                _errorMessage.value = "Failed to analyze skill gap: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun findVocationalTraining(interest: String, location: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = gyaanAI.findVocationalTraining(interest, location)
                _vocationalTraining.value = result
            } catch (e: Exception) {
                _errorMessage.value = "Failed to find vocational training: ${e.message}"
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
        gyaanAI.cleanup()
    }
}
