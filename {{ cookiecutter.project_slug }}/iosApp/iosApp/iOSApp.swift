import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        #if DEBUG
        Init.shared.doInitAll(isDebug: true)
        #else
        Init.shared.doInitAll(isDebug: false)
        #endif
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
