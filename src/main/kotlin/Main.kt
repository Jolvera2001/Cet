import pluginSystem.PluginSystem
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent.inject
import javax.swing.BoxLayout
import javax.swing.JPanel


fun main() {
    startAppKoin()

    application {
        Window(
            onCloseRequest = ::exitApplication,
        ) {
            App()
        }

    }
}

@Composable
@Preview
fun App() {

    val pluginSystem: PluginSystem by inject()

    val state = mutableStateOf("public static void main(String[] args) {\n\n}")

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            ) {
                // some ui here in this row
            }
            CodeEditor(state, modifier = Modifier.fillMaxSize().padding(10.dp))
        }
    }
}

@Composable
fun CodeEditor(code: MutableState<String>, modifier: Modifier = Modifier) {
    SwingPanel(
        modifier = modifier,
        background = Color.White,
        factory = {
            // Create a new text area each time
            val textArea = RSyntaxTextArea(20, 60).apply {
                syntaxEditingStyle = SyntaxConstants.SYNTAX_STYLE_JAVA
                isCodeFoldingEnabled = true
                antiAliasingEnabled = true
                text = code.value

                // Update the state when text changes
                document.addDocumentListener(object : javax.swing.event.DocumentListener {
                    override fun insertUpdate(e: javax.swing.event.DocumentEvent) {
                        code.value = text
                    }

                    override fun removeUpdate(e: javax.swing.event.DocumentEvent) {
                        code.value = text
                    }

                    override fun changedUpdate(e: javax.swing.event.DocumentEvent) {
                        code.value = text
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
            if (textArea.text != code.value) {
                textArea.text = code.value
            }
        }
    )
}
