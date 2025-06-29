import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import javax.swing.BoxLayout
import javax.swing.JPanel

class EditorPlugin() : BasePlugin(), IContentProvider {
    override val id: String = "editor-plugin"
    override val version: String = "0.1.0"
    private val providerId: String = "editor-main"
    private val tooltipText: String = "Code Editor"
    private val viewModel = CodeEditorViewModel()

    override suspend fun onInitialize(context: PluginContext) {
        super.onInitialize(context)

        val scope = pluginContext.scope
        val eventSystem = pluginContext.eventSystem

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
        val currentState = viewModel.state.collectAsState()

        TextField(
            state = viewModel.state.value.content,
            lineLimits = TextFieldLineLimits.MultiLine(),
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}