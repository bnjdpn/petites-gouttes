import XCTest

@MainActor
final class SnapshotTests: XCTestCase {
    var app: XCUIApplication!

    override func setUpWithError() throws {
        continueAfterFailure = false
        app = XCUIApplication()
        setupSnapshot(app)
        app.launchArguments += ["-SCREENSHOT_MODE"]
    }

    func testScreenshot01_Dashboard() {
        app.launch()
        snapshot("01_Dashboard")
    }

    func testScreenshot02_Freezer() {
        app.launch()
        navigateToTab(index: 1)
        snapshot("02_Congelateur")
    }

    func testScreenshot03_Stats() {
        app.launch()
        navigateToTab(index: 3)
        snapshot("03_Statistiques")
    }

    func testScreenshot04_AddBag() {
        app.launch()
        app.buttons["plus"].firstMatch.tap()
        sleep(1)
        snapshot("04_AjouterSachet")
    }

    func testScreenshot05_Settings() {
        app.launch()
        app.buttons["gearshape"].firstMatch.tap()
        sleep(1)
        snapshot("05_Reglages")
    }

    private func navigateToTab(index: Int) {
        let tabBar = app.tabBars.firstMatch
        let buttons = tabBar.buttons
        if buttons.count > index {
            buttons.element(boundBy: index).tap()
        }
        sleep(1)
    }
}
