package core.ui

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SideBar() {
    Surface(
        modifier = Modifier.width(250.dp),
        color = MaterialTheme.colors.surface,
    ) {
        LazyColumn {
            items(5) { index ->
                Button( onClick = {} ) { Text("Item: $index") }
            }
        }
    }
}