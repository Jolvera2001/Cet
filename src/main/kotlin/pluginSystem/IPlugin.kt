package pluginSystem

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope

interface IPlugin {
    val id: String
    val version: String
    fun onInitialize(eventHandler: EventHandler, scope: CoroutineScope)
    fun onDisable()
    @Composable
    fun Render(areas: UIAreas)
}

enum class UIAreas {
    MainArea
}

enum class PluginState {
    STOPPED,
    ACTIVE,
    DISABLED,
    FAILED
}

