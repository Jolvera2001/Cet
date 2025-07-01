import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Base plugin to be extended and implemented for new plugins.
 * Provides the PluginContext that a plugin needs, plugin state, and
 * implements the onDisable logic.
 * @property pluginContext
 * @property state
 */
abstract class BasePlugin() : IPlugin {
    abstract override val id: String
    abstract override val version: String
    abstract override val scopeName: String
    override var state: PluginState = PluginState.STOPPED
    protected lateinit var pluginContext: PluginContext

    /**
     * Sends a PluginLifecycle event stating that this plugin has turned on
     * with the id and version parameters that are set by the implementing plugin.
     * Also sets the pluginContext for use.
     * @param context
     */
    override suspend fun onInitialize(context: PluginContext) {
        state = PluginState.ACTIVE
        this.pluginContext = context

        val lifeCycleEvent = CetEvent.BaseEvents.PluginLifecycle(
            pluginId = id,
            state = state,
            timestamp = System.currentTimeMillis(),
        )
        context.scope.launch {
            context.eventSystem.publish(lifeCycleEvent)
        }
    }

    /**
     * Sets state to DISABLED, and sends a lifecycle event to show that
     * this plugin is now disabled. Also cancel's the scope that this plugin has.
     * NOTE: This logic may change in the future to account for restarting
     */
    override fun onDisable() {
        state = PluginState.DISABLED
        val scope = pluginContext.scope
        val eventSystem = pluginContext.eventSystem

        val lifeCycleEvent = CetEvent.BaseEvents.PluginLifecycle(
            pluginId = id,
            state = state,
            timestamp = System.currentTimeMillis(),
        )

        if (::pluginContext.isInitialized) {
            val publishJob = scope.launch {
                eventSystem.publish(lifeCycleEvent)
            }

            scope.launch {
                publishJob.join()
                scope.cancel()
            }
        }
    }
}