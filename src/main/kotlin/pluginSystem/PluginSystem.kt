package pluginSystem

import core.CorePlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Core system that handles plugins for the Cet app.
 * Planned to have an event system and plugin tracking
 *
 */
// TODO: Once we have a basic implementation, work on coroutine usage
class PluginSystem {
    private val _eventSystem = EventHandler()
    private val _plugins = mutableMapOf<String, IPlugin>()
    private val supervisor = SupervisorJob()

    private val systemScope = CoroutineScope(
        supervisor + Dispatchers.Default
    )

    fun startup() {
        registerPlugins()

        systemScope.launch {
            startPlugins()
        }
    }

    fun stop() {
        supervisor.cancel()
        shutDownPlugins()
    }

    fun getPlugins(): Map<String, IPlugin> {
        return _plugins.toMap()
    }

    private fun registerPlugins() {
        // TODO: Not sustainable for all plugins, need to find a way to dynamically search for all available plugins
        _plugins.put("core", CorePlugin(systemScope.coroutineContext))
    }

    private suspend fun startPlugins() {
        _plugins.forEach { (id, plugin) ->
            systemScope.launch {
                plugin.onInitialize(_eventSystem)

            }
        }
    }

    private fun shutDownPlugins() {
        _plugins.forEach { (_, plugin) -> plugin.onDisable() }
    }
}