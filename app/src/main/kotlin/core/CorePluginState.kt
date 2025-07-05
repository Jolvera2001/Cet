package core

import IContentProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import sharedItems.SideBarItem
import kotlin.collections.plus

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