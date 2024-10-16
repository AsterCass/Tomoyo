package data.store

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import platform.Foundation.NSUserDefaults

actual class SettingsWrapper {
    @OptIn(ExperimentalSettingsApi::class)
    actual fun createSettings(): FlowSettings {
        val delegate: NSUserDefaults = NSUserDefaults.standardUserDefaults
        return NSUserDefaultsSettings(delegate).toFlowSettings()
    }
}