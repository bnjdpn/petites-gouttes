# Petites Gouttes

Android app (Kotlin) for managing frozen breast milk bag inventory. French-language UI.

## Build

Requires Java 17:
```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
./gradlew assembleDebug        # debug build
./gradlew assembleRelease      # signed release APK → app/build/outputs/apk/release/
```

## Architecture

MVVM with Jetpack Compose. No DI framework — manual dependency wiring via `PetitesGouttesApp.database`.

```
app/src/main/java/com/bnjdpn/petitesgouttes/
├── data/
│   ├── database/       # Room: AppDatabase, MilkBagEntity, MilkBagDao
│   ├── preferences/    # DataStore: SettingsDataStore
│   └── repository/     # MilkBagRepository
├── viewmodel/          # DashboardVM, FreezerVM, HistoryVM, StatsVM, SettingsVM
├── ui/
│   ├── screens/        # Dashboard, FreezerList, History, Stats, Settings, AddEditBag
│   ├── components/     # AlertBanner, StatsChart, MilkBagCard
│   ├── navigation/     # AppNavigation (sealed Screen class, bottom nav)
│   └── theme/          # Material 3 theme (Color, Theme, Type)
├── MainActivity.kt
└── PetitesGouttesApp.kt  # Application subclass, holds database singleton
```

## Key details

- **Navigation**: Bottom nav (Dashboard, Freezer, History, Stats) + Settings via top bar icon
- **Database**: Room with KSP annotation processing. Single `AppDatabase` singleton
- **Min SDK**: 26 (Android 8.0), target SDK 34, compile SDK 36
- **No tests**: No unit or instrumentation tests exist yet
- **Signing**: Release keystore at project root (password in build.gradle.kts — not sensitive, open-source project)

## Code style

- Kotlin with Compose
- ViewModels take Application + Repository params, no DI
- French strings inline (not in resources)
