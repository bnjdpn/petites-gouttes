import SwiftUI

struct AlertBanner: View {
    let icon: String
    let message: String
    let style: Style

    enum Style {
        case warning, danger

        var backgroundColor: Color {
            switch self {
            case .warning: PGTheme.alertWarningBg
            case .danger: PGTheme.alertDangerBg
            }
        }

        var textColor: Color {
            switch self {
            case .warning: PGTheme.alertWarningText
            case .danger: PGTheme.alertDangerText
            }
        }
    }

    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: icon)
                .font(.title3)
            Text(message)
                .font(.subheadline)
        }
        .foregroundStyle(style.textColor)
        .padding()
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(style.backgroundColor, in: .rect(cornerRadius: 12))
    }
}
