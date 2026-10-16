# ONE THING — Grilli Type Editorial Palette, Smooth Navigation & Deep Distraction Shield

This revised plan incorporates the Grilli Type editorial color palette, introduces a persistent and fluid bottom navigation bar for effortless switching between Home, Distraction Shield, and Skills Settings, resolves the YouTube Shorts and Instagram Reels bypass using an Android Accessibility Service with instant auto-back redirection, enforces a 5-strike complete app lockout per focus session, and protects ONE THING against uninstallation or force-stopping during active focus periods.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> **Summary of User Requirements & Revisions**:
> 1. **Grilli Type Editorial Palette**:
>    - `EditorialBlack = Color(0xFF111111)` — Stark contrast text, headlines, and heavy editorial border strokes.
>    - `WarmPaper = Color(0xFFFFFDF9)` — Warm off-white canvas and card backgrounds.
>    - `ElectricPurple = Color(0xFF6A0DAD)` — Vibrant poster accent purple for badges, active states, and focal points.
>    - `VibrantOrange = Color(0xFFFF5722)` — High-energy highlight tone for primary buttons, alerts, and strikes.
>    - `MustardYellow = Color(0xFFF4D06F)` — Warm editorial highlight for category chips and streak badges.
>    - `MintGreen = Color(0xFFE2ECE9)` — Calm secondary background for lesson callouts, quizzes, and success cards.
> 2. **Smooth & Easy Navigation**:
>    - Add a persistent, sleek editorial Navigation Bar at the bottom of the screen with smooth animated crossfades between **Home**, **Distraction Shield**, and **Skill Settings**.
>    - BackHandler integration ensures natural, intuitive back-stack navigation from detail screens (Lesson, Quiz, Lock) without getting trapped.
> 3. **YouTube Shorts & Instagram Reels Blocker**:
>    - Utilizes an Android `AccessibilityService` (`OneThingAccessibilityShieldService`) to inspect active UI nodes in real time.
>    - When the Shorts player or Reels viewer is entered, the service immediately triggers `GLOBAL_ACTION_BACK` to return the user to the YouTube or Instagram Home screen.
> 4. **5-Strike Session Lockout Rule**:
>    - Increments strike counter on each Shorts/Reels attempt (e.g., Strike 1/5, Strike 2/5).
>    - On the 5th attempt in the same focus session, the parent app (YouTube or Instagram) is locked completely for that session. Any attempt to reopen it triggers `GLOBAL_ACTION_HOME` and displays the ONE THING lockout shield.
> 5. **Anti-Deletion & Anti-Tamper Shield**:
>    - Intercepts Android Package Installer (`com.android.packageinstaller`, `com.google.android.packageinstaller`) and App Info in Settings (`com.android.settings`) during active focus periods.
>    - Prevents uninstallation or force-stopping by immediately navigating home and displaying a tamper alert.

---

### 1. Overview & Core Concept

1. **Grilli Type Editorial Design System**:
   - Replaces the previous color definitions with the exact Grilli Type palette.
   - Clean, high-contrast editorial typography, distinct bordered cards, warm off-white background, and vibrant poster accents.
2. **Unified Navigation Shell**:
   - A modern, distraction-free bottom navigation bar allowing 1-tap switching between:
     - 📖 **Today's One Thing** (Home, Topic Cards, Daily Focus)
     - 🛡️ **Distraction Shield** (Live countdown, Blocked apps, Strike monitor, Permissions)
     - ⚙️ **Skill Settings** (AI Goal extraction, 12 disciplines, Custom skills, API key)
3. **Real-Time Shorts & Reels Interception**:
   - Accessibility node inspection detects `shorts_player`, `reel_player`, `clips_viewer`, and "Shorts"/"Reels" tabs.
   - Immediate programmatic Back action redirects users to the home feed before doomscrolling begins.
4. **5-Strike Session Lockout**:
   - First 4 attempts: Instant Back + informative toast: *"Shorts blocked! Strike X of 5"*.
   - 5th attempt: Entire app locked out for the rest of the focus session. Reopening launches the ONE THING lockout screen.
5. **Session Tamper Protection**:
   - Protects against impulsive app deletion or disablement in Android Settings while a focus timer is running.

---

### 2. User Experience & Visual Design

#### Navigation Bar Layout & Screen Transitions
```
┌────────────────────────────────────────────────────────────────────────┐
│                              TOP APP BAR                               │
│  [ISSUE 044]              ONE THING              [STREAK: 7 DAYS 🔥]   │
├────────────────────────────────────────────────────────────────────────┤
│                                                                        │
│                      ACTIVE SCREEN CONTENT                             │
│                                                                        │
│  [HOME SCREEN]            [SHIELD SCREEN]        [SETTINGS SCREEN]     │
│  - Daily Topic Cards      - 45m Focus Timer      - AI Prompt Input     │
│  - Art Canvas Cover       - Live Strikes (0/5)   - 12 Discipline Grid  │
│  - "I'll Learn This"      - Apps Checklist       - Custom Skills       │
│  - Active Lesson Study    - Service Status       - Gemini API Key      │
│                                                                        │
├────────────────────────────────────────────────────────────────────────┤
│                    SMOOTH EDITORIAL BOTTOM BAR                         │
│  ┌────────────────────┬────────────────────┬────────────────────┐      │
│  │   📖 TODAY         │   🛡️ SHIELD (ON)   │   ⚙️ SETTINGS      │      │
│  │   [Active Pill]    │   [Strike Count]   │   [Category Badges]│      │
│  └────────────────────┴────────────────────┴────────────────────┘      │
└────────────────────────────────────────────────────────────────────────┘
```

#### Color Mapping & Design Tokens
- **Background**: `WarmPaper` (`#FFFDF9`)
- **Primary Text & Outlines**: `EditorialBlack` (`#111111`) with 1.5.dp to 2.dp solid border strokes
- **Primary CTA Buttons**: `VibrantOrange` (`#FFFF5722`) with `WarmPaper` text
- **Secondary Surfaces & Chips**: `MintGreen` (`#E2ECE9`)
- **Focal Highlights & Active Navigation**: `ElectricPurple` (`#6A0DAD`)
- **Warning & Strike Badges**: `MustardYellow` (`#F4D06F`) and `VibrantOrange`

---

### 3. Key Product Decisions & Trade-Offs

- **Decision 1: Persistent Bottom Navigation**:
  - *Chosen Approach*: An editorial bottom navigation bar visible on primary screens (Home, Shield, Settings), with sub-screens (Study Lesson, Quiz, Lockout) taking full focus with explicit Back buttons.
  - *Why*: Eliminates buried menus and enables instant, fluid navigation between your learning queue, distraction shield status, and skill configuration.
- **Decision 2: In-App Shorts/Reels Detection via Accessibility**:
  - *Chosen Approach*: `AccessibilityService` listening for `TYPE_WINDOW_STATE_CHANGED` and `TYPE_WINDOW_CONTENT_CHANGED`.
  - *Why*: The only native Android API capable of distinguishing between viewing an educational video vs. doomscrolling Shorts/Reels, and the only API capable of executing an automated Back action.
- **Decision 3: 5-Strike Lockout Policy**:
  - *Chosen Approach*: Tracks strikes per app within the active focus session. Reaching 5 strikes hard-locks that app until the focus timer completes.
  - *Why*: Balances flexibility (allowing brief lookups) with firm discipline against habitual relapses.
- **Decision 4: Anti-Deletion Protection**:
  - *Chosen Approach*: Accessibility service intercepts Package Installer and Settings App Info for ONE THING during active sessions, triggering Home navigation.
  - *Why*: Prevents impulsive bypasses without requiring complex enterprise enrollment.

---

### 4. Technical Architecture & Component Hierarchy

```
┌────────────────────────────────────────────────────────────────────────┐
│                        UPDATED SYSTEM TOPOLOGY                         │
├────────────────────────────────────────────────────────────────────────┤
│                                                                        │
│  THEME & COLOR SYSTEM                                                  │
│  └── Color.kt (Grilli Type palette: EditorialBlack, WarmPaper,         │
│                ElectricPurple, VibrantOrange, MustardYellow, MintGreen)│
│  └── Theme.kt (Editorial M3 ColorScheme & border styles)               │
│                                                                        │
│  NAVIGATION & UI LAYER                                                 │
│  ├── MainActivity.kt (Manages active tab: HOME | SHIELD | SETTINGS)    │
│  ├── EditorialBottomBar.kt (Smooth animated tab switcher)              │
│  ├── HomeScreen.kt (Masthead, topic cards, AI re-roll, daily progress) │
│  ├── DistractionShieldScreen.kt (Focus timer, strikes, permission guide│
│  ├── SkillSettingsScreen.kt (AI goal input, 12 disciplines, API key)   │
│  ├── StudyDetailScreen.kt (Lesson view, 5-min task, reflection prompt) │
│  └── QuizScreen.kt (Comprehension quiz with immediate feedback)        │
│                                                                        │
│  SERVICES & BACKGROUND MONITORING                                      │
│  ├── OneThingAccessibilityShieldService.kt                             │
│  │   ├── In-app Shorts & Reels detection via AccessibilityNodeInfo     │
│  │   ├── Immediate performGlobalAction(GLOBAL_ACTION_BACK)             │
│  │   ├── 5-strike counter tracking & session lockout                   │
│  │   └── Anti-deletion interception for Package Installer / Settings   │
│  ├── FocusShieldService.kt (Foreground countdown & notification)      │
│  └── accessibility_service_config.xml (Event types, feedback, flags)   │
│                                                                        │
│  DATA & STATE REPOSITORIES                                             │
│  ├── ShieldRepository.kt (Strikes, hard-locked session apps, settings) │
│  ├── UserProgressRepository.kt (Completed topics, streak, reflections) │
│  └── OneThingViewModel.kt (Shared UI state, navigation state, timers)   │
└────────────────────────────────────────────────────────────────────────┘
```

#### Files to Create & Modify
1. **`app/src/main/java/com/example/ui/theme/Color.kt`** *(Modify)*:
   - Insert exact Grilli Type palette: `EditorialBlack`, `WarmPaper`, `ElectricPurple`, `VibrantOrange`, `MustardYellow`, `MintGreen`.
2. **`app/src/main/java/com/example/ui/theme/Theme.kt`** *(Modify)*:
   - Wire Grilli Type colors into Material3 `lightColorScheme` and custom editorial styles.
3. **`app/src/main/java/com/example/ui/components/EditorialBottomBar.kt`** *(New)*:
   - Minimalist editorial bottom navigation bar with icons, labels, strike counter indicator, and smooth animated active states.
4. **`app/src/main/res/xml/accessibility_service_config.xml`** *(New)*:
   - Accessibility service metadata XML configuring window and content change events with node content inspection.
5. **`app/src/main/java/com/example/service/OneThingAccessibilityShieldService.kt`** *(New)*:
   - Android `AccessibilityService` that identifies YouTube Shorts and Instagram Reels, executes immediate Back action, counts strikes up to 5, locks the entire app upon the 5th strike, and blocks uninstallation in Settings.
6. **`app/src/main/java/com/example/data/ShieldRepository.kt`** *(Modify)*:
   - Tracks per-app strikes, session lockouts, accessibility state helpers, and reset routines.
7. **`app/src/main/AndroidManifest.xml`** *(Modify)*:
   - Register `OneThingAccessibilityShieldService` with `BIND_ACCESSIBILITY_SERVICE` permission.
8. **`app/src/main/java/com/example/MainActivity.kt`** *(Modify)*:
   - Integrate `EditorialBottomBar` into the root scaffold with animated transitions between Home, Shield, and Settings tabs.
9. **`app/src/main/java/com/example/ui/screens/DistractionShieldScreen.kt`** *(Modify)*:
   - Display Accessibility Service status card, deep link to Accessibility Settings, live strike counters (e.g., YouTube: 2/5 strikes), and session lockout status.
10. **`UPDATED.md`** *(Modify)*:
    - Comprehensive documentation of all changes, color definitions, navigation architecture, and Accessibility shielding.

---

### Verification & Testing Plan
- **CUJ 1: Grilli Type Visual Consistency**:
  - Verify all screens use `WarmPaper` background, `EditorialBlack` contrast borders and text, `ElectricPurple` accents, `VibrantOrange` primary buttons, `MustardYellow` highlight badges, and `MintGreen` lesson cards.
- **CUJ 2: Smooth Navigation**:
  - Tap Bottom Bar tabs (Today, Shield, Settings) -> Seamless, smooth crossfade between screens with state preserved.
  - Navigate to Lesson / Quiz -> Bottom bar cleanly hides or respects back navigation via `BackHandler`.
- **CUJ 3: YouTube Shorts Instant Back**:
  - Start focus session -> Open YouTube -> Tap Shorts -> Instantly sent back to Home feed -> Toast confirms strike 1 of 5.
- **CUJ 4: Instagram Reels Instant Back**:
  - Open Instagram -> Tap Reels -> Instantly sent back to Home feed -> Strike increments.
- **CUJ 5: 5-Strike Complete Session Lockout**:
  - Re-attempt Shorts/Reels 5 times -> On 5th attempt, YouTube/Instagram is locked for that session -> Re-opening takes user straight to ONE THING with session lockout notice.
- **CUJ 6: Anti-Deletion Shield**:
  - Try opening Android Settings -> Apps -> ONE THING -> System redirected home with alert that settings are locked during active focus.
- **CUJ 7: Build & Compilation**:
  - Run `compile_applet` and test suite to guarantee 100% build success and clean lint.
