package PluginSystem

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Event system that the plugin system relies on for
 * plugins to send in events and receive/listen to others
 */
class EventHandler {
    // TODO: Change into another type, String is a placeholder for now
    private val _events = MutableSharedFlow<CetEvent>()
    val events = _events.asSharedFlow()

    /**
     * Provides access to event stream for plugins to subscribe to.
     * Here is a starting example:
     * ```
     * override fun onInitialize(eventHandler: EventHandler) {
     *         state = PluginState.ACTIVE
     *         eventHandler.Subscribe().onEach { event ->
     *             when (event) {
     *                 // TODO: define events
     *             }
     *         }.launchIn(scope)
     *     }
     * ```
     *
     * @return A SharedFlow that emits all published events on the event system
     */
    fun Subscribe(): SharedFlow<CetEvent> = events

    /**
     * Allows plugins to send in events to the event system for other plugins to react to.
     *
     * @param event The event that the plugin is sending to the event system
     */
    suspend fun Publish(event: CetEvent) {
        _events.emit(event)
    }

    fun PublishBlocking(event: CetEvent) {
        _events.tryEmit(event)
    }
}

abstract class CetEvent {
    abstract val name: String
    abstract val timestamp: Long

    // some event available for plugins to use straightaway
    sealed class BaseEvents : CetEvent() {
        data class PluginLifecycleEvent(
            val pluginId: String,
            val state: PluginState,
            override val name: String,
            override val timestamp: Long
        ) : BaseEvents()
    }

    sealed class PluginEvent : CetEvent() {
        // left empty to let plugins implement
    }
}