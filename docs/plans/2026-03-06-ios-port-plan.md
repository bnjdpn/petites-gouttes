# Petites Gouttes iOS Port — Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Port the Petites Gouttes Android app to iOS and deploy to the App Store.

**Architecture:** SwiftUI + SwiftData MVVM, following the exact conventions of the user's other iOS apps (BrewMeter, NeatShift, etc.). XcodeGen for project config, Fastlane for deployment. FR-only, local storage only.

**Tech Stack:** Swift 6, iOS 17+, SwiftData, SwiftUI, Swift Charts, XcodeGen, Fastlane

---

### Task 1: Project Scaffold

**Files:**
- Create: `ios/project.yml`
- Create: `ios/PetitesGouttes/App/PetitesGouttesApp.swift` (placeholder)

**Step 1: Create directory structure**

```bash
cd /Users/benjamin/Documents/petites-gouttes
mkdir -p ios/PetitesGouttes/{App,Models,ViewModels,Views/{Dashboard,Freezer,History,Stats,Settings,AddEditBag,Components},Navigation,Theme,Resources}
mkdir -p ios/PetitesGouttesTests
mkdir -p ios/PetitesGouttesUITests
mkdir -p ios/fastlane/metadata/fr-FR
```

**Step 2: Create project.yml**

```yaml
name: PetitesGouttes
options:
  bundleIdPrefix: com.bnjdpn
  deploymentTarget:
    iOS: "17.0"
  xcodeVersion: "16.0"
  generateEmptyDirectories: true

settings:
  base:
    SWIFT_VERSION: "6.0"
    SWIFT_STRICT_CONCURRENCY: complete
    MARKETING_VERSION: "1.0.0"
    CURRENT_PROJECT_VERSION: 1
    DEVELOPMENT_TEAM: 767SX34A7Z

targets:
  PetitesGouttes:
    type: application
    platform: iOS
    sources:
      - path: PetitesGouttes
    settings:
      base:
        PRODUCT_BUNDLE_IDENTIFIER: com.bnjdpn.petitesgouttes
        INFOPLIST_KEY_CFBundleDisplayName: Petites Gouttes
        INFOPLIST_KEY_LSApplicationCategoryType: public.app-category.healthcare-fitness
        INFOPLIST_KEY_UILaunchScreen_Generation: true
        INFOPLIST_KEY_UISupportedInterfaceOrientations: UIInterfaceOrientationPortrait
        INFOPLIST_KEY_UISupportedInterfaceOrientations_iPad: "UIInterfaceOrientationPortrait UIInterfaceOrientationPortraitUpsideDown UIInterfaceOrientationLandscapeLeft UIInterfaceOrientationLandscapeRight"
        ASSETCATALOG_COMPILER_APPICON_NAME: AppIcon
        ASSETCATALOG_COMPILER_GLOBAL_ACCENT_COLOR_NAME: AccentColor

  PetitesGouttesTests:
    type: bundle.unit-test
    platform: iOS
    sources:
      - path: PetitesGouttesTests
    dependencies:
      - target: PetitesGouttes
    settings:
      base:
        PRODUCT_BUNDLE_IDENTIFIER: com.bnjdpn.petitesgouttes.tests

  PetitesGouttesUITests:
    type: bundle.ui-testing
    platform: iOS
    sources:
      - path: PetitesGouttesUITests
    dependencies:
      - target: PetitesGouttes
    settings:
      base:
        PRODUCT_BUNDLE_IDENTIFIER: com.bnjdpn.petitesgouttes.uitests
```

**Step 3: Create minimal App entry point (placeholder)**

```swift
// PetitesGouttes/App/PetitesGouttesApp.swift
import SwiftUI
import SwiftData

@main
struct PetitesGouttesApp: App {
    var body: some Scene {
        WindowGroup {
            Text("Petites Gouttes")
        }
        .modelContainer(for: MilkBag.self)
    }
}
```

This won't compile yet (MilkBag doesn't exist) — that's fine, Task 2 fixes it.

**Step 4: Create Assets.xcassets with AccentColor**

```bash
mkdir -p ios/PetitesGouttes/Resources/Assets.xcassets/AccentColor.colorset
mkdir -p ios/PetitesGouttes/Resources/Assets.xcassets/AppIcon.appiconset
```

Create `ios/PetitesGouttes/Resources/Assets.xcassets/Contents.json`:
```json
{
  "info" : {
    "author" : "xcode",
    "version" : 1
  }
}
```

Create `ios/PetitesGouttes/Resources/Assets.xcassets/AccentColor.colorset/Contents.json`:
```json
{
  "colors" : [
    {
      "color" : {
        "color-space" : "srgb",
        "components" : {
          "alpha" : "1.000",
          "blue" : "0x8A",
          "green" : "0x71",
          "red" : "0xB4"
        }
      },
      "idiom" : "universal"
    },
    {
      "appearances" : [
        {
          "appearance" : "luminosity",
          "value" : "dark"
        }
      ],
      "color" : {
        "color-space" : "srgb",
        "components" : {
          "alpha" : "1.000",
          "blue" : "0xD0",
          "green" : "0xB0",
          "red" : "0xFF"
        }
      },
      "idiom" : "universal"
    }
  ],
  "info" : {
    "author" : "xcode",
    "version" : 1
  }
}
```

Create `ios/PetitesGouttes/Resources/Assets.xcassets/AppIcon.appiconset/Contents.json`:
```json
{
  "images" : [
    {
      "idiom" : "universal",
      "platform" : "ios",
      "size" : "1024x1024"
    }
  ],
  "info" : {
    "author" : "xcode",
    "version" : 1
  }
}
```

**Step 5: Create placeholder test files**

Create `ios/PetitesGouttesTests/PetitesGouttesTests.swift`:
```swift
import Testing

@Suite("Petites Gouttes Tests")
struct PetitesGouttesTests {
    @Test func placeholder() {
        #expect(true)
    }
}
```

Create `ios/PetitesGouttesUITests/SnapshotTests.swift`:
```swift
import XCTest

final class SnapshotTests: XCTestCase {
    // Will be implemented in Task 11
}
```

**Step 6: Generate Xcode project and verify**

```bash
cd /Users/benjamin/Documents/petites-gouttes/ios
xcodegen generate
```

Expected: "Generated project PetitesGouttes.xcodeproj"

**Step 7: Add .gitignore for Xcode artifacts**

Create `ios/.gitignore`:
```
*.xcodeproj
*.xcworkspace
xcuserdata/
DerivedData/
build/
```

**Step 8: Commit**

```bash
git add ios/
git commit -m "feat(ios): scaffold project with XcodeGen"
```

---

### Task 2: Data Model

**Files:**
- Create: `ios/PetitesGouttes/Models/MilkBag.swift`

**Step 1: Create MilkBag SwiftData model**

```swift
// PetitesGouttes/Models/MilkBag.swift
import Foundation
import SwiftData

@Model
final class MilkBag {
    var volumeMl: Int
    var pumpDate: Date
    var expiryDate: Date
    var removedFromFreezer: Bool
    var removalDate: Date?
    var createdAt: Date

    init(volumeMl: Int, pumpDate: Date) {
        self.volumeMl = volumeMl
        self.pumpDate = pumpDate
        self.expiryDate = Calendar.current.date(byAdding: .month, value: 4, to: pumpDate) ?? pumpDate
        self.removedFromFreezer = false
        self.removalDate = nil
        self.createdAt = .now
    }

    var daysUntilExpiry: Int {
        Calendar.current.dateComponents([.day], from: .now, to: expiryDate).day ?? 0
    }

    var expiryStatus: ExpiryStatus {
        let days = daysUntilExpiry
        if days < 14 { return .urgent }
        if days < 30 { return .warning }
        return .safe
    }

    enum ExpiryStatus {
        case safe, warning, urgent

        var label: String {
            switch self {
            case .safe: "OK"
            case .warning: "Bientot"
            case .urgent: "Urgent"
            }
        }
    }
}
```

**Step 2: Build to verify model compiles**

```bash
cd /Users/benjamin/Documents/petites-gouttes/ios
xcodegen generate && xcodebuild -project PetitesGouttes.xcodeproj -scheme PetitesGouttes -destination 'platform=iOS Simulator,name=iPhone 16' build 2>&1 | tail -5
```

Expected: BUILD SUCCEEDED

**Step 3: Commit**

```bash
git add ios/PetitesGouttes/Models/
git commit -m "feat(ios): add MilkBag SwiftData model"
```

---

### Task 3: Theme

**Files:**
- Create: `ios/PetitesGouttes/Theme/Theme.swift`

**Step 1: Create theme with colors and card style**

```swift
// PetitesGouttes/Theme/Theme.swift
import SwiftUI

enum PGTheme {
    // MARK: - Primary (rose/mauve)
    static let primary = Color(light: .init(hex: 0xB4718A), dark: .init(hex: 0xFFB0D0))
    static let primaryContainer = Color(light: .init(hex: 0xFFD9E6), dark: .init(hex: 0x5C3A4A))

    // MARK: - Secondary (lavande)
    static let secondary = Color(light: .init(hex: 0x8B7091), dark: .init(hex: 0xD4BDD9))

    // MARK: - DLC Status
    static let dlcSafe = Color(light: .init(hex: 0x4CAF50), dark: .init(hex: 0x81C784))
    static let dlcWarning = Color(light: .init(hex: 0xFF9800), dark: .init(hex: 0xFFB74D))
    static let dlcUrgent = Color(light: .init(hex: 0xE53935), dark: .init(hex: 0xEF5350))

    // MARK: - Alerts
    static let alertWarningBg = Color(light: .init(hex: 0xFFF3E0), dark: .init(hex: 0x4E3A20))
    static let alertWarningText = Color(light: .init(hex: 0xE65100), dark: .init(hex: 0xFFB74D))
    static let alertDangerBg = Color(light: .init(hex: 0xFFEBEE), dark: .init(hex: 0x4E2020))
    static let alertDangerText = Color(light: .init(hex: 0xB71C1C), dark: .init(hex: 0xEF5350))
}

// MARK: - Color helpers

private extension Color {
    init(light: Color.Resolved, dark: Color.Resolved) {
        self.init(uiColor: UIColor { traits in
            UIColor(traits.userInterfaceStyle == .dark ? dark : light)
        })
    }
}

private extension Color.Resolved {
    init(hex: UInt32) {
        self.init(
            red: Float((hex >> 16) & 0xFF) / 255.0,
            green: Float((hex >> 8) & 0xFF) / 255.0,
            blue: Float(hex & 0xFF) / 255.0
        )
    }
}

// MARK: - Card Style

struct CardStyleModifier: ViewModifier {
    func body(content: Content) -> some View {
        content
            .padding()
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(.regularMaterial, in: .rect(cornerRadius: 16))
    }
}

extension View {
    func cardStyle() -> some View {
        modifier(CardStyleModifier())
    }
}

// MARK: - DLC color mapping

extension MilkBag.ExpiryStatus {
    var color: Color {
        switch self {
        case .safe: PGTheme.dlcSafe
        case .warning: PGTheme.dlcWarning
        case .urgent: PGTheme.dlcUrgent
        }
    }
}
```

**Step 2: Build to verify**

```bash
cd /Users/benjamin/Documents/petites-gouttes/ios
xcodegen generate && xcodebuild -project PetitesGouttes.xcodeproj -scheme PetitesGouttes -destination 'platform=iOS Simulator,name=iPhone 16' build 2>&1 | tail -5
```

**Step 3: Commit**

```bash
git add ios/PetitesGouttes/Theme/
git commit -m "feat(ios): add theme with rose/mauve palette and card style"
```

---

### Task 4: ViewModels

**Files:**
- Create: `ios/PetitesGouttes/ViewModels/DashboardViewModel.swift`
- Create: `ios/PetitesGouttes/ViewModels/FreezerViewModel.swift`
- Create: `ios/PetitesGouttes/ViewModels/HistoryViewModel.swift`
- Create: `ios/PetitesGouttes/ViewModels/StatsViewModel.swift`
- Create: `ios/PetitesGouttes/ViewModels/SettingsViewModel.swift`

**Step 1: Create DashboardViewModel**

```swift
// PetitesGouttes/ViewModels/DashboardViewModel.swift
import SwiftUI
import SwiftData

@Observable
@MainActor
final class DashboardViewModel {
    private let modelContext: ModelContext

    var activeBags: [MilkBag] = []

    var activeBagCount: Int { activeBags.count }

    var totalActiveVolume: Int {
        activeBags.reduce(0) { $0 + $1.volumeMl }
    }

    var nextBagToUse: MilkBag? {
        activeBags.min(by: { $0.pumpDate < $1.pumpDate })
    }

    var bagsExpiringSoon: [MilkBag] {
        activeBags.filter { $0.daysUntilExpiry <= 14 }
    }

    func stockDays(dailyConsumption: Int) -> Int {
        guard dailyConsumption > 0 else { return 0 }
        return totalActiveVolume / dailyConsumption
    }

    func isLowStock(threshold: Int) -> Bool {
        totalActiveVolume < threshold
    }

    init(modelContext: ModelContext) {
        self.modelContext = modelContext
        fetchBags()
    }

    func fetchBags() {
        let predicate = #Predicate<MilkBag> { !$0.removedFromFreezer }
        let descriptor = FetchDescriptor<MilkBag>(predicate: predicate, sortBy: [SortDescriptor(\.pumpDate)])
        activeBags = (try? modelContext.fetch(descriptor)) ?? []
    }
}
```

**Step 2: Create FreezerViewModel**

```swift
// PetitesGouttes/ViewModels/FreezerViewModel.swift
import SwiftUI
import SwiftData

enum SortOrder: String, CaseIterable {
    case dateAsc = "Date (ancien)"
    case dateDesc = "Date (recent)"
    case volumeAsc = "Volume (croissant)"
    case volumeDesc = "Volume (decroissant)"
}

@Observable
@MainActor
final class FreezerViewModel {
    private let modelContext: ModelContext

    var activeBags: [MilkBag] = []
    var sortOrder: SortOrder = .dateAsc

    init(modelContext: ModelContext) {
        self.modelContext = modelContext
        fetchBags()
    }

    func fetchBags() {
        let predicate = #Predicate<MilkBag> { !$0.removedFromFreezer }
        let descriptor = FetchDescriptor<MilkBag>(predicate: predicate)
        activeBags = sortedBags((try? modelContext.fetch(descriptor)) ?? [])
    }

    func removeBag(_ bag: MilkBag) {
        bag.removedFromFreezer = true
        bag.removalDate = .now
        try? modelContext.save()
        fetchBags()
    }

    func deleteBag(_ bag: MilkBag) {
        modelContext.delete(bag)
        try? modelContext.save()
        fetchBags()
    }

    func setSortOrder(_ order: SortOrder) {
        sortOrder = order
        activeBags = sortedBags(activeBags)
    }

    private func sortedBags(_ bags: [MilkBag]) -> [MilkBag] {
        switch sortOrder {
        case .dateAsc: bags.sorted { $0.pumpDate < $1.pumpDate }
        case .dateDesc: bags.sorted { $0.pumpDate > $1.pumpDate }
        case .volumeAsc: bags.sorted { $0.volumeMl < $1.volumeMl }
        case .volumeDesc: bags.sorted { $0.volumeMl > $1.volumeMl }
        }
    }
}
```

**Step 3: Create HistoryViewModel**

```swift
// PetitesGouttes/ViewModels/HistoryViewModel.swift
import SwiftUI
import SwiftData

@Observable
@MainActor
final class HistoryViewModel {
    private let modelContext: ModelContext

    var removedBags: [MilkBag] = []

    init(modelContext: ModelContext) {
        self.modelContext = modelContext
        fetchBags()
    }

    func fetchBags() {
        let predicate = #Predicate<MilkBag> { $0.removedFromFreezer }
        let descriptor = FetchDescriptor<MilkBag>(predicate: predicate, sortBy: [SortDescriptor(\.removalDate, order: .reverse)])
        removedBags = (try? modelContext.fetch(descriptor)) ?? []
    }

    func restoreBag(_ bag: MilkBag) {
        bag.removedFromFreezer = false
        bag.removalDate = nil
        try? modelContext.save()
        fetchBags()
    }

    func deleteBag(_ bag: MilkBag) {
        modelContext.delete(bag)
        try? modelContext.save()
        fetchBags()
    }
}
```

**Step 4: Create StatsViewModel**

```swift
// PetitesGouttes/ViewModels/StatsViewModel.swift
import SwiftUI
import SwiftData

@Observable
@MainActor
final class StatsViewModel {
    private let modelContext: ModelContext

    var allBags: [MilkBag] = []

    init(modelContext: ModelContext) {
        self.modelContext = modelContext
        fetchBags()
    }

    func fetchBags() {
        let descriptor = FetchDescriptor<MilkBag>(sortBy: [SortDescriptor(\.pumpDate)])
        allBags = (try? modelContext.fetch(descriptor)) ?? []
    }

    struct DailyVolume: Identifiable {
        let id = UUID()
        let date: Date
        let volume: Int
    }

    func dailyVolumes(days: Int) -> [DailyVolume] {
        let calendar = Calendar.current
        let today = calendar.startOfDay(for: .now)

        return (0..<days).reversed().map { offset in
            let day = calendar.date(byAdding: .day, value: -offset, to: today)!
            let nextDay = calendar.date(byAdding: .day, value: 1, to: day)!
            let volume = allBags
                .filter { $0.pumpDate >= day && $0.pumpDate < nextDay }
                .reduce(0) { $0 + $1.volumeMl }
            return DailyVolume(date: day, volume: volume)
        }
    }

    func weeklyVolumes(weeks: Int) -> [DailyVolume] {
        let calendar = Calendar.current
        let today = calendar.startOfDay(for: .now)

        return (0..<weeks).reversed().map { offset in
            let weekStart = calendar.date(byAdding: .weekOfYear, value: -offset, to: today)!
            let weekEnd = calendar.date(byAdding: .weekOfYear, value: 1, to: weekStart)!
            let volume = allBags
                .filter { $0.pumpDate >= weekStart && $0.pumpDate < weekEnd }
                .reduce(0) { $0 + $1.volumeMl }
            return DailyVolume(date: weekStart, volume: volume)
        }
    }

    func averageDailyVolume(days: Int) -> Double {
        guard days > 0 else { return 0 }
        let total = dailyVolumes(days: days).reduce(0) { $0 + $1.volume }
        return Double(total) / Double(days)
    }

    func monthlyTotal(monthsAgo: Int) -> Int {
        let calendar = Calendar.current
        let now = Date.now
        guard let monthStart = calendar.date(byAdding: .month, value: -monthsAgo, to: calendar.startOfDay(for: now)),
              let monthEnd = calendar.date(byAdding: .month, value: 1, to: monthStart) else { return 0 }

        let start = calendar.date(from: calendar.dateComponents([.year, .month], from: monthStart))!
        let end = calendar.date(from: calendar.dateComponents([.year, .month], from: monthEnd))!

        return allBags
            .filter { $0.pumpDate >= start && $0.pumpDate < end }
            .reduce(0) { $0 + $1.volumeMl }
    }

    var hasLactationDrop: Bool {
        let avg7 = averageDailyVolume(days: 7)
        let avg30 = averageDailyVolume(days: 30)
        guard avg30 > 0 else { return false }
        return avg7 < avg30 * 0.80
    }

    var lactationTrendPercent: Double {
        let avg7 = averageDailyVolume(days: 7)
        let avg30 = averageDailyVolume(days: 30)
        guard avg30 > 0 else { return 0 }
        return ((avg7 - avg30) / avg30) * 100
    }
}
```

**Step 5: Create SettingsViewModel**

```swift
// PetitesGouttes/ViewModels/SettingsViewModel.swift
import SwiftUI

@Observable
@MainActor
final class SettingsViewModel {
    @ObservationIgnored
    @AppStorage("lowStockThresholdMl") var lowStockThresholdMl: Int = 1500

    @ObservationIgnored
    @AppStorage("dailyConsumptionMl") var dailyConsumptionMl: Int = 300

    @ObservationIgnored
    @AppStorage("daycareDaysPerWeek") var daycareDaysPerWeek: Int = 5
}
```

**Step 6: Build to verify**

```bash
cd /Users/benjamin/Documents/petites-gouttes/ios
xcodegen generate && xcodebuild -project PetitesGouttes.xcodeproj -scheme PetitesGouttes -destination 'platform=iOS Simulator,name=iPhone 16' build 2>&1 | tail -5
```

**Step 7: Commit**

```bash
git add ios/PetitesGouttes/ViewModels/
git commit -m "feat(ios): add all ViewModels (Dashboard, Freezer, History, Stats, Settings)"
```

---

### Task 5: Reusable Components

**Files:**
- Create: `ios/PetitesGouttes/Views/Components/MilkBagCard.swift`
- Create: `ios/PetitesGouttes/Views/Components/AlertBanner.swift`

**Step 1: Create MilkBagCard**

```swift
// PetitesGouttes/Views/Components/MilkBagCard.swift
import SwiftUI

struct MilkBagCard: View {
    let bag: MilkBag
    var showActions: Bool = true
    var onEdit: (() -> Void)?
    var onRemove: (() -> Void)?
    var onRestore: (() -> Void)?
    var onDelete: (() -> Void)?

    @State private var showDeleteConfirmation = false

    private var dateFormatter: DateFormatter {
        let f = DateFormatter()
        f.dateFormat = "dd/MM/yyyy"
        return f
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack(alignment: .top) {
                VStack(alignment: .leading, spacing: 4) {
                    Text("\(bag.volumeMl) ml")
                        .font(.system(.title2, design: .rounded, weight: .bold))

                    Text("Pompe le \(dateFormatter.string(from: bag.pumpDate))")
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                }

                Spacer()

                dlcBadge
            }

            HStack {
                Image(systemName: "calendar.badge.clock")
                    .foregroundStyle(.secondary)
                Text("DLC : \(dateFormatter.string(from: bag.expiryDate))")
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }

            if let removalDate = bag.removalDate {
                HStack {
                    Image(systemName: "arrow.uturn.right")
                        .foregroundStyle(.secondary)
                    Text("Retire le \(dateFormatter.string(from: removalDate))")
                        .font(.caption)
                        .foregroundStyle(.secondary)
                }
            }

            if showActions {
                Divider()
                actionButtons
            }
        }
        .cardStyle()
        .confirmationDialog("Supprimer ce sachet ?", isPresented: $showDeleteConfirmation) {
            Button("Supprimer", role: .destructive) { onDelete?() }
        } message: {
            Text("Cette action est irreversible.")
        }
    }

    private var dlcBadge: some View {
        let status = bag.expiryStatus
        let days = bag.daysUntilExpiry
        let text = days <= 0 ? "Expire" : "\(days)j"

        return Text(text)
            .font(.system(.caption, design: .rounded, weight: .semibold))
            .padding(.horizontal, 8)
            .padding(.vertical, 4)
            .background(status.color.opacity(0.15), in: Capsule())
            .foregroundStyle(status.color)
    }

    @ViewBuilder
    private var actionButtons: some View {
        HStack(spacing: 16) {
            if let onEdit {
                Button { onEdit() } label: {
                    Label("Modifier", systemImage: "pencil")
                        .font(.caption)
                }
            }

            if let onRemove {
                Button { onRemove() } label: {
                    Label("Retirer", systemImage: "fork.knife")
                        .font(.caption)
                }
                .tint(PGTheme.dlcWarning)
            }

            if let onRestore {
                Button { onRestore() } label: {
                    Label("Restaurer", systemImage: "arrow.uturn.backward")
                        .font(.caption)
                }
                .tint(PGTheme.primary)
            }

            Spacer()

            if onDelete != nil {
                Button { showDeleteConfirmation = true } label: {
                    Label("Supprimer", systemImage: "trash")
                        .font(.caption)
                }
                .tint(.red)
            }
        }
    }
}
```

**Step 2: Create AlertBanner**

```swift
// PetitesGouttes/Views/Components/AlertBanner.swift
import SwiftUI

struct AlertBanner: View {
    let icon: String
    let message: String
    let style: Style

    enum Style {
        case warning, danger

        var backgroundColor: Color {
            switch self {
            case .warning: PGTheme.alertWarningBg
            case .danger: PGTheme.alertDangerBg
            }
        }

        var textColor: Color {
            switch self {
            case .warning: PGTheme.alertWarningText
            case .danger: PGTheme.alertDangerText
            }
        }
    }

    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: icon)
                .font(.title3)
            Text(message)
                .font(.subheadline)
        }
        .foregroundStyle(style.textColor)
        .padding()
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(style.backgroundColor, in: .rect(cornerRadius: 12))
    }
}
```

**Step 3: Build to verify**

```bash
cd /Users/benjamin/Documents/petites-gouttes/ios
xcodegen generate && xcodebuild -project PetitesGouttes.xcodeproj -scheme PetitesGouttes -destination 'platform=iOS Simulator,name=iPhone 16' build 2>&1 | tail -5
```

**Step 4: Commit**

```bash
git add ios/PetitesGouttes/Views/Components/
git commit -m "feat(ios): add MilkBagCard and AlertBanner components"
```

---

### Task 6: Dashboard Screen

**Files:**
- Create: `ios/PetitesGouttes/Views/Dashboard/DashboardView.swift`

**Step 1: Create DashboardView**

```swift
// PetitesGouttes/Views/Dashboard/DashboardView.swift
import SwiftUI
import SwiftData

struct DashboardView: View {
    @Environment(\.modelContext) private var modelContext
    @State private var vm: DashboardViewModel?
    @State private var showAddSheet = false

    @AppStorage("lowStockThresholdMl") private var lowStockThreshold = 1500
    @AppStorage("dailyConsumptionMl") private var dailyConsumption = 300

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: 16) {
                    if let vm {
                        // Alerts
                        if vm.isLowStock(threshold: lowStockThreshold) {
                            AlertBanner(
                                icon: "exclamationmark.triangle.fill",
                                message: "Stock bas ! Moins de \(lowStockThreshold) ml disponibles.",
                                style: .warning
                            )
                        }

                        if !vm.bagsExpiringSoon.isEmpty {
                            AlertBanner(
                                icon: "clock.badge.exclamationmark.fill",
                                message: "\(vm.bagsExpiringSoon.count) sachet(s) proche(s) de la DLC.",
                                style: .danger
                            )
                        }

                        // Summary cards
                        HStack(spacing: 12) {
                            summaryCard(
                                icon: "bag.fill",
                                value: "\(vm.activeBagCount)",
                                label: "Sachets"
                            )
                            summaryCard(
                                icon: "drop.fill",
                                value: "\(vm.totalActiveVolume) ml",
                                label: "Volume total"
                            )
                        }

                        // Stock days
                        let days = vm.stockDays(dailyConsumption: dailyConsumption)
                        summaryCard(
                            icon: "calendar",
                            value: "\(days) jour\(days > 1 ? "s" : "")",
                            label: "Stock restant"
                        )

                        // Next bag FIFO
                        if let next = vm.nextBagToUse {
                            VStack(alignment: .leading, spacing: 8) {
                                Text("Prochain sachet a utiliser")
                                    .font(.headline)
                                MilkBagCard(bag: next, showActions: false)
                            }
                        }

                        if vm.activeBagCount == 0 {
                            ContentUnavailableView(
                                "Aucun sachet",
                                systemImage: "snowflake",
                                description: Text("Ajoutez votre premier sachet de lait.")
                            )
                        }
                    }
                }
                .padding()
            }
            .navigationTitle("Tableau de bord")
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button { showAddSheet = true } label: {
                        Image(systemName: "plus")
                    }
                }
            }
            .sheet(isPresented: $showAddSheet) {
                AddEditBagView { vm?.fetchBags() }
            }
            .task {
                vm = DashboardViewModel(modelContext: modelContext)
            }
        }
    }

    private func summaryCard(icon: String, value: String, label: String) -> some View {
        VStack(spacing: 6) {
            Image(systemName: icon)
                .font(.title3)
                .foregroundStyle(PGTheme.primary)
            Text(value)
                .font(.system(.headline, design: .rounded, weight: .bold))
            Text(label)
                .font(.caption)
                .foregroundStyle(.secondary)
        }
        .frame(maxWidth: .infinity)
        .cardStyle()
    }
}
```

**Step 2: Create placeholder AddEditBagView** (will be fully implemented in Task 9)

```swift
// PetitesGouttes/Views/AddEditBag/AddEditBagView.swift
import SwiftUI
import SwiftData

struct AddEditBagView: View {
    var bagToEdit: MilkBag?
    var onSave: (() -> Void)?

    @Environment(\.modelContext) private var modelContext
    @Environment(\.dismiss) private var dismiss

    @State private var volumeText = ""
    @State private var pumpDate = Date.now

    var body: some View {
        NavigationStack {
            Text("Placeholder — Task 9")
                .navigationTitle(bagToEdit == nil ? "Ajouter un sachet" : "Modifier le sachet")
                .toolbar {
                    ToolbarItem(placement: .cancellationAction) {
                        Button("Annuler") { dismiss() }
                    }
                }
        }
    }
}
```

**Step 3: Build to verify**

```bash
cd /Users/benjamin/Documents/petites-gouttes/ios
xcodegen generate && xcodebuild -project PetitesGouttes.xcodeproj -scheme PetitesGouttes -destination 'platform=iOS Simulator,name=iPhone 16' build 2>&1 | tail -5
```

**Step 4: Commit**

```bash
git add ios/PetitesGouttes/Views/Dashboard/ ios/PetitesGouttes/Views/AddEditBag/
git commit -m "feat(ios): add Dashboard screen with summary cards and alerts"
```

---

### Task 7: Freezer List Screen

**Files:**
- Create: `ios/PetitesGouttes/Views/Freezer/FreezerListView.swift`

**Step 1: Create FreezerListView**

```swift
// PetitesGouttes/Views/Freezer/FreezerListView.swift
import SwiftUI
import SwiftData

struct FreezerListView: View {
    @Environment(\.modelContext) private var modelContext
    @State private var vm: FreezerViewModel?
    @State private var showAddSheet = false
    @State private var bagToEdit: MilkBag?

    var body: some View {
        NavigationStack {
            Group {
                if let vm {
                    if vm.activeBags.isEmpty {
                        ContentUnavailableView(
                            "Congelateur vide",
                            systemImage: "snowflake",
                            description: Text("Ajoutez votre premier sachet.")
                        )
                    } else {
                        ScrollView {
                            LazyVStack(spacing: 12) {
                                ForEach(vm.activeBags) { bag in
                                    MilkBagCard(
                                        bag: bag,
                                        onEdit: { bagToEdit = bag },
                                        onRemove: { vm.removeBag(bag) },
                                        onDelete: { vm.deleteBag(bag) }
                                    )
                                }
                            }
                            .padding()
                        }
                    }
                }
            }
            .navigationTitle("Congelateur")
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    if let vm {
                        Menu {
                            ForEach(SortOrder.allCases, id: \.self) { order in
                                Button {
                                    vm.setSortOrder(order)
                                } label: {
                                    HStack {
                                        Text(order.rawValue)
                                        if vm.sortOrder == order {
                                            Image(systemName: "checkmark")
                                        }
                                    }
                                }
                            }
                        } label: {
                            Image(systemName: "arrow.up.arrow.down")
                        }
                    }
                }
                ToolbarItem(placement: .topBarTrailing) {
                    Button { showAddSheet = true } label: {
                        Image(systemName: "plus")
                    }
                }
            }
            .sheet(isPresented: $showAddSheet) {
                AddEditBagView { vm?.fetchBags() }
            }
            .sheet(item: $bagToEdit) { bag in
                AddEditBagView(bagToEdit: bag) { vm?.fetchBags() }
            }
            .task {
                vm = FreezerViewModel(modelContext: modelContext)
            }
        }
    }
}
```

**Step 2: Build and commit**

```bash
cd /Users/benjamin/Documents/petites-gouttes/ios
xcodegen generate && xcodebuild -project PetitesGouttes.xcodeproj -scheme PetitesGouttes -destination 'platform=iOS Simulator,name=iPhone 16' build 2>&1 | tail -5
git add ios/PetitesGouttes/Views/Freezer/
git commit -m "feat(ios): add Freezer list screen with sort and actions"
```

---

### Task 8: History & Stats Screens

**Files:**
- Create: `ios/PetitesGouttes/Views/History/HistoryView.swift`
- Create: `ios/PetitesGouttes/Views/Stats/StatsView.swift`

**Step 1: Create HistoryView**

```swift
// PetitesGouttes/Views/History/HistoryView.swift
import SwiftUI
import SwiftData

struct HistoryView: View {
    @Environment(\.modelContext) private var modelContext
    @State private var vm: HistoryViewModel?

    var body: some View {
        NavigationStack {
            Group {
                if let vm {
                    if vm.removedBags.isEmpty {
                        ContentUnavailableView(
                            "Aucun historique",
                            systemImage: "clock",
                            description: Text("Les sachets retires apparaitront ici.")
                        )
                    } else {
                        ScrollView {
                            LazyVStack(spacing: 12) {
                                Text("\(vm.removedBags.count) sachet(s) retire(s)")
                                    .font(.subheadline)
                                    .foregroundStyle(.secondary)

                                ForEach(vm.removedBags) { bag in
                                    MilkBagCard(
                                        bag: bag,
                                        onRestore: { vm.restoreBag(bag) },
                                        onDelete: { vm.deleteBag(bag) }
                                    )
                                }
                            }
                            .padding()
                        }
                    }
                }
            }
            .navigationTitle("Historique")
            .task {
                vm = HistoryViewModel(modelContext: modelContext)
            }
        }
    }
}
```

**Step 2: Create StatsView**

```swift
// PetitesGouttes/Views/Stats/StatsView.swift
import SwiftUI
import SwiftData
import Charts

struct StatsView: View {
    @Environment(\.modelContext) private var modelContext
    @State private var vm: StatsViewModel?

    var body: some View {
        NavigationStack {
            ScrollView {
                if let vm {
                    VStack(spacing: 16) {
                        // Lactation drop alert
                        if vm.hasLactationDrop {
                            AlertBanner(
                                icon: "arrow.down.right",
                                message: "Baisse de lactation detectee (moyenne 7j < 80% de la moyenne 30j).",
                                style: .warning
                            )
                        }

                        // Metric cards 2x2
                        LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible())], spacing: 12) {
                            let avg7 = vm.averageDailyVolume(days: 7)
                            let trend = vm.lactationTrendPercent

                            metricCard(
                                title: "Moyenne 7j",
                                value: "\(Int(avg7))",
                                unit: "ml/j",
                                detail: String(format: "%+.0f%%", trend),
                                detailColor: trend >= 0 ? PGTheme.dlcSafe : PGTheme.dlcUrgent
                            )

                            metricCard(
                                title: "Moyenne 30j",
                                value: "\(Int(vm.averageDailyVolume(days: 30)))",
                                unit: "ml/j"
                            )

                            metricCard(
                                title: "Ce mois",
                                value: "\(vm.monthlyTotal(monthsAgo: 0))",
                                unit: "ml"
                            )

                            metricCard(
                                title: "Mois dernier",
                                value: "\(vm.monthlyTotal(monthsAgo: 1))",
                                unit: "ml"
                            )
                        }

                        // Daily chart
                        chartSection(title: "Volume quotidien (30 derniers jours)", data: vm.dailyVolumes(days: 30))

                        // Weekly chart
                        chartSection(title: "Volume hebdomadaire (12 semaines)", data: vm.weeklyVolumes(weeks: 12))
                    }
                    .padding()
                }
            }
            .navigationTitle("Statistiques")
            .task {
                vm = StatsViewModel(modelContext: modelContext)
            }
        }
    }

    private func metricCard(title: String, value: String, unit: String, detail: String? = nil, detailColor: Color = .secondary) -> some View {
        VStack(spacing: 6) {
            HStack(alignment: .firstTextBaseline, spacing: 2) {
                Text(value)
                    .font(.system(.headline, design: .rounded, weight: .bold))
                Text(unit)
                    .font(.caption2)
                    .foregroundStyle(.secondary)
            }

            if let detail {
                Text(detail)
                    .font(.system(.caption, design: .rounded, weight: .semibold))
                    .foregroundStyle(detailColor)
            }

            Text(title)
                .font(.caption)
                .foregroundStyle(.secondary)
        }
        .frame(maxWidth: .infinity)
        .cardStyle()
    }

    private func chartSection(title: String, data: [StatsViewModel.DailyVolume]) -> some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(title)
                .font(.headline)

            Chart(data) { item in
                BarMark(
                    x: .value("Date", item.date, unit: .day),
                    y: .value("Volume", item.volume)
                )
                .foregroundStyle(PGTheme.primary.gradient)
                .cornerRadius(4)
            }
            .chartYAxisLabel("ml")
            .frame(height: 200)
            .cardStyle()
        }
    }
}
```

**Step 3: Build and commit**

```bash
cd /Users/benjamin/Documents/petites-gouttes/ios
xcodegen generate && xcodebuild -project PetitesGouttes.xcodeproj -scheme PetitesGouttes -destination 'platform=iOS Simulator,name=iPhone 16' build 2>&1 | tail -5
git add ios/PetitesGouttes/Views/History/ ios/PetitesGouttes/Views/Stats/
git commit -m "feat(ios): add History and Stats screens with Swift Charts"
```

---

### Task 9: AddEditBag & Settings Screens

**Files:**
- Modify: `ios/PetitesGouttes/Views/AddEditBag/AddEditBagView.swift`
- Create: `ios/PetitesGouttes/Views/Settings/SettingsView.swift`

**Step 1: Implement full AddEditBagView**

Replace the placeholder with:

```swift
// PetitesGouttes/Views/AddEditBag/AddEditBagView.swift
import SwiftUI
import SwiftData

struct AddEditBagView: View {
    var bagToEdit: MilkBag?
    var onSave: (() -> Void)?

    @Environment(\.modelContext) private var modelContext
    @Environment(\.dismiss) private var dismiss

    @State private var volumeText = ""
    @State private var pumpDate = Date.now
    @State private var showVolumeError = false

    private var isEditing: Bool { bagToEdit != nil }

    private var expiryDate: Date {
        Calendar.current.date(byAdding: .month, value: 4, to: pumpDate) ?? pumpDate
    }

    private var dateFormatter: DateFormatter {
        let f = DateFormatter()
        f.dateFormat = "dd/MM/yyyy"
        return f
    }

    var body: some View {
        NavigationStack {
            Form {
                Section("Volume") {
                    HStack {
                        TextField("Volume en ml", text: $volumeText)
                            .keyboardType(.numberPad)
                        Text("ml")
                            .foregroundStyle(.secondary)
                    }
                    if showVolumeError {
                        Text("Le volume doit etre superieur a 0")
                            .font(.caption)
                            .foregroundStyle(.red)
                    }
                }

                Section("Date de pompage") {
                    DatePicker(
                        "Date",
                        selection: $pumpDate,
                        in: ...Date.now,
                        displayedComponents: .date
                    )
                    .environment(\.locale, Locale(identifier: "fr_FR"))
                }

                Section("Date limite de consommation") {
                    HStack {
                        Image(systemName: "calendar.badge.clock")
                            .foregroundStyle(.secondary)
                        Text(dateFormatter.string(from: expiryDate))
                    }
                }
            }
            .navigationTitle(isEditing ? "Modifier le sachet" : "Ajouter un sachet")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Annuler") { dismiss() }
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button(isEditing ? "Modifier" : "Ajouter") {
                        save()
                    }
                }
            }
            .onAppear {
                if let bag = bagToEdit {
                    volumeText = "\(bag.volumeMl)"
                    pumpDate = bag.pumpDate
                }
            }
        }
    }

    private func save() {
        guard let volume = Int(volumeText), volume > 0 else {
            showVolumeError = true
            return
        }

        if let bag = bagToEdit {
            bag.volumeMl = volume
            bag.pumpDate = pumpDate
            bag.expiryDate = expiryDate
        } else {
            let bag = MilkBag(volumeMl: volume, pumpDate: pumpDate)
            modelContext.insert(bag)
        }

        try? modelContext.save()
        onSave?()
        dismiss()
    }
}
```

**Step 2: Create SettingsView**

```swift
// PetitesGouttes/Views/Settings/SettingsView.swift
import SwiftUI

struct SettingsView: View {
    @AppStorage("lowStockThresholdMl") private var lowStockThreshold = 1500
    @AppStorage("dailyConsumptionMl") private var dailyConsumption = 300
    @AppStorage("daycareDaysPerWeek") private var daycareDays = 5

    var body: some View {
        NavigationStack {
            Form {
                Section("Alertes & Consommation") {
                    HStack {
                        Text("Seuil stock bas")
                        Spacer()
                        TextField("ml", value: $lowStockThreshold, format: .number)
                            .keyboardType(.numberPad)
                            .multilineTextAlignment(.trailing)
                            .frame(width: 80)
                        Text("ml")
                            .foregroundStyle(.secondary)
                    }

                    HStack {
                        Text("Consommation quotidienne")
                        Spacer()
                        TextField("ml", value: $dailyConsumption, format: .number)
                            .keyboardType(.numberPad)
                            .multilineTextAlignment(.trailing)
                            .frame(width: 80)
                        Text("ml")
                            .foregroundStyle(.secondary)
                    }

                    Stepper("Jours de garde : \(daycareDays)", value: $daycareDays, in: 1...7)
                }

                Section("A propos") {
                    HStack {
                        Text("Version")
                        Spacer()
                        Text("Petites Gouttes v1.0.0")
                            .foregroundStyle(.secondary)
                    }

                    HStack {
                        Image(systemName: "lock.shield")
                            .foregroundStyle(PGTheme.primary)
                        Text("Toutes vos donnees sont stockees localement sur votre appareil.")
                            .font(.caption)
                            .foregroundStyle(.secondary)
                    }
                }
            }
            .navigationTitle("Reglages")
        }
    }
}
```

**Step 3: Build and commit**

```bash
cd /Users/benjamin/Documents/petites-gouttes/ios
xcodegen generate && xcodebuild -project PetitesGouttes.xcodeproj -scheme PetitesGouttes -destination 'platform=iOS Simulator,name=iPhone 16' build 2>&1 | tail -5
git add ios/PetitesGouttes/Views/AddEditBag/ ios/PetitesGouttes/Views/Settings/
git commit -m "feat(ios): add AddEditBag form and Settings screen"
```

---

### Task 10: Navigation & App Entry Point

**Files:**
- Create: `ios/PetitesGouttes/Navigation/ContentView.swift`
- Modify: `ios/PetitesGouttes/App/PetitesGouttesApp.swift`

**Step 1: Create ContentView with TabView**

```swift
// PetitesGouttes/Navigation/ContentView.swift
import SwiftUI

struct ContentView: View {
    var body: some View {
        TabView {
            DashboardView()
                .tabItem {
                    Label("Tableau de bord", systemImage: "house.fill")
                }

            FreezerListView()
                .tabItem {
                    Label("Congelateur", systemImage: "snowflake")
                }

            HistoryView()
                .tabItem {
                    Label("Historique", systemImage: "clock.fill")
                }

            StatsView()
                .tabItem {
                    Label("Statistiques", systemImage: "chart.bar.fill")
                }
        }
        .tint(PGTheme.primary)
    }
}
```

**Step 2: Update App entry point**

```swift
// PetitesGouttes/App/PetitesGouttesApp.swift
import SwiftUI
import SwiftData

@main
struct PetitesGouttesApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
        .modelContainer(for: MilkBag.self)
    }
}
```

**Step 3: Build and run on simulator**

```bash
cd /Users/benjamin/Documents/petites-gouttes/ios
xcodegen generate && xcodebuild -project PetitesGouttes.xcodeproj -scheme PetitesGouttes -destination 'platform=iOS Simulator,name=iPhone 16' build 2>&1 | tail -5
```

**Step 4: Commit**

```bash
git add ios/PetitesGouttes/Navigation/ ios/PetitesGouttes/App/
git commit -m "feat(ios): add TabView navigation and finalize app entry point"
```

---

### Task 11: Settings Access via Toolbar

The Settings screen needs to be accessible from any tab via a toolbar icon, not as a tab. Update ContentView:

**Step 1: Move Settings to toolbar**

```swift
// PetitesGouttes/Navigation/ContentView.swift
import SwiftUI

struct ContentView: View {
    @State private var showSettings = false

    var body: some View {
        TabView {
            DashboardView()
                .tabItem {
                    Label("Tableau de bord", systemImage: "house.fill")
                }

            FreezerListView()
                .tabItem {
                    Label("Congelateur", systemImage: "snowflake")
                }

            HistoryView()
                .tabItem {
                    Label("Historique", systemImage: "clock.fill")
                }

            StatsView()
                .tabItem {
                    Label("Statistiques", systemImage: "chart.bar.fill")
                }
        }
        .tint(PGTheme.primary)
        .sheet(isPresented: $showSettings) {
            SettingsView()
        }
    }
}
```

Note: Each screen's NavigationStack toolbar should include a settings gear button that sets `showSettings`. Since each screen manages its own NavigationStack, add a settings button to the toolbar of each screen. The simplest approach is a shared ViewModifier or pass the binding. For cleanliness, use an environment-based approach or just add the gear icon to each screen's toolbar individually.

Alternative simpler approach: add a settings gear button to each screen's toolbar:

In DashboardView, FreezerListView, HistoryView, StatsView — add to toolbar:
```swift
ToolbarItem(placement: .topBarTrailing) {
    Button { showSettings = true } label: {
        Image(systemName: "gearshape")
    }
}
```

And add `@State private var showSettings = false` and `.sheet(isPresented: $showSettings) { SettingsView() }` to each.

**Step 2: Build and commit**

```bash
cd /Users/benjamin/Documents/petites-gouttes/ios
xcodegen generate && xcodebuild -project PetitesGouttes.xcodeproj -scheme PetitesGouttes -destination 'platform=iOS Simulator,name=iPhone 16' build 2>&1 | tail -5
git add ios/PetitesGouttes/
git commit -m "feat(ios): add Settings access via toolbar gear icon on all screens"
```

---

### Task 12: Screenshot Data & Fastlane Snapshot Tests

**Files:**
- Create: `ios/PetitesGouttes/App/ScreenshotDataService.swift`
- Modify: `ios/PetitesGouttesUITests/SnapshotTests.swift`

**Step 1: Create screenshot data service**

```swift
// PetitesGouttes/App/ScreenshotDataService.swift
import SwiftData
import Foundation

enum ScreenshotDataService {
    static var isScreenshotMode: Bool {
        ProcessInfo.processInfo.arguments.contains("-SCREENSHOT_MODE")
    }

    @MainActor
    static func seedData(modelContext: ModelContext) {
        let calendar = Calendar.current
        let today = Date.now

        // Create sample bags spread over the last 2 months
        let samples: [(Int, Int)] = [
            // (volumeMl, daysAgo)
            (150, 90), (120, 85), (180, 80), (130, 75),
            (160, 70), (140, 65), (170, 60), (110, 55),
            (150, 50), (130, 45), (160, 40), (120, 35),
            (180, 30), (140, 25), (150, 20), (170, 15),
            (130, 10), (160, 7), (140, 5), (120, 3),
            (150, 2), (180, 1),
        ]

        for (volume, daysAgo) in samples {
            let pumpDate = calendar.date(byAdding: .day, value: -daysAgo, to: today)!
            let bag = MilkBag(volumeMl: volume, pumpDate: pumpDate)
            modelContext.insert(bag)
        }

        // Mark a few as removed (for history)
        let removed: [(Int, Int, Int)] = [
            // (volumeMl, daysAgoPumped, daysAgoRemoved)
            (150, 95, 60), (120, 88, 55), (140, 82, 50),
        ]

        for (volume, pumpDaysAgo, removeDaysAgo) in removed {
            let pumpDate = calendar.date(byAdding: .day, value: -pumpDaysAgo, to: today)!
            let bag = MilkBag(volumeMl: volume, pumpDate: pumpDate)
            bag.removedFromFreezer = true
            bag.removalDate = calendar.date(byAdding: .day, value: -removeDaysAgo, to: today)
            modelContext.insert(bag)
        }

        try? modelContext.save()
    }
}
```

**Step 2: Update App entry point for screenshot mode**

```swift
// PetitesGouttes/App/PetitesGouttesApp.swift
import SwiftUI
import SwiftData

@main
struct PetitesGouttesApp: App {
    let modelContainer: ModelContainer

    init() {
        let schema = Schema([MilkBag.self])
        if ScreenshotDataService.isScreenshotMode {
            let config = ModelConfiguration(isStoredInMemoryOnly: true)
            modelContainer = try! ModelContainer(for: schema, configurations: config)
        } else {
            modelContainer = try! ModelContainer(for: schema)
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .task {
                    if ScreenshotDataService.isScreenshotMode {
                        await ScreenshotDataService.seedData(modelContext: modelContainer.mainContext)
                    }
                }
        }
        .modelContainer(modelContainer)
    }
}
```

**Step 3: Implement snapshot tests**

```swift
// PetitesGouttesUITests/SnapshotTests.swift
import XCTest

@MainActor
final class SnapshotTests: XCTestCase {
    var app: XCUIApplication!

    override func setUpWithError() throws {
        continueAfterFailure = false
        app = XCUIApplication()
        setupSnapshot(app)
        app.launchArguments += ["-SCREENSHOT_MODE"]
    }

    func testScreenshot01_Dashboard() {
        app.launch()
        snapshot("01_Dashboard")
    }

    func testScreenshot02_Freezer() {
        app.launch()
        navigateToTab(index: 1)
        snapshot("02_Congelateur")
    }

    func testScreenshot03_Stats() {
        app.launch()
        navigateToTab(index: 3)
        snapshot("03_Statistiques")
    }

    func testScreenshot04_AddBag() {
        app.launch()
        // Tap the + button on Dashboard
        app.navigationBars.buttons.matching(identifier: "plus").firstMatch.tap()
        sleep(1)
        snapshot("04_AjouterSachet")
    }

    func testScreenshot05_Settings() {
        app.launch()
        // Tap the gear button
        app.navigationBars.buttons.matching(identifier: "gearshape").firstMatch.tap()
        sleep(1)
        snapshot("05_Reglages")
    }

    private func navigateToTab(index: Int) {
        let tabBar = app.tabBars.firstMatch
        let buttons = tabBar.buttons
        if buttons.count > index {
            buttons.element(boundBy: index).tap()
        }
        sleep(1)
    }
}
```

**Step 4: Build and commit**

```bash
cd /Users/benjamin/Documents/petites-gouttes/ios
xcodegen generate && xcodebuild -project PetitesGouttes.xcodeproj -scheme PetitesGouttes -destination 'platform=iOS Simulator,name=iPhone 16' build 2>&1 | tail -5
git add ios/PetitesGouttes/App/ ios/PetitesGouttesUITests/
git commit -m "feat(ios): add screenshot data service and Fastlane snapshot tests"
```

---

### Task 13: Fastlane Configuration

**Files:**
- Create: `ios/fastlane/Appfile`
- Create: `ios/fastlane/Matchfile`
- Create: `ios/fastlane/Fastfile`
- Create: `ios/fastlane/Snapfile`
- Create: `ios/fastlane/metadata/fr-FR/name.txt`
- Create: `ios/fastlane/metadata/fr-FR/subtitle.txt`
- Create: `ios/fastlane/metadata/fr-FR/keywords.txt`
- Create: `ios/fastlane/metadata/fr-FR/description.txt`
- Create: `ios/fastlane/metadata/fr-FR/release_notes.txt`
- Create: `ios/fastlane/metadata/fr-FR/promotional_text.txt`
- Create: `ios/fastlane/metadata/fr-FR/privacy_url.txt`

**Step 1: Create Appfile**

```ruby
app_identifier("com.bnjdpn.petitesgouttes")
team_id("767SX34A7Z")

for_platform :ios do
  for_lane :release do
    app_identifier("com.bnjdpn.petitesgouttes")
  end
end
```

**Step 2: Create Matchfile**

```ruby
git_url("https://github.com/bnjdpn/certificates.git")
git_branch("certificates")

storage_mode("git")
type("appstore")

app_identifier(["com.bnjdpn.petitesgouttes"])
```

**Step 3: Create Fastfile**

```ruby
default_platform(:ios)

platform :ios do
  desc "Run tests"
  lane :test do
    run_tests(
      project: "PetitesGouttes.xcodeproj",
      scheme: "PetitesGouttes",
      devices: ["iPhone 16"],
      code_coverage: true
    )
  end

  desc "Capture screenshots"
  lane :screenshots do
    capture_screenshots
  end

  desc "Upload metadata only"
  lane :metadata do
    deliver(
      skip_binary_upload: true,
      skip_screenshots: true,
      force: true,
      api_key_path: "fastlane/asc_api_key.json"
    )
  end

  desc "Upload screenshots only"
  lane :upload_screenshots do
    deliver(
      skip_binary_upload: true,
      skip_metadata: true,
      overwrite_screenshots: true,
      force: true,
      api_key_path: "fastlane/asc_api_key.json"
    )
  end

  desc "Deploy to TestFlight"
  lane :beta do
    increment_build_number(
      build_number: latest_testflight_build_number(
        api_key_path: "fastlane/asc_api_key.json"
      ) + 1,
      xcodeproj: "PetitesGouttes.xcodeproj"
    )
    match(type: "appstore")
    build_app(
      project: "PetitesGouttes.xcodeproj",
      scheme: "PetitesGouttes"
    )
    upload_to_testflight(
      api_key_path: "fastlane/asc_api_key.json"
    )
  end

  desc "Deploy to App Store (full: screenshots + build + metadata)"
  lane :release do
    increment_build_number(
      build_number: latest_testflight_build_number(
        api_key_path: "fastlane/asc_api_key.json"
      ) + 1,
      xcodeproj: "PetitesGouttes.xcodeproj"
    )
    capture_screenshots
    match(type: "appstore")
    build_app(
      project: "PetitesGouttes.xcodeproj",
      scheme: "PetitesGouttes"
    )
    upload_to_app_store(
      api_key_path: "fastlane/asc_api_key.json",
      force: true,
      overwrite_screenshots: true
    )
  end

  desc "Deploy to App Store (quick: build + metadata, no screenshots)"
  lane :release_quick do
    increment_build_number(
      build_number: latest_testflight_build_number(
        api_key_path: "fastlane/asc_api_key.json"
      ) + 1,
      xcodeproj: "PetitesGouttes.xcodeproj"
    )
    match(type: "appstore")
    build_app(
      project: "PetitesGouttes.xcodeproj",
      scheme: "PetitesGouttes"
    )
    upload_to_app_store(
      api_key_path: "fastlane/asc_api_key.json",
      skip_screenshots: true,
      force: true
    )
  end
end
```

**Step 4: Create Snapfile**

```ruby
devices([
  "iPhone 15 Pro Max"
])

languages([
  "fr-FR"
])

output_directory("./app_store_screenshots")
clear_previous_screenshots(true)
override_status_bar(false)
```

**Step 5: Create metadata files**

`ios/fastlane/metadata/fr-FR/name.txt`:
```
Petites Gouttes
```

`ios/fastlane/metadata/fr-FR/subtitle.txt`:
```
Gerez votre stock de lait maternel
```

`ios/fastlane/metadata/fr-FR/keywords.txt`:
```
lait maternel,congelateur,stock,allaitement,bebe,sachets,DLC,tire-lait,lactation,suivi
```

`ios/fastlane/metadata/fr-FR/description.txt`:
```
Gerez simplement votre stock de lait maternel congele.

TABLEAU DE BORD
Vue d'ensemble de votre stock en un coup d'oeil : nombre de sachets, volume total et jours de stock restants. Recevez des alertes quand le stock est bas ou qu'un sachet approche de sa date limite de consommation.

CONGELATEUR
Consultez et gerez tous vos sachets congeles. Triez par date ou par volume, retirez un sachet pour l'utiliser ou supprimez-le. Le principe FIFO (premier entre, premier sorti) vous indique toujours quel sachet utiliser en priorite.

STATISTIQUES
Suivez l'evolution de votre production avec des graphiques clairs. Comparez vos moyennes sur 7 et 30 jours, visualisez vos volumes quotidiens et hebdomadaires. Une alerte vous previent en cas de baisse significative de lactation.

HISTORIQUE
Retrouvez tous les sachets que vous avez utilises. Restaurez un sachet dans le congelateur si necessaire.

REGLAGES PERSONNALISES
Adaptez l'application a vos besoins : seuil d'alerte de stock bas, consommation quotidienne estimee et nombre de jours de garde par semaine.

VIE PRIVEE
Toutes vos donnees sont stockees localement sur votre appareil. Aucun compte, aucun serveur, aucune collecte de donnees.
```

`ios/fastlane/metadata/fr-FR/release_notes.txt`:
```
Premiere version de Petites Gouttes pour iOS !

- Tableau de bord avec alertes de stock et DLC
- Gestion du congelateur avec tri et actions rapides
- Statistiques de production avec graphiques
- Historique des sachets utilises
- Reglages personnalises
- Toutes les donnees restent sur votre appareil
```

`ios/fastlane/metadata/fr-FR/promotional_text.txt`:
```
Gerez votre stock de lait maternel congele en toute simplicite.
```

**Step 6: Commit**

```bash
git add ios/fastlane/
git commit -m "feat(ios): add Fastlane config with metadata FR and screenshot setup"
```

---

### Task 14: CLAUDE.md for iOS Project

**Files:**
- Create: `ios/CLAUDE.md`

**Step 1: Create project context file**

```markdown
# Petites Gouttes iOS

iOS port of the Android breast milk bag inventory app. French-language UI.

## Build

```bash
cd ios
xcodegen generate
xcodebuild -project PetitesGouttes.xcodeproj -scheme PetitesGouttes -destination 'platform=iOS Simulator,name=iPhone 16' build
```

## Architecture

MVVM with SwiftUI + SwiftData. No DI framework. @Observable ViewModels with @MainActor.

```
PetitesGouttes/
├── App/             # Entry point, screenshot data service
├── Models/          # MilkBag SwiftData model
├── ViewModels/      # Dashboard, Freezer, History, Stats, Settings
├── Views/           # SwiftUI screens and components
├── Navigation/      # ContentView with TabView
└── Theme/           # Colors, card styles
```

## Key details

- iOS 17+, Swift 6, strict concurrency
- SwiftData local only (no CloudKit)
- French only (no localization)
- XcodeGen: project.yml is source of truth (.xcodeproj gitignored)
- Fastlane for screenshots and App Store deployment
- Settings via @AppStorage (UserDefaults)

## Deploy

```bash
cd ios
fastlane screenshots      # capture screenshots
fastlane beta             # TestFlight
fastlane release          # App Store (full)
fastlane release_quick    # App Store (no screenshots)
```
```

**Step 2: Commit**

```bash
git add ios/CLAUDE.md
git commit -m "docs(ios): add CLAUDE.md project context"
```

---

### Task 15: Build Verification & App Icon

**Step 1: Generate a simple app icon**

Use an SF Symbol-based approach or create a simple 1024x1024 PNG. For now, the app will build without an icon (just shows blank). The user can provide a custom icon later.

**Step 2: Full build verification**

```bash
cd /Users/benjamin/Documents/petites-gouttes/ios
xcodegen generate
xcodebuild -project PetitesGouttes.xcodeproj -scheme PetitesGouttes -destination 'platform=iOS Simulator,name=iPhone 16' build 2>&1 | tail -20
```

Expected: BUILD SUCCEEDED

**Step 3: Run on simulator to verify**

```bash
cd /Users/benjamin/Documents/petites-gouttes/ios
xcodebuild -project PetitesGouttes.xcodeproj -scheme PetitesGouttes -destination 'platform=iOS Simulator,name=iPhone 16' build 2>&1 | grep -E "(BUILD|error:)"
```

Expected: BUILD SUCCEEDED, no errors

**Step 4: Run screenshot tests**

```bash
cd /Users/benjamin/Documents/petites-gouttes/ios
fastlane screenshots
```

**Step 5: Final commit**

```bash
git add -A
git commit -m "feat(ios): complete v1.0.0 iOS port ready for App Store"
```

---

## Execution Summary

| Task | Description | Depends on |
|------|-------------|------------|
| 1 | Project scaffold (XcodeGen) | — |
| 2 | MilkBag SwiftData model | 1 |
| 3 | Theme (colors, card style) | 1 |
| 4 | ViewModels (5) | 2 |
| 5 | Components (MilkBagCard, AlertBanner) | 2, 3 |
| 6 | Dashboard screen | 4, 5 |
| 7 | Freezer list screen | 4, 5 |
| 8 | History & Stats screens | 4, 5 |
| 9 | AddEditBag & Settings screens | 4, 5 |
| 10 | Navigation & App entry point | 6, 7, 8, 9 |
| 11 | Settings toolbar access | 10 |
| 12 | Screenshot data & UI tests | 11 |
| 13 | Fastlane config & metadata | 1 |
| 14 | CLAUDE.md | — |
| 15 | Build verification & deploy | All |

**Parallel groups:**
- Tasks 2 + 3 (independent)
- Tasks 6 + 7 + 8 + 9 (independent screens, all depend on 4+5)
- Tasks 13 + 14 (independent of code tasks)
