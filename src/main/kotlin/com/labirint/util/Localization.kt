package com.labirint.util

import androidx.compose.runtime.mutableStateOf
import com.labirint.data.Settings
import com.labirint.store.SettingsName
import com.labirint.store.settingsRepository
import java.util.*


object Localization {
    private var _bundle = mutableStateOf<ResourceBundle?>(null)
    private val bundle: ResourceBundle
        get() = _bundle.value ?: throw IllegalStateException("ResourceBundle not initialized")

    init {
        settingsRepository.getSetting(SettingsName.lang)?.let {
            setLanguage(it.settingsValue)
        } ?:
        setLanguage("pl", "PL")
    }

    fun getString(key: String): String {
        return bundle.getString(key)
    }

    fun getLanguage(): String {
        return bundle.locale.language
    }

    fun getLanguages(): List<String> {
        return listOf("ru", "pl", "en")
    }

    fun setLanguage(language: String, country: String = "") {
        _bundle.value = ResourceBundle.getBundle("Messages", Locale(language, country))
        settingsRepository.insertOrUpdateSettingsObject(Settings(SettingsName.lang.name, language))
    }
}
