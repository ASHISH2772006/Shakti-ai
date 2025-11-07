package com.shakti.ai.ai

import android.content.Context
import com.shakti.ai.models.EmotionalState
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * SathiAI - Emotional Support & Mental Health AI using LSTM model
 * Provides emotional analysis and mental health support
 */
class SathiAI(private val context: Context) {

    private var interpreter: Interpreter? = null
    private val modelInputSize = 128
    private val maxSequenceLength = 50

    init {
        loadModel()
    }

    private fun loadModel() {
        try {
            // Load the LSTM model from assets
            val modelFile = context.assets.open("sathi_lstm.tflite")
            val modelBytes = modelFile.readBytes()
            modelFile.close()

            val buffer = ByteBuffer.allocateDirect(modelBytes.size)
            buffer.order(ByteOrder.nativeOrder())
            buffer.put(modelBytes)

            interpreter = Interpreter(buffer)
        } catch (e: Exception) {
            e.printStackTrace()
            // Model file not found - will use fallback logic
        }
    }

    suspend fun analyzeEmotion(text: String): EmotionalState = withContext(Dispatchers.Default) {
        try {
            // Preprocess text to numerical input
            val input = preprocessText(text)

            // Run inference
            val output = Array(1) { FloatArray(5) } // 5 emotion classes
            interpreter?.run(input, output)

            // Get dominant emotion
            val emotions = listOf("happy", "sad", "angry", "fearful", "neutral")
            val maxIndex = output[0].indices.maxByOrNull { output[0][it] } ?: 4
            val confidence = output[0][maxIndex]

            EmotionalState(
                userId = "current_user",
                emotion = emotions[maxIndex],
                intensity = confidence
            )
        } catch (e: Exception) {
            // Fallback: simple keyword-based analysis
            analyzeSentimentFallback(text)
        }
    }

    private fun preprocessText(text: String): Array<FloatArray> {
        // Simple text preprocessing
        // In production, use proper tokenization and embedding
        val words = text.lowercase().split(" ")
        val sequence = FloatArray(maxSequenceLength) { 0f }

        words.take(maxSequenceLength).forEachIndexed { index, word ->
            sequence[index] = word.hashCode().toFloat() % 1000 / 1000f
        }

        return arrayOf(sequence)
    }

    private fun analyzeSentimentFallback(text: String): EmotionalState {
        val lowerText = text.lowercase()

        val positiveWords =
            listOf("happy", "joy", "great", "wonderful", "excellent", "good", "love")
        val negativeWords =
            listOf("sad", "depressed", "anxious", "worried", "scared", "fear", "bad")
        val angryWords = listOf("angry", "furious", "mad", "irritated", "frustrated")

        val positiveCount = positiveWords.count { lowerText.contains(it) }
        val negativeCount = negativeWords.count { lowerText.contains(it) }
        val angryCount = angryWords.count { lowerText.contains(it) }

        val emotion = when {
            angryCount > 0 -> "angry"
            positiveCount > negativeCount -> "happy"
            negativeCount > 0 -> "sad"
            else -> "neutral"
        }

        val intensity = ((positiveCount + negativeCount + angryCount).coerceAtMost(5) / 5f)

        return EmotionalState(
            userId = "current_user",
            emotion = emotion,
            intensity = intensity.coerceAtLeast(0.3f)
        )
    }

    suspend fun generateSupportMessage(emotion: String, intensity: Float): String =
        withContext(Dispatchers.Default) {
            when (emotion.lowercase()) {
                "sad", "depressed" -> {
                    when {
                        intensity > 0.7f -> "I can see you're going through a tough time. Remember, it's okay to not be okay. Would you like to talk about it or explore some coping strategies?"
                        intensity > 0.4f -> "I notice you're feeling down. Sometimes sharing can help. I'm here to listen."
                        else -> "It seems like something is bothering you. How can I support you today?"
                    }
                }

                "angry", "frustrated" -> {
                    "I understand you're feeling frustrated. Let's take a moment to breathe together. Would you like to talk about what's bothering you?"
                }

                "fearful", "anxious" -> {
                    "I can sense your worry. Remember, you're not alone in this. Let's work through this together. What's concerning you most right now?"
                }

                "happy", "joy" -> {
                    "It's wonderful to see you in good spirits! What's bringing you joy today?"
                }

                else -> {
                    "How are you feeling today? I'm here to support you in any way I can."
                }
            }
        }

    fun cleanup() {
        interpreter?.close()
        interpreter = null
    }

    companion object {
        private var instance: SathiAI? = null

        fun getInstance(context: Context): SathiAI {
            return instance ?: synchronized(this) {
                instance ?: SathiAI(context.applicationContext).also { instance = it }
            }
        }
    }
}
