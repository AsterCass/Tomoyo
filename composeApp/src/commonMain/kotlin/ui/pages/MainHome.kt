package ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainHomeScreen(
) {
    println("reload MainHomeScreen")

    Column(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "This is home page",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}