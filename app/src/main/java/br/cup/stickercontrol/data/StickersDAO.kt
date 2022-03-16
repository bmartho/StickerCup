package br.cup.stickercontrol.data

import androidx.room.*
import br.cup.stickercontrol.model.Sticker
import kotlinx.coroutines.flow.Flow

@Dao
interface StickersDAO {
    @Query("SELECT * FROM sticker ORDER BY id ASC")
    fun getStickers(): Flow<List<Sticker>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(listSticker: List<Sticker>)

    @Query("DELETE FROM sticker")
    fun deleteAll()

    @Update
    suspend fun updateSticker(sticker: Sticker)
}