# ShaktiAI 3.0 - AI Model Specifications

## 📋 Overview

This document provides detailed specifications for all 8 AI models in the ShaktiAI 3.0 platform.
Each model is purpose-built for specific aspects of women's safety and empowerment.

## 🤖 AI Models

### 1. Sathi AI - Mental Health Support

| Attribute | Details |
|-----------|---------|
| **Purpose** | Mental health support |
| **Algorithm** | LSTM Seq2Seq |
| **Technology** | Gemini 2.0 Flash |
| **Training Data** | 10,000+ counseling chats |

**Description**: Emotional support companion providing compassionate mental health assistance using
LSTM sequence-to-sequence model trained on thousands of real counseling conversations. Provides
culturally sensitive support in multiple Indian languages.

**Use Cases**:

- Anxiety and stress management
- Depression support
- Family pressure counseling
- Career and education guidance
- Emotional wellness tracking

---

### 2. Guardian AI - Threat Detection

| Attribute | Details |
|-----------|---------|
| **Purpose** | Threat detection |
| **Algorithm** | YOLOv5 Audio |
| **Technology** | TensorFlow Lite |
| **Training Data** | 5,000+ audio samples |

**Description**: Real-time audio threat detection system using YOLOv5 architecture adapted for audio
analysis. Detects distress signals, screams, and threatening situations from ambient audio with high
accuracy.

**Use Cases**:

- Distress signal detection
- Scream recognition
- Threat assessment
- Emergency alert triggering
- Background audio monitoring

---

### 3. Nyaya AI - Legal Advisor

| Attribute | Details |
|-----------|---------|
| **Purpose** | Legal advisor |
| **Algorithm** | NLP Transformer |
| **Technology** | Gemini 2.0 Flash + IPC |
| **Training Data** | Indian legal documents |

**Description**: Intelligent legal advisor specialized in Indian laws related to women's rights.
Uses NLP transformers to understand legal queries and provide advice based on IPC, Domestic Violence
Act, POSH Act, and other relevant laws.

**Use Cases**:

- FIR generation
- Legal rights explanation
- Domestic violence cases
- Workplace harassment (POSH)
- Property and divorce laws
- Pro-bono lawyer connection

**Covered Laws**:

- Indian Penal Code (IPC) - Women-related sections
- Protection of Women from Domestic Violence Act, 2005
- Sexual Harassment of Women at Workplace Act, 2013
- Dowry Prohibition Act, 1961
- Hindu Marriage Act, 1955

---

### 4. Dhan Shakti AI - Financial Planning

| Attribute | Details |
|-----------|---------|
| **Purpose** | Financial planning |
| **Algorithm** | XGBoost + Regression |
| **Technology** | Gemini 2.0 Flash |
| **Training Data** | Credit scoring dataset |

**Description**: Financial literacy and planning assistant using XGBoost machine learning with
regression analysis. Helps women achieve financial independence through personalized budgeting,
savings plans, and investment recommendations.

**Use Cases**:

- Loan eligibility assessment
- Budget planning
- Savings strategies
- Investment recommendations
- Government scheme finder
- Business startup guidance
- Micro-credit opportunities

**Focus Areas**:

- Low-cost solutions for economically disadvantaged women
- Government subsidies and schemes
- Risk-free investment options
- Savings discipline

---

### 5. Sangam AI - Community Matching

| Attribute | Details |
|-----------|---------|
| **Purpose** | Community matching |
| **Algorithm** | Collaborative Filter |
| **Technology** | Aptos Smart Contracts |
| **Training Data** | User interaction patterns |

**Description**: Community connection platform using collaborative filtering algorithms deployed on
Aptos blockchain. Safely connects women with similar interests, support groups, and mentorship
opportunities while maintaining privacy.

**Use Cases**:

- Interest-based matching
- Support group recommendations
- Mentor-mentee connections
- Local event discovery
- Skill-sharing communities
- Safe networking

**Blockchain Benefits**:

- Privacy-preserving connections
- Verified user identities
- Secure data storage
- Tamper-proof reputation system

---

### 6. Gyaan AI - Education Guidance

| Attribute | Details |
|-----------|---------|
| **Purpose** | Education guidance |
| **Algorithm** | Text Classification |
| **Technology** | Gemini 2.0 Flash |
| **Training Data** | Course + skill database |

**Description**: Educational advisor using advanced text classification to match users with learning
opportunities. Provides personalized course recommendations, scholarship information, and skill
development pathways.

**Use Cases**:

- Skill gap analysis
- Course recommendations
- Scholarship finder
- Career pathway planning
- Vocational training matching
- Upskilling guidance

**Focus Areas**:

- Women-centric education programs
- Low-cost/free learning resources
- High-demand skills training
- Flexible learning schedules
- Industry-aligned courses

---

### 7. Swasthya AI - Health Companion

| Attribute | Details |
|-----------|---------|
| **Purpose** | Health companion |
| **Algorithm** | Symptom Classifier |
| **Technology** | Custom LSTM |
| **Training Data** | Medical knowledge base |

**Description**: Reproductive health and wellness companion using custom LSTM-based symptom
classifier. Tracks menstrual cycles, provides health education, and connects users with telemedicine
services while maintaining complete privacy.

**Use Cases**:

- Menstrual cycle tracking
- Ovulation prediction
- Symptom analysis
- Health education
- Telemedicine connection
- Nutrition guidance
- Reproductive health support

**Privacy Features**:

- On-device processing
- Encrypted data storage
- Anonymous telemedicine consultations
- No data sharing without consent

---

### 8. Raksha AI - Domestic Violence Detection

| Attribute | Details |
|-----------|---------|
| **Purpose** | DV detection |
| **Algorithm** | Pattern Recognition |
| **Technology** | Anomaly Detection |
| **Training Data** | Abuse incident patterns |

**Description**: Domestic violence detection system using pattern recognition and anomaly detection
algorithms. Identifies abusive patterns, creates safety plans, and provides discreet emergency
resources and support.

**Use Cases**:

- Abuse pattern identification
- Safety plan creation
- Shelter/NGO connection
- Legal remedy guidance
- Psychological first aid
- Escape route planning
- Emergency resource access

**Safety Features**:

- Discreet operation mode
- Quick exit functionality
- Camouflaged app interface
- Encrypted communication
- Emergency contact alerts

---

## 💻 Using Model Specifications in Code

### Accessing Model Specifications

```kotlin
import com.shakti.ai.models.AIModelSpecs

// Get all model specifications
val allModels = AIModelSpecs.getAllModels()

// Access specific model
val sathiSpec = AIModelSpecs.SATHI_AI
println("${sathiSpec.modelName}: ${sathiSpec.purpose}")

// Get model by name
val nyayaSpec = AIModelSpecs.getModelByName("Nyaya AI")
nyayaSpec?.let {
    println("Algorithm: ${it.algorithm}")
    println("Technology: ${it.technology}")
    println("Training Data: ${it.trainingData}")
}
```

### Example: Display Model Info in UI

```kotlin
// In your Fragment or Activity
val modelSpec = AIModelSpecs.GUARDIAN_AI

textViewTitle.text = modelSpec.modelName
textViewPurpose.text = modelSpec.purpose
textViewAlgorithm.text = "Algorithm: ${modelSpec.algorithm}"
textViewTechnology.text = "Technology: ${modelSpec.technology}"
textViewTrainingData.text = "Training Data: ${modelSpec.trainingData}"
textViewDescription.text = modelSpec.description
```

### Example: Create Dynamic Model Cards

```kotlin
class AIModelsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Get all models
        val models = AIModelSpecs.getAllModels()
        
        // Create cards for each model
        models.forEach { model ->
            val card = createModelCard(model)
            containerLayout.addView(card)
        }
    }
    
    private fun createModelCard(spec: AIModelSpecification): View {
        // Inflate and populate card view with model specs
        // ...
    }
}
```

## 📊 Model Comparison Matrix

| Model | Algorithm Type | Real-time | On-Device | Cloud-Based | Privacy Level |
|-------|---------------|-----------|-----------|-------------|---------------|
| Sathi AI | Deep Learning | ❌ | ❌ | ✅ | Medium |
| Guardian AI | Computer Vision | ✅ | ✅ | ❌ | High |
| Nyaya AI | NLP | ❌ | ❌ | ✅ | Medium |
| Dhan Shakti AI | ML + Regression | ❌ | ❌ | ✅ | Medium |
| Sangam AI | Recommendation | ❌ | ❌ | ✅ (Blockchain) | High |
| Gyaan AI | Classification | ❌ | ❌ | ✅ | Low |
| Swasthya AI | Deep Learning | ❌ | ✅ | ✅ | Very High |
| Raksha AI | Anomaly Detection | ✅ | ✅ | ✅ | Very High |

## 🔧 Model Integration Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    ShaktiAI 3.0                         │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌──────────────┐    ┌──────────────┐                 │
│  │  On-Device   │    │  Cloud-Based │                 │
│  │   Models     │    │    Models    │                 │
│  ├──────────────┤    ├──────────────┤                 │
│  │ Guardian AI  │    │  Sathi AI    │                 │
│  │ Swasthya AI  │    │  Nyaya AI    │                 │
│  │  Raksha AI   │    │ DhanShakti   │                 │
│  └──────────────┘    │  Gyaan AI    │                 │
│                      └──────────────┘                 │
│                                                         │
│  ┌─────────────────────────────────┐                  │
│  │  Blockchain (Aptos)             │                  │
│  ├─────────────────────────────────┤                  │
│  │      Sangam AI                   │                  │
│  └─────────────────────────────────┘                  │
│                                                         │
│  ┌─────────────────────────────────┐                  │
│  │  TensorFlow Lite Models         │                  │
│  ├─────────────────────────────────┤                  │
│  │  sathi_lstm.tflite              │                  │
│  │  guardian_audio.tflite          │                  │
│  │  swasthya_symptom.tflite        │                  │
│  │  raksha_pattern.tflite          │                  │
│  └─────────────────────────────────┘                  │
└─────────────────────────────────────────────────────────┘
```

## 📈 Performance Metrics

### Target Performance Metrics

| Model | Accuracy Target | Latency | Memory |
|-------|----------------|---------|--------|
| Sathi AI | 85%+ | <2s | Cloud |
| Guardian AI | 90%+ | <500ms | <50MB |
| Nyaya AI | 88%+ | <3s | Cloud |
| Dhan Shakti AI | 85%+ | <2s | Cloud |
| Sangam AI | 80%+ | <1s | Cloud |
| Gyaan AI | 90%+ | <2s | Cloud |
| Swasthya AI | 92%+ | <1s | <30MB |
| Raksha AI | 95%+ | <500ms | <40MB |

## 🚀 Future Enhancements

1. **Sathi AI**: Multi-modal support (text + voice + emotion detection)
2. **Guardian AI**: Video threat detection integration
3. **Nyaya AI**: Automated legal document generation
4. **Dhan Shakti AI**: Real-time stock market integration
5. **Sangam AI**: Cross-platform community features
6. **Gyaan AI**: AR-based skill training
7. **Swasthya AI**: IoT device integration (fitness trackers)
8. **Raksha AI**: Predictive abuse prevention

## 📚 References

- **LSTM**: Long Short-Term Memory networks for sequence modeling
- **YOLOv5**: Real-time object detection adapted for audio
- **NLP Transformers**: BERT-based models for language understanding
- **XGBoost**: Gradient boosting for regression/classification
- **Collaborative Filtering**: Recommendation system algorithm
- **Text Classification**: Multi-class text categorization
- **Symptom Classifier**: Medical symptom analysis using ML
- **Anomaly Detection**: Pattern-based anomaly identification

---

## 📝 Notes

All model specifications are defined in:

```
app/src/main/java/com/shakti/ai/models/DataModels.kt
```

The `AIModelSpecification` data class and `AIModelSpecs` object provide a centralized way to access
all model information throughout the application.
