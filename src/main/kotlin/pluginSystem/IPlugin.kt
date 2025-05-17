package pluginSystem

interface IPlugin {
    val id: String
    val version: String
    fun onInitialize(eventHandler: EventHandler)
    fun onDisable()
}

enum class PluginState {
    STOPPED,
    ACTIVE,
    DISABLED,
    FAILED
}