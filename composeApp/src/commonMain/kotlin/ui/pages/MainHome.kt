package ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.model.ArticleScreenModel
import org.koin.compose.koinInject

@Composable
fun MainHomeScreen(
    screenModel: ArticleScreenModel = koinInject(),
) {

    println("reload MainHomeScreen")
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 20.dp),
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(5.dp),
                text = "没想好放什么",
                style = MaterialTheme.typography.headlineSmall
            )

        }

    }
}