package ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import data.PlatformInitData


@Composable
expect fun PlatformSetting(
    getPlatformData: () -> PlatformInitData = { PlatformInitData() },
    updatePlatformData: (PlatformInitData) -> Unit = {},
    modifier: Modifier,
)
