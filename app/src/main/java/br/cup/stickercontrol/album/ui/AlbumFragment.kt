package br.cup.stickercontrol.album.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.cup.stickercontrol.components.AlbumStickerButton
import br.cup.stickercontrol.databinding.FragmentAlbumBinding


class AlbumFragment : Fragment() {

    private lateinit var binding: FragmentAlbumBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val list = listOf(
            AlbumStickerButton(requireContext(), 1),
            AlbumStickerButton(requireContext(), 2),
            AlbumStickerButton(requireContext(), 3),
            AlbumStickerButton(requireContext(), 4),
            AlbumStickerButton(requireContext(), 5)
        )

        context?.let {
            binding.mainGrid.adapter = StickersAdapter(list)
        }

        return binding.root
    }
}