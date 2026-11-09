# DAWG Games — Clean-Room Android Reconstruction

DAWG Games is a modern, clean-room, privacy-first Android gaming hub and discovery application engineered with **Kotlin**, **Jetpack Compose (Material 3)**, and **Room Database**.

This codebase was reconstructed cleanly from static and dynamic analysis of the reference application, stripping all proprietary ad networks (AppLovin MAX, RTB trackers, telemetry beacons), removing mandatory cloud lock-in, and implementing 100% offline-first local persistence.

---

## 🚀 Key Features & Custom Modifications

### 1. Completely Free & Zero Telemetry
- **No Ad SDKs**: Entirely purged of AppLovin MAX, RTB collectors, and tracking SDKs.
- **No Mandatory Cloud Auth**: No account creation or third-party web3 wallets required.
- **Privacy First**: Only standard network permissions for catalog fetching and git sync.

### 2. Offline-First Local Storage & Progress (Custom Change 1)
- **Room SQLite Database**: All game progress, playtime logs, gamer notes, and completion statuses are persisted locally on the device in `dawg_games_local.db`.
- **Preloaded Default Catalog**: The database is pre-seeded with custom games and starter achievements so the app is instantly usable offline without internet.
- **Device & Git Repository Sync**: Includes full **JSON Export & Import** capabilities in the **My Library** screen. You can export your progress, commit it to a Git repository, and restore it on any internet-connected device.

### 3. Custom Dashboard Action Button (Custom Change 2)
- Added an **"Add Game"** custom action button to the top app bar of the main dashboard.
- Opens an interactive modal to add custom games, set categories, and modify local library entries on the fly.

### 4. GameGenie Assistant (Reconstructed Clean-Room AI)
- Fully reconstructed local gaming assistant with quick prompt chips (*"Offline casual games"*, *"Best strategy games"*, *"How to sync progress to Git?"*).
- Complete chat history stored locally in Room database with instant search and clear capabilities.

---

## 🏛️ Clean Architecture Structure

```
app/src/main/java/com/dawg/games/
├── DawgApplication.kt              # App lifecycle & DI setup
├── MainActivity.kt                 # Single Activity entry point
├── model/                          # Domain models
│   ├── Game.kt                     # Game entity & category definitions
│   ├── GameProgress.kt             # Local playtime, progress %, & notes
│   └── ChatMessage.kt              # Chat message & sender model
├── data/
│   ├── local/                      # Room Database Layer
│   │   ├── AppDatabase.kt          # Room DB with pre-seeded values
│   │   ├── GameEntity.kt / GameDao.kt
│   │   ├── ProgressEntity.kt / ProgressDao.kt
│   │   └── ChatEntity.kt / ChatDao.kt
│   └── repository/                 # Clean Repositories
│       ├── GameRepository.kt
│       ├── ProgressRepository.kt   # JSON export/import for Git sync
│       └── AssistantRepository.kt  # Local AI logic & history
├── navigation/                     # Jetpack Compose Navigation
│   ├── Screen.kt                   # Route definitions
│   └── NavGraph.kt                 # Bottom navigation bar & graph
└── ui/
    ├── MainViewModel.kt            # StateFlow ViewModel
    ├── theme/                      # Material 3 Theme & Colors
    ├── components/                 # Reusable UI widgets
    │   ├── GameCard.kt
    │   ├── CategoryChip.kt
    │   ├── ProgressStatCard.kt
    │   └── ChatBubble.kt
    └── screens/                    # Core Screens
        ├── DashboardScreen.kt      # Main catalog + Custom Action Button
        ├── GameDetailScreen.kt     # Detail view + Playtime & Notes logger
        ├── AssistantScreen.kt      # GameGenie Assistant UI
        └── LibraryScreen.kt        # Progress overview + Git JSON sync
```

---

## 🛠️ Building & Running

### Prerequisites
- JDK 17 or higher
- Android SDK with API 35 installed
- Android Studio Ladybug / Meerkat or later

### Command-Line Build
To build the debug APK:
```bash
./gradlew assembleDebug
```
The resulting APK will be located at:
`app/build/outputs/apk/debug/app-debug.apk`

---

## 📦 Pushing to Repository
To push this project to your remote Git repository:
```bash
git init
git add .
git commit -m "Initial commit: DAWG clean-room Android reconstruction"
git remote add origin <your-git-repo-url>
git branch -M main
git push -u origin main
```
