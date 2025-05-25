package core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pluginSystem.IContentProvider

@Composable
fun MainArea(
    activeContentId: String?,
    contentProviders: Map<String, IContentProvider>,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        when {
            activeContentId == null -> {
                WelcomeScreen()
            }
            contentProviders.containsKey(activeContentId) -> {
                contentProviders[activeContentId]?.ProvideMainContent()
            }
            else -> {
                ErrorScreen("Content not found: $activeContentId")
            }
        }
    }
}

@Composable
private fun WelcomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text("Welcome!")
    }
}

@Composable
private fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(message, color = MaterialTheme.colorScheme.error)
    }
}