package com.labirint

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.labirint.db.Database

val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:test.db")
fun createDb() {
    try {
        Database.Schema.create(driver)
    } catch (e: Exception) {
        null
    }
}
