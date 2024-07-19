package ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainHomeScreen(
    modifier: Modifier = Modifier
) {
    println("reload MainHomeScreen")

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "This is home page",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}