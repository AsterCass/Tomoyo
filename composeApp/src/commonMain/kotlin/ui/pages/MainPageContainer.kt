package ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Constraints
import ui.components.NotificationComponent

@Composable
fun MainPageContainerScreen(
    pageScreen: @Composable (Constraints) -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {

        val constraints = this.constraints

        pageScreen(constraints)

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            NotificationComponent()
        }

    }
}


