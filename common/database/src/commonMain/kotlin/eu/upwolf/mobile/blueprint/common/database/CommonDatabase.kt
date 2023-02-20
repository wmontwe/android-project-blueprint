/*
 * ISC License
 *
 * Copyright (c) 2022. Wolf-Martell Montwé (bitfunk)
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
 * REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
 * LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
 * OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THIS SOFTWARE.
 */

package eu.upwolf.mobile.blueprint.common.database

import com.squareup.sqldelight.db.SqlDriver

// TODO
// - add logger
// - add background dispatcher
class CommonDatabase(
    private val driverProvider: suspend (SqlDriver.Schema) -> SqlDriver,
) : CommonDatabaseContract {

    private var database: CommonSqlDatabase? = null

    suspend fun initDatabase() {
        if (database == null) {
            database = driverProvider(CommonSqlDatabase.Schema).createDatabase()
        }
    }

    suspend operator fun <R> invoke(block: suspend (CommonSqlDatabase) -> R): R {
        initDatabase()
        return block(database!!)
    }

    private fun SqlDriver.createDatabase(): CommonSqlDatabase {
        return CommonSqlDatabase.invoke(this)
    }
}
