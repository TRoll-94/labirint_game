package com.labirint.util

import com.labirint.data.Settings
import com.labirint.store.SettingsName
import com.labirint.store.settingsRepository

enum class DifficultyName {
    easy,
    normal,
    hard,
    random,
}

object Difficulty {
    fun getDifficultyName(difficulty: DifficultyName): String {
        return when (difficulty) {
            DifficultyName.easy -> Localization.getString("easy")
            DifficultyName.normal -> Localization.getString("normal")
            DifficultyName.hard -> Localization.getString("hard")
            DifficultyName.random -> Localization.getString("random")
        }
    }

    fun getDifficulties(): List<String> {
        return DifficultyName.entries.map { it.name }
    }

    fun getDifficulty(): String {
        return settingsRepository.getSetting(SettingsName.difficulty)?.let {
            DifficultyName.valueOf(it.settingsValue).name
        } ?: DifficultyName.random.name
    }

    fun setDifficulty(difficulty: String) {
        settingsRepository.insertOrUpdateSettingsObject(Settings(SettingsName.difficulty.name, difficulty))
    }

}