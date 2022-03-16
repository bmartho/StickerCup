package br.cup.stickercontrol.album.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.cup.stickercontrol.StickerControlApplication
import br.cup.stickercontrol.components.AlbumStickerButton
import br.cup.stickercontrol.databinding.FragmentAlbumBinding
import br.cup.stickercontrol.model.Sticker
import kotlinx.coroutines.runBlocking

class AlbumFragment : Fragment(), UpdateStickerInterface {

    private lateinit var binding: FragmentAlbumBinding
    private lateinit var appContext: Context

    private val albumViewModel: AlbumViewModel by viewModels {
        AlbumViewModelFactory((requireActivity().application as StickerControlApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        context?.let {
            appContext = it
        }

        albumViewModel.allStickers.observe(requireActivity()) { allStickers ->
            if (allStickers.isNotEmpty()) {
                binding.mainGrid.adapter =
                    StickersAdapter(buildAlbumStickerButtonObjects(allStickers))

                albumViewModel.allStickers.removeObservers(requireActivity())
            }
        }

        return binding.root
    }

    private fun buildAlbumStickerButtonObjects(listStickers: List<Sticker>): List<AlbumStickerButton> {
        val list = mutableListOf<AlbumStickerButton>()
        listStickers.forEach { sticker ->
            list.add(AlbumStickerButton(appContext, sticker, this))
        }

        return list
    }

    override fun updateSticker(sticker: Sticker) {
        runBlocking {
            albumViewModel.updateSticker(sticker)
        }
    }
}