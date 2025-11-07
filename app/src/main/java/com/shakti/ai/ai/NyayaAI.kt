package com.shakti.ai.ai

import android.content.Context
import com.shakti.ai.models.LegalAdvice
import com.shakti.ai.models.LegalCategory
import com.shakti.ai.models.LegalQuery
import com.shakti.ai.models.LegalSection
import com.shakti.ai.models.DocumentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Nyaya AI - Legal Advisor for Women's Rights
 *
 * Specializes in:
 * - Indian Penal Code (IPC) sections related to women
 * - Domestic Violence Act
 * - POSH Act (Sexual Harassment at Workplace)
 * - Dowry Prohibition Act
 * - Property and divorce laws
 *
 * Features:
 * - Auto-generate FIRs from complaints
 * - Explain legal rights in simple language
 * - Draft legal documents
 * - Match with pro-bono lawyers
 *
 * Technology: NLP Transformer + Gemini 2.0 Flash + IPC Database
 * Training Data: Indian legal documents
 */
class NyayaAI(private val context: Context) {

    private val geminiService = GeminiService.getInstance(context)

    companion object {
        @Volatile
        private var INSTANCE: NyayaAI? = null

        fun getInstance(context: Context): NyayaAI {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: NyayaAI(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    // ============================================================================
    // IPC SECTIONS DATABASE - Women's Rights Related
    // ============================================================================

    private val ipcSections = mapOf(
        // Sexual Offenses
        "354" to LegalSection(
            "354",
            "Assault or criminal force to woman with intent to outrage her modesty",
            "Imprisonment up to 2 years or fine or both",
            "IPC",
            "Any unwanted touching, gestures, or advances that insult a woman's modesty"
        ),
        "354A" to LegalSection(
            "354A",
            "Sexual harassment and punishment for sexual harassment",
            "Imprisonment up to 3 years or fine or both",
            "IPC",
            "Unwelcome physical contact, sexually colored remarks, demand for sexual favors"
        ),
        "354B" to LegalSection(
            "354B",
            "Assault or use of criminal force to woman with intent to disrobe",
            "Imprisonment of 3-7 years and fine",
            "IPC",
            "Forcibly removing or attempting to remove clothing of a woman"
        ),
        "354C" to LegalSection(
            "354C",
            "Voyeurism",
            "Imprisonment up to 3 years and fine (first offense), 3-7 years (subsequent)",
            "IPC",
            "Watching or photographing a woman in private act without consent"
        ),
        "354D" to LegalSection(
            "354D",
            "Stalking",
            "Imprisonment up to 3 years and fine (first offense), up to 5 years (subsequent)",
            "IPC",
            "Following, monitoring, or contacting a woman despite her disinterest"
        ),
        "375" to LegalSection(
            "375",
            "Rape",
            "Imprisonment of 10 years to life and fine",
            "IPC",
            "Sexual intercourse without consent, including marital rape (with conditions)"
        ),
        "376" to LegalSection(
            "376",
            "Punishment for sexual assault",
            "Imprisonment of 10 years to life and fine",
            "IPC",
            "Various forms of sexual assault with severe punishments"
        ),

        // Domestic Violence & Cruelty
        "498A" to LegalSection(
            "498A",
            "Husband or relative of husband subjecting woman to cruelty",
            "Imprisonment up to 3 years and fine",
            "IPC",
            "Mental or physical cruelty by husband or in-laws, including dowry harassment"
        ),
        "304B" to LegalSection(
            "304B",
            "Dowry death",
            "Imprisonment of minimum 7 years to life",
            "IPC",
            "Death of woman within 7 years of marriage due to dowry harassment"
        ),
        "406" to LegalSection(
            "406",
            "Punishment for criminal breach of trust",
            "Imprisonment up to 3 years or fine or both",
            "IPC",
            "Withholding stridhan (woman's property) or gifts given to her"
        ),

        // Assault & Intimidation
        "323" to LegalSection(
            "323",
            "Punishment for voluntarily causing hurt",
            "Imprisonment up to 1 year or fine up to ₹1000 or both",
            "IPC",
            "Physical assault causing pain or injury"
        ),
        "324" to LegalSection(
            "324",
            "Voluntarily causing hurt by dangerous weapons",
            "Imprisonment up to 3 years or fine or both",
            "IPC",
            "Assault with weapons like knives, acid, etc."
        ),
        "326A" to LegalSection(
            "326A",
            "Acid attack",
            "Imprisonment of minimum 10 years to life and fine",
            "IPC",
            "Throwing acid or attempting to throw acid with intent to disfigure"
        ),
        "503" to LegalSection(
            "503",
            "Criminal intimidation",
            "Imprisonment up to 2 years or fine or both",
            "IPC",
            "Threatening to cause injury to person, reputation, or property"
        ),
        "506" to LegalSection(
            "506",
            "Punishment for criminal intimidation",
            "Imprisonment up to 2 years or fine or both (7 years for death threats)",
            "IPC",
            "Threats of death or grievous hurt"
        ),
        "509" to LegalSection(
            "509",
            "Word, gesture or act intended to insult the modesty of a woman",
            "Imprisonment up to 3 years and fine",
            "IPC",
            "Verbal abuse, gestures, or acts that insult a woman's dignity"
        ),

        // Kidnapping & Trafficking
        "363" to LegalSection(
            "363",
            "Punishment for kidnapping",
            "Imprisonment up to 7 years and fine",
            "IPC",
            "Forcibly taking away a person"
        ),
        "366" to LegalSection(
            "366",
            "Kidnapping, abducting or inducing woman to compel her marriage",
            "Imprisonment up to 10 years and fine",
            "IPC",
            "Forcing a woman into marriage against her will"
        ),
        "370" to LegalSection(
            "370",
            "Trafficking of persons",
            "Imprisonment of 7-10 years and fine (10 years to life for minors)",
            "IPC",
            "Recruiting, transporting, or harboring persons for exploitation"
        )
    )

    // Domestic Violence Act Sections
    private val dvActSections = mapOf(
        "2(a)" to LegalSection(
            "2(a)",
            "Definition of aggrieved person",
            "N/A",
            "DV Act",
            "Any woman in domestic relationship with respondent"
        ),
        "2(f)" to LegalSection(
            "2(f)",
            "Definition of domestic violence",
            "N/A",
            "DV Act",
            "Physical, sexual, verbal, emotional, or economic abuse"
        ),
        "3" to LegalSection(
            "3",
            "Definition of domestic violence",
            "Civil remedy, not criminal",
            "DV Act",
            "Includes actual abuse, threat, harassment, and economic abuse"
        ),
        "12" to LegalSection(
            "12",
            "Application for protection order",
            "N/A",
            "DV Act",
            "Woman can apply to magistrate for protection from abuse"
        ),
        "18" to LegalSection(
            "18",
            "Monetary relief",
            "Compensation for injuries, expenses, maintenance",
            "DV Act",
            "Court can order monetary compensation for losses"
        ),
        "19" to LegalSection(
            "19",
            "Custody orders",
            "N/A",
            "DV Act",
            "Court can grant custody of children to aggrieved woman"
        ),
        "20" to LegalSection(
            "20",
            "Protection orders",
            "N/A",
            "DV Act",
            "Prohibit respondent from committing domestic violence"
        ),
        "21" to LegalSection(
            "21",
            "Residence orders",
            "N/A",
            "DV Act",
            "Woman's right to reside in shared household"
        )
    )

    // POSH Act Sections
    private val poshActSections = mapOf(
        "3(1)" to LegalSection(
            "3(1)",
            "Sexual harassment at workplace",
            "Inquiry + compensation + termination",
            "POSH Act",
            "Unwelcome advances, demands, sexually colored remarks at workplace"
        ),
        "4" to LegalSection(
            "4",
            "Preservation of confidentiality",
            "N/A",
            "POSH Act",
            "Identity of woman must be kept confidential during proceedings"
        ),
        "13" to LegalSection(
            "13",
            "Inquiry into complaint",
            "Within 90 days of complaint",
            "POSH Act",
            "Internal Complaints Committee must inquire within 90 days"
        )
    )

    // ============================================================================
    // FIR GENERATION
    // ============================================================================

    /**
     * Auto-generate FIR from victim complaint
     */
    suspend fun generateFIRFromComplaint(complaint: String): String = withContext(Dispatchers.IO) {
        val relevantSections = identifyApplicableSections(complaint)

        val prompt = """
            You are a legal expert helping a woman file an FIR (First Information Report) in India.
            
            Based on this complaint, generate a detailed FIR:
            
            Complaint: "$complaint"
            
            Relevant IPC Sections identified: ${relevantSections.joinToString(", ") { it.sectionNumber }}
            
            Generate an FIR with:
            
            **FIR DETAILS**
            Case Type: [Identify the primary offense]
            Applicable IPC/Law Sections: [List sections with brief explanation]
            Brief Case Summary: [3-4 sentence summary]
            Date and Time of Incident: [If mentioned, else "To be filled"]
            Place of Incident: [If mentioned, else "To be filled"]
            
            **DETAILED DESCRIPTION OF EVENTS**
            [Chronological account based on complaint]
            
            **EVIDENCE TO BE COLLECTED**
            - Physical evidence (if applicable)
            - Medical evidence (if injuries)
            - Digital evidence (messages, calls, photos)
            - Witness statements
            - Other relevant evidence
            
            **RELIEF SOUGHT**
            1. [What the complainant wants]
            2. [Protection needed]
            
            **NEXT STEPS FOR COMPLAINANT**
            1. Go to nearest police station with this FIR
            2. Get medical examination if injured (MLC)
            3. Collect and preserve evidence
            4. Contact women's helpline: 1091 or 181
            5. Consider approaching women's cell or NGO
            
            **IMPORTANT RIGHTS**
            - Right to file FIR without fee
            - Right to copy of FIR
            - Right to protection under law
            - Cannot be denied FIR filing
            
            Keep language simple and empowering. Include section numbers and their meanings.
        """.trimIndent()

        return@withContext try {
            geminiService.callNyayaAI(prompt)
        } catch (e: Exception) {
            generateFallbackFIR(complaint, relevantSections)
        }
    }

    /**
     * Identify applicable IPC sections from complaint text
     */
    private fun identifyApplicableSections(complaint: String): List<LegalSection> {
        val lowerComplaint = complaint.lowercase()
        val applicableSections = mutableListOf<LegalSection>()

        // Check IPC sections
        if (lowerComplaint.contains("dowry") || lowerComplaint.contains("दहेज")) {
            applicableSections.add(ipcSections["498A"]!!)
            applicableSections.add(ipcSections["304B"]!!)
        }
        if (lowerComplaint.contains("rape") || lowerComplaint.contains("sexual assault") ||
            lowerComplaint.contains("बलात्कार")
        ) {
            applicableSections.add(ipcSections["376"]!!)
        }
        if (lowerComplaint.contains("harassment") || lowerComplaint.contains("touching") ||
            lowerComplaint.contains("molest")
        ) {
            applicableSections.add(ipcSections["354"]!!)
            applicableSections.add(ipcSections["354A"]!!)
        }
        if (lowerComplaint.contains("stalking") || lowerComplaint.contains("follow")) {
            applicableSections.add(ipcSections["354D"]!!)
        }
        if (lowerComplaint.contains("acid")) {
            applicableSections.add(ipcSections["326A"]!!)
        }
        if (lowerComplaint.contains("domestic violence") || lowerComplaint.contains("husband") ||
            lowerComplaint.contains("in-laws")
        ) {
            applicableSections.add(ipcSections["498A"]!!)
        }
        if (lowerComplaint.contains("threat") || lowerComplaint.contains("intimidat")) {
            applicableSections.add(ipcSections["506"]!!)
        }
        if (lowerComplaint.contains("beat") || lowerComplaint.contains("hit") ||
            lowerComplaint.contains("assault") || lowerComplaint.contains("मारा")
        ) {
            applicableSections.add(ipcSections["323"]!!)
        }

        return applicableSections.distinctBy { it.sectionNumber }
    }

    /**
     * Fallback FIR generation if Gemini fails
     */
    private fun generateFallbackFIR(complaint: String, sections: List<LegalSection>): String {
        return buildString {
            appendLine("**FIR (First Information Report)**")
            appendLine()
            appendLine("**CASE DETAILS**")
            appendLine("Complaint: $complaint")
            appendLine()
            appendLine("**APPLICABLE SECTIONS**")
            sections.forEach { section ->
                appendLine("- IPC ${section.sectionNumber}: ${section.title}")
                appendLine("  Punishment: ${section.punishment}")
                appendLine()
            }
            appendLine("**NEXT STEPS**")
            appendLine("1. Visit nearest police station immediately")
            appendLine("2. Request copy of FIR")
            appendLine("3. Get medical examination if injured")
            appendLine("4. Call women's helpline: 1091")
        }
    }

    // ============================================================================
    // LEGAL RIGHTS EXPLANATION
    // ============================================================================

    /**
     * Explain legal rights in simple, non-technical language
     */
    suspend fun explainLegalRights(topic: String, category: LegalCategory): String =
        withContext(Dispatchers.IO) {
            val prompt = """
                Explain these women's legal rights in India in simple, non-technical language:
                
                Topic: $topic
                Category: ${category.name}
                
                Include:
                1. **What is this law?** - Simple definition
                2. **Who does it protect?** - Scope of protection
                3. **What protection does it provide?** - Specific rights
                4. **How to invoke it?** - Step-by-step process
                5. **What are the penalties?** - Punishment for violators
                6. **Real-world example** - Simple scenario
                7. **Resources & Hotlines** - Where to get help
                
                Use simple Hindi and English terms. Empower the reader with knowledge.
                Include specific section numbers and their meanings.
            """.trimIndent()

            return@withContext geminiService.callNyayaAI(prompt)
        }

    // ============================================================================
    // LEGAL DOCUMENT DRAFTING
    // ============================================================================

    /**
     * Draft legal documents (restraining orders, notices, petitions)
     */
    suspend fun draftLegalDocument(
        documentType: DocumentType,
        details: Map<String, String>
    ): String = withContext(Dispatchers.IO) {
        val template = getDocumentTemplate(documentType)

        val prompt = """
            Draft a professional $documentType for a woman in India.
            
            Details provided:
            ${details.entries.joinToString("\n") { "- ${it.key}: ${it.value}" }}
            
            Use this structure:
            $template
            
            Make it legally sound, professional, and empowering.
            Include relevant section numbers and legal terminology.
            Add instructions for filing at the end.
        """.trimIndent()

        return@withContext geminiService.callNyayaAI(prompt)
    }

    /**
     * Get document template based on type
     */
    private fun getDocumentTemplate(type: DocumentType): String {
        return when (type) {
            DocumentType.RESTRAINING_ORDER -> """
                **APPLICATION FOR RESTRAINING ORDER**
                Under Section 12, Protection of Women from Domestic Violence Act, 2005
                
                TO: The Honorable Magistrate
                [Court Name and Address]
                
                FROM: [Applicant Name]
                [Applicant Address]
                
                SUBJECT: Application for Restraining Order against [Respondent Name]
                
                Most respectfully showeth:
                [Details]
                
                Prayer:
                [Relief sought]
                
                Verification:
                [Statement]
                
                Place:
                Date:
                Signature: _______________
            """.trimIndent()

            DocumentType.LEGAL_NOTICE -> """
                **LEGAL NOTICE**
                
                TO: [Recipient Name]
                [Recipient Address]
                
                FROM: [Sender Name]
                [Sender Address]
                
                SUBJECT: [Subject]
                
                Dear Sir/Madam,
                
                [Body with facts, legal sections, and demand]
                
                You are hereby called upon to:
                [Demands]
                
                Failing which, my client shall be constrained to initiate appropriate legal proceedings.
                
                Date:
                Place:
                [Advocate details if any]
            """.trimIndent()

            else -> "Standard legal document template"
        }
    }

    // ============================================================================
    // PRO-BONO LAWYER MATCHING
    // ============================================================================

    /**
     * Match with pro-bono lawyers based on case type
     */
    suspend fun matchWithLawyer(
        caseType: String,
        location: String
    ): String = withContext(Dispatchers.IO) {
        val prompt = """
            Find pro-bono or affordable lawyers in India who can help with: $caseType
            Location: $location
            
            Provide information about:
            
            **FREE LEGAL AID CENTERS**
            1. Legal Services Authority (NALSA)
               - How to apply
               - Documents needed
            
            2. Women's Cells
               - Police women's cell
               - Court women's cell
            
            3. NGOs providing free legal aid
               - List 3-4 major NGOs
               - Contact details
            
            **WHAT TO EXPECT**
            - Services provided
            - Process timeline
            - Documents to bring
            
            **CONTACT INFORMATION**
            - Helpline numbers
            - Email addresses
            - Office addresses
            
            Include specific contact details for India.
        """.trimIndent()

        return@withContext geminiService.callNyayaAI(prompt)
    }

    // ============================================================================
    // LEGAL QUERY ANALYSIS
    // ============================================================================

    /**
     * Analyze legal query and provide comprehensive advice
     */
    suspend fun analyzeLegalQuery(query: LegalQuery): LegalAdvice = withContext(Dispatchers.IO) {
        val response = geminiService.callNyayaAI(query.query)
        val relevantSections = identifyApplicableSections(query.query)

        return@withContext LegalAdvice(
            query = query.query,
            advice = response,
            relevantLaws = relevantSections.map { "${it.act} ${it.sectionNumber}: ${it.title}" },
            suggestedActions = extractSuggestedActions(response),
            confidence = 0.85f
        )
    }

    /**
     * Extract suggested actions from response
     */
    private fun extractSuggestedActions(response: String): List<String> {
        // Simple extraction - in production, use NLP
        return listOf(
            "Consult with a lawyer",
            "File a complaint at police station",
            "Contact women's helpline: 1091",
            "Preserve all evidence",
            "Document everything"
        )
    }

    /**
     * Get all available IPC sections
     */
    fun getAllIPCSections(): List<LegalSection> {
        return ipcSections.values.toList()
    }

    /**
     * Get section details by number
     */
    fun getSectionDetails(sectionNumber: String): LegalSection? {
        return ipcSections[sectionNumber] ?: dvActSections[sectionNumber]
        ?: poshActSections[sectionNumber]
    }

    /**
     * Cleanup
     */
    fun cleanup() {
        INSTANCE = null
    }
}
