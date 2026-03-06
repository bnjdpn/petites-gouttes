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
