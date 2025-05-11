package PluginSystem

/**
 * Core system that handles plugins for the Cet app.
 * Planned to have an event system and plugin tracking
 *
 * @property[EventHandler] Property containing the event system for plugins to send and listen to events
 * @property[PluginDirectory] Property containing directory of plugins within the application
 */
class PluginSystem {
    private val _eventSystem = EventHandler()

    fun Startup() {
        TODO()
    }
}