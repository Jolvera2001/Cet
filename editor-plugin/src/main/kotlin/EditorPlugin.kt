import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import pluginSystem.EventHandler
import pluginSystem.IContentProvider
import pluginSystem.IPlugin

class EditorPlugin(
    override val id: String,
    override val version: String
) : IPlugin, IContentProvider {
    override fun onInitialize(eventHandler: EventHandler, scope: CoroutineScope) {
        TODO("Not yet implemented")
    }

    override fun onDisable() {
        TODO("Not yet implemented")
    }

    @Composable
    override fun ProvideMainContent() {
        TODO("Not yet implemented")
    }

}