package data.store

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

actual class SettingsWrapper(private val context: Context) {

    actual fun createSettings(): Settings {
        val delegate = context.getSharedPreferences("tomoyo_preferences", Context.MODE_PRIVATE)
        return SharedPreferencesSettings(delegate)
    }

}