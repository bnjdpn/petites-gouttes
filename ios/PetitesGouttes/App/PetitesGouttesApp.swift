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
