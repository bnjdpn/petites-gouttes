import SwiftUI
import SwiftData

@Observable
@MainActor
final class DashboardViewModel {
    private let modelContext: ModelContext
    var activeBags: [MilkBag] = []

    var activeBagCount: Int { activeBags.count }
    var totalActiveVolume: Int { activeBags.reduce(0) { $0 + $1.volumeMl } }
    var nextBagToUse: MilkBag? { activeBags.min(by: { $0.pumpDate < $1.pumpDate }) }
    var bagsExpiringSoon: [MilkBag] { activeBags.filter { $0.daysUntilExpiry <= 14 } }

    func stockDays(dailyConsumption: Int) -> Int {
        guard dailyConsumption > 0 else { return 0 }
        return totalActiveVolume / dailyConsumption
    }

    func isLowStock(threshold: Int) -> Bool { totalActiveVolume < threshold }

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
