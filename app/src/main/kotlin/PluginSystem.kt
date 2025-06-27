import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.ServiceLoader
import kotlin.collections.forEach
import kotlin.coroutines.CoroutineContext

/**
 * Core system that handles plugins for the Cet app.
 * Planned to have an event system and plugin tracking
 */
class PluginSystem(eventHandler: EventHandler, context: CoroutineContext) {
    private val _plugins = mutableMapOf<String, IPlugin>()
    private val _eventSystem = eventHandler
    private val systemScope = CoroutineScope(context)

    @Composable
    fun renderApplication() {
        val corePlugin = _plugins["core"] as? ICorePlugin
            ?: throw IllegalStateException("Core plugin not found")

        corePlugin.RootUI()
    }

    fun start() {
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
        val plugins = loader.toList()

        plugins.forEach { plugin -> _plugins[plugin.id] = plugin }
    }

    private suspend fun startPlugins() {
        val pluginContext = PluginContext(_eventSystem, systemScope)

        // Initialize core plugin first
        val corePlugin = _plugins["core"]
        corePlugin?.onInitialize(pluginContext)

        // Then initialize all other plugins
        _plugins.filter { it.key != "core" }.forEach { (id, plugin) ->
            systemScope.launch {
                try {
                    plugin.onInitialize(pluginContext)
                } catch (e: Exception) {
                    _eventSystem.publish(CetEvent.BaseEvents.SystemEvent(
                        timestamp = System.currentTimeMillis(),
                        message = "Error starting plugin: ${id}, ${e.message}; ${plugin.id}",
                        type = "ERROR"
                    ))
                }
            }
        }
    }

    private fun shutDownPlugins() {
        _plugins.forEach { (_, plugin) -> plugin.onDisable() }
    }
}