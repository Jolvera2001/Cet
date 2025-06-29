import androidx.compose.foundation.text.input.TextFieldState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CodeEditorState(
    var content: TextFieldState = TextFieldState(
        initialText = "Hello world!"
    )
    // add fields when document model is introduced
)
class CodeEditorViewModel {
    private val _state = MutableStateFlow(CodeEditorState())
    val state: StateFlow<CodeEditorState> = _state.asStateFlow()
}