package br.cup.stickercontrol

import android.app.Application
import br.cup.stickercontrol.data.StickerDatabase
import br.cup.stickercontrol.data.repositories.StickerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class StickerControlApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { StickerDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { StickerRepository(database.stickersDAO()) }
}