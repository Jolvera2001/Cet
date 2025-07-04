import androidx.compose.runtime.Composable

interface IPlugin {
    val id: String
    val version: String
    val state: PluginState
    suspend fun onInitialize(context: PluginContext)
    fun onDisable()
}

interface ICorePlugin {
    @Composable
    fun RootUI()
}

enum class PluginState {
    STOPPED,
    ACTIVE,
    DISABLED,
    FAILED
}

