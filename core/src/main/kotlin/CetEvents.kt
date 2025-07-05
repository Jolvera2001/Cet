import sharedItems.SideBarItem
import sharedItems.SubMenuItem

/**
 * Event type to be sent into the EventSystem.
 * Contains PluginEvent abstract class to extend
 * for custom plugin events
 */
abstract class CetEvent {
    abstract val timestamp: Long

    sealed class BaseEvents : CetEvent() {
        data class SystemEvent(
            override val timestamp: Long,
            val message: String,
            val type: String
            ) : BaseEvents()
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

        data class RegisterMenuItem(
            val pluginId: String,
            val menuKey: String,
            val subItem: SubMenuItem,
            override val timestamp: Long,
        ) : UIEvent()
    }

    // TODO: Add in tagging system for other plugins to listen for other specific plugins without coupling
    abstract class PluginEvent : CetEvent()
}