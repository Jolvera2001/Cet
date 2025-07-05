package sharedItems

import androidx.compose.ui.graphics.vector.ImageVector

data class SideBarItem(
    val id: String,
    val icon: ImageVector,
    val tooltip: String,
    val contentProviderId: String? = null
)
