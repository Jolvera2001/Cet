import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class EditorPlugin() : BasePlugin(), IContentProvider {
    override val id: String = "editor-plugin"
    override val version: String = "0.1.0"
    override val scopeName: String = "editor-plugin-scope"
    private val providerId: String = "editor-main"
    private val tooltipText: String = "Code Editor"
    lateinit var viewModel: CodeEditorViewModel

    override suspend fun onInitialize(context: PluginContext) {
        super.onInitialize(context)

        val scope = pluginContext.scope
        val eventSystem = pluginContext.eventSystem
        viewModel = CodeEditorViewModel(scope)

        scope.launch {
            eventSystem.publish(
                CetEvent.UIEvent.RegisterContent(
                    pluginId = id,
                    providerId = providerId,
                    timestamp = System.currentTimeMillis(),
                    contentProvider = this@EditorPlugin
                )
            )

            eventSystem.publish(
                CetEvent.UIEvent.RegisterSidebarItem(
                    pluginId = id,
                    timestamp = System.currentTimeMillis(),
                    item = SideBarItem(
                        id = "editor",
                        icon = Icons.Default.Edit,
                        tooltip = tooltipText,
                        contentProviderId = providerId
                    ),
                )
            )
        }
    }

    @Composable
    override fun ProvideMainContent() {
        CodeEditor()
    }

    @Composable
    private fun CodeEditor() {
        TextField(
            state = viewModel.textFieldState,
            lineLimits = TextFieldLineLimits.MultiLine(),
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 0.dp),
        )
    }
}