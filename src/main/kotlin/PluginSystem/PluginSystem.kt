package PluginSystem

import Core.CorePlugin

/**
 * Core system that handles plugins for the Cet app.
 * Planned to have an event system and plugin tracking
 *
 */
// TODO: Once we have a basic implementation, work on coroutine usage
class PluginSystem {
    private val _eventSystem = EventHandler()
    private val _plugins = mutableMapOf<String, IPlugin>()

    fun Startup() {
        registerPlugins()
        startPlugins()
    }

    fun stop() {
        shutDownPlugins()
    }

    private fun registerPlugins() {
        // TODO: Not sustainable for all plugins, need to find a way to dynamically search for all available plugins
        _plugins.put("core", CorePlugin())
    }

    private fun startPlugins() {
        _plugins.forEach { (_, plugin) -> plugin.onInitialize(_eventSystem) }
    }

    private fun shutDownPlugins() {
        _plugins.forEach { (_, plugin) -> plugin.onDisable() }
    }
}