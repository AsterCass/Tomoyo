package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import constant.enums.MainNavigationEnum
import org.jetbrains.compose.resources.stringResource
import theme.unselectedColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    currentScreen: MainNavigationEnum,
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        title = { Text(stringResource(currentScreen.title)) },
    )
}

@Composable
fun MainAppNavigationBar(
    currentScreen: MainNavigationEnum,
    navigationClicked: (MainNavigationEnum) -> Unit,
    extraNavigationList: List<MainNavigationEnum> = emptyList(),
) {

    Row(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        MainNavigationEnum.entries.toTypedArray().forEach { nav ->
            if (nav == MainNavigationEnum.HOME || extraNavigationList.contains(nav)) {
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors().copy(
                        unselectedIconColor = MaterialTheme.colorScheme.unselectedColor,
                    ),
                    icon = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = nav.icon,
                                contentDescription = nav.name,
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                modifier = Modifier.padding(top = 3.dp),
                                text = nav.name,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    },
                    selected = currentScreen == nav,
                    onClick = { navigationClicked(nav) },
                )
            }
        }
    }
}

