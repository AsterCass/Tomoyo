package data.store

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import java.util.prefs.Preferences

actual class SettingsWrapper {
    @OptIn(ExperimentalSettingsApi::class)
    actual fun createSettings(): FlowSettings? {
        val delegate: Preferences = Preferences.userRoot()
        return PreferencesSettings(delegate).toFlowSettings()
    }
}