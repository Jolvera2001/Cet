package PluginSystem

interface IPlugin {
    fun onInitialize(eventHandler: EventHandler)
    fun onDisable()
}