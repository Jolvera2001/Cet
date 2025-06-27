import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


fun main() = application {
    val eventHandler = EventHandler()
    val pluginSystem = PluginSystem(eventHandler, Dispatchers.IO + SupervisorJob())

    pluginSystem.start()

    Window(
        alwaysOnTop = true,
        onCloseRequest = {
            pluginSystem.stop()
            exitApplication()
        },
        title = "Cet"
    ) {
        pluginSystem.renderApplication()
    }
}
