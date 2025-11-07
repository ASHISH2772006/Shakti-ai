# TensorFlow Lite Models

This directory contains the TensorFlow Lite models used by ShaktiAI 3.0.

## Required Models

### 1. sathi_lstm.tflite

**Purpose**: Emotional analysis for Sathi AI  
**Input**: Text sequence (tokenized)  
**Output**: Emotion classification (5 classes: happy, sad, angry, fearful, neutral)  
**Model Type**: LSTM (Long Short-Term Memory)

**Training Data**: Emotion-labeled text dataset  
**Expected Accuracy**: >85%

---

### 2. guardian_audio.tflite

**Purpose**: Audio distress detection for Guardian AI  
**Input**: Audio waveform/spectrogram  
**Output**: Binary classification (distress/normal) + confidence score  
**Model Type**: YOLOv5 Audio or CNN-based audio classifier

**Training Data**: Audio samples with distress signals and normal speech  
**Expected Accuracy**: >90%

---

### 3. nyaya_nlp.tflite

**Purpose**: Legal query classification for Nyaya AI  
**Input**: Legal query text  
**Output**: Legal category classification  
**Model Type**: BERT/DistilBERT or custom NLP model

**Categories**:

- Domestic Violence
- Workplace Harassment
- Property Rights
- Family Law
- General

---

### 4. dhanshakti_xgboost.tflite

**Purpose**: Financial analysis for DhanShakti AI  
**Input**: Financial profile features  
**Output**: Risk assessment and recommendations  
**Model Type**: XGBoost (converted to TFLite)

**Features**: Income, expenses, savings, investments, debt  
**Output**: Financial health score + category-wise advice

---

### 5. sangam_recommender.tflite

**Purpose**: Community recommendation for Sangam AI  
**Input**: User interests and location  
**Output**: Recommended groups and events  
**Model Type**: Collaborative filtering or content-based recommender

---

### 6. gyaan_classifier.tflite

**Purpose**: Educational content classification for Gyaan AI  
**Input**: Content text  
**Output**: Category and difficulty level  
**Model Type**: Multi-label text classifier

**Categories**: Health, Finance, Legal Rights, Career, Technology, Life Skills  
**Difficulty**: Beginner, Intermediate, Advanced

---

### 7. swasthya_health.tflite

**Purpose**: Health metrics analysis for Swasthya AI  
**Input**: Health metrics (heart rate, steps, sleep, stress)  
**Output**: Health risk assessment and recommendations  
**Model Type**: Gradient Boosting or Neural Network

---

### 8. raksha_pattern.tflite

**Purpose**: Behavior pattern recognition for Raksha AI  
**Input**: User activity sequence  
**Output**: Risk level prediction  
**Model Type**: RNN or Transformer-based sequence model

---

## How to Add Models

### Option 1: Using Pre-trained Models

1. Download the `.tflite` files from the model repository
2. Place them in this directory (`app/src/main/ml/`)
3. Android Studio will automatically generate model wrappers

### Option 2: Train Your Own Models

#### For Python/TensorFlow models:

```python
import tensorflow as tf

# After training your model
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_model = converter.convert()

# Save the model
with open('model_name.tflite', 'wb') as f:
    f.write(tflite_model)
```

#### For XGBoost models:

```python
import xgboost as xgb
import tensorflow as tf

# Convert XGBoost to TFLite
# This requires additional conversion steps
# See: https://github.com/tensorflow/tensorflow/tree/master/tensorflow/lite
```

### Option 3: Use Placeholder Models

For development and testing, the app includes fallback logic that works without the actual models.
The AI modules will use rule-based systems until proper models are added.

## Model Metadata

Each model should include:

- Input shape and type
- Output shape and type
- Preprocessing requirements
- Postprocessing steps
- Version number
- Training date
- Accuracy metrics

## Model Optimization

For better performance:

1. **Quantization**: Convert to INT8 for faster inference
2. **Pruning**: Remove unnecessary weights
3. **GPU Delegation**: Enable GPU acceleration
4. **NNAPI**: Use Neural Networks API on supported devices

```kotlin
// Example: GPU delegation
val options = Interpreter.Options()
options.addDelegate(GpuDelegate())
val interpreter = Interpreter(modelFile, options)
```

## Model Updates

Models can be updated over-the-air (OTA) using:

- Firebase ML
- Custom backend service
- APK updates

## Testing Models

Test each model before deployment:

```bash
# Using TensorFlow Lite benchmark tool
./benchmark_model \
  --graph=model_name.tflite \
  --num_threads=4 \
  --warmup_runs=1 \
  --num_runs=50
```

## Security Considerations

- Models contain proprietary algorithms
- Encrypt sensitive models if needed
- Validate model signatures
- Prevent model extraction

## File Size Guidelines

Target sizes:

- Small models: <5 MB
- Medium models: 5-20 MB
- Large models: >20 MB (use dynamic download)

## Resources

- [TensorFlow Lite Guide](https://www.tensorflow.org/lite/guide)
- [Model Maker](https://www.tensorflow.org/lite/guide/model_maker)
- [ML Kit](https://developers.google.com/ml-kit)
- [Android ML Best Practices](https://developer.android.com/ml)

## Support

For model-related issues:

- Create an issue in the GitHub repository
- Contact: ml-team@shaktiai.org
- Documentation: https://docs.shaktiai.org/models

---

**Note**: The actual trained models are not included in the repository due to size constraints.
Please train your own models or contact the team for pre-trained versions.
