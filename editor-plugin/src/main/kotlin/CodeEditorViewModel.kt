import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CodeEditorState(
    var currentFileName: String? = null,
    var textFieldState: TextFieldState = TextFieldState(),
    var dialogOpen: Boolean = false,
)

class CodeEditorViewModel(private val scope: CoroutineScope) {
    private val _state = MutableStateFlow(CodeEditorState())
    val state: StateFlow<CodeEditorState> = _state.asStateFlow()

    init {
        scope.launch {
            snapshotFlow { _state.value.textFieldState?.text.toString() }
                .collect { newText ->
                    println(newText)
                }
        }
    }

    fun openFileDialog() {
        _state.value = _state.value.copy(dialogOpen = true)
    }

    fun closeFileDialog() {
        _state.value = _state.value.copy(dialogOpen = false)
    }

    fun openFile() {
        try {

        } catch (e: Exception) {
            println("Error: ${e.localizedMessage}")
        }
    }
}