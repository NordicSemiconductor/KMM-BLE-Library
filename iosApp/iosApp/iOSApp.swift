import SwiftUI
import shared

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

	var body: some Scene {
		WindowGroup {
		    ZStack {
		        Color.white.ignoresSafeArea(.all) // status bar color
			    ContentView()
			}.preferredColorScheme(.light)
		}
	}
}

class AppDelegate: UIResponder, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization
        CommonModuleKt.doInitKoin()
        return true
    }
}
