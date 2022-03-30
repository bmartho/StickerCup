package br.cup.stickercontrol.album.ui

import androidx.lifecycle.*
import br.cup.stickercontrol.data.repositories.StickerRepository
import br.cup.stickercontrol.model.Sticker

class AlbumViewModel(private val repository: StickerRepository) : ViewModel() {
    private val _isRepeatedTab = MutableLiveData(false)
    val isRepeatedTab: LiveData<Boolean> = _isRepeatedTab
    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading
    private val _clearAll = MutableLiveData(false)
    val clearAll: LiveData<Boolean> = _clearAll
    private val _shareStickers = MutableLiveData(
        ShareOptions(
            missingStickers = false,
            repeatedStickers = false
        )
    )

    fun init(){
        _isRepeatedTab.value = false
        _isLoading.value = true
        _clearAll.value = false
        _shareStickers.value = ShareOptions(
            missingStickers = false,
            repeatedStickers = false
        )
    }

    val shareStickers: LiveData<ShareOptions> = _shareStickers

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

    suspend fun clearAll(value: Boolean) {
        _clearAll.value = value
        repository.clearAll()
    }

    fun shareStickers(value: ShareOptions) {
        _shareStickers.value = value
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

data class ShareOptions(
    val missingStickers: Boolean,
    val repeatedStickers: Boolean
) {
    fun isInvalid() = !missingStickers && !repeatedStickers
}