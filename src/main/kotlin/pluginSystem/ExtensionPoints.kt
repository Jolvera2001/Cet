package pluginSystem

sealed class ExtensionPoints(val id: String) {
    object MainToolBar : ExtensionPoints("main_toolbar")
    object Sidebar : ExtensionPoints("sidebar")
    object StatusBar : ExtensionPoints("status_bar")
}