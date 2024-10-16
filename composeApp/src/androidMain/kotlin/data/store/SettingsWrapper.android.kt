package data.store

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.datastore.DataStoreSettings

actual class SettingsWrapper(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore("userSettings")
    }


    @OptIn(ExperimentalSettingsImplementation::class, ExperimentalSettingsApi::class)
    actual fun createSettings(): FlowSettings {
        return DataStoreSettings(context.dataStore)
    }

}