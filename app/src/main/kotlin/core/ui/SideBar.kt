package core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import SideBarItem

//@Preview
//@Composable
//fun SideBarPreview() {
//    val items = listOf(
//        SideBarItem(
//            id = "Explorer",
//            icon = Icons.Default.AccountBox,
//            tooltip = "Folder Explorer"
//        ),
//
//    )
//
//    SideBar(sideBarItems = items)
//}

@Composable
fun SideBar(
    sideBarItems: List<SideBarItem>,
    selectedItemId: String? = null,
    onItemClick: (SideBarItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .width(48.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp,
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(8.dp),
        ) {
            items<SideBarItem>(sideBarItems) { item ->
                SideBarButton(
                    item.icon,
                    item.tooltip,
                    isSelected = item.contentProviderId == selectedItemId,
                    onClick = { onItemClick(item) }
                )
            }
        }
    }
}

@Composable
fun SideBarButton(
    icon: ImageVector,
    tooltip: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.Transparent
    }

    val iconColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }


    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.small
            )
            .selectable(
                selected = isSelected,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = tooltip,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
    }
}

