import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

abstract class BasePlugin() : IPlugin {
    abstract override val id: String
    abstract override val version: String
    override var state: PluginState = PluginState.STOPPED
    protected lateinit var pluginContext: PluginContext

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