import kotlinx.coroutines.CoroutineScope

/**
 * Context for plugins to use throughout their lifecycles and processes
 */
data class PluginContext(val eventSystem: EventHandler, val scope: CoroutineScope)