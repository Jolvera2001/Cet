import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import kotlinx.coroutines.launch
import sharedItems.SideBarItem
import sharedItems.SubMenuItem

class EditorPlugin() : BasePlugin(), IContentProvider {
    override val id: String = "editor"
    override val version: String = "0.1.0"
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

            eventSystem.publish(
                CetEvent.UIEvent.RegisterMenuItem(
                    pluginId = id,
                    menuKey = "File",
                    subItem = SubMenuItem("Open") {
                        println("Open FileDialog")
                        viewModel.openFileDialog()
                    },
                    timestamp = System.currentTimeMillis(),
                )
            )
        }
    }

    @Composable
    override fun ProvideMainContent() {
        CodeEditor(viewModel)
    }

    @Composable
    private fun CodeEditor(viewmodel: CodeEditorViewModel) {
        val state by viewmodel.state.collectAsState()

//        if (state.dialogOpen) {
//            LaunchedEffect(state.dialogOpen) {
//                val fileChooser = JFileChooser().apply {
//                    currentDirectory = File(System.getProperty("user.home"))
//                }
//
//                val result = fileChooser.showOpenDialog(null)
//
//                when (result) {
//                    JFileChooser.APPROVE_OPTION -> {}
//                    JFileChooser.CANCEL_OPTION -> {}
//                    JFileChooser.ERROR_OPTION -> {}
//                }
//
//                viewmodel.closeFileDialog()
//            }
//        }

        // Gutter()
        FilePicker(
            show = state.dialogOpen
        ) {
            platformFile ->
            println("FilePicker callback triggered! File: $platformFile")
            viewmodel.closeFileDialog()
        }
        BasicTextField(
            state = state.textFieldState,
            lineLimits = TextFieldLineLimits.MultiLine(),
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 0.dp),
        )
    }
}