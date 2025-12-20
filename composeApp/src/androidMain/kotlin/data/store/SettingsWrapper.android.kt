package data.store

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.datastore.DataStoreSettings
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class SettingsWrapper : KoinComponent {
    private val context: Context by inject()

    companion object {
        private val Context.dataStore by preferencesDataStore("userSettings")
    }


    @OptIn(ExperimentalSettingsImplementation::class, ExperimentalSettingsApi::class)
    actual fun createSettings(): FlowSettings {
        return DataStoreSettings(context.dataStore)
    }

}