import SwiftUI

struct SettingsView: View {
    @Environment(\.dismiss) private var dismiss

    @AppStorage("lowStockThreshold") private var lowStockThreshold = 1000
    @AppStorage("dailyConsumption") private var dailyConsumption = 200
    @AppStorage("daycareDays") private var daycareDays = 5

    var body: some View {
        NavigationStack {
            Form {
                Section("Alertes & Consommation") {
                    HStack {
                        Text("Seuil stock bas (ml)")
                        Spacer()
                        TextField("ml", value: $lowStockThreshold, format: .number)
                            .keyboardType(.numberPad)
                            .multilineTextAlignment(.trailing)
                            .frame(width: 80)
                    }

                    HStack {
                        Text("Consommation journaliere (ml)")
                        Spacer()
                        TextField("ml", value: $dailyConsumption, format: .number)
                            .keyboardType(.numberPad)
                            .multilineTextAlignment(.trailing)
                            .frame(width: 80)
                    }

                    Stepper("Jours de creche : \(daycareDays)", value: $daycareDays, in: 1...7)
                }

                Section("A propos") {
                    HStack {
                        Text("Version")
                        Spacer()
                        Text("Petites Gouttes v1.0.0")
                            .foregroundStyle(.secondary)
                    }

                    VStack(alignment: .leading, spacing: 4) {
                        Text("Confidentialite")
                            .font(.subheadline)
                        Text("Toutes vos donnees restent sur votre appareil. Aucune information n'est transmise a des tiers.")
                            .font(.caption)
                            .foregroundStyle(.secondary)
                    }
                }
            }
            .navigationTitle("Reglages")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .confirmationAction) {
                    Button("OK") {
                        dismiss()
                    }
                }
            }
        }
    }
}
