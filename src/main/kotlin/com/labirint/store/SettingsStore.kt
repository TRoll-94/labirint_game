package com.labirint.store

import app.cash.sqldelight.db.SqlDriver
import com.labirint.data.Settings
import com.labirint.db.Database
import com.labirint.driver


enum class SettingsName {
    lang,
    difficulty,
}


class SettingsRepository(private val driver: SqlDriver) {
    private val database = Database(driver)

    fun getSetting(name: SettingsName): Settings? {
        return database.settingsQueries.getByNameSettingsObject(name.name).executeAsOneOrNull()
    }

    fun getAllSettings(): List<Settings> {
        return database.settingsQueries.selectAll().executeAsList()
    }

    fun insertOrUpdateSettingsObject(settings: Settings) {
        database.settingsQueries.insertOrUpdateSettingsObject(settings)
    }

}
val settingsRepository = SettingsRepository(driver)