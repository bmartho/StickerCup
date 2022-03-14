package br.cup.stickercontrol.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import br.cup.stickercontrol.R

class AlbumStickerButton : ConstraintLayout {
    constructor(context: Context, stickerNumber: Int) : super(context) {
        init(context, stickerNumber)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context, 0)
    }

    private fun init(context: Context, stickerNumber: Int) {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.album_sticker_button_component, this)

        val sticker = findViewById<TextView>(R.id.sticker)
        sticker.text = stickerNumber.toString()
    }
}
