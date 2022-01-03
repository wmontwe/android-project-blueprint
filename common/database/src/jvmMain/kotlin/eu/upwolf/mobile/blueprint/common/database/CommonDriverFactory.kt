/*
 * Copyright (c) 2021 Wolf-Martell Montwé. All rights reserved.
 */

package eu.upwolf.mobile.blueprint.common.database

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

actual class CommonDriverFactory() : CommonDatabaseContract.DriverFactory {

    actual override suspend fun createDriver(schema: SqlDriver.Schema): SqlDriver {
        return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also { driver ->
            schema.create(driver)
        }
    }
}
