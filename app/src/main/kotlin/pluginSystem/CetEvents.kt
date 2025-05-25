package pluginSystem

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