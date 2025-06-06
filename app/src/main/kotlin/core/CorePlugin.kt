package core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import core.ui.MainArea
import core.ui.SideBar
import CetEvent
import EventHandler
import IPlugin
import PluginState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import IContentProvider
import ICorePlugin
import SideBarItem

class CorePlugin() : IPlugin, ICorePlugin {
    override val id = "core"
    override val version = "0.1.0"
    var state = PluginState.STOPPED
    var viewModel = CoreViewModel()
    private lateinit var scope: CoroutineScope

    private lateinit var eventHandler: EventHandler

    override suspend fun onInitialize(eventHandler: EventHandler, scope: CoroutineScope) {
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
                viewModel.addNewSidebarItem(event.item)
            }.launchIn(scope)

        eventHandler.subscribe<CetEvent.UIEvent.RegisterContent>()
            .onEach { event ->
                viewModel.addNewProvider(event.providerId, event.contentProvider)
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

    fun setActiveContent(contentId: String?) {
        _state.value = _state.value.copy(activeContentId = contentId)
    }
}