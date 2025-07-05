import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Gutter(lineCount: Int, currentLine: Int) {
    LazyColumn {
        items(lineCount) { i ->
            val lineNumber = i + 1

        }
    }
}