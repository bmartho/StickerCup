package br.cup.stickercontrol.album.ui

import androidx.lifecycle.*
import br.cup.stickercontrol.data.repositories.StickerRepository
import br.cup.stickercontrol.model.Sticker

class AlbumViewModel(private val repository: StickerRepository) : ViewModel() {
    private val _isRepeatedTab = MutableLiveData(false)
    val isRepeatedTab: LiveData<Boolean> = _isRepeatedTab
    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    val allStickers: LiveData<List<Sticker>> = repository.getAllStickers().asLiveData()

    suspend fun updateSticker(sticker: Sticker) {
        repository.updateSticker(sticker)
    }

    fun setRepeatedTab(value: Boolean) {
        _isRepeatedTab.value = value
    }

    fun setLoading(value: Boolean) {
        _isLoading.value = value
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