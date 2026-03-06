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
    }
}
