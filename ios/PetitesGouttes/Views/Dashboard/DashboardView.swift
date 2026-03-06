import SwiftUI
import SwiftData

struct DashboardView: View {
    @Environment(\.modelContext) private var modelContext
    @State private var vm: DashboardViewModel?
    @State private var showAddSheet = false
    @State private var showSettings = false

    @AppStorage("lowStockThreshold") private var lowStockThreshold = 1000
    @AppStorage("dailyConsumption") private var dailyConsumption = 200

    private static let dateFormatter: DateFormatter = {
        let f = DateFormatter()
        f.dateFormat = "dd/MM/yyyy"
        return f
    }()

    var body: some View {
        NavigationStack {
            Group {
                if let vm {
                    if vm.activeBags.isEmpty {
                        ContentUnavailableView(
                            "Aucun sachet",
                            systemImage: "snowflake",
                            description: Text("Ajoutez votre premier sachet de lait maternel.")
                        )
                    } else {
                        scrollContent(vm: vm)
                    }
                } else {
                    ProgressView()
                }
            }
            .navigationTitle("Tableau de bord")
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    HStack(spacing: 12) {
                        Button { showAddSheet = true } label: {
                            Image(systemName: "plus")
                        }
                        Button { showSettings = true } label: {
                            Image(systemName: "gearshape")
                        }
                    }
                }
            }
            .sheet(isPresented: $showAddSheet) {
                AddEditBagView {
                    vm?.fetchBags()
                }
            }
            .sheet(isPresented: $showSettings) {
                SettingsView()
            }
            .task {
                if vm == nil {
                    vm = DashboardViewModel(modelContext: modelContext)
                }
            }
        }
    }

    @ViewBuilder
    private func scrollContent(vm: DashboardViewModel) -> some View {
        ScrollView {
            VStack(spacing: 16) {
                // Alerts
                if vm.isLowStock(threshold: lowStockThreshold) {
                    AlertBanner(
                        icon: "exclamationmark.triangle",
                        message: "Stock bas : \(vm.totalActiveVolume) ml restants",
                        style: .warning
                    )
                }

                if !vm.bagsExpiringSoon.isEmpty {
                    AlertBanner(
                        icon: "clock.badge.exclamationmark",
                        message: "\(vm.bagsExpiringSoon.count) sachet(s) expirent bientot",
                        style: .danger
                    )
                }

                // Summary cards
                HStack(spacing: 12) {
                    summaryCard(
                        title: "Sachets",
                        value: "\(vm.activeBagCount)",
                        icon: "bag.fill"
                    )
                    summaryCard(
                        title: "Volume total",
                        value: "\(vm.totalActiveVolume) ml",
                        icon: "drop.fill"
                    )
                }

                // Stock days
                VStack(alignment: .leading, spacing: 8) {
                    Label("Autonomie", systemImage: "calendar")
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                    let days = vm.stockDays(dailyConsumption: dailyConsumption)
                    Text("\(days) jour\(days > 1 ? "s" : "")")
                        .font(.system(.title, design: .rounded, weight: .bold))
                        .foregroundStyle(PGTheme.primary)
                }
                .cardStyle()

                // Next bag FIFO
                if let nextBag = vm.nextBagToUse {
                    VStack(alignment: .leading, spacing: 8) {
                        Text("Prochain sachet a utiliser")
                            .font(.subheadline)
                            .foregroundStyle(.secondary)
                        MilkBagCard(bag: nextBag, showActions: false)
                    }
                }
            }
            .padding()
        }
        .refreshable {
            vm.fetchBags()
        }
    }

    private func summaryCard(title: String, value: String, icon: String) -> some View {
        VStack(alignment: .leading, spacing: 8) {
            Label(title, systemImage: icon)
                .font(.subheadline)
                .foregroundStyle(.secondary)
            Text(value)
                .font(.system(.title2, design: .rounded, weight: .bold))
                .foregroundStyle(PGTheme.primary)
        }
        .cardStyle()
    }
}
