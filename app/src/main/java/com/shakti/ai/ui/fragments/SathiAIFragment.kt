package com.shakti.ai.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shakti.ai.R
import com.shakti.ai.viewmodel.SathiViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * SathiAIFragment - Mental Health Support Module
 * Interactive implementation with Gemini AI integration via ViewModel
 */
class SathiAIFragment : Fragment() {

    // Use ViewModel - NO direct GeminiService calls
    private val viewModel: SathiViewModel by viewModels()

    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    // UI Elements
    private lateinit var messageInput: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var voiceButton: Button
    private lateinit var uploadButton: Button
    private lateinit var breathingButton: Button
    private lateinit var gratitudeButton: Button
    private lateinit var supportGroupButton: Button
    private lateinit var emergencyButton: Button
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var moodScore: TextView
    private lateinit var anxietyScore: TextView
    private lateinit var conversationCount: TextView
    private lateinit var moodProgress: ProgressBar
    private lateinit var anxietyProgress: ProgressBar

    // Media recording
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private var audioFilePath: String? = null

    // Chat messages
    private val chatMessages = mutableListOf<ChatMessage>()
    private lateinit var chatAdapter: ChatAdapter

    // Permissions
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.RECORD_AUDIO] == true -> {
                startVoiceRecording()
            }
            else -> {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val pickMediaLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                handleMediaUpload(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sathi_ai, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("SathiAI", Context.MODE_PRIVATE)

        initializeViews(view)
        setupClickListeners()
        setupRecyclerView()
        loadSavedData()
        observeViewModel()

        // Initialize Sathi AI session via ViewModel
        if (viewModel.chatMessages.value.isEmpty()) {
            viewModel.initializeSathiSession()
        }
    }

    private fun initializeViews(view: View) {
        messageInput = view.findViewById(R.id.message_input)
        sendButton = view.findViewById(R.id.btn_send_message)
        voiceButton = view.findViewById(R.id.btn_voice_message)
        uploadButton = view.findViewById(R.id.btn_upload_media)
        breathingButton = view.findViewById(R.id.btn_breathing_exercise)
        gratitudeButton = view.findViewById(R.id.btn_gratitude_journal)
        supportGroupButton = view.findViewById(R.id.btn_support_group)
        emergencyButton = view.findViewById(R.id.btn_emergency_helpline)
        chatRecyclerView = view.findViewById(R.id.chat_recycler_view)
        moodScore = view.findViewById(R.id.mood_score)
        anxietyScore = view.findViewById(R.id.anxiety_score)
        conversationCount = view.findViewById(R.id.conversation_count)
        moodProgress = view.findViewById(R.id.mood_progress)
        anxietyProgress = view.findViewById(R.id.anxiety_progress)
    }

    private fun setupClickListeners() {
        sendButton.setOnClickListener {
            sendMessage()
        }

        voiceButton.setOnClickListener {
            handleVoiceRecording()
        }

        uploadButton.setOnClickListener {
            openMediaPicker()
        }

        breathingButton.setOnClickListener {
            startBreathingExercise()
        }

        gratitudeButton.setOnClickListener {
            openGratitudeJournal()
        }

        supportGroupButton.setOnClickListener {
            joinSupportGroup()
        }

        emergencyButton.setOnClickListener {
            showEmergencyContacts()
        }

        // Tab switching
        view?.findViewById<TextView>(R.id.tab_ai_companion)?.setOnClickListener {
            switchTab(it as TextView)
        }
        view?.findViewById<TextView>(R.id.tab_mental_health)?.setOnClickListener {
            switchTab(it as TextView)
        }
        view?.findViewById<TextView>(R.id.tab_support_resources)?.setOnClickListener {
            switchTab(it as TextView)
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(chatMessages)
        chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }
    }

    private fun observeViewModel() {
        // Observe chat messages from ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.chatMessages.collect { messages ->
                chatMessages.clear()
                messages.forEach { (sender, text) ->
                    chatMessages.add(
                        ChatMessage(
                            text = text,
                            isUser = sender == "User",
                            timestamp = SimpleDateFormat(
                                "h:mm a",
                                Locale.getDefault()
                            ).format(Date())
                        )
                    )
                }
                chatAdapter.notifyDataSetChanged()
                if (chatMessages.isNotEmpty()) {
                    chatRecyclerView.scrollToPosition(chatMessages.size - 1)
                }
            }
        }

        // Observe mood score
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.moodScore.collect { score ->
                val mood = (score * 10).coerceIn(0, 100)
                moodProgress.progress = mood
                moodScore.text = "$mood%"

                val anxiety = 100 - mood
                anxietyProgress.progress = anxiety
                anxietyScore.text = "$anxiety%"
            }
        }

        // Observe loading state
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                sendButton.isEnabled = !isLoading
                messageInput.isEnabled = !isLoading
            }
        }

        // Observe crisis detection
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isCrisisDetected.collect { isCrisis ->
                if (isCrisis) {
                    emergencyButton.setBackgroundColor(
                        resources.getColor(
                            android.R.color.holo_red_light,
                            null
                        )
                    )
                    Toast.makeText(
                        context,
                        "Crisis detected. Emergency resources available.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun loadSavedData() {
        val savedMood = sharedPreferences.getInt("mood_score", 65)
        val savedAnxiety = sharedPreferences.getInt("anxiety_score", 35)
        val savedConversations = sharedPreferences.getInt("conversation_count", 0)

        moodProgress.progress = savedMood
        anxietyProgress.progress = savedAnxiety
        moodScore.text = "$savedMood%"
        anxietyScore.text = "$savedAnxiety%"
        conversationCount.text = savedConversations.toString()
    }

    private fun saveData() {
        sharedPreferences.edit().apply {
            putInt("mood_score", moodProgress.progress)
            putInt("anxiety_score", anxietyProgress.progress)
            putInt("conversation_count", conversationCount.text.toString().toIntOrNull() ?: 0)
            apply()
        }
    }

    private fun sendMessage() {
        val message = messageInput.text.toString().trim()
        if (message.isNotEmpty()) {
            messageInput.text.clear()

            // Call ViewModel instead of GeminiService directly
            val moodRating = moodProgress.progress / 10
            viewModel.sendMessageToSathi(message, moodRating)

            // Update conversation count
            val count = conversationCount.text.toString().toIntOrNull() ?: 0
            conversationCount.text = (count + 1).toString()
            saveData()
        }
    }

    private fun handleVoiceRecording() {
        if (isRecording) {
            stopVoiceRecording()
        } else {
            checkAudioPermissionAndRecord()
        }
    }

    private fun checkAudioPermissionAndRecord() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                startVoiceRecording()
            }
            else -> {
                requestPermissionLauncher.launch(
                    arrayOf(Manifest.permission.RECORD_AUDIO)
                )
            }
        }
    }

    private fun startVoiceRecording() {
        try {
            audioFilePath =
                "${requireContext().externalCacheDir?.absolutePath}/audio_${System.currentTimeMillis()}.3gp"

            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(audioFilePath)
                prepare()
                start()
            }

            isRecording = true
            voiceButton.text = "⏹️ Stop Recording"
            voiceButton.setBackgroundColor(resources.getColor(android.R.color.holo_red_light, null))
            Toast.makeText(context, "🎤 Recording started...", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(context, "Failed to start recording: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun stopVoiceRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            isRecording = false
            voiceButton.text = "🎤 Voice Message"
            voiceButton.setBackgroundColor(resources.getColor(R.color.primary, null))

            Toast.makeText(context, "✅ Recording saved!", Toast.LENGTH_SHORT).show()

            // Send voice message indicator via ViewModel
            viewModel.sendMessageToSathi("🎤 Voice message recorded", 5)
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to stop recording: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun openMediaPicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/* video/*"
        pickMediaLauncher.launch(intent)
    }

    private fun handleMediaUpload(uri: Uri) {
        Toast.makeText(context, "📎 Media uploaded: ${uri.lastPathSegment}", Toast.LENGTH_SHORT)
            .show()

        // Send media upload indicator via ViewModel
        viewModel.sendMessageToSathi("📎 Media uploaded: ${uri.lastPathSegment}", 5)
    }

    private fun startBreathingExercise() {
        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setTitle("🫁 Breathing Exercise")
            .setMessage(
                """
                Let's practice the 4-7-8 breathing technique:
                
                1. Breathe in slowly through your nose for 4 seconds
                2. Hold your breath for 7 seconds
                3. Breathe out slowly through your mouth for 8 seconds
                4. Repeat 4-5 times
                
                Focus on your breath and let go of tension.
                This technique helps reduce anxiety and promote relaxation.
            """.trimIndent()
            )
            .setPositiveButton("Start") { _, _ ->
                // Send via ViewModel instead of direct Gemini call
                viewModel.sendMessageToSathi(
                    "I'm doing a breathing exercise. Give me a short encouraging message.",
                    5
                )
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    private fun openGratitudeJournal() {
        val input = EditText(requireContext())
        input.hint = "What are you grateful for today?"
        input.setPadding(50, 40, 50, 40)

        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setTitle("💗 Gratitude Journal")
            .setMessage("Taking time to appreciate the good things in life can significantly improve your mood and mental well-being.")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val gratitude = input.text.toString()
                if (gratitude.isNotEmpty()) {
                    // Send via ViewModel instead of direct Gemini call
                    viewModel.sendMessageToSathi(
                        "I'm grateful for: $gratitude. Acknowledge this and encourage me.",
                        7
                    )

                    // Boost mood score
                    val currentMood = moodProgress.progress
                    val newMood = minOf(100, currentMood + 10)
                    moodProgress.progress = newMood
                    moodScore.text = "$newMood%"

                    val newAnxiety = 100 - newMood
                    anxietyProgress.progress = newAnxiety
                    anxietyScore.text = "$newAnxiety%"

                    saveData()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    private fun joinSupportGroup() {
        val groups = arrayOf(
            "Anxiety Support Group (45 members)",
            "Depression Support Circle (32 members)",
            "Women's Wellness Community (128 members)",
            "Crisis Support Network (67 members)"
        )

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("👥 Join Support Group")
            .setItems(groups) { _, which ->
                val selectedGroup = groups[which]
                Toast.makeText(context, "✅ Joined: $selectedGroup", Toast.LENGTH_SHORT).show()

                // Send via ViewModel instead of direct Gemini call
                viewModel.sendMessageToSathi(
                    "I just joined a support group: $selectedGroup. Encourage me about this decision.",
                    6
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEmergencyContacts() {
        val message = """
            🆘 Emergency Mental Health Helplines:
            
            NIMHANS Helpline: 080-4611-0007
            (Available 24/7 for mental health emergencies)
            
            Women Helpline: 1091
            (For women in distress)
            
            National Emergency: 112
            (For immediate danger)
            
            Vandrevala Foundation: 1860-2662-345
            (24/7 Mental Health Support)
            
            If you're having thoughts of self-harm, please call these numbers immediately. Your life matters.
        """.trimIndent()

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("🚨 Emergency Support")
            .setMessage(message)
            .setPositiveButton("Call NIMHANS") { _, _ ->
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:08046110007")
                }
                startActivity(intent)
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun switchTab(selectedTab: TextView) {
        // Reset all tabs
        view?.findViewById<TextView>(R.id.tab_ai_companion)?.apply {
            setBackgroundColor(resources.getColor(android.R.color.transparent, null))
            setTextColor(resources.getColor(R.color.text_secondary, null))
        }
        view?.findViewById<TextView>(R.id.tab_mental_health)?.apply {
            setBackgroundColor(resources.getColor(android.R.color.transparent, null))
            setTextColor(resources.getColor(R.color.text_secondary, null))
        }
        view?.findViewById<TextView>(R.id.tab_support_resources)?.apply {
            setBackgroundColor(resources.getColor(android.R.color.transparent, null))
            setTextColor(resources.getColor(R.color.text_secondary, null))
        }

        // Highlight selected tab
        selectedTab.apply {
            setBackgroundColor(resources.getColor(R.color.primary_light, null))
            setTextColor(resources.getColor(R.color.text_primary, null))
        }

        Toast.makeText(context, "Switched to ${selectedTab.text}", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaRecorder?.release()
        mediaRecorder = null
        saveData()
    }

    // Data classes
    data class ChatMessage(
        val text: String,
        val isUser: Boolean,
        val timestamp: String
    )

    // Chat Adapter
    inner class ChatAdapter(private val messages: List<ChatMessage>) :
        RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

        inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val messageText: TextView = view.findViewById(android.R.id.text1)
            val timeText: TextView = view.findViewById(android.R.id.text2)
            val card: CardView = view as CardView
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val card = CardView(parent.context).apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 8, 16, 8)
                }
                radius = 16f
                cardElevation = 4f

                val layout = LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(32, 24, 32, 24)

                    addView(TextView(context).apply {
                        id = android.R.id.text1
                        textSize = 15f
                        setTextColor(resources.getColor(R.color.text_primary, null))
                        setLineSpacing(4f, 1.1f)
                    })

                    addView(TextView(context).apply {
                        id = android.R.id.text2
                        textSize = 11f
                        setTextColor(resources.getColor(R.color.text_secondary, null))
                        setPadding(0, 12, 0, 0)
                    })
                }

                addView(layout)
            }

            return MessageViewHolder(card)
        }

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            val message = messages[position]
            holder.messageText.text = message.text
            holder.timeText.text = message.timestamp

            // Different styling for user and AI messages
            if (message.isUser) {
                holder.card.setCardBackgroundColor(resources.getColor(R.color.primary_light, null))
                holder.messageText.setTextColor(resources.getColor(R.color.text_primary, null))
            } else {
                holder.card.setCardBackgroundColor(resources.getColor(android.R.color.white, null))
                holder.messageText.setTextColor(resources.getColor(R.color.text_primary, null))
            }
        }

        override fun getItemCount() = messages.size
    }
}
