package br.cup.stickercontrol.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.cup.stickercontrol.model.Sticker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Sticker::class], version = 1, exportSchema = false)
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

        fun populateDatabase(stickersDAO: StickersDAO) {
            // Delete all content here.
            stickersDAO.deleteAll()

            stickersDAO.insert(generateStickers())
        }

        fun generateStickers(): List<Sticker> {
            val list = mutableListOf<Sticker>()
            for (stickerNumber in 1..NUMBER_OF_STICKERS) {
                list.add(Sticker(stickerNumber, stickerNumber.toString(), false, 0))
            }
            return list
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: StickerDatabase? = null

        private const val NUMBER_OF_STICKERS = 500

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
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}

//TO THE FUTURE
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("UPDATE sticker SET number = 'abc' WHERE id = 5")
    }
}

