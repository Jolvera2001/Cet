package pluginSystem

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

interface IPlugin {
    val id: String
    val version: String
    fun onInitialize(eventHandler: EventHandler, context: CoroutineScope)
    fun onDisable()
}

enum class PluginState {
    STOPPED,
    ACTIVE,
    DISABLED,
    FAILED
}