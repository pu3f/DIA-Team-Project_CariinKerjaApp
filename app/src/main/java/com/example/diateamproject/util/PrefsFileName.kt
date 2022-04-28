package com.example.diateamproject.util

import com.pixplicity.easyprefs.library.Prefs

class PrefsFileName {
    companion object {

        fun saveString(key: String, value: String) {
            Prefs.putString(key, value);
        }

        fun loadString(key: String, defaultValue: String): String {
            return Prefs.getString(key, defaultValue)
        }

        fun clear() {
            Prefs.clear()
                .apply()
        }
    }
}