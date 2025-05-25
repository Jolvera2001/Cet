package core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import core.ui.MainArea
import core.ui.SideBar
import core.ui.TopBar
import pluginSystem.CetEvent
import pluginSystem.EventHandler
import pluginSystem.IPlugin
import pluginSystem.PluginState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import pluginSystem.IContentProvider
import pluginSystem.SideBarItem
import pluginSystem.UIAreas

class CorePlugin() : IPlugin {
    override val id = "core"
    override val version = "0.1.0"
    var state = PluginState.STOPPED
    var viewModel = CoreViewModel()
    private lateinit var scope: CoroutineScope

    private lateinit var eventHandler: EventHandler

    override fun onInitialize(eventHandler: EventHandler, scope: CoroutineScope) {
        state = PluginState.ACTIVE
        this.eventHandler = eventHandler
        this.scope = scope

        scope.launch {
            val lifeCycleEvent = CetEvent.BaseEvents.PluginLifecycle(
                pluginId = id,
                state = state,
                timestamp = System.currentTimeMillis(),
            )
            eventHandler.publish(lifeCycleEvent)
        }

        eventHandler.subscribe<CetEvent.UIEvent.RegisterSidebarItem>()
            .onEach { event ->

            }.launchIn(scope)

        eventHandler.subscribe<CetEvent.UIEvent.RegisterContent>()
            .onEach { event ->

            }.launchIn(scope)
    }

    override fun onDisable() {
        state = PluginState.DISABLED
        val lifeCycleEvent = CetEvent.BaseEvents.PluginLifecycle(
            pluginId = id,
            state = state,
            timestamp = System.currentTimeMillis(),
        )

        if (::scope.isInitialized) {
            val publishJob = scope.launch {
                eventHandler.publish(lifeCycleEvent)
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
        val currentState by viewModel.state.collectAsState()

        MaterialTheme {
            Column {
                // TopBar()
                Row {
                    SideBar(
                        sideBarItems = exampleItems(),
                        selectedItemId = currentState.activeContentId,
                        onItemClick = { item ->
                            currentState.activeContentId = item.contentProviderId
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

data class CorePluginState(
    var activeContentId: String? = null,
    val contentProviders: Map<String, IContentProvider> = emptyMap(),
    val sidebarItems: List<SideBarItem> = emptyList(),
)

class CoreViewModel() {
    private val _state = MutableStateFlow(CorePluginState())
    val state: StateFlow<CorePluginState> = _state.asStateFlow()

    fun addNewProvider(id: String, provider: IContentProvider) {
        _state.value = _state.value.copy(
            contentProviders = _state.value.contentProviders + (id to provider)
        )
    }

    fun addNewSidebarItem(sideBarItem: SideBarItem) {
        _state.value = _state.value.copy(
            sidebarItems = _state.value.sidebarItems + sideBarItem
        )
    }

    fun setActiveContent(contentId: String) {
        _state.value = _state.value.copy(activeContentId = contentId)
    }
}