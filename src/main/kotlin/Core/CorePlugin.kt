package Core

import PluginSystem.EventHandler
import PluginSystem.IPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext

class CorePlugin(context: CoroutineContext) : IPlugin {
    private val scope = CoroutineScope(context)

    override fun onInitialize(eventHandler: EventHandler) {
        eventHandler.Subscribe().onEach { event ->
            when (event) {
                // TODO: define events
            }
        }
            .launchIn(scope)
    }

    override fun onDisable() {
        scope.cancel()
    }
}