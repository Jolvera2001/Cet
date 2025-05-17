package pluginSystem

import kotlinx.coroutines.CoroutineScope

interface IPlugin {
    val id: String
    val version: String
    fun onInitialize(eventHandler: EventHandler, scope: CoroutineScope)
    fun onDisable()
}

enum class PluginState {
    STOPPED,
    ACTIVE,
    DISABLED,
    FAILED
}