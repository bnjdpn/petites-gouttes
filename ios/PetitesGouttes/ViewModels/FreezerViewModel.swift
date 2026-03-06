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
