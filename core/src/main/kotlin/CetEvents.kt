import javax.management.monitor.StringMonitor

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
    }

    abstract class PluginEvent : CetEvent()
}