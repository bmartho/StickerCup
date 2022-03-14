package br.cup.stickercontrol.album.ui

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import br.cup.stickercontrol.components.AlbumStickerButton


class StickersAdapter constructor(
    private val listStickers: List<AlbumStickerButton>
) : BaseAdapter() {

    override fun getCount(): Int {
        return listStickers.size
    }

    override fun getItem(i: Int): Any {
        return listStickers[i]
    }

    override fun getItemId(i: Int): Long {
        return 0L
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
        return listStickers[i]
    }
}