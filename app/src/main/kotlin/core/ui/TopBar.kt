package core.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sharedItems.SubMenuItem

@Preview
@Composable
fun TopBar(menuItems: Map<String, List<SubMenuItem>>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(MaterialTheme.colorScheme.surfaceDim),
    ) {
        LazyRow {
            items(menuItems.toList()) {(menu, subMenu) ->
                Dropdown(menu, subMenu)
            }
        }
    }
}

@Composable
fun Dropdown(menuName: String, menuItems: List<SubMenuItem>) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextButton(
            onClick = { expanded = true },
            shape = RectangleShape,
        ) {
            Text(
                menuName,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        DropdownMenu(
            expanded = expanded,
            offset = DpOffset(0.dp, 0.dp),
            shape = MenuDefaults.shape,
            onDismissRequest = { expanded = false }
        ) {
            menuItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.text) },
                    onClick = {
                        item.callback
                        expanded = false
                    }
                )
            }
        }
    }
}