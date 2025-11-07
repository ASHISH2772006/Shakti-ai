# Migration to Traditional Android Views

## 📋 Overview

The project has been updated to use **traditional Android Views** (XML layouts) instead of Jetpack
Compose, as per the updated requirements. This document outlines the changes made and what still
needs to be implemented.

## ✅ Changes Completed

### 1. Build Configuration Updated

#### `app/build.gradle.kts`

- ✅ Removed Jetpack Compose dependencies
- ✅ Added traditional Android dependencies (AppCompat, Material, ConstraintLayout, RecyclerView)
- ✅ Added all AI/ML dependencies (TensorFlow Lite, Gemini AI)
- ✅ Added Aptos blockchain SDK (Kaptos)
- ✅ Updated to Java 17
- ✅ Updated to compileSdk 34, targetSdk 34
- ✅ Enabled ML Model Binding

#### `build.gradle.kts` (Project Level)

- ✅ Removed Compose plugin
- ✅ Added kapt plugin for annotation processing
- ✅ Added Google Services plugin for Firebase

### 2. UI Layer Migrated to Fragments

#### Created Fragments:

- ✅ `MainActivity.kt` - Main activity with fragment management
- ✅ `HomeFragment.kt` - Home screen with AI module grid
- ✅ `SathiAIFragment.kt` - Emotional support chat interface
- ✅ `GuardianAIFragment.kt` - Safety monitoring
- ✅ `NyayaAIFragment.kt` - Legal assistance
- ✅ `DhanShaktiAIFragment.kt` - Financial literacy
- ✅ `SangamAIFragment.kt` - Community connections
- ✅ `GyaanAIFragment.kt` - Education
- ✅ `SwasthyaAIFragment.kt` - Health monitoring
- ✅ `RakshaAIFragment.kt` - Pattern recognition
- ✅ `ProfileFragment.kt` - User profile

#### Removed Old Compose Files:

- ❌ Deleted all `*Screen.kt` files (Compose-based)

### 3. Core AI Services (Unchanged)

All AI service files remain intact:

- ✅ `SathiAI.kt`, `GuardianAI.kt`, `NyayaAI.kt`, etc.
- ✅ All ViewModels work with both Compose and traditional views
- ✅ Data models unchanged
- ✅ Blockchain services unchanged

## 🔧 What Needs to be Created

### 1. XML Layout Files

Create these layout files in `app/src/main/res/layout/`:

#### Main Activity Layout

**File**: `activity_main.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <FrameLayout
        android:id="@+id/fragment_container"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toTopOf="@+id/bottom_navigation"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>

    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/bottom_navigation"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:menu="@menu/bottom_nav_menu"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```

#### Home Fragment Layout

**File**: `fragment_home.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <TextView
        android:id="@+id/title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="ShaktiAI 3.0"
        android:textSize="24sp"
        android:textStyle="bold"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"/>

    <TextView
        android:id="@+id/subtitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Empowering Women Through AI"
        android:textSize="16sp"
        app:layout_constraintTop_toBottomOf="@+id/title"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="8dp"/>

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recycler_ai_modules"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintTop_toBottomOf="@+id/subtitle"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="16dp"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```

#### Sathi AI Fragment Layout

**File**: `fragment_sathi_ai.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recycler_messages"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toTopOf="@+id/input_container"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>

    <LinearLayout
        android:id="@+id/input_container"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:padding="8dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent">

        <EditText
            android:id="@+id/input_message"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:hint="Share your feelings..."
            android:maxLines="4"/>

        <ImageButton
            android:id="@+id/btn_send"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@android:drawable/ic_menu_send"
            android:contentDescription="Send"/>

    </LinearLayout>

</androidx.constraintlayout.widget.ConstraintLayout>
```

#### Other Fragment Layouts (Simple Template)

Create these files with simple placeholder layouts:

- `fragment_guardian_ai.xml`
- `fragment_nyaya_ai.xml`
- `fragment_dhanshakti_ai.xml`
- `fragment_sangam_ai.xml`
- `fragment_gyaan_ai.xml`
- `fragment_swasthya_ai.xml`
- `fragment_raksha_ai.xml`
- `fragment_profile.xml`

### 2. Menu Resource Files

#### Bottom Navigation Menu

**File**: `app/src/main/res/menu/bottom_nav_menu.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/nav_home"
        android:icon="@android:drawable/ic_menu_home"
        android:title="Home"/>
    <item
        android:id="@+id/nav_sathi"
        android:icon="@android:drawable/ic_menu_mood"
        android:title="Sathi"/>
    <item
        android:id="@+id/nav_guardian"
        android:icon="@android:drawable/ic_lock_lock"
        android:title="Guardian"/>
    <item
        android:id="@+id/nav_profile"
        android:icon="@android:drawable/ic_menu_myplaces"
        android:title="Profile"/>
</menu>
```

### 3. RecyclerView Adapters

Create these adapter files in `app/src/main/java/com/shakti/ai/ui/adapters/`:

#### AIModuleAdapter.kt

```kotlin
package com.shakti.ai.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shakti.ai.R
import com.shakti.ai.ui.models.AIModule

class AIModuleAdapter(private val modules: List<AIModule>) :
    RecyclerView.Adapter<AIModuleAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.module_icon)
        val title: TextView = view.findViewById(R.id.module_title)
        val description: TextView = view.findViewById(R.id.module_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ai_module, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val module = modules[position]
        holder.title.text = module.name
        holder.description.text = module.description
        holder.icon.setImageResource(module.iconRes)
        holder.itemView.setOnClickListener { module.onClick() }
    }

    override fun getItemCount() = modules.size
}
```

#### MessageAdapter.kt

```kotlin
package com.shakti.ai.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shakti.ai.R
import com.shakti.ai.models.ConversationMessage

class MessageAdapter : ListAdapter<ConversationMessage, MessageAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.findViewById(R.id.message_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutRes = if (viewType == VIEW_TYPE_USER) {
            R.layout.item_message_user
        } else {
            R.layout.item_message_ai
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.messageText.text = getItem(position).message
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isUser) VIEW_TYPE_USER else VIEW_TYPE_AI
    }

    companion object {
        const val VIEW_TYPE_USER = 1
        const val VIEW_TYPE_AI = 2
    }

    class DiffCallback : DiffUtil.ItemCallback<ConversationMessage>() {
        override fun areItemsTheSame(old: ConversationMessage, new: ConversationMessage) =
            old.id == new.id
        override fun areContentsTheSame(old: ConversationMessage, new: ConversationMessage) =
            old == new
    }
}
```

### 4. Data Model for UI

**File**: `app/src/main/java/com/shakti/ai/ui/models/AIModule.kt`

```kotlin
package com.shakti.ai.ui.models

data class AIModule(
    val name: String,
    val description: String,
    val iconRes: Int,
    val onClick: () -> Unit
)
```

### 5. RecyclerView Item Layouts

Create in `app/src/main/res/layout/`:

**File**: `item_ai_module.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.google.android.material.card.MaterialCardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="150dp"
    android:layout_margin="8dp"
    app:cardElevation="4dp"
    app:cardCornerRadius="8dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:gravity="center"
        android:padding="16dp">

        <ImageView
            android:id="@+id/module_icon"
            android:layout_width="48dp"
            android:layout_height="48dp"/>

        <TextView
            android:id="@+id/module_title"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textSize="16sp"
            android:textStyle="bold"
            android:layout_marginTop="8dp"
            android:gravity="center"/>

        <TextView
            android:id="@+id/module_description"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textSize="12sp"
            android:gravity="center"/>

    </LinearLayout>

</com.google.android.material.card.MaterialCardView>
```

**Files**: `item_message_user.xml` and `item_message_ai.xml`
(Simple chat bubble layouts)

### 6. Drawable Resources

Create placeholder drawable resources for icons in `app/src/main/res/drawable/`:

- `ic_sathi.xml`
- `ic_guardian.xml`
- `ic_nyaya.xml`
- `ic_dhanshakti.xml`
- `ic_sangam.xml`
- `ic_gyaan.xml`
- `ic_swasthya.xml`
- `ic_raksha.xml`

## 🚀 Next Steps

1. **Sync Gradle**: Open the project in Android Studio and sync Gradle
2. **Create XML Layouts**: Create all the layout files listed above
3. **Create Adapters**: Implement the RecyclerView adapters
4. **Create Drawables**: Add icon resources
5. **Test Build**: Build and run the app

## 📝 Key Differences

### Before (Jetpack Compose)

```kotlin
@Composable
fun HomeScreen() {
    // Declarative UI
}
```

### After (Traditional Views)

```kotlin
class HomeFragment : Fragment() {
    override fun onCreateView(...): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}
```

## ✅ Benefits of This Approach

1. **Compatibility**: Works with older Android versions
2. **Stability**: More mature, well-tested framework
3. **Performance**: Generally better for complex lists
4. **Resources**: More examples and documentation
5. **Team Knowledge**: Wider developer familiarity

## 📚 Resources

- [Android Fragment Guide](https://developer.android.com/guide/fragments)
- [RecyclerView Guide](https://developer.android.com/guide/topics/ui/layout/recyclerview)
- [Material Design Components](https://material.io/develop/android)
- [Navigation Component](https://developer.android.com/guide/navigation)

---

**Note**: All ViewModels, AI services, and business logic remain unchanged and work seamlessly with
both UI approaches!
