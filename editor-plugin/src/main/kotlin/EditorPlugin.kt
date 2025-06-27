import androidx.compose.foundation.layout.fillMaxSize
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
        var code by remember { mutableStateOf("") }

        SwingPanel(
            modifier = Modifier
                .fillMaxSize(),
            background = Color.White,
            factory = {
                val textArea = RSyntaxTextArea(20, 60).apply {
                    syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JAVA
                    isCodeFoldingEnabled = true
                    antiAliasingEnabled = true
                    text = code

                    document.addDocumentListener(object : javax.swing.event.DocumentListener {
                        override fun insertUpdate(e: javax.swing.event.DocumentEvent) {
                            code = text
                        }

                        override fun removeUpdate(e: javax.swing.event.DocumentEvent) {
                            code = text
                        }

                        override fun changedUpdate(e: javax.swing.event.DocumentEvent) {
                            code = text
                        }
                    })
                }

                val scrollPane = RTextScrollPane(textArea)

                JPanel().apply {
                    layout = BoxLayout(this, BoxLayout.Y_AXIS)
                    add(scrollPane)
                }
            },
            update = { panel ->
                val scrollPane = panel.components[0] as RTextScrollPane
                val textArea = scrollPane.textArea as RSyntaxTextArea

                if (textArea.text != code) {
                    textArea.text = code
                }
            }
        )
    }
}