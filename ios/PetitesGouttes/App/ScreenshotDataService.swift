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

        let samples: [(Int, Int)] = [
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

        let removed: [(Int, Int, Int)] = [
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
