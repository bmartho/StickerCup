package br.cup.stickercontrol.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import br.cup.stickercontrol.R
import br.cup.stickercontrol.album.ui.UpdateStickerInterface
import br.cup.stickercontrol.model.Sticker

@SuppressLint("ViewConstructor")
class AlbumStickerButton(
    context: Context,
    private val stickerBD: Sticker,
    private val updateSticker: UpdateStickerInterface
) : ConstraintLayout(context) {
    private lateinit var sticker: TextView

    init {
        init(context)
    }

    private fun init(context: Context) {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.album_sticker_button_component, this)

        sticker = findViewById(R.id.sticker)
        sticker.text = stickerBD.number

        setBackgroundColor(getStickerColor())
        setOnClickListener(::stickerClick)
    }

    private fun stickerClick(view: View) {
        stickerBD.isMarked = !stickerBD.isMarked
        view.setBackgroundColor(getStickerColor())

        updateSticker.updateSticker(stickerBD)
    }

    private fun getStickerColor() = if (stickerBD.isMarked) {
        resources.getColor(R.color.marked_sticker, null)
    } else {
        resources.getColor(R.color.white, null)
    }
}
