package br.cup.stickercontrol.album.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.cup.stickercontrol.components.AlbumStickerButton
import br.cup.stickercontrol.databinding.FragmentAlbumBinding
import br.cup.stickercontrol.model.Sticker
import kotlinx.coroutines.runBlocking

class AlbumFragment : Fragment(), UpdateStickerInterface {

    private lateinit var binding: FragmentAlbumBinding
    private lateinit var appContext: Context
    private lateinit var adapter: StickersAdapter
    private var stickerList: List<Sticker> = listOf()

    private lateinit var albumViewModel: AlbumViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        context?.let {
            appContext = it
        }

        albumViewModel = ViewModelProvider(requireActivity()).get(AlbumViewModel::class.java)

        albumViewModel.allStickers.observe(requireActivity()) { allStickers ->
            stickerList = allStickers
            if (allStickers.isNotEmpty()) {
                adapter = StickersAdapter(allStickers, requireContext(), this)
                binding.mainGrid.adapter = adapter

                albumViewModel.setLoading(false)
                albumViewModel.allStickers.removeObservers(requireActivity())
            }
        }

        albumViewModel.isRepeatedTab.observe(requireActivity()) { isRepeatedTab ->
            if (stickerList.isNotEmpty()) {
                adapter.setRepeatedTab(isRepeatedTab)
            }
        }

        return binding.root
    }

    override fun updateSticker(sticker: Sticker) {
        runBlocking {
            albumViewModel.updateSticker(sticker)
        }
    }
}