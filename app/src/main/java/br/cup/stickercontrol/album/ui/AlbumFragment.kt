package br.cup.stickercontrol.album.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.cup.stickercontrol.R
import br.cup.stickercontrol.databinding.FragmentAlbumBinding
import br.cup.stickercontrol.model.Sticker
import kotlinx.coroutines.runBlocking

class AlbumFragment : Fragment(), UpdateStickerInterface {

    private lateinit var binding: FragmentAlbumBinding
    private lateinit var appContext: Context
    private lateinit var adapter: StickersAdapter
    private var stickerList: List<Sticker> = listOf()

    private lateinit var albumViewModel: AlbumViewModel
    private var updateGrid = true
    private var isRepeatedTab = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        context?.let {
            appContext = it
        }

        albumViewModel = ViewModelProvider(requireActivity()).get(AlbumViewModel::class.java)

        albumViewModel.allStickers.observe(viewLifecycleOwner) { allStickers ->
            if (updateGrid) {
                stickerList = allStickers
                if (allStickers.isNotEmpty()) {
                    adapter = StickersAdapter(allStickers, requireContext(), this, isRepeatedTab)
                    binding.mainGrid.adapter = adapter

                    albumViewModel.setLoading(false)
                    updateGrid = false
                }
            }

            updateTotalStickerText()
        }

        albumViewModel.isRepeatedTab.observe(viewLifecycleOwner) { value ->
            isRepeatedTab = value
            if (stickerList.isNotEmpty()) {
                adapter.setRepeatedTab(isRepeatedTab)
            }
        }

        albumViewModel.clearAll.observe(viewLifecycleOwner) { clearAll ->
            if (clearAll) {
                updateGrid = true
            }
        }

        albumViewModel.shareStickers.observe(viewLifecycleOwner) { shareOptions ->
            if (!shareOptions.isInvalid()) {
                if (hasStickersToShare(shareOptions)) {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, getShareString(shareOptions))
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                } else {
                    Toast.makeText(
                        requireContext(),
                        R.string.share_options_error,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        return binding.root
    }

    private fun updateTotalStickerText() {
        val totalMarked = stickerList.filter { sticker -> sticker.isMarked }.size
        val percentOfAlbum: Double = (totalMarked.toDouble() / stickerList.size) * 100
        binding.totalStickersText.text =
            getString(R.string.total_stickers, totalMarked, stickerList.size, percentOfAlbum)
    }

    private fun hasStickersToShare(shareOptions: ShareOptions): Boolean {
        var value = false
        for (sticker in stickerList) {
            if (shareOptions.missingStickers && !sticker.isMarked) {
                value = true
                break
            }

            if (shareOptions.repeatedStickers && sticker.numRepeated > 0) {
                value = true
                break
            }
        }
        return value
    }

    override fun updateSticker(sticker: Sticker) {
        runBlocking {
            albumViewModel.updateSticker(sticker)
        }
    }

    private fun getShareString(shareOptions: ShareOptions): String {
        var stringToShare = ""
        if (shareOptions.missingStickers) {
            val missingNumbers = stickerList
                .filter { sticker ->
                    !sticker.isMarked
                }
                .joinToString(", ") { sticker ->
                    sticker.number
                }

            if (missingNumbers.isNotBlank()) {
                stringToShare = stringToShare.plus(getString(R.string.share_stickers_missed))
                stringToShare = stringToShare.plus("\n\n")
                stringToShare = stringToShare.plus(missingNumbers)
            }
        }

        if (shareOptions.repeatedStickers) {
            if (stringToShare.isNotBlank()) {
                stringToShare = stringToShare.plus("\n\n")
            }

            val repeatedNumbers =
                stickerList
                    .filter { sticker ->
                        sticker.numRepeated > 0
                    }
                    .joinToString(", ") { sticker ->
                        sticker.number.plus("(" + sticker.numRepeated + ")")
                    }

            if (repeatedNumbers.isNotBlank()) {
                stringToShare = stringToShare.plus(getString(R.string.share_stickers_repeated))
                stringToShare = stringToShare.plus("\n\n")
                stringToShare = stringToShare.plus(repeatedNumbers)
            }
        }

        return stringToShare
    }
}