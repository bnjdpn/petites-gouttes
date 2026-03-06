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
