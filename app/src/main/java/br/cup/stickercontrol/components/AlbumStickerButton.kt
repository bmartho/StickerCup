package br.cup.stickercontrol.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Button
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
    private var gluedStickerNumber: TextView
    private var repeatedContainer: ConstraintLayout
    private var repeatedStickerQuantity: TextView
    private var buttonRepeatedPlus: Button
    private var buttonRepeatedMinus: Button

    init {
        inflate(context, R.layout.sticker_component, this)

        gluedStickerNumber = findViewById(R.id.glued_sticker_number)
        repeatedContainer = findViewById(R.id.repeated_sticker_container)
        repeatedStickerQuantity = findViewById(R.id.repeated_sticker_quantity)
        buttonRepeatedPlus = findViewById(R.id.plus_repeated_sticker_button)
        buttonRepeatedMinus = findViewById(R.id.minus_repeated_sticker_button)

        init()
    }

    private fun init(isRepeatedTab: Boolean = false) {
        if (isRepeatedTab) {
            configureRepeatedSticker()
        } else {
            configureGluedSticker()
        }
    }

    private fun configureGluedSticker() {
        gluedStickerNumber.visibility = VISIBLE
        repeatedContainer.visibility = GONE

        gluedStickerNumber.text = stickerBD.number
        setBackgroundColor(getStickerColor())

        setOnClickListener(::stickerClick)
    }

    private fun configureRepeatedSticker() {
        gluedStickerNumber.visibility = GONE
        repeatedContainer.visibility = VISIBLE

        val stickerNumber = findViewById<TextView>(R.id.repeated_sticker_number)
        stickerNumber.text = stickerBD.number

        setBackgroundColor(resources.getColor(R.color.white, null))
        setOnClickListener(null)

        repeatedStickerQuantity.text = stickerBD.numRepeated.toString()
        buttonRepeatedPlus.setOnClickListener {
            stickerBD.numRepeated = stickerBD.numRepeated + 1
            repeatedStickerQuantity.text = stickerBD.numRepeated.toString()
            updateSticker.updateSticker(stickerBD)
        }

        buttonRepeatedMinus.setOnClickListener {
            if (stickerBD.numRepeated > 0) {
                stickerBD.numRepeated = stickerBD.numRepeated - 1
                repeatedStickerQuantity.text = stickerBD.numRepeated.toString()
                updateSticker.updateSticker(stickerBD)
            }
        }
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

    fun setRepeatedTab(value: Boolean) {
        init(value)
    }
}
