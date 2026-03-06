import SwiftUI
import SwiftData

struct AddEditBagView: View {
    @Environment(\.modelContext) private var modelContext
    @Environment(\.dismiss) private var dismiss

    var bagToEdit: MilkBag? = nil
    var onSave: (() -> Void)? = nil

    @State private var volumeText = ""
    @State private var pumpDate = Date.now

    private var isEditing: Bool { bagToEdit != nil }

    private var expiryDate: Date {
        Calendar.current.date(byAdding: .month, value: 4, to: pumpDate) ?? pumpDate
    }

    private var isValid: Bool {
        guard let volume = Int(volumeText) else { return false }
        return volume > 0
    }

    private static let dateFormatter: DateFormatter = {
        let f = DateFormatter()
        f.dateFormat = "dd/MM/yyyy"
        f.locale = Locale(identifier: "fr_FR")
        return f
    }()

    var body: some View {
        NavigationStack {
            Form {
                Section("Volume") {
                    TextField("Volume en ml", text: $volumeText)
                        .keyboardType(.numberPad)
                }

                Section("Date de tirage") {
                    DatePicker(
                        "Date",
                        selection: $pumpDate,
                        in: ...Date.now,
                        displayedComponents: .date
                    )
                    .environment(\.locale, Locale(identifier: "fr_FR"))
                }

                Section("Date d'expiration") {
                    Text(Self.dateFormatter.string(from: expiryDate))
                        .foregroundStyle(.secondary)
                }
            }
            .navigationTitle(isEditing ? "Modifier le sachet" : "Nouveau sachet")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Annuler") {
                        dismiss()
                    }
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button("Enregistrer") {
                        save()
                    }
                    .disabled(!isValid)
                }
            }
            .onAppear {
                if let bag = bagToEdit {
                    volumeText = String(bag.volumeMl)
                    pumpDate = bag.pumpDate
                }
            }
        }
    }

    private func save() {
        guard let volume = Int(volumeText), volume > 0 else { return }

        if let bag = bagToEdit {
            bag.volumeMl = volume
            bag.pumpDate = pumpDate
            bag.expiryDate = expiryDate
        } else {
            let newBag = MilkBag(volumeMl: volume, pumpDate: pumpDate)
            modelContext.insert(newBag)
        }

        try? modelContext.save()
        onSave?()
        dismiss()
    }
}
