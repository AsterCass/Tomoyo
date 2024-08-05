package data.store

import com.russhwolf.settings.Settings

expect class SettingsWrapper {
    fun createSettings(): Settings
}