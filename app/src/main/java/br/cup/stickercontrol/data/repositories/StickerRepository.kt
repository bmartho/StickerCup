package br.cup.stickercontrol.data.repositories

import br.cup.stickercontrol.data.StickersDAO
import br.cup.stickercontrol.model.Sticker
import kotlinx.coroutines.flow.Flow

class StickerRepository(private val dao: StickersDAO) {
    fun getAllStickers(): Flow<List<Sticker>> = dao.getStickers()

    suspend fun updateSticker(sticker: Sticker) {
        dao.updateSticker(sticker)
    }
}