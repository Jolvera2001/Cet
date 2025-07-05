import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

interface IContentProvider {
    @Composable
    fun ProvideMainContent()
}