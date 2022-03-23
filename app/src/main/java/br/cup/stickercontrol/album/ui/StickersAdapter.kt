package br.cup.stickercontrol.album.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import br.cup.stickercontrol.components.AlbumStickerButton
import br.cup.stickercontrol.model.Sticker


class StickersAdapter constructor(
    private val listStickers: List<Sticker>,
    private val appContext: Context,
    private val updateSticker: UpdateStickerInterface

) : BaseAdapter() {
    private var isRepeatedTab = false

    override fun getCount(): Int {
        return listStickers.size
    }

    override fun getItem(i: Int): Any {
        return listStickers[i]
    }

    override fun getItemId(i: Int): Long {
        return 0L
    }

    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val stickerButtonView = if (convertView == null) {
            AlbumStickerButton(appContext)
        } else {
            convertView as AlbumStickerButton
        }

        stickerButtonView.init(isRepeatedTab, listStickers[i], updateSticker)
        return stickerButtonView
    }

    fun setRepeatedTab(value: Boolean) {
        isRepeatedTab = value
        notifyDataSetChanged()
    }
}