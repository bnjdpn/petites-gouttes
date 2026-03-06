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
        guard let ref = calendar.date(byAdding: .month, value: -monthsAgo, to: now) else { return 0 }
        let start = calendar.date(from: calendar.dateComponents([.year, .month], from: ref))!
        let end = calendar.date(byAdding: .month, value: 1, to: start)!
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
