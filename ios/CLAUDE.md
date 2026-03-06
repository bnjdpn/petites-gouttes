# Petites Gouttes iOS

iOS port of the Android breast milk bag inventory app. French-language UI.

## Build

Requires Xcode 16+ and XcodeGen:

    cd ios
    xcodegen generate
    xcodebuild -project PetitesGouttes.xcodeproj -scheme PetitesGouttes -destination 'platform=iOS Simulator,name=iPhone 17 Pro' build

## Architecture

MVVM with SwiftUI + SwiftData. No DI framework. @Observable ViewModels with @MainActor.

    PetitesGouttes/
    ├── App/             # Entry point, screenshot data service
    ├── Models/          # MilkBag SwiftData model
    ├── ViewModels/      # Dashboard, Freezer, History, Stats, Settings
    ├── Views/           # SwiftUI screens and components
    ├── Navigation/      # ContentView with TabView
    └── Theme/           # Colors, card styles

## Key details

- iOS 17+, Swift 6, strict concurrency
- SwiftData local only (no CloudKit)
- French only (no localization)
- XcodeGen: project.yml is source of truth (.xcodeproj gitignored)
- Fastlane for screenshots and App Store deployment
- Settings via @AppStorage (UserDefaults)

## Deploy

    cd ios
    fastlane screenshots      # capture screenshots
    fastlane beta             # TestFlight
    fastlane release          # App Store (full)
    fastlane release_quick    # App Store (no screenshots)
