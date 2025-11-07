package com.shakti.ai.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shakti.ai.R
import com.shakti.ai.viewmodel.GuardianViewModel

/**
 * GuardianAIFragment - Physical Safety Module
 * Interactive threat detection and mesh network
 */
class GuardianAIFragment : Fragment() {

    private val viewModel: GuardianViewModel by viewModels()

    private lateinit var guardianSwitch: SwitchCompat
    private lateinit var threatScoreText: TextView
    private lateinit var environmentalProgress: ProgressBar
    private lateinit var guardianRecyclerView: RecyclerView
    private lateinit var becomeGuardianButton: Button

    private val guardians = mutableListOf<Guardian>()
    private lateinit var guardianAdapter: GuardianAdapter

    private var isGuardianMode = true
    private var threatScore = 15

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_guardian_ai, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupClickListeners()
        setupRecyclerView()
        loadGuardians()

        viewModel.startGuardianMonitoring()
    }

    private fun initializeViews(view: View) {
        guardianSwitch = view.findViewById(R.id.guardian_switch)
        threatScoreText = view.findViewById(R.id.threat_score_number)
        environmentalProgress = view.findViewById(R.id.environmental_safety_progress)
        guardianRecyclerView = view.findViewById(R.id.guardian_recycler_view)
        becomeGuardianButton = view.findViewById(R.id.btn_become_guardian)

        threatScoreText.text = threatScore.toString()
        environmentalProgress.progress = 85
    }

    private fun setupClickListeners() {
        guardianSwitch.setOnCheckedChangeListener { _, isChecked ->
            isGuardianMode = isChecked
            if (isChecked) {
                Toast.makeText(context, "Guardian Mode: ACTIVE", Toast.LENGTH_SHORT).show()
                startThreatMonitoring()
            } else {
                Toast.makeText(context, "Guardian Mode: OFF", Toast.LENGTH_SHORT).show()
            }
        }

        becomeGuardianButton.setOnClickListener {
            showBecomeGuardianDialog()
        }
    }

    private fun setupRecyclerView() {
        guardianAdapter = GuardianAdapter(guardians)
        guardianRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = guardianAdapter
        }
    }

    private fun loadGuardians() {
        guardians.clear()
        guardians.addAll(
            listOf(
                Guardian("#247", "45m", "1 min", 4.9f),
                Guardian("#156", "120m", "2 min", 4.8f),
                Guardian("#389", "180m", "2 min", 5.0f),
                Guardian("#512", "250m", "3 min", 4.7f),
                Guardian("#091", "310m", "3 min", 4.9f)
            )
        )
        guardianAdapter.notifyDataSetChanged()
    }

    private fun startThreatMonitoring() {
        view?.postDelayed({
            if (isGuardianMode) {
                simulateThreatDetection()
            }
        }, 5000)
    }

    private fun simulateThreatDetection() {
        val random = (0..100).random()

        if (random < 10) {
            threatScore = 85
            threatScoreText.text = threatScore.toString()
            showThreatAlert("HIGH", "Suspicious activity detected nearby")
        } else if (random < 30) {
            threatScore = 45
            threatScoreText.text = threatScore.toString()
        }

        if (isGuardianMode) {
            view?.postDelayed({ simulateThreatDetection() }, 10000)
        }
    }

    private fun showThreatAlert(level: String, message: String) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("⚠️ Threat Detected - $level")
            .setMessage(message)
            .setPositiveButton("Alert Guardians") { _, _ ->
                Toast.makeText(context, "Alert sent to 12 guardians", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Dismiss", null)
            .show()
    }

    private fun showBecomeGuardianDialog() {
        val message =
            "Become a Guardian and help protect other women!\n\nYou'll receive alerts when someone nearby needs help."

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("👤 Become a Guardian")
            .setMessage(message)
            .setPositiveButton("Join Now") { _, _ ->
                Toast.makeText(context, "✅ You are now a Guardian!", Toast.LENGTH_LONG).show()
                guardians.add(0, Guardian("#YOU", "0m", "< 1 min", 5.0f))
                guardianAdapter.notifyItemInserted(0)
            }
            .setNegativeButton("Maybe Later", null)
            .show()
    }

    // Data class
    data class Guardian(
        val id: String,
        val distance: String,
        val responseTime: String,
        val rating: Float
    )

    // Guardian Adapter
    inner class GuardianAdapter(private val guardians: List<Guardian>) :
        RecyclerView.Adapter<GuardianAdapter.GuardianViewHolder>() {

        inner class GuardianViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idText: TextView = view.findViewById(android.R.id.text1)
            val detailsText: TextView = view.findViewById(android.R.id.text2)
            val ratingText: TextView = view.findViewById(android.R.id.summary)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuardianViewHolder {
            val view = LinearLayout(parent.context).apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 8, 16, 8)
                }
                orientation = LinearLayout.VERTICAL
                setPadding(24, 16, 24, 16)
                setBackgroundColor(resources.getColor(android.R.color.white, null))

                addView(TextView(context).apply {
                    id = android.R.id.text1
                    textSize = 16f
                    setTextColor(resources.getColor(R.color.text_primary, null))
                })

                addView(TextView(context).apply {
                    id = android.R.id.text2
                    textSize = 12f
                    setTextColor(resources.getColor(R.color.text_secondary, null))
                    setPadding(0, 8, 0, 0)
                })

                addView(TextView(context).apply {
                    id = android.R.id.summary
                    textSize = 14f
                    setTextColor(resources.getColor(R.color.guardian_color, null))
                    setPadding(0, 8, 0, 0)
                })
            }

            return GuardianViewHolder(view)
        }

        override fun onBindViewHolder(holder: GuardianViewHolder, position: Int) {
            val guardian = guardians[position]
            holder.idText.text = "Guardian ${guardian.id}"
            holder.detailsText.text =
                "${guardian.distance} away • Response: ${guardian.responseTime}"
            holder.ratingText.text = "⭐ ${guardian.rating}"
        }

        override fun getItemCount() = guardians.size
    }
}
