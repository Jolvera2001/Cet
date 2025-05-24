package core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import core.ui.MainArea
import core.ui.SideBar
import core.ui.SideBarItem
import core.ui.TopBar
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

    // we won't need to do this, might just end up using it later,
    // but I want to use a different function for now
    @Composable
    override fun Render(areas: UIAreas) {
    }

    @Composable
    fun RootUI() {
        // Main App Layout here
        MaterialTheme {
            Column {
                TopBar()
                Row {
                    SideBar(sideBarItems = exampleItems())
                    MainArea()
                }
            }
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

    private fun exampleItems(): List<SideBarItem> {
        return listOf(
            SideBarItem(
                id = "Explorer",
                icon = Icons.Default.AccountBox,
                tooltip = "Folder Explorer"
            ) {},

            )
    }
}

sealed class CoreEvents : CetEvent.PluginEvent() {
//    data class()
}