import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CodeEditorViewModel(private val scope: CoroutineScope) {
    val textFieldState = TextFieldState(initialText = "Hello world")

    init {
        scope.launch {
            snapshotFlow { textFieldState.text.toString() }
                .collect { newText ->
                    println(newText)
                }
        }
    }
}