package pluginSystem

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapMerge

/**
 * Event system that the plugin system relies on for
 * plugins to send in events and receive/listen to others
 */
class EventHandler {
    // TODO: Change into another type, String is a placeholder for now
    private val _events = MutableSharedFlow<CetEvent>()
    val events = _events.asSharedFlow()

    /**
     * Provides access to event flow for plugins to subscribe to.
     * Here is a starting example:
     * ```
     * override fun onInitialize(eventHandler: EventHandler) {
     *         state = PluginState.ACTIVE
     *         eventHandler.Subscribe<SomeEvent>().onEach { event ->
     *             when (event) {
     *                 // TODO: define events
     *             }
     *         }.launchIn(scope)
     *     }
     * ```
     *
     * @return A flow emitting the specified event type
     */
    inline fun <reified T: CetEvent> subscribe(): Flow<T> = events.filterIsInstance<T>()

    /**
     * Allows plugins to send in events to the event system for other plugins to react to.
     *
     * @param event The event that the plugin is sending to the event system
     */
    suspend fun publish(event: CetEvent) {
        _events.emit(event)
    }

    fun publishBlocking(event: CetEvent) {
        _events.tryEmit(event)
    }
}

abstract class CetEvent {
    abstract val timestamp: Long

    // some event available for plugins to use straightaway
    sealed class BaseEvents : CetEvent() {
        data class PluginLifecycle(
            val pluginId: String,
            val state: PluginState,
            override val timestamp: Long
        ) : BaseEvents()
    }

    sealed class UIEvent : BaseEvents() {
        data class RegisterSidebarItem(
            override val timestamp: Long,
            val pluginId: String,
            val item: SideBarItem,
        ) : UIEvent()

        data class RegisterContent(
            val pluginId: String,
            val providerId: String,
            override val timestamp: Long,
            val contentProvider: IContentProvider
        ) : UIEvent()
    }

    abstract class PluginEvent : CetEvent() {
        // left empty to let plugins implement
    }
}