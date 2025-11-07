# API Key Setup Guide - ShaktiAI 3.0

## ✅ API Key Stored Successfully!

Your Gemini AI API key has been securely stored in `local.properties` and configured for use in the
app.

## 🔒 Security Setup

### 1. Storage Location

```properties
# File: local.properties (NOT committed to Git)
GEMINI_API_KEY=AIzaSyAumTB29I9OotrjcVsKoFtIDvisONkH3xQ
```

### 2. Git Ignore Status

✅ `local.properties` is already in `.gitignore`  
✅ Your API key will **NEVER** be committed to version control  
✅ Safe from accidental exposure on GitHub

### 3. Build Configuration

The API key is exposed via `BuildConfig`:

```kotlin
// In app/build.gradle.kts
buildConfigField("String", "GEMINI_API_KEY", "\"${properties.getProperty("GEMINI_API_KEY", "")}\"")
```

## 🔧 How It's Used in the App

### Automatic Usage

The `GeminiService` automatically uses your API key:

```kotlin
// Default usage - uses BuildConfig.GEMINI_API_KEY
val geminiService = GeminiService.getInstance()

// Or with custom key
val geminiService = GeminiService.getInstance("your_custom_key")
```

### In ViewModels & AI Services

All AI modules that need Gemini will automatically use the configured key:

```kotlin
// Example: In a ViewModel
class SathiViewModel : ViewModel() {
    private val geminiService = GeminiService.getInstance()
    
    fun analyzeEmotion(text: String) {
        viewModelScope.launch {
            val result = geminiService.analyzeSentiment(text)
            // Process result...
        }
    }
}
```

## 📱 API Key Features Used

Your Gemini AI key enables these features in ShaktiAI:

### 1. **Sathi AI** - Emotional Support

- Sentiment analysis
- Empathetic responses
- Mental health support conversations

### 2. **Nyaya AI** - Legal Assistance

- Legal query understanding
- Advice generation
- Law explanation

### 3. **DhanShakti AI** - Financial Literacy

- Personalized financial advice
- Budget recommendations
- Investment guidance

### 4. **Swasthya AI** - Health Monitoring

- Health advice generation
- Wellness recommendations
- Symptom understanding

### 5. **General AI Processing**

- Text understanding
- Context-aware responses
- Natural language generation

## 🔐 Security Best Practices

### ✅ What We're Doing Right:

1. **Stored in local.properties** - Not in source code
2. **Not committed to Git** - Protected by .gitignore
3. **Used via BuildConfig** - Secure compile-time injection
4. **No hardcoding** - Never written directly in .kt files

### ⚠️ Important Reminders:

1. **Never** share your API key publicly
2. **Never** commit local.properties to Git
3. **Never** log the API key in production
4. **Rotate** the key if accidentally exposed

### 🔄 If API Key Gets Compromised:

1. Go to [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Delete the compromised key
3. Generate a new key
4. Update `local.properties` with new key
5. Rebuild the app

## 📊 API Key Quota & Limits

### Gemini 1.5 Flash (Free Tier):

- **Rate Limit**: 15 requests per minute
- **Daily Limit**: 1,500 requests per day
- **Free Quota**: Generous for development

### Monitoring Usage:

- Check usage at: https://makersuite.google.com/app/apikey
- Monitor in Google Cloud Console
- Set up billing alerts if needed

## 🧪 Testing the API Key

### After Gradle Sync:

```kotlin
// Test in any Activity/Fragment
lifecycleScope.launch {
    val gemini = GeminiService.getInstance()
    val result = gemini.generateContent("Hello, Gemini!")
    
    result.onSuccess { response ->
        Log.d("GeminiTest", "Response: $response")
    }.onFailure { error ->
        Log.e("GeminiTest", "Error: ${error.message}")
    }
}
```

### Expected Response:

If working correctly, you'll see a response from Gemini AI.

### Common Errors:

- **"API key not valid"** → Check key in local.properties
- **"Quota exceeded"** → Wait for rate limit reset
- **"Network error"** → Check internet connection

## 🔄 Environment-Specific Keys

### Development vs Production

For different environments, you can:

**Option 1: Multiple local.properties**

```properties
# local.properties
GEMINI_API_KEY_DEV=your_dev_key
GEMINI_API_KEY_PROD=your_prod_key
```

**Option 2: Build variants**

```kotlin
// In build.gradle.kts
buildTypes {
    debug {
        buildConfigField("String", "GEMINI_API_KEY", "\"${devKey}\"")
    }
    release {
        buildConfigField("String", "GEMINI_API_KEY", "\"${prodKey}\"")
    }
}
```

## 📚 Additional Resources

- **Gemini AI Documentation**: https://ai.google.dev/docs
- **API Key Management**: https://makersuite.google.com/app/apikey
- **Pricing**: https://ai.google.dev/pricing
- **Best Practices**: https://ai.google.dev/docs/api_key_best_practices

## ✅ Setup Checklist

- [x] API key stored in `local.properties`
- [x] `local.properties` in `.gitignore`
- [x] BuildConfig configured
- [x] GeminiService updated to use BuildConfig
- [ ] Gradle sync completed (do this next!)
- [ ] Test API key with sample request

## 🎯 Next Steps

1. **Sync Gradle** (this will make BuildConfig available)
   ```bash
   Click "Sync Now" in Android Studio
   ```

2. **Build the app**
   ```bash
   ./gradlew assembleDebug
   ```

3. **Test Gemini integration**
    - Run the app
    - Navigate to Sathi AI
    - Try sending a message
    - Verify AI response

## 🚨 Troubleshooting

### Issue: BuildConfig.GEMINI_API_KEY not found

**Solution**: Sync Gradle first

### Issue: API key is empty string

**Solution**: Check `local.properties` format - should be:

```properties
GEMINI_API_KEY=your_key_here
```

No quotes, no spaces around `=`

### Issue: "Invalid API key" error

**Solution**:

1. Verify key at https://makersuite.google.com/app/apikey
2. Ensure no extra characters/spaces
3. Regenerate if necessary

---

**Your API key is ready to use! Just sync Gradle and start building.** 🚀
