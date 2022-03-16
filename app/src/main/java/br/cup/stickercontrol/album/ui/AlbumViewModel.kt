package br.cup.stickercontrol.album.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import br.cup.stickercontrol.data.repositories.StickerRepository
import br.cup.stickercontrol.model.Sticker

class AlbumViewModel(private val repository: StickerRepository) : ViewModel() {
    val allStickers : LiveData<List<Sticker>> = repository.getAllStickers().asLiveData()

    suspend fun updateSticker(sticker: Sticker) {
        repository.updateSticker(sticker)
    }
}

class AlbumViewModelFactory(private val repository: StickerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlbumViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}