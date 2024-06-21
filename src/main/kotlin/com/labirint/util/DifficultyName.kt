package com.labirint.util

import com.labirint.data.Settings
import com.labirint.store.SettingsName
import com.labirint.store.settingsRepository
import kotlin.random.Random

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

    fun getSizeByDifficulty(): Int {
        val difficulty = getDifficulty()
        return when (difficulty) {
            DifficultyName.easy.name -> Random(0).nextInt(3, 6)
            DifficultyName.normal.name -> Random(0).nextInt(4, 8)
            DifficultyName.hard.name -> Random(0).nextInt(8, 15)
            else -> Random(0).nextInt(4, 15)
        }
    }

    fun setDifficulty(difficulty: String) {
        settingsRepository.insertOrUpdateSettingsObject(Settings(SettingsName.difficulty.name, difficulty))
    }

}