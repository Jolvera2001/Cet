import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterIsInstance

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
     * override fun onInitialize(eventHandler: EventHandler, scope: CoroutineScope) {
     *         state = PluginState.ACTIVE
     *         eventHandler.Subscribe<SomeEvent>().onEach { event ->
     *             ...
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