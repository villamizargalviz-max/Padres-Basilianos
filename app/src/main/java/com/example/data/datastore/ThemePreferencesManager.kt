package com.example.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ui.theme.BasilianAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.basilianDataStore: DataStore<Preferences> by preferencesDataStore(name = "basilian_app_preferences")

class ThemePreferencesManager(private val context: Context) {

    companion object {
        val KEY_THEME_ID = stringPreferencesKey("selected_app_theme_id")
        val KEY_CUSTOM_CREST_URI = stringPreferencesKey("custom_crest_uri")
        val KEY_CUSTOM_PRAYER_IMAGE_URI = stringPreferencesKey("custom_prayer_side_image_uri")
        val KEY_CUSTOM_FRATERNITY_COVER_URI = stringPreferencesKey("custom_fraternity_cover_uri")
    }

    val themeIdFlow: Flow<String> = context.basilianDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[KEY_THEME_ID] ?: BasilianAppTheme.ESCUDO_BASILIANO.id
        }

    val customCrestUriFlow: Flow<String?> = context.basilianDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[KEY_CUSTOM_CREST_URI]
        }

    val customPrayerSideImageUriFlow: Flow<String?> = context.basilianDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[KEY_CUSTOM_PRAYER_IMAGE_URI]
        }

    val customFraternityCoverUriFlow: Flow<String?> = context.basilianDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[KEY_CUSTOM_FRATERNITY_COVER_URI]
        }

    suspend fun saveThemeId(themeId: String) {
        context.basilianDataStore.edit { preferences ->
            preferences[KEY_THEME_ID] = themeId
        }
    }

    suspend fun saveCustomCrestUri(uriString: String?) {
        context.basilianDataStore.edit { preferences ->
            if (uriString != null) {
                preferences[KEY_CUSTOM_CREST_URI] = uriString
            } else {
                preferences.remove(KEY_CUSTOM_CREST_URI)
            }
        }
    }

    suspend fun saveCustomPrayerSideImageUri(uriString: String?) {
        context.basilianDataStore.edit { preferences ->
            if (uriString != null) {
                preferences[KEY_CUSTOM_PRAYER_IMAGE_URI] = uriString
            } else {
                preferences.remove(KEY_CUSTOM_PRAYER_IMAGE_URI)
            }
        }
    }

    suspend fun saveCustomFraternityCoverUri(uriString: String?) {
        context.basilianDataStore.edit { preferences ->
            if (uriString != null) {
                preferences[KEY_CUSTOM_FRATERNITY_COVER_URI] = uriString
            } else {
                preferences.remove(KEY_CUSTOM_FRATERNITY_COVER_URI)
            }
        }
    }

    suspend fun resetAllCustomImages() {
        context.basilianDataStore.edit { preferences ->
            preferences.remove(KEY_CUSTOM_CREST_URI)
            preferences.remove(KEY_CUSTOM_PRAYER_IMAGE_URI)
            preferences.remove(KEY_CUSTOM_FRATERNITY_COVER_URI)
        }
    }
}
