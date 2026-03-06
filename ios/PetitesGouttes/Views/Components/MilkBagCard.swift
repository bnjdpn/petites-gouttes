import SwiftUI

struct MilkBagCard: View {
    let bag: MilkBag
    var showActions: Bool = true
    var onEdit: (() -> Void)?
    var onRemove: (() -> Void)?
    var onRestore: (() -> Void)?
    var onDelete: (() -> Void)?

    @State private var showDeleteConfirmation = false

    private static let dateFormatter: DateFormatter = {
        let f = DateFormatter()
        f.dateFormat = "dd/MM/yyyy"
        return f
    }()

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack(alignment: .top) {
                VStack(alignment: .leading, spacing: 4) {
                    Text("\(bag.volumeMl) ml")
                        .font(.system(.title2, design: .rounded, weight: .bold))
                    Text("Pomp\u{00E9} le \(Self.dateFormatter.string(from: bag.pumpDate))")
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                }
                Spacer()
                dlcBadge
            }

            HStack {
                Image(systemName: "calendar.badge.clock")
                    .foregroundStyle(.secondary)
                Text("DLC : \(Self.dateFormatter.string(from: bag.expiryDate))")
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }

            if let removalDate = bag.removalDate {
                HStack {
                    Image(systemName: "arrow.uturn.right")
                        .foregroundStyle(.secondary)
                    Text("Retir\u{00E9} le \(Self.dateFormatter.string(from: removalDate))")
                        .font(.caption)
                        .foregroundStyle(.secondary)
                }
            }

            if showActions {
                Divider()
                actionButtons
            }
        }
        .cardStyle()
        .confirmationDialog("Supprimer ce sachet ?", isPresented: $showDeleteConfirmation) {
            Button("Supprimer", role: .destructive) { onDelete?() }
        } message: {
            Text("Cette action est irr\u{00E9}versible.")
        }
    }

    private var dlcBadge: some View {
        let status = bag.expiryStatus
        let days = bag.daysUntilExpiry
        let text = days <= 0 ? "Expir\u{00E9}" : "\(days)j"

        return Text(text)
            .font(.system(.caption, design: .rounded, weight: .semibold))
            .padding(.horizontal, 8)
            .padding(.vertical, 4)
            .background(status.color.opacity(0.15), in: Capsule())
            .foregroundStyle(status.color)
    }

    @ViewBuilder
    private var actionButtons: some View {
        HStack(spacing: 16) {
            if let onEdit {
                Button { onEdit() } label: {
                    Label("Modifier", systemImage: "pencil")
                        .font(.caption)
                }
            }
            if let onRemove {
                Button { onRemove() } label: {
                    Label("Retirer", systemImage: "fork.knife")
                        .font(.caption)
                }
                .tint(PGTheme.dlcWarning)
            }
            if let onRestore {
                Button { onRestore() } label: {
                    Label("Restaurer", systemImage: "arrow.uturn.backward")
                        .font(.caption)
                }
                .tint(PGTheme.primary)
            }
            Spacer()
            if onDelete != nil {
                Button { showDeleteConfirmation = true } label: {
                    Label("Supprimer", systemImage: "trash")
                        .font(.caption)
                }
                .tint(.red)
            }
        }
    }
}
