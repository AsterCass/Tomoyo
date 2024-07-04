package data.session

import java.util.prefs.Preferences

class UserPreferences {
    private val preferences: Preferences = Preferences.userRoot().node(this::class.java.name)

    fun storeData(key: String, value: String) = preferences.put(key, value)
    fun getData(key: String): String? = preferences.get(key, null)
    fun deleteData(key: String) = preferences.remove(key)
}