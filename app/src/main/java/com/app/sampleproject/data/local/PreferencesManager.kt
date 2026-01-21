package com.app.sampleproject.data.local

import android.content.Context
import android.content.SharedPreferences
import com.app.sampleproject.data.remote.dto.UserDataDto
import com.google.gson.Gson
import javax.inject.Inject
import androidx.core.content.edit

class PreferencesManager @Inject constructor(
    context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "PartyHardPrefs",
        Context.MODE_PRIVATE
    )
    private val gson = Gson()

    companion object {
        private const val KEY_USER_DATA = "user_data"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    fun saveUserData(userData: UserDataDto) {
        val json = gson.toJson(userData)
        prefs.edit {
            putString(KEY_USER_DATA, json)
                .putBoolean(KEY_IS_LOGGED_IN, true)
        }
    }

    fun getUserData(): UserDataDto? {
        val json = prefs.getString(KEY_USER_DATA, null) ?: return null
        return try {
            gson.fromJson(json, UserDataDto::class.java)
        } catch (_: Exception) {
            null
        }
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun clearUserData() {
        prefs.edit {
            remove(KEY_USER_DATA)
                .putBoolean(KEY_IS_LOGGED_IN, false)
        }
    }
}