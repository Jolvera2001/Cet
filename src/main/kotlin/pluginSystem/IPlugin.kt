package pluginSystem

import kotlin.coroutines.CoroutineContext

interface IPlugin {
    val id: String
    val version: String
    fun onInitialize(eventHandler: EventHandler, context: CoroutineContext)
    fun onDisable()
}

enum class PluginState {
    STOPPED,
    ACTIVE,
    DISABLED,
    FAILED
}