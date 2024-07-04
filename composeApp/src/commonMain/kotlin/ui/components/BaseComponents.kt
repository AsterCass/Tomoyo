package ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import constant.enums.MainNavigationEnum
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    currentScreen: MainNavigationEnum,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        modifier = modifier,
    )
}

@Composable
fun MainAppNavigationBar(
    currentScreen: MainNavigationEnum,
    navigationClicked: (MainNavigationEnum) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
    ) {
        MainNavigationEnum.entries.toTypedArray().forEach { nav ->
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Favorite, contentDescription = nav.name) },
                label = { Text(nav.name) },
                selected = currentScreen == nav,
                onClick = { navigationClicked(nav) }
            )
        }
    }
}

