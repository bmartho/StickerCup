package br.cup.stickercontrol.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.cup.stickercontrol.listOfSessions
import br.cup.stickercontrol.model.Sticker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Sticker::class], version = 3, exportSchema = false)
abstract class StickerDatabase : RoomDatabase() {
    abstract fun stickersDAO(): StickersDAO

    private class StickerDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.stickersDAO())
                }
            }
        }

        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
            super.onDestructiveMigration(db)

            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.stickersDAO())
                }
            }
        }

        fun populateDatabase(stickersDAO: StickersDAO) {
            // Delete all content here.
            stickersDAO.deleteAll()

            stickersDAO.insert(generateStickers())
        }

        fun generateStickers(): List<Sticker> {
            val list = mutableListOf<Sticker>()
            var stickerId = 1
            var sessionNumber = 1
            for (session in listOfSessions) {
                if (sessionNumber == 1) {
                    list.add(
                        Sticker(
                            id = stickerId,
                            label = "00",
                            number = "",
                            isMarked = false,
                            numRepeated = 0
                        )
                    )
                } else {
                    for (sessionStickerNumber in 1..session.second) {
                        val numberOfSticker = if (sessionNumber == listOfSessions.size - 1) {
                            (18 + sessionStickerNumber).toString()
                        } else if (sessionStickerNumber < 10) {
                            "0".plus(sessionStickerNumber)
                        } else {
                            sessionStickerNumber.toString()
                        }

                        list.add(
                            Sticker(
                                id = stickerId,
                                label = session.first,
                                number = numberOfSticker,
                                isMarked = false,
                                numRepeated = 0
                            )
                        )
                        stickerId++
                    }
                }
                sessionNumber++
            }
            return list
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: StickerDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): StickerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StickerDatabase::class.java,
                    "sticker_database"
                ).addCallback(StickerDatabaseCallback(scope))
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        //nothing
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("UPDATE sticker SET label = 'IRN' WHERE label = 'IRA'")
        database.execSQL("UPDATE sticker SET label = 'KSA' WHERE label = 'ARA'")
        database.execSQL("UPDATE sticker SET label = 'ESP' WHERE label = 'SPA'")
        database.execSQL("UPDATE sticker SET label = 'CRC' WHERE label = 'COS'")
        database.execSQL("UPDATE sticker SET label = 'JPN' WHERE label = 'JAP'")
        database.execSQL("UPDATE sticker SET label = 'SRB' WHERE label = 'SER'")
        database.execSQL("UPDATE sticker SET label = 'SUI' WHERE label = 'SWL'")
        database.execSQL("UPDATE sticker SET label = 'CMR' WHERE label = 'CAM'")
        database.execSQL("UPDATE sticker SET label = 'GHA' WHERE label = 'GAN'")
    }
}

