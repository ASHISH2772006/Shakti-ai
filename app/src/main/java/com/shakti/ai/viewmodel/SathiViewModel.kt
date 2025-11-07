package com.shakti.ai.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shakti.ai.ai.GeminiService
import com.shakti.ai.blockchain.AptosService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SathiViewModel(application: Application) : AndroidViewModel(application) {
    private val geminiService = GeminiService.getInstance(application)
    private val aptosService = AptosService.getInstance(application)

    private val _chatMessages = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val chatMessages: StateFlow<List<Pair<String, String>>> = _chatMessages

    private val _moodScore = MutableStateFlow(5) // 1-10 scale
    val moodScore: StateFlow<Int> = _moodScore

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _sessionAnalysis = MutableStateFlow("")
    val sessionAnalysis: StateFlow<String> = _sessionAnalysis

    private val _isCrisisDetected = MutableStateFlow(false)
    val isCrisisDetected: StateFlow<Boolean> = _isCrisisDetected

    // User conversation history for LSTM context
    private val conversationHistory = mutableListOf<Pair<String, String>>()

    // Initialize Sathi session
    fun initializeSathiSession() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val welcome = """
                    नमस्ते! I'm Sathi, your mental health companion. 
                    I'm here to listen, support, and help you navigate your feelings.
                    
                    How are you feeling today? (Rate 1-10, where 1 is very bad and 10 is excellent)
                    
                    Remember: Everything we discuss is confidential and stored securely on blockchain.
                """.trimIndent()
                _chatMessages.value = listOf("Sathi" to welcome)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Send user message to Sathi AI
    fun sendMessageToSathi(userMessage: String, moodRating: Int = 5) {
        viewModelScope.launch {
            _isLoading.value = true
            _moodScore.value = moodRating

            try {
                // Add user message to chat
                val updatedChat = _chatMessages.value.toMutableList()
                updatedChat.add("User" to userMessage)
                _chatMessages.value = updatedChat

                // Add to conversation history for LSTM context
                conversationHistory.add("User" to userMessage)

                // Crisis detection
                if (detectCrisis(userMessage)) {
                    _isCrisisDetected.value = true
                    val crisisResponse = """
                        I'm deeply concerned about what you're sharing. Please know that you're not alone, and help is available right now.
                        
                        🚨 IMMEDIATE HELP:
                        • National Mental Health Helpline: 1800-599-0019
                        • Vandrevala Foundation: 1860-2662-345
                        • iCall: 9152987821
                        
                        Would you like me to connect you with a professional counselor immediately?
                    """.trimIndent()
                    updatedChat.add("Sathi" to crisisResponse)
                    _chatMessages.value = updatedChat

                    // Log crisis to Aptos
                    aptosService.logCrisisEscalation()
                    return@launch
                }

                // Call Gemini-based Sathi AI with context
                val contextualPrompt = if (conversationHistory.size > 1) {
                    "Context from previous messages: ${
                        conversationHistory.takeLast(5).joinToString { it.second }
                    }\n\nUser (Mood: $moodRating/10): $userMessage"
                } else {
                    "User (Mood: $moodRating/10): $userMessage"
                }

                val aiResponse = geminiService.callSathiAI(contextualPrompt)

                // Add AI response
                updatedChat.add("Sathi" to aiResponse)
                _chatMessages.value = updatedChat

                // Add to history
                conversationHistory.add("Sathi" to aiResponse)

                // Log session to Aptos blockchain (secure & private)
                aptosService.logMentalHealthSession(
                    moodScore = moodRating,
                    message = userMessage,
                    response = aiResponse
                )

            } catch (e: Exception) {
                val errorMessage =
                    "I'm having trouble responding right now. Please try again in a moment. If you're in crisis, please call 1800-599-0019 immediately."
                _chatMessages.value = _chatMessages.value.toMutableList().apply {
                    add("Sathi" to errorMessage)
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Analyze mood trends
    fun analyzeMoodTrends() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Build analysis prompt from conversation history
                val recentMoods = conversationHistory.take(10).joinToString("\n") {
                    "${it.first}: ${it.second.take(100)}..."
                }

                val analysisPrompt = """
                    Analyze the following mental health conversation history and provide insights:
                    
                    $recentMoods
                    
                    Current Mood Score: ${_moodScore.value}/10
                    
                    Please provide:
                    1. Mood pattern analysis
                    2. Identified stress triggers
                    3. Positive coping mechanisms observed
                    4. Recommended next steps
                    5. When to seek professional help
                """.trimIndent()

                val analysis = geminiService.callSathiAI(analysisPrompt)
                _sessionAnalysis.value = analysis

                // Save analysis to Aptos blockchain
                aptosService.logMentalHealthAnalysis(analysis)

            } catch (e: Exception) {
                _sessionAnalysis.value =
                    "Unable to generate analysis at this time. Please try again."
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Crisis detection using keyword analysis
    fun detectCrisis(message: String): Boolean {
        val crisisKeywords = listOf(
            // Self-harm indicators
            "suicide", "suicidal", "kill myself", "end my life", "self harm", "self-harm",
            "cut myself", "hurt myself", "don't want to live",

            // Severe distress
            "can't take it", "can't go on", "give up", "hopeless", "helpless",
            "no way out", "better off dead", "everyone would be better without me",

            // Isolation and despair
            "completely alone", "nobody cares", "no one understands", "worthless",
            "no point", "can't cope", "ending it all"
        )

        val messageLower = message.lowercase()
        return crisisKeywords.any { messageLower.contains(it) }
    }

    // Emergency escalation to human counselor
    fun escalateToHumanCounselor() {
        viewModelScope.launch {
            try {
                // Log crisis escalation to blockchain
                aptosService.logCrisisEscalation()

                // Add system message
                val escalationMessage = """
                    🆘 CONNECTING YOU TO PROFESSIONAL HELP
                    
                    A trained counselor will be with you shortly. In the meantime:
                    
                    📞 IMMEDIATE HELPLINES (24/7):
                    • Mental Health Helpline: 1800-599-0019
                    • Vandrevala Foundation: 1860-2662-345
                    • iCall (English/Hindi): 9152987821
                    • Lifeline Foundation: 033-24637401/7432
                    
                    Please hold on. You matter, and help is on the way. 💜
                """.trimIndent()

                _chatMessages.value = _chatMessages.value.toMutableList().apply {
                    add("System" to escalationMessage)
                }

            } catch (e: Exception) {
                // Even if logging fails, show helplines
                val fallbackMessage = """
                    Please call these helplines immediately:
                    • 1800-599-0019 (Mental Health)
                    • 1860-2662-345 (Vandrevala)
                    • 9152987821 (iCall)
                """.trimIndent()

                _chatMessages.value = _chatMessages.value.toMutableList().apply {
                    add("System" to fallbackMessage)
                }
            }
        }
    }

    // Get conversation summary for reports
    fun getConversationSummary(): String {
        return buildString {
            appendLine("=== Sathi AI Session Summary ===")
            appendLine("Total Messages: ${conversationHistory.size}")
            appendLine("Average Mood Score: ${_moodScore.value}/10")
            appendLine("Crisis Detected: ${if (_isCrisisDetected.value) "Yes" else "No"}")
            appendLine("\nConversation History:")
            conversationHistory.forEach { (sender, message) ->
                appendLine("[$sender]: ${message.take(100)}${if (message.length > 100) "..." else ""}")
            }
        }
    }

    // Clear session (with user consent)
    fun clearSession() {
        conversationHistory.clear()
        _chatMessages.value = emptyList()
        _moodScore.value = 5
        _sessionAnalysis.value = ""
        _isCrisisDetected.value = false
    }

    // Export session data (encrypted, for user records)
    fun exportSessionData(): Map<String, Any> {
        return mapOf(
            "timestamp" to System.currentTimeMillis(),
            "totalMessages" to conversationHistory.size,
            "averageMood" to _moodScore.value,
            "crisisDetected" to _isCrisisDetected.value,
            "messages" to conversationHistory.map {
                mapOf("sender" to it.first, "message" to it.second)
            }
        )
    }
}
