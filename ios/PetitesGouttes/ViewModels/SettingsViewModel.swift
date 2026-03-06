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
