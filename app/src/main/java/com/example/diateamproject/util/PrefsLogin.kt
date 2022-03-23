package com.example.diateamproject.util

import com.pixplicity.easyprefs.library.Prefs

class PrefsLogin {
    companion object {

        fun saveString(key: String, value: String) {
            Prefs.putString(key, value);
        }

        fun saveInt(key: String, value: Int) {
            Prefs.putInt(key, value)
        }

        fun loadString(key: String, defaultValue: String): String {
            return Prefs.getString(key, defaultValue)
        }

        fun loadInt(key: String, defaultValue: Int): Int {
            return Prefs.getInt(key, defaultValue)
        }

        fun clear() {
            Prefs.clear()
                .apply()
        }
    }
}