package PluginSystem

import kotlin.coroutines.CoroutineContext

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