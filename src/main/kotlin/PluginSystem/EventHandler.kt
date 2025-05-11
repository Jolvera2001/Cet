package PluginSystem

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Event system that the plugin system relies on for
 * plugins to send in events and receive/listen to others
 */
class EventHandler {
    // TODO: Change into another type, String is a placeholder for now
    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    /**
     * Provides access to event stream for plugins to subscribe to
     *
     * @return A SharedFlow that emits all published events on the event system
     */
    fun Subscribe() = events

    /**
     * Allows plugins to send in events to the event system for other plugins to react to.
     * **Note**: the type is a work in progress, will not stay a String
     *
     * @param event The event that the plugin is sending to the event system
     */
    fun Publish(event: String) {
        TODO("Not yet implemented")
    }
}