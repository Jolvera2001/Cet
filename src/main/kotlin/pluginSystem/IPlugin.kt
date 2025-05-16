package pluginSystem

interface IPlugin {
    fun onInitialize(eventHandler: EventHandler)
    fun onDisable()
}

enum class PluginState {
    STOPPED,
    ACTIVE,
    DISABLED,
    FAILED
}