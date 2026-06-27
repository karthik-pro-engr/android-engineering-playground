package com.karthik.pro.engr.github.api.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {

    val MIGRATION_1_2 = object : Migration(
        1,
        2
    ) {

        override fun migrate(
            db: SupportSQLiteDatabase
        ) {

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS RepoLanguageEntity(
                    repoId INTEGER NOT NULL,
                    language TEXT NOT NULL,
                    bytes INTEGER NOT NULL,
                    PRIMARY KEY(repoId, language),
                    FOREIGN KEY(repoId)
                        REFERENCES RepoEntity(id)
                        ON DELETE CASCADE
                )
                """.trimIndent()
            )

            db.execSQL(
                """
                CREATE INDEX IF NOT EXISTS
                index_RepoLanguageEntity_repoId
                ON RepoLanguageEntity(repoId)
                """.trimIndent()
            )

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS RepoReleaseEntity(
                    id INTEGER NOT NULL,
                    repoId INTEGER NOT NULL,
                    title TEXT NOT NULL,
                    version TEXT NOT NULL,
                    description TEXT NOT NULL,
                    date TEXT NOT NULL,
                    PRIMARY KEY(id),
                    FOREIGN KEY(repoId)
                        REFERENCES RepoEntity(id)
                        ON DELETE CASCADE
                )
                """.trimIndent()
            )

            db.execSQL(
                """
                CREATE INDEX IF NOT EXISTS
                index_RepoReleaseEntity_repoId
                ON RepoReleaseEntity(repoId)
                """.trimIndent()
            )
        }
    }
}