package data.store

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toBlockingObservableSettings
import com.russhwolf.settings.set

class DataStorageManager @OptIn(ExperimentalSettingsApi::class) constructor(private val settings: FlowSettings) {

    @OptIn(ExperimentalSettingsApi::class)
    private val observableSettings: ObservableSettings by lazy { settings.toBlockingObservableSettings() }


    fun setString(key: String, value: String) {
        observableSettings.set(key = key, value = value)
    }

    fun getNonFlowString(key: String) = observableSettings.getString(
        key = key,
        defaultValue = "",
    )


    fun clearPreferences() {
        observableSettings.clear()
    }

    companion object {
        const val USER_DATA = "user_data"
        const val FAV_AUDIO_ID_LIST = "fav_audio_id_list"
    }

}