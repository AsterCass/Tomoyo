package ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import data.PlatformInitData

@Composable
actual fun PlatformSetting(
    getPlatformData: () -> PlatformInitData,
    updatePlatformData: (PlatformInitData) -> Unit,
    modifier: Modifier
) {

    Column {

    }
}