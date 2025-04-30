package com.example.clubactivity_app.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("ClubActivityPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_CURRENT_USER = "current_user"
    }

    fun saveLoginState(isLoggedIn: Boolean) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun saveCurrentUser(userId: Int) {
        prefs.edit().putInt(KEY_CURRENT_USER, userId).apply()
    }

    fun getCurrentUser(): Int {
        return prefs.getInt(KEY_CURRENT_USER, -1)
    }

    fun clearSession() {
        prefs.edit()
            .remove(KEY_IS_LOGGED_IN)
            .remove(KEY_CURRENT_USER)
            .apply()
    }
}