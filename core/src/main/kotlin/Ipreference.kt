package com.example.manglyextension.plugins


abstract class IPreferences(settingsPreferences: Any?, ctx: Any?) {
    var settings: Any? = settingsPreferences
    var context: Any? = ctx

    abstract fun getBoolean(key: String, defaultValue: Boolean): Boolean
    abstract fun setBoolean(key: String, value: Boolean, uiElement: PreferenceUi? = null)

    abstract fun getString(key: String, defaultValue: String): String
    abstract fun setString(key: String, value: String, uiElement: PreferenceUi? = null)

    abstract fun getInt(key: String, defaultValue: Int): Int
    abstract fun setInt(key: String, value: Int, uiElement: PreferenceUi? = null)

}

enum class PreferenceUi {
    TEXTAREA,
    SWITCH
}