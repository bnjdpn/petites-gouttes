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
