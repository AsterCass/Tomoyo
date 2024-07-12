package ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import constant.enums.WindowsSizeEnum
import data.PlatformInitData

@Composable
actual fun PlatformSetting(
    getPlatformData: () -> PlatformInitData,
    updatePlatformData: (PlatformInitData) -> Unit,
    modifier: Modifier
) {
    var state by remember { mutableStateOf(WindowsSizeEnum.STD) }

    Column(Modifier.selectableGroup()) {
        WindowsSizeEnum.entries.forEach { resolution ->
            Row(
                Modifier.fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (resolution == state),
                        onClick = {
                            state = resolution
                            val platformInitData = getPlatformData()
                            platformInitData.winState.size = resolution.data
                            updatePlatformData(platformInitData)
                        },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (resolution == state),
                    onClick = {}
                )
                Text(
                    text = resolution.title,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }

}