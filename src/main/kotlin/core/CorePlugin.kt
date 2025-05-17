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

class CorePlugin() : IPlugin {
    override val id = "core"
    override val version = "0.1.0"
    var state = PluginState.STOPPED
    private lateinit var scope: CoroutineScope

    private lateinit var eventHandler: EventHandler

    override fun onInitialize(eventHandler: EventHandler, scope: CoroutineScope) {
        state = PluginState.ACTIVE
        this.eventHandler = eventHandler
        this.scope = scope

        scope.launch {
            val lifeCycleEvent = CetEvent.BaseEvents.PluginLifecycleEvent(
                pluginId = id,
                state = state,
                name = "Core Plugin Initialized",
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
        state = PluginState.DISABLED
        val lifeCycleEvent = CetEvent.BaseEvents.PluginLifecycleEvent(
            pluginId = id,
            state = state,
            name = "Core Plugin Disabled",
            timestamp = System.currentTimeMillis(),
        )

        if (::scope.isInitialized) {
            val publishJob = scope.launch {
                eventHandler.Publish(lifeCycleEvent)
            }

            scope.launch {
                publishJob.join()
                scope.cancel()
            }
        }
    }
}

sealed class CoreEvents : CetEvent.PluginEvent() {
//    data class()
}