package ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import data.PlatformInitData
import ui.components.PlatformSetting

@Composable
fun MainSettingsScreen(
    getPlatformData: () -> PlatformInitData = { PlatformInitData() },
    updatePlatformData: (PlatformInitData) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "This is settings page",
            style = MaterialTheme.typography.headlineSmall
        )

        PlatformSetting(
            getPlatformData = getPlatformData,
            updatePlatformData = updatePlatformData,
            modifier = modifier,
        )

    }
}