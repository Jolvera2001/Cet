package pluginSystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

interface IContentProvider {
    @Composable
    fun ProvideMainContent()
}

data class SideBarItem(
    val id: String,
    val icon: ImageVector,
    val tooltip: String,
    val contentProviderId: String? = null
)