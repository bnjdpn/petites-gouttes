import SwiftUI
import SwiftData

struct HistoryView: View {
    @Environment(\.modelContext) private var modelContext
    @State private var vm: HistoryViewModel?
    @State private var showSettings = false

    var body: some View {
        NavigationStack {
            Group {
                if let vm {
                    if vm.removedBags.isEmpty {
                        ContentUnavailableView(
                            "Aucun historique",
                            systemImage: "clock",
                            description: Text("Les sachets retires apparaitront ici.")
                        )
                    } else {
                        scrollContent(vm: vm)
                    }
                } else {
                    ProgressView()
                }
            }
            .navigationTitle("Historique")
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
                    vm = HistoryViewModel(modelContext: modelContext)
                }
            }
            .onAppear { vm?.fetchBags() }
        }
    }

    @ViewBuilder
    private func scrollContent(vm: HistoryViewModel) -> some View {
        ScrollView {
            VStack(spacing: 12) {
                Text("\(vm.removedBags.count) sachet(s) retire(s)")
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
                    .frame(maxWidth: .infinity, alignment: .leading)

                LazyVStack(spacing: 12) {
                    ForEach(vm.removedBags) { bag in
                        MilkBagCard(
                            bag: bag,
                            onRestore: { vm.restoreBag(bag) },
                            onDelete: { vm.deleteBag(bag) }
                        )
                    }
                }
            }
            .padding()
        }
        .refreshable {
            vm.fetchBags()
        }
    }
}
