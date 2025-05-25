import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import javax.swing.BoxLayout
import javax.swing.JPanel

class EditorPlugin(
    override val id: String,
    override val version: String,
) : IPlugin, IContentProvider {
    private val providerId: String = "editor-main"
    private val tooltipText: String = "Code Editor"
    private var state = PluginState.STOPPED
    private lateinit var scope: CoroutineScope
    private lateinit var eventHandler: EventHandler

    override suspend fun onInitialize(eventHandler: EventHandler, scope: CoroutineScope) {
        state = PluginState.ACTIVE
        this.scope = scope

        scope.launch {
            eventHandler.publish(
                CetEvent.UIEvent.RegisterContent(
                    pluginId = id,
                    providerId = providerId,
                    timestamp = System.currentTimeMillis(),
                    contentProvider = this@EditorPlugin
                )
            )

            eventHandler.publish(
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

    override fun onDisable() {
        state = PluginState.DISABLED
        val lifeCycleEvent = CetEvent.BaseEvents.PluginLifecycle(
            pluginId = id,
            state = state,
            timestamp = System.currentTimeMillis(),
        )

        if (::scope.isInitialized) {
            val publishJob = scope.launch {
                eventHandler.publish(lifeCycleEvent)
            }

            scope.launch {
                publishJob.join()
                scope.cancel()
            }
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
                .fillMaxSize()
                .padding(10.dp),
            background = Color.White,
            factory = {
                // Create a new text area each time
                val textArea = RSyntaxTextArea(20, 60).apply {
                    syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JAVA
                    isCodeFoldingEnabled = true
                    antiAliasingEnabled = true
                    text = code

                    // Update the state when text changes
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

                // Create scroll pane with text area
                val scrollPane = RTextScrollPane(textArea)

                // Create panel to hold the scroll pane
                JPanel().apply {
                    layout = BoxLayout(this, BoxLayout.Y_AXIS)
                    add(scrollPane)
                }
            },
            update = { panel ->
                // Update logic if needed
                val scrollPane = panel.components[0] as RTextScrollPane
                val textArea = scrollPane.textArea as RSyntaxTextArea

                // Only update if the text is different to avoid infinite loops
                if (textArea.text != code) {
                    textArea.text = code
                }
            }
        )
    }
}