package core

import BasePlugin
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import core.ui.MainArea
import core.ui.SideBar
import CetEvent
import EventHandler
import ICorePlugin
import PluginContext
import core.ui.TopBar
import kotlinx.coroutines.CoroutineScope
import sharedItems.SideBarItem

class CorePlugin() : ICorePlugin, BasePlugin() {
    override val id = "core"
    override val version = "0.1.0"
    var viewModel = CoreViewModel()

    override suspend fun onInitialize(context: PluginContext) {
        super.onInitialize(context)

        val scope = pluginContext.scope
        val eventSystem = pluginContext.eventSystem

        launchListeners(scope, eventSystem)
    }

    @Composable
    override fun RootUI() {
        val currentState by viewModel.state.collectAsState()

        MaterialTheme {
            Column {
                TopBar(currentState.menuItems)
                Row {
                    SideBar(
                        sideBarItems = currentState.sidebarItems,
                        selectedItemId = currentState.activeContentId,
                        onItemClick = { item: SideBarItem ->
                            viewModel.setActiveContent(item.contentProviderId)
                        }
                    )
                    MainArea(
                        activeContentId = currentState.activeContentId,
                        contentProviders = currentState.contentProviders,
                    )
                }
            }
        }
    }

    private fun launchListeners(scope: CoroutineScope, eventSystem: EventHandler) {
        eventSystem.subscribe<CetEvent.BaseEvents.SystemEvent>()
            .onEach { event ->
                println(event)
            }.launchIn(scope)

        eventSystem.subscribe<CetEvent.UIEvent.RegisterSidebarItem>()
            .onEach { event ->
                viewModel.addNewSidebarItem(event.item)
            }.launchIn(scope)

        eventSystem.subscribe<CetEvent.UIEvent.RegisterContent>()
            .onEach { event ->
                viewModel.addNewProvider(event.providerId, event.contentProvider)
            }.launchIn(scope)

        eventSystem.subscribe<CetEvent.UIEvent.RegisterMenuItem>()
            .onEach { event ->
                viewModel.addNewMenuItem(event.menuKey, event.subItem)
            }.launchIn(scope)
    }
}