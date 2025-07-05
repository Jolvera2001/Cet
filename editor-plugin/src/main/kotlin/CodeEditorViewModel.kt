import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CodeEditorState(
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

    fun FileFlip() {
        _state.value.dialogOpen = !_state.value.dialogOpen
    }
    fun OpenFile() {

    }
}