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
import ICorePlugin
import PluginContext

class CorePlugin() : ICorePlugin, BasePlugin() {
    override val id = "core"
    override val version = "0.1.0"
    var viewModel = CoreViewModel()

    override suspend fun onInitialize(context: PluginContext) {
        super.onInitialize(context)

        val scope = pluginContext.scope
        val eventSystem = pluginContext.eventSystem

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
    }

    @Composable
    override fun RootUI() {
        val currentState by viewModel.state.collectAsState()

        MaterialTheme {
            Column {
                // TopBar()
                Row {
                    SideBar(
                        sideBarItems = currentState.sidebarItems,
                        selectedItemId = currentState.activeContentId,
                        onItemClick = { item ->
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
}