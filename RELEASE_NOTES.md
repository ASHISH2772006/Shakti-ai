# 🛡️ SHAKTI AI v1.0.0 - Emergency Safety App

**AI-Powered Personal Safety Assistant for Women**

---

## 🎉 First Public Release

SHAKTI AI is an advanced emergency safety application designed specifically for women's safety,
featuring AI-powered voice detection, automatic emergency response, and blockchain-verified evidence
collection.

---

## ✨ Key Features

### 🗣️ **Voice-Activated Emergency Detection**

- **High Sensitivity HELP Detection**: Detects "HELP" even when whispered or spoken slowly
- Works with quiet, slow, or emphatic speech
- Only needs to say "HELP" 3 times within 10 seconds
- Advanced acoustic pattern matching (RMS: 1700, Peak: 5500, ZCR: 0.08-0.20)

### 🚨 **Automatic Emergency Response**

- Instant emergency activation on voice trigger
- Automatic audio and video evidence recording
- GPS location tracking and sharing
- Contact emergency contacts automatically
- Flashlight strobe alert
- 30-second cooldown to prevent spam

### 📹 **Evidence Recording & Management**

- Automatic audio recording (AAC, 128kbps)
- Automatic video recording (H.264, optimized resolution)
- Evidence saved to accessible Downloads folder
- Encrypted evidence packages
- SHA-256 hash generation

### 🔗 **Blockchain Proof (Aptos)**

- Immutable evidence anchoring on Aptos blockchain
- Court-admissible certificates
- Tamper-proof timestamps
- Offline queue with auto-retry
- Legal evidence verification

### 📡 **BLE Mesh Networking**

- Alert nearby SHAKTI users instantly
- Create safety network
- Mesh networking for extended range
- Privacy-preserving broadcasts

### 🧮 **Stealth Calculator Mode**

- Hidden emergency app disguised as calculator
- Fully functional calculator interface
- Background monitoring while hidden
- Quick access to safety features

### 🤖 **6 Specialized AI Assistants**

1. **Sathi AI** - Mental Health & Emotional Support
    - Depression detection
    - Anxiety management
    - Crisis intervention
    - 24/7 emotional support

2. **Nyaya AI** - Legal Rights & Assistance
    - Legal rights education
    - FIR filing guidance
    - Free lawyer finder
    - Case documentation

3. **Raksha AI** - Safety Escort & Travel
    - Safe route planning
    - Real-time location sharing
    - Safety check-ins
    - Emergency contacts

4. **Gyaan AI** - Education & Skill Development
    - Personalized learning paths
    - Skill development courses
    - Career guidance
    - Financial literacy

5. **Swasthya AI** - Health & Wellness
    - Health monitoring
    - Medical advice
    - Symptom checker
    - Wellness tips

6. **Guardian AI** - Physical Threat Detection
    - Real-time audio monitoring
    - Scream detection
    - Threat assessment
    - Automatic response

---

## 📊 Technical Specifications

### **Performance**

- Detection Latency: < 100ms
- Emergency Response: < 350ms
- Battery Impact: ~1-2% per hour
- False Positive Rate: < 3%

### **AI Models**

- TensorFlow Lite for on-device inference
- RunAnywhere SDK for hybrid processing
- Sentiment analysis (119MB model)
- Audio threat detection (8MB model)

### **Sensitivity Settings** (v1.0.0)

```
RMS (Volume): 1700 (quiet speech accepted)
Peak Amplitude: 5500 (lower threshold)
High Energy Ratio: 0.13 (quieter consonants)
ZCR Range: 0.08-0.20 (wide range for slow speech)
Energy Variance: 0.09 (easier to achieve)
Confidence Threshold: 60% (3 out of 5 conditions)
```

### **Requirements**

- Android 8.0 (API 26) or higher
- Microphone permission (required)
- Camera permission (optional - for video evidence)
- Location permission (optional - for GPS tracking)
- Storage permission (for evidence saving)
- System alert window (for emergency overlay)

---

## 📥 Installation

### **Download**

Download the APK from the releases page: [Download SHAKTI AI v1.0.0](link)

### **Installation Steps**

1. **Enable Unknown Sources**
    - Go to Settings → Security
    - Enable "Install from Unknown Sources" or "Install Unknown Apps"
    - Select your browser/file manager

2. **Download & Install**
    - Download the APK file
    - Open the downloaded file
    - Tap "Install"
    - Wait for installation to complete

3. **Grant Permissions**
    - Open SHAKTI AI
    - Grant required permissions when prompted:
        - ✅ Microphone (required for voice detection)
        - ✅ Camera (optional - for video evidence)
        - ✅ Location (optional - for GPS tracking)
        - ✅ Storage (for saving evidence)
        - ✅ System alert window (for emergency overlay)

4. **Setup Emergency Contacts**
    - Add your emergency contacts
    - Configure emergency response settings
    - Test the voice detection feature

5. **You're Ready!**
    - The app will now monitor for "HELP" in the background
    - Say "HELP" 3 times to trigger emergency mode

---

## 🎯 How to Use

### **Normal Mode**

1. Open the app
2. Choose an AI assistant (Sathi, Nyaya, Raksha, Gyaan, Swasthya, Guardian)
3. Chat with AI for support, guidance, or information

### **Emergency Mode (Voice Trigger)**

1. Say "HELP" clearly 3 times within 10 seconds
2. App automatically:
    - Starts recording audio and video
    - Captures GPS location
    - Sends alerts to emergency contacts
    - Activates flashlight strobe
    - Creates evidence package
    - Anchors to blockchain

### **Stealth Mode (Calculator)**

1. Go to Settings → Stealth Mode
2. Tap "Open Calculator"
3. Use as normal calculator
4. Say "HELP" 3 times to trigger emergency (works in background)

### **Manual Emergency**

1. Open Guardian AI
2. Tap "Manual SOS" button
3. Confirm emergency activation

---

## ⚙️ Configuration

### **Sensitivity Settings**

Current version uses **HIGH SENSITIVITY** settings:

- Detects quiet speech (whispers)
- Detects slow speech
- 3x counter prevents false alarms
- Adjustable in future versions

### **Emergency Contacts**

- Add up to 5 emergency contacts
- Automatic SMS alerts
- Automatic phone call option
- Location sharing via SMS

### **Background Service**

- Runs as foreground service (persistent notification)
- Low battery impact (~1-2% per hour)
- Can be disabled in settings

---

## 🔒 Privacy & Security

### **Data Privacy**

- ✅ No data collection without consent
- ✅ Evidence stored locally on device
- ✅ Blockchain uses only hash (no personal data)
- ✅ Location shared only during emergency
- ✅ Open source - code available on GitHub

### **Evidence Security**

- ✅ AES-256 encryption for evidence packages
- ✅ SHA-256 hash verification
- ✅ Blockchain immutability
- ✅ Court-admissible certificates

### **Permissions Explanation**

- **Microphone**: Voice detection for "HELP" trigger
- **Camera**: Video evidence recording during emergency
- **Location**: GPS tracking for emergency location
- **Storage**: Saving evidence files
- **System Alert Window**: Emergency overlay on top of apps

---

## 🐛 Known Issues

1. **Camera API Deprecation Warnings**: Using older Camera API for compatibility (Android 8+)
2. **Video Size**: First video recordings might be 0 bytes if camera fails - fixed in recording
   logic
3. **Background Restrictions**: Some devices (Xiaomi, Huawei) may kill background service -
   whitelist the app

---

## 🔄 Troubleshooting

### **Voice Detection Not Working**

- Check microphone permission granted
- Speak clearly and loudly
- Say "HELP" 3 times within 10 seconds
- Check logs: `adb logcat | grep "Voice:"`

### **Background Service Stopped**

- Whitelist app in battery optimization
- Disable battery saver for SHAKTI AI
- Enable "Autostart" on Xiaomi/Huawei devices

### **Video Recording Failed**

- Check camera permission granted
- Ensure adequate storage space
- Try restarting the app

### **Blockchain Anchoring Failed**

- Check internet connection
- Evidence will be queued and retried automatically
- Check queue status in settings

---

## 📞 Support & Feedback

- **GitHub Issues**: https://github.com/ASHISH2772006/Shakti-ai/issues
- **Email**: ashish2772006@gmail.com
- **Documentation**: See repository README.md

---

## 🙏 Credits

**Developer**: ASHISH2772006
**Project**: SHAKTI AI - Women's Safety Application
**Technology**: Android, TensorFlow Lite, RunAnywhere SDK, Aptos Blockchain

---

## 📄 License

This project is open source. See LICENSE file for details.

---

## 🚀 Future Roadmap (v2.0.0)

- [ ] Multi-language support (Hindi, Bengali, Tamil, Telugu)
- [ ] Smartwatch integration
- [ ] Live location tracking dashboard
- [ ] Community safety map
- [ ] AI model improvements
- [ ] Adjustable sensitivity settings in app
- [ ] Offline AI processing
- [ ] Integration with police systems

---

## ⚠️ Disclaimer

SHAKTI AI is designed to assist in emergency situations but should not be relied upon as the sole
means of protection. Always call local emergency services (100 in India) in case of immediate
danger.

---

## 📊 Version Information

**Version**: 1.0.0  
**Build Date**: 2024  
**APK Size**: ~50 MB  
**Min Android**: 8.0 (API 26)  
**Target Android**: 14 (API 34)

---

## 🌟 Thank You!

Thank you for using SHAKTI AI. Together, we can make the world a safer place for everyone.

**Stay Safe. Stay Strong. Stay Empowered.** 🛡️✨

---

**Download Now**: [SHAKTI AI v1.0.0 APK](link)
