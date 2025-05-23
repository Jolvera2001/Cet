package core

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import pluginSystem.CetEvent
import pluginSystem.EventHandler
import pluginSystem.IPlugin
import pluginSystem.PluginState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import pluginSystem.UIAreas

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

    @Composable
    override fun Render(areas: UIAreas) {

    }

    @Composable
    fun RootUI() {
        // Main App Layout here
        MaterialTheme {
            Text("Hello world!")
        }
    }

//    @Composable
//    override fun Render(areas: UIAreas) {
//        when (areas) {
//            UIAreas.MainArea -> CoreMainAreaUI()
//            else -> Text("I should not exist...")
//        }
//    }
//
//    private fun CoreMainAreaUI() {
//        // editor here...
//    }
}

sealed class CoreEvents : CetEvent.PluginEvent() {
//    data class()
}