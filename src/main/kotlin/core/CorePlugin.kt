package core

import pluginSystem.CetEvent
import pluginSystem.EventHandler
import pluginSystem.IPlugin
import pluginSystem.PluginState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CorePlugin(context: CoroutineContext) : IPlugin {
    private var state = PluginState.STOPPED
    private val scope = CoroutineScope(context)
    private val _id = "core"

    private lateinit var eventHandler: EventHandler;

    override fun onInitialize(eventHandler: EventHandler) {
        state = PluginState.ACTIVE
        this.eventHandler = eventHandler

        scope.launch {
            val lifeCycleEvent = CetEvent.BaseEvents.PluginLifecycleEvent(
                pluginId = _id,
                state = state,
                name = "uh idk? WHy did I add this?",
                timestamp = System.currentTimeMillis(),
            )
            eventHandler.Publish(lifeCycleEvent)
        }

        eventHandler.Subscribe().onEach { event ->
            when (event) {
                // TODO: define events
            }
        }.launchIn(scope)
    }

    override fun onDisable() {
        state = PluginState.STOPPED

        scope.launch {
            val lifeCycleEvent = CetEvent.BaseEvents.PluginLifecycleEvent(
                pluginId = _id,
                state = state,
                name = "uh idk? WHy did I add this?",
                timestamp = System.currentTimeMillis(),
            )
            eventHandler.Publish(lifeCycleEvent)
        }

        scope.cancel()
    }
}

sealed class CoreEvents : CetEvent.PluginEvent() {
//    data class()
}