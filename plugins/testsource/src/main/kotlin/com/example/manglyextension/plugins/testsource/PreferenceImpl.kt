package com.example.manglyextension.plugins.testsource

import android.content.Context
import android.content.SharedPreferences
import com.example.manglyextension.plugins.IPreferences
import com.example.manglyextension.plugins.PreferenceUi

// This is a sample implementation of IPreferences.
// The actual implementation is being handled by the main Mangly app, all this does is allow create the extension and test it using mock preferences
class PreferenceImpl(
    sharedPreferences: SharedPreferences?,
    context: Context?
) : IPreferences(
    sharedPreferences,
    context) {

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return defaultValue
    }

    override fun setBoolean(key: String, value: Boolean, uiElement: PreferenceUi?) {
        TODO("Not yet implemented")
    }

    override fun getString(key: String, defaultValue: String): String {
        return defaultValue
    }

    override fun setString(key: String, value: String, uiElement: PreferenceUi?) {
        TODO("Not yet implemented")
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return defaultValue
    }

    override fun setInt(key: String, value: Int, uiElement: PreferenceUi?) {
        TODO("Not yet implemented")
    }
}