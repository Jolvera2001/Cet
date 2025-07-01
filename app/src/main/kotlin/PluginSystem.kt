import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.util.ServiceLoader
import kotlin.collections.forEach
import kotlin.coroutines.CoroutineContext

/**
 * Core system that handles plugins for the Cet app.
 */
class PluginSystem(eventHandler: EventHandler, context: CoroutineContext) {
    private val _plugins = mutableMapOf<String, IPlugin>()
    private val _eventSystem = eventHandler
    private val _systemScope = CoroutineScope(context)

    /**
     * Application entry point
     */
    @Composable
    fun renderApplication() {
        val corePlugin = _plugins["core"] as? ICorePlugin
            ?: throw IllegalStateException("Core plugin not found")

        corePlugin.RootUI()
    }

    /**
     * Starts plugin system
     * **NOTE:** run this first before rendering application
     */
    fun start() {
        registerPlugins()

        _systemScope.launch {
            startPlugins()
        }
    }

    /**
     * Stops all plugins and cancels any existing children coroutines
     */
    fun stop() {
        shutDownPlugins()
        _systemScope.cancel()
    }

    /**
     * Adds a plugin in the registry. Mainly for testing purposes
     */
    fun addPlugin(plugin: IPlugin) {
        _plugins[plugin.id] = plugin
    }

    private fun registerPlugins() {
        val loader = ServiceLoader.load(IPlugin::class.java)
        val plugins = loader.toList()

        plugins.forEach { plugin -> _plugins[plugin.id] = plugin }
    }

    private suspend fun startPlugins() {
        val corePlugin = _plugins["core"]
        if (corePlugin != null) {
            val pluginContext = createPluginContext("core")
            try {
                corePlugin.onInitialize(pluginContext)
            } catch (e: Exception) {
                _eventSystem.publish(CetEvent.BaseEvents.SystemEvent(
                    timestamp = System.currentTimeMillis(),
                    message = "Critical error starting core plugin: ${e.message}",
                    type = "ERROR"
                ))
                throw e
            }
        }

        _plugins.filter { it.key != "core" }.forEach { (id, plugin) ->
            _systemScope.launch {
                try {
                    val pluginContext = createPluginContext(id)
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

    private fun createPluginContext(id: String): PluginContext {
        return PluginContext(
            _eventSystem,
            _systemScope + CoroutineName("Plugin - $id")
        )
    }
}