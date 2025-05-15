package Core

import PluginSystem.EventHandler
import PluginSystem.IPlugin
import PluginSystem.PluginState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext

class CorePlugin(context: CoroutineContext) : IPlugin {
    private var state = PluginState.STOPPED
    private val scope = CoroutineScope(context)

    override fun onInitialize(eventHandler: EventHandler) {
        state = PluginState.ACTIVE
        eventHandler.Subscribe().onEach { event ->
            when (event) {
                // TODO: define events
            }
        }.launchIn(scope)
    }

    override fun onDisable() {
        state = PluginState.STOPPED
        scope.cancel()
    }
}

sealed class CoreEvents {

}