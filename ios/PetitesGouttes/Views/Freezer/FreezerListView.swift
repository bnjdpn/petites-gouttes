import SwiftUI
import SwiftData

struct FreezerListView: View {
    @Environment(\.modelContext) private var modelContext
    @State private var vm: FreezerViewModel?
    @State private var showAddSheet = false
    @State private var bagToEdit: MilkBag?
    @State private var showSettings = false

    var body: some View {
        NavigationStack {
            Group {
                if let vm {
                    if vm.activeBags.isEmpty {
                        ContentUnavailableView(
                            "Congelateur vide",
                            systemImage: "snowflake",
                            description: Text("Aucun sachet dans le congelateur.")
                        )
                    } else {
                        scrollContent(vm: vm)
                    }
                } else {
                    ProgressView()
                }
            }
            .navigationTitle("Congelateur")
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    if vm != nil {
                        sortMenu
                    }
                }
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
            .sheet(item: $bagToEdit) { bag in
                AddEditBagView(bagToEdit: bag) {
                    vm?.fetchBags()
                }
            }
            .sheet(isPresented: $showSettings) {
                SettingsView()
            }
            .task {
                if vm == nil {
                    vm = FreezerViewModel(modelContext: modelContext)
                }
            }
        }
    }

    private var sortMenu: some View {
        Menu {
            ForEach(SortOrder.allCases, id: \.self) { order in
                Button {
                    vm?.setSortOrder(order)
                } label: {
                    HStack {
                        Text(order.rawValue)
                        if vm?.sortOrder == order {
                            Image(systemName: "checkmark")
                        }
                    }
                }
            }
        } label: {
            Label("Trier", systemImage: "arrow.up.arrow.down")
        }
    }

    @ViewBuilder
    private func scrollContent(vm: FreezerViewModel) -> some View {
        ScrollView {
            LazyVStack(spacing: 12) {
                ForEach(vm.activeBags) { bag in
                    MilkBagCard(
                        bag: bag,
                        onEdit: { bagToEdit = bag },
                        onRemove: { vm.removeBag(bag) },
                        onDelete: { vm.deleteBag(bag) }
                    )
                }
            }
            .padding()
        }
        .refreshable {
            vm.fetchBags()
        }
    }
}
