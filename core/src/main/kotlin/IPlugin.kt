import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope

interface IPlugin {
    val id: String
    val version: String
    suspend fun onInitialize(context: PluginContext)
    fun onDisable()
}

interface ICorePlugin : IPlugin {
    @Composable
    fun RootUI()
}

enum class PluginState {
    STOPPED,
    ACTIVE,
    DISABLED,
    FAILED
}

