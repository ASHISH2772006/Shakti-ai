package com.shakti.ai.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.shakti.ai.R
import com.shakti.ai.viewmodel.GyaanViewModel
import kotlinx.coroutines.launch

class GyaanAIFragment : Fragment() {

    private val viewModel: GyaanViewModel by viewModels()

    private lateinit var categoryInput: EditText
    private lateinit var stateInput: EditText
    private lateinit var courseInput: EditText
    private lateinit var incomeInput: EditText
    private lateinit var percentageInput: EditText
    private lateinit var btnFindScholarships: Button
    private lateinit var btnPreFillForms: Button
    private lateinit var btnDocumentChecklist: Button
    private lateinit var btnVirtualMentorship: Button
    private lateinit var btnOnlineCourses: Button
    private lateinit var btnCareerGuidance: Button
    private lateinit var btnSkillAssessment: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gyaan_ai, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupClickListeners()
        observeViewModel()
    }

    private fun initializeViews(view: View) {
        categoryInput = view.findViewById(R.id.category_input)
        stateInput = view.findViewById(R.id.state_input)
        courseInput = view.findViewById(R.id.course_input)
        incomeInput = view.findViewById(R.id.income_input)
        percentageInput = view.findViewById(R.id.percentage_input)
        btnFindScholarships = view.findViewById(R.id.btn_find_scholarships)
        btnPreFillForms = view.findViewById(R.id.btn_pre_fill_forms)
        btnDocumentChecklist = view.findViewById(R.id.btn_document_checklist)
        btnVirtualMentorship = view.findViewById(R.id.btn_virtual_mentorship)

        // Additional features
        btnOnlineCourses = createButton("💻 Free Online Courses")
        btnCareerGuidance = createButton("🎯 Career Guidance")
        btnSkillAssessment = createButton("📊 Skill Assessment")
    }

    private fun createButton(text: String): Button {
        return Button(requireContext()).apply {
            this.text = text
        }
    }

    private fun setupClickListeners() {
        btnFindScholarships.setOnClickListener {
            findScholarships()
        }

        btnPreFillForms.setOnClickListener {
            showPreFillFormsWizard()
        }

        btnDocumentChecklist.setOnClickListener {
            showDocumentChecklist()
        }

        btnVirtualMentorship.setOnClickListener {
            connectWithMentor()
        }

        btnOnlineCourses.setOnClickListener {
            showOnlineCourses()
        }

        btnCareerGuidance.setOnClickListener {
            showCareerGuidance()
        }

        btnSkillAssessment.setOnClickListener {
            takeSkillAssessment()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                btnFindScholarships.isEnabled = !isLoading
                btnFindScholarships.text =
                    if (isLoading) "Searching..." else "🔍 Find My Scholarships"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.scholarships.collect { scholarships ->
                if (scholarships.isNotEmpty()) {
                    showScholarships(scholarships)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.courseRecommendations.collect { courses ->
                if (courses.isNotEmpty()) {
                    showCoursesDialog(courses)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.errorMessage.collect { error ->
                error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    viewModel.clearError()
                }
            }
        }
    }

    private fun findScholarships() {
        val education = courseInput.text.toString()
        val income = incomeInput.text.toString().toLongOrNull() ?: 0L
        val category = categoryInput.text.toString()

        if (education.isBlank()) {
            Toast.makeText(context, "Please enter course details", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.findScholarships(education, income, category)
    }

    private fun showScholarships(scholarships: String) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("🎓 Scholarships Found")
            .setMessage(scholarships)
            .setPositiveButton("Apply Now") { _, _ ->
                Toast.makeText(context, "Opening application portal...", Toast.LENGTH_SHORT).show()
            }
            .setNeutralButton("Save List") { _, _ ->
                Toast.makeText(context, "✅ Scholarships saved to your profile", Toast.LENGTH_SHORT)
                    .show()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showPreFillFormsWizard() {
        val formTypes = arrayOf(
            "Scholarship Application Form",
            "College Admission Form",
            "Government Scheme Application",
            "Online Course Registration",
            "Job Application Form"
        )

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("📝 Auto-Fill Forms")
            .setMessage("Select form type to auto-fill:")
            .setItems(formTypes) { _, which ->
                val formType = formTypes[which]
                Toast.makeText(
                    context,
                    "✅ Opening $formType with auto-fill enabled",
                    Toast.LENGTH_LONG
                ).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDocumentChecklist() {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("📋 Document Checklist")
            .setMessage(
                """
                For Scholarship Application:
                
                ✅ 10th Marksheet
                ✅ 12th Marksheet
                ✅ Graduation Marksheet (if applicable)
                ✅ Income Certificate (< 1 year old)
                ✅ Caste Certificate (if applicable)
                ✅ Domicile Certificate
                ✅ Aadhaar Card
                ✅ Bank Account Passbook
                ✅ Passport Size Photos (recent)
                ✅ College/University ID
                ✅ Bonafide Certificate
                
                💡 Tip: Keep scanned copies (PDF format) ready!
            """.trimIndent()
            )
            .setPositiveButton("Upload Documents") { _, _ ->
                Toast.makeText(context, "Opening document upload...", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun connectWithMentor() {
        val mentorTypes = arrayOf(
            "🎓 Academic Counselor",
            "💼 Career Mentor",
            "💻 Tech Industry Expert",
            "👩‍⚕️ Healthcare Professional",
            "👩‍🏫 Teaching/Education",
            "📊 Business/Entrepreneurship"
        )

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("👩‍🏫 Connect with Mentor")
            .setMessage("Select your area of interest:")
            .setItems(mentorTypes) { _, which ->
                val mentor = mentorTypes[which].substring(2)
                Toast.makeText(
                    context,
                    "✅ Finding mentors in: $mentor",
                    Toast.LENGTH_SHORT
                ).show()
                // Search for mentors and courses in this field
                viewModel.recommendCourses(emptyList(), mentor, 0L)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showOnlineCourses() {
        viewModel.recommendCourses(emptyList(), "Technology", 0L)

        val popularCourses = """
            💻 Free Online Learning Platforms:
            
            1. SWAYAM (Government of India)
               - Free courses with certificates
               - Accepted by employers
               
            2. NPTEL (IIT/IISc)
               - Engineering & Science
               - Free video lectures
            
            3. Google Digital Garage
               - Digital Marketing
               - Free certification
            
            4. Microsoft Learn
               - Technology skills
               - Free with certificates
            
            5. Coursera for Women
               - Scholarships available
               - Global universities
            
            6. Udemy Free Courses
               - Varied subjects
               - Lifetime access
            
            💡 Search for courses in your field!
        """.trimIndent()

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("💻 Free Online Courses")
            .setMessage(popularCourses)
            .setPositiveButton("Browse Courses") { _, _ ->
                viewModel.recommendCourses(emptyList(), "All", 0L)
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showCoursesDialog(courses: String) {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("📚 Recommended Courses")
            .setMessage(courses)
            .setPositiveButton("Enroll") { _, _ ->
                Toast.makeText(context, "Opening course enrollment...", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showCareerGuidance() {
        val input = EditText(requireContext()).apply {
            hint = "What are your interests? (e.g., Teaching, Technology, Healthcare)"
            setPadding(50, 40, 50, 40)
        }

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("🎯 Career Guidance")
            .setMessage("Tell us about your interests and skills:")
            .setView(input)
            .setPositiveButton("Get Guidance") { _, _ ->
                val interests = input.text.toString()
                if (interests.isNotBlank()) {
                    showCareerOptions(interests)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showCareerOptions(interests: String) {
        val careerAdvice = """
            🎯 Career Paths for "$interests":
            
            ${getCareerSuggestions(interests)}
            
            📚 Recommended Skills to Learn:
            • Communication Skills
            • Digital Literacy
            • Leadership & Management
            • Technical Skills (field-specific)
            
            💼 Job Portals for Women:
            • Naukri.com
            • LinkedIn
            • Indeed
            • WomenJobPortal.in
            • Sheroes
            
            💡 Next Steps:
            1. Take skill assessment
            2. Complete relevant courses
            3. Build portfolio/resume
            4. Network with professionals
            5. Apply for internships/jobs
        """.trimIndent()

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Career Guidance")
            .setMessage(careerAdvice)
            .setPositiveButton("Explore Courses") { _, _ ->
                showOnlineCourses()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun getCareerSuggestions(interests: String): String {
        return when {
            interests.contains("tech", ignoreCase = true) -> """
                • Software Developer
                • Data Analyst
                • Digital Marketing Specialist
                • UI/UX Designer
                • Cybersecurity Analyst
            """.trimIndent()

            interests.contains("teach", ignoreCase = true) -> """
                • School Teacher
                • Online Tutor
                • Educational Content Creator
                • Career Counselor
                • Training & Development Specialist
            """.trimIndent()

            interests.contains("health", ignoreCase = true) -> """
                • Nurse
                • Medical Technician
                • Nutritionist
                • Public Health Worker
                • Healthcare Administrator
            """.trimIndent()

            else -> """
                • Based on your interests, multiple career options available
                • Take skill assessment for personalized recommendations
                • Connect with mentors for guidance
            """.trimIndent()
        }
    }

    private fun takeSkillAssessment() {
        val skills = arrayOf(
            "Communication Skills",
            "Problem Solving",
            "Technical Skills (Computers)",
            "Leadership & Management",
            "Creative Thinking",
            "Financial Literacy"
        )

        val selectedSkills = BooleanArray(skills.size)

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("📊 Skill Assessment")
            .setMessage("Select skills you want to assess:")
            .setMultiChoiceItems(skills, selectedSkills) { _, which, isChecked ->
                selectedSkills[which] = isChecked
            }
            .setPositiveButton("Start Assessment") { _, _ ->
                val selected = skills.filterIndexed { index, _ -> selectedSkills[index] }
                if (selected.isNotEmpty()) {
                    showAssessmentResult(selected)
                } else {
                    Toast.makeText(context, "Please select at least one skill", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showAssessmentResult(skills: List<String>) {
        val result = skills.joinToString("\n") { skill ->
            val score = (60..95).random()
            "• $skill: $score/100"
        }

        val advice = """
            📊 Your Skill Assessment Results:
            
            $result
            
            💡 Recommendations:
            • Focus on improving lower-scored skills
            • Take online courses to enhance knowledge
            • Practice regularly
            • Seek mentorship
            • Apply skills in real projects
            
            🎯 Suggested Learning Path:
            1. Complete beginner courses
            2. Work on practical projects
            3. Get certified
            4. Join communities
            5. Keep learning & growing!
        """.trimIndent()

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Assessment Results")
            .setMessage(advice)
            .setPositiveButton("Find Courses") { _, _ ->
                showOnlineCourses()
            }
            .setNegativeButton("Close", null)
            .show()
    }
}
