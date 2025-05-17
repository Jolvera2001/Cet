package pluginSystem

import core.CorePlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.ServiceLoader
import kotlin.coroutines.CoroutineContext

/**
 * Core system that handles plugins for the Cet app.
 * Planned to have an event system and plugin tracking
 *
 */
// TODO: Once we have a basic implementation, work on coroutine usage
class PluginSystem(eventHandler: EventHandler, context: CoroutineContext) {
    private val _eventSystem = eventHandler
    private val _plugins = mutableMapOf<String, IPlugin>()
    private val systemScope = CoroutineScope(context)

    fun startup() {
        registerPlugins()

        systemScope.launch {
            startPlugins()
        }
    }

    fun stop() {
        systemScope.cancel()
        shutDownPlugins()
    }

    fun getPlugins(): Map<String, IPlugin> {
        return _plugins.toMap()
    }

    private fun registerPlugins() {
        val loader = ServiceLoader.load(IPlugin::class.java)
        loader.forEach { plugin: IPlugin -> _plugins.put(plugin.id, plugin) }
    }

    private suspend fun startPlugins() {
        _plugins.forEach { (id, plugin) ->
            systemScope.launch {
                plugin.onInitialize(_eventSystem, systemScope)

            }
        }
    }

    private fun shutDownPlugins() {
        _plugins.forEach { (_, plugin) -> plugin.onDisable() }
    }
}