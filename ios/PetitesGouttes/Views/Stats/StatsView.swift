import SwiftUI
import SwiftData
import Charts

struct StatsView: View {
    @Environment(\.modelContext) private var modelContext
    @State private var vm: StatsViewModel?
    @State private var showSettings = false

    var body: some View {
        NavigationStack {
            Group {
                if let vm {
                    scrollContent(vm: vm)
                } else {
                    ProgressView()
                }
            }
            .navigationTitle("Statistiques")
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button { showSettings = true } label: {
                        Image(systemName: "gearshape")
                    }
                }
            }
            .sheet(isPresented: $showSettings) {
                SettingsView()
            }
            .task {
                if vm == nil {
                    vm = StatsViewModel(modelContext: modelContext)
                }
            }
        }
    }

    @ViewBuilder
    private func scrollContent(vm: StatsViewModel) -> some View {
        ScrollView {
            VStack(spacing: 16) {
                // Lactation drop alert
                if vm.hasLactationDrop {
                    AlertBanner(
                        icon: "exclamationmark.triangle",
                        message: "Baisse de lactation detectee",
                        style: .warning
                    )
                }

                // Metric cards 2x2
                let columns = [GridItem(.flexible(), spacing: 12), GridItem(.flexible(), spacing: 12)]
                LazyVGrid(columns: columns, spacing: 12) {
                    metricCard(
                        title: "Moy. 7 jours",
                        value: String(format: "%.0f ml", vm.averageDailyVolume(days: 7)),
                        detail: trendText(vm.lactationTrendPercent)
                    )
                    metricCard(
                        title: "Moy. 30 jours",
                        value: String(format: "%.0f ml", vm.averageDailyVolume(days: 30))
                    )
                    metricCard(
                        title: "Ce mois",
                        value: "\(vm.monthlyTotal(monthsAgo: 0)) ml"
                    )
                    metricCard(
                        title: "Mois dernier",
                        value: "\(vm.monthlyTotal(monthsAgo: 1)) ml"
                    )
                }

                // Daily volumes chart (30 days)
                VStack(alignment: .leading, spacing: 8) {
                    Text("Volume journalier (30j)")
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                    dailyChart(data: vm.dailyVolumes(days: 30))
                }
                .cardStyle()

                // Weekly volumes chart (12 weeks)
                VStack(alignment: .leading, spacing: 8) {
                    Text("Volume hebdomadaire (12 sem.)")
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                    weeklyChart(data: vm.weeklyVolumes(weeks: 12))
                }
                .cardStyle()
            }
            .padding()
        }
        .refreshable {
            vm.fetchBags()
        }
    }

    private func metricCard(title: String, value: String, detail: String? = nil) -> some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(title)
                .font(.caption)
                .foregroundStyle(.secondary)
            Text(value)
                .font(.system(.title3, design: .rounded, weight: .bold))
                .foregroundStyle(PGTheme.primary)
            if let detail {
                Text(detail)
                    .font(.caption2)
                    .foregroundStyle(.secondary)
            }
        }
        .cardStyle()
    }

    private func trendText(_ percent: Double) -> String {
        let sign = percent >= 0 ? "+" : ""
        return "\(sign)\(String(format: "%.1f", percent))%"
    }

    private func dailyChart(data: [StatsViewModel.DailyVolume]) -> some View {
        Chart(data) { item in
            BarMark(
                x: .value("Jour", item.date, unit: .day),
                y: .value("Volume", item.volume)
            )
            .foregroundStyle(PGTheme.primary.gradient)
        }
        .frame(height: 200)
        .chartXAxis {
            AxisMarks(values: .stride(by: .day, count: 7)) { _ in
                AxisGridLine()
                AxisValueLabel(format: .dateTime.day().month(.abbreviated), centered: true)
            }
        }
    }

    private func weeklyChart(data: [StatsViewModel.DailyVolume]) -> some View {
        Chart(data) { item in
            BarMark(
                x: .value("Semaine", item.date, unit: .weekOfYear),
                y: .value("Volume", item.volume)
            )
            .foregroundStyle(PGTheme.primary.gradient)
        }
        .frame(height: 200)
        .chartXAxis {
            AxisMarks(values: .stride(by: .weekOfYear, count: 2)) { _ in
                AxisGridLine()
                AxisValueLabel(format: .dateTime.day().month(.abbreviated), centered: true)
            }
        }
    }
}
