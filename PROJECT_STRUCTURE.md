# ShaktiAI 3.0 - Complete Project Structure

## 📁 Project Overview

This document provides a complete overview of the ShaktiAI 3.0 project structure, file organization,
and implementation details.

## 🗂️ Directory Structure

```
ShaktiAI3.0/
├── .gitignore                              # Git ignore rules
├── README.md                               # Main project documentation
├── PROJECT_STRUCTURE.md                    # This file
├── build.gradle.kts                        # Project-level build configuration
├── settings.gradle.kts                     # Gradle settings
├── gradle.properties                       # Gradle properties
├── gradlew                                 # Gradle wrapper (Unix)
├── gradlew.bat                             # Gradle wrapper (Windows)
├── local.properties                        # Local SDK paths (not in git)
│
├── gradle/
│   ├── libs.versions.toml                  # Centralized dependency versions
│   └── wrapper/
│       └── gradle-wrapper.properties       # Gradle wrapper config
│
└── app/
    ├── build.gradle.kts                    # App-level build configuration
    ├── proguard-rules.pro                  # ProGuard rules
    │
    ├── src/
    │   ├── main/
    │   │   ├── AndroidManifest.xml         # App manifest with permissions
    │   │   │
    │   │   ├── java/com/shakti/ai/
    │   │   │   │
    │   │   │   ├── ui/                     # 🎨 UI Layer (Jetpack Compose)
    │   │   │   │   ├── MainActivity.kt     # Main entry point
    │   │   │   │   ├── HomeScreen.kt       # Home dashboard
    │   │   │   │   ├── SathiAIScreen.kt    # Emotional support UI
    │   │   │   │   ├── GuardianAIScreen.kt # Safety monitoring UI
    │   │   │   │   ├── NyayaAIScreen.kt    # Legal assistance UI
    │   │   │   │   ├── DhanShaktiAIScreen.kt # Financial literacy UI
    │   │   │   │   ├── SangamAIScreen.kt   # Community UI
    │   │   │   │   ├── GyaanAIScreen.kt    # Education UI
    │   │   │   │   ├── SwasthyaAIScreen.kt # Health UI
    │   │   │   │   └── RakshaAIScreen.kt   # Pattern recognition UI
    │   │   │   │
    │   │   │   ├── viewmodel/              # 🔄 ViewModel Layer (MVVM)
    │   │   │   │   ├── SathiViewModel.kt   # Emotional support VM
    │   │   │   │   ├── GuardianViewModel.kt # Safety monitoring VM
    │   │   │   │   ├── NyayaViewModel.kt   # Legal assistance VM
    │   │   │   │   ├── DhanShaktiViewModel.kt # Financial VM
    │   │   │   │   ├── SangamViewModel.kt  # Community VM
    │   │   │   │   ├── GyaanViewModel.kt   # Education VM
    │   │   │   │   ├── SwasthyaViewModel.kt # Health VM
    │   │   │   │   └── RakshaViewModel.kt  # Pattern recognition VM
    │   │   │   │
    │   │   │   ├── ai/                     # 🤖 AI/ML Service Layer
    │   │   │   │   ├── GeminiService.kt    # Gemini AI integration
    │   │   │   │   ├── SathiAI.kt          # LSTM emotional analysis
    │   │   │   │   ├── GuardianAI.kt       # YOLOv5 audio detection
    │   │   │   │   ├── NyayaAI.kt          # NLP legal assistance
    │   │   │   │   ├── DhanShaktiAI.kt     # XGBoost financial analysis
    │   │   │   │   ├── SangamAI.kt         # Recommendation system
    │   │   │   │   ├── GyaanAI.kt          # Content classification
    │   │   │   │   ├── SwasthyaAI.kt       # Health monitoring
    │   │   │   │   └── RakshaAI.kt         # Pattern recognition
    │   │   │   │
    │   │   │   ├── blockchain/             # ⛓️ Blockchain Layer (Aptos)
    │   │   │   │   ├── AptosService.kt     # Blockchain service
    │   │   │   │   ├── TransactionBuilder.kt # Transaction management
    │   │   │   │   └── SmartContractManager.kt # Smart contracts
    │   │   │   │
    │   │   │   └── models/                 # 📊 Data Models
    │   │   │       ├── DataModels.kt       # Core data structures
    │   │   │       └── ResponseModels.kt   # API response models
    │   │   │
    │   │   ├── ml/                         # 🧠 ML Models (TensorFlow Lite)
    │   │   │   ├── README.md               # Model documentation
    │   │   │   ├── .gitkeep                # Git placeholder
    │   │   │   ├── sathi_lstm.tflite       # Emotional analysis model
    │   │   │   ├── guardian_audio.tflite   # Audio detection model
    │   │   │   └── ... (other models)
    │   │   │
    │   │   └── res/                        # 🎨 Resources
    │   │       ├── drawable/               # Images and icons
    │   │       ├── mipmap/                 # App icons
    │   │       ├── values/                 # Strings, colors, themes
    │   │       └── xml/                    # XML configs
    │   │
    │   ├── androidTest/                    # 🧪 Instrumentation Tests
    │   │   └── java/
    │   │
    │   └── test/                           # 🧪 Unit Tests
    │       └── java/
```

## 📦 Key Components

### 1. UI Layer (Jetpack Compose)

**Location**: `app/src/main/java/com/shakti/ai/ui/`

- **MainActivity.kt**: Entry point, sets up navigation
- **HomeScreen.kt**: Dashboard with 8 AI module cards
- **[Feature]AIScreen.kt**: Individual screens for each AI module

**Tech**: Jetpack Compose, Material 3, Navigation Compose

### 2. ViewModel Layer

**Location**: `app/src/main/java/com/shakti/ai/viewmodel/`

Implements MVVM pattern with:

- StateFlow for reactive state management
- Coroutines for async operations
- Lifecycle-aware components

### 3. AI Services Layer

**Location**: `app/src/main/java/com/shakti/ai/ai/`

Each AI module is a singleton service:

- **SathiAI**: LSTM for emotion detection
- **GuardianAI**: YOLOv5 for distress detection
- **NyayaAI**: NLP for legal queries
- **DhanShaktiAI**: XGBoost for financial analysis
- **SangamAI**: Recommender system
- **GyaanAI**: Content classifier
- **SwasthyaAI**: Health analyzer
- **RakshaAI**: Pattern recognizer

### 4. Blockchain Layer

**Location**: `app/src/main/java/com/shakti/ai/blockchain/`

Aptos blockchain integration for:

- Secure data storage
- Transaction management
- Smart contract interaction

### 5. Data Models

**Location**: `app/src/main/java/com/shakti/ai/models/`

- **DataModels.kt**: Core business models
- **ResponseModels.kt**: API and service responses

## 🔧 Configuration Files

### build.gradle.kts (App)

- Dependencies declaration
- Build configuration
- ML model binding enabled

### gradle/libs.versions.toml

Centralized dependency management:

- TensorFlow Lite 2.14.0
- Gemini AI 0.9.0
- Compose BOM 2024.09.00
- Navigation 2.8.0
- Coroutines 1.8.0
- And more...

### AndroidManifest.xml

Permissions:

- Internet, Network State
- Location (Fine, Coarse)
- Audio Recording
- Phone, SMS
- Contacts

## 🎯 Feature Implementation Status

| Module | UI | ViewModel | AI Service | Status |
|--------|----|-----------|-----------||--------|
| Sathi AI | ✅ | ✅ | ✅ | Complete |
| Guardian AI | ✅ | ✅ | ✅ | Complete |
| Nyaya AI | ✅ | ✅ | ✅ | Complete |
| DhanShakti AI | ✅ | ✅ | ✅ | Complete |
| Sangam AI | ✅ | ✅ | ✅ | Complete |
| Gyaan AI | ✅ | ✅ | ✅ | Complete |
| Swasthya AI | ✅ | ✅ | ✅ | Complete |
| Raksha AI | ✅ | ✅ | ✅ | Complete |
| Blockchain | - | - | ✅ | Complete |

## 🔄 Data Flow

```
User Interaction
    ↓
UI Screen (Compose)
    ↓
ViewModel (StateFlow)
    ↓
AI Service / Blockchain
    ↓
TensorFlow Lite Model / Aptos
    ↓
Result Processing
    ↓
StateFlow Update
    ↓
UI Re-composition
```

## 🧩 Dependencies

### Core Android

- Kotlin 2.0.21
- Compose BOM 2024.09.00
- Navigation Compose 2.8.0
- Lifecycle 2.9.4

### AI/ML

- TensorFlow Lite 2.14.0
- Gemini AI 0.9.0

### Networking

- Retrofit 2.9.0
- OkHttp 4.12.0

### Blockchain

- Web3j 4.10.3

### Async

- Coroutines 1.8.0

## 🚀 Build & Run

```bash
# Clean build
./gradlew clean

# Build debug
./gradlew assembleDebug

# Build release
./gradlew assembleRelease

# Run tests
./gradlew test

# Install on device
./gradlew installDebug
```

## 📝 Code Style

- **Language**: Kotlin
- **Architecture**: MVVM
- **UI**: Declarative (Compose)
- **Async**: Coroutines + Flow
- **DI**: Manual (Singleton pattern)

## 🔐 Security Features

1. **Permissions**: Runtime permission requests
2. **Encryption**: Secure data storage
3. **Blockchain**: Immutable record keeping
4. **Local Processing**: Privacy-first ML inference

## 📊 Performance Optimizations

1. **Lazy Loading**: Singleton AI services
2. **Efficient Compose**: Remember, keys, stable types
3. **ML Optimization**: Quantized models, GPU delegation
4. **Memory Management**: Proper lifecycle handling

## 🧪 Testing Strategy

- **Unit Tests**: ViewModels, AI services
- **Integration Tests**: End-to-end flows
- **UI Tests**: Compose testing
- **ML Tests**: Model accuracy validation

## 📚 Documentation

- **README.md**: Main project documentation
- **PROJECT_STRUCTURE.md**: This file
- **ml/README.md**: ML model documentation
- **Code Comments**: Inline documentation

## 🎓 Learning Resources

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [TensorFlow Lite](https://www.tensorflow.org/lite)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [MVVM Pattern](https://developer.android.com/topic/architecture)
- [Aptos Blockchain](https://aptos.dev/)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Follow the existing code structure
4. Write tests
5. Submit a pull request

## 📞 Support

For questions or issues:

- GitHub Issues
- Email: dev@shaktiai.org
- Documentation: https://docs.shaktiai.org

---

**Last Updated**: 2025-01-07  
**Version**: 3.0  
**Maintainer**: ShaktiAI Team
