import SwiftUI

enum PGTheme {
    static let primary = Color(light: .init(hex: 0xB4718A), dark: .init(hex: 0xFFB0D0))
    static let primaryContainer = Color(light: .init(hex: 0xFFD9E6), dark: .init(hex: 0x5C3A4A))
    static let secondary = Color(light: .init(hex: 0x8B7091), dark: .init(hex: 0xD4BDD9))

    static let dlcSafe = Color(light: .init(hex: 0x4CAF50), dark: .init(hex: 0x81C784))
    static let dlcWarning = Color(light: .init(hex: 0xFF9800), dark: .init(hex: 0xFFB74D))
    static let dlcUrgent = Color(light: .init(hex: 0xE53935), dark: .init(hex: 0xEF5350))

    static let alertWarningBg = Color(light: .init(hex: 0xFFF3E0), dark: .init(hex: 0x4E3A20))
    static let alertWarningText = Color(light: .init(hex: 0xE65100), dark: .init(hex: 0xFFB74D))
    static let alertDangerBg = Color(light: .init(hex: 0xFFEBEE), dark: .init(hex: 0x4E2020))
    static let alertDangerText = Color(light: .init(hex: 0xB71C1C), dark: .init(hex: 0xEF5350))
}

private extension Color {
    init(light: Color.Resolved, dark: Color.Resolved) {
        self.init(uiColor: UIColor { traits in
            let c = traits.userInterfaceStyle == .dark ? dark : light
            return UIColor(red: CGFloat(c.red), green: CGFloat(c.green), blue: CGFloat(c.blue), alpha: CGFloat(c.opacity))
        })
    }
}

private extension Color.Resolved {
    init(hex: UInt32) {
        self.init(
            red: Float((hex >> 16) & 0xFF) / 255.0,
            green: Float((hex >> 8) & 0xFF) / 255.0,
            blue: Float(hex & 0xFF) / 255.0
        )
    }
}

struct CardStyleModifier: ViewModifier {
    func body(content: Content) -> some View {
        content
            .padding()
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(.regularMaterial, in: .rect(cornerRadius: 16))
    }
}

extension View {
    func cardStyle() -> some View {
        modifier(CardStyleModifier())
    }
}

extension MilkBag.ExpiryStatus {
    var color: Color {
        switch self {
        case .safe: PGTheme.dlcSafe
        case .warning: PGTheme.dlcWarning
        case .urgent: PGTheme.dlcUrgent
        }
    }
}
