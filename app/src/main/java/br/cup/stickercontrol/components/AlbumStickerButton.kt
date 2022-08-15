package br.cup.stickercontrol.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import br.cup.stickercontrol.R
import br.cup.stickercontrol.album.ui.UpdateStickerInterface
import br.cup.stickercontrol.model.Sticker

@SuppressLint("ViewConstructor")
class AlbumStickerButton(
    context: Context
) : ConstraintLayout(context) {
    private var gluedStickerNumber: TextView
    private var gluedStickerLabel: TextView
    private var repeatedContainer: ConstraintLayout
    private var repeatedStickerQuantity: TextView
    private var buttonRepeatedPlus: Button
    private var buttonRepeatedMinus: Button
    private var buttonLayoutRepeatedPlus: FrameLayout
    private var buttonLayoutRepeatedMinus: FrameLayout

    private lateinit var stickerBD: Sticker
    private lateinit var updateSticker: UpdateStickerInterface

    init {
        inflate(context, R.layout.sticker_component, this)

        gluedStickerNumber = findViewById(R.id.glued_sticker_number)
        gluedStickerLabel = findViewById(R.id.glued_sticker_label)
        repeatedContainer = findViewById(R.id.repeated_sticker_container)
        repeatedStickerQuantity = findViewById(R.id.repeated_sticker_quantity)
        buttonRepeatedPlus = findViewById(R.id.plus_repeated_sticker_button)
        buttonRepeatedMinus = findViewById(R.id.minus_repeated_sticker_button)
        buttonLayoutRepeatedPlus = findViewById(R.id.plus_repeated_sticker_button_layout)
        buttonLayoutRepeatedMinus = findViewById(R.id.minus_repeated_sticker_button_layout)
    }

    fun init(
        isRepeatedTab: Boolean,
        sticker: Sticker,
        updateStickerInterface: UpdateStickerInterface
    ) {
        stickerBD = sticker
        updateSticker = updateStickerInterface

        if (isRepeatedTab) {
            configureRepeatedSticker()
        } else {
            configureGluedSticker()
        }
    }

    private fun configureGluedSticker() {
        gluedStickerNumber.visibility = VISIBLE
        gluedStickerLabel.visibility = VISIBLE
        repeatedContainer.visibility = GONE

        gluedStickerNumber.text = stickerBD.number
        gluedStickerLabel.text = stickerBD.label
        setBackgroundColor(getStickerColor())

        setOnClickListener(::stickerClick)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun configureRepeatedSticker() {
        gluedStickerNumber.visibility = GONE
        gluedStickerLabel.visibility = GONE
        repeatedContainer.visibility = VISIBLE

        val stickerNumber = findViewById<TextView>(R.id.repeated_sticker_number)
        stickerNumber.text = "${stickerBD.label} ${stickerBD.number}"

        setBackgroundColor(getRepeatedStickerColor())
        setOnClickListener(null)

        buttonLayoutRepeatedPlus.setOnTouchListener(plusLayoutClickEvent)
        repeatedStickerQuantity.text = stickerBD.numRepeated.toString()
        buttonRepeatedPlus.setOnClickListener {
            stickerBD.numRepeated = stickerBD.numRepeated + 1
            repeatedStickerQuantity.text = stickerBD.numRepeated.toString()
            updateSticker.updateSticker(stickerBD)
            this@AlbumStickerButton.setBackgroundColor(getRepeatedStickerColor())
        }

        buttonLayoutRepeatedMinus.setOnTouchListener(minusLayoutClickEvent)
        buttonRepeatedMinus.setOnClickListener {
            if (stickerBD.numRepeated > 0) {
                stickerBD.numRepeated = stickerBD.numRepeated - 1
                repeatedStickerQuantity.text = stickerBD.numRepeated.toString()
                updateSticker.updateSticker(stickerBD)
                this@AlbumStickerButton.setBackgroundColor(getRepeatedStickerColor())
            }
        }

        buttonRepeatedPlus.isPressed = false
        buttonRepeatedMinus.isPressed = false
    }

    private fun stickerClick(view: View) {
        stickerBD.isMarked = !stickerBD.isMarked
        view.setBackgroundColor(getStickerColor())

        updateSticker.updateSticker(stickerBD)
    }

    private fun getStickerColor() = if (stickerBD.isMarked) {
        if (isStrongStickerNumber()) {
            resources.getColor(R.color.marked_sticker_strong, null)
        } else {
            resources.getColor(R.color.marked_sticker, null)
        }
    } else {
        if (isStrongStickerNumber()) {
            resources.getColor(R.color.sticker_background_strong, null)
        } else {
            resources.getColor(R.color.sticker_background, null)
        }
    }

    private fun getRepeatedStickerColor() = if (stickerBD.numRepeated > 0) {
        if (isStrongStickerNumber()) {
            resources.getColor(R.color.repeated_sticker_strong, null)
        } else {
            resources.getColor(R.color.repeated_sticker, null)
        }
    } else {
        if (isStrongStickerNumber()) {
            resources.getColor(R.color.sticker_background_strong, null)
        } else {
            resources.getColor(R.color.sticker_background, null)
        }
    }

    private val plusLayoutClickEvent: (View, MotionEvent) -> Boolean = { _, arg1 ->
        when (arg1.action) {
            MotionEvent.ACTION_DOWN -> {
                buttonRepeatedPlus.isPressed = true
            }
            MotionEvent.ACTION_UP -> {
                buttonRepeatedPlus.isPressed = false
                buttonRepeatedPlus.performClick()
            }
            MotionEvent.ACTION_CANCEL -> {
                buttonRepeatedPlus.isPressed = false
            }
        }
        true
    }

    private val minusLayoutClickEvent: (View, MotionEvent) -> Boolean = { _, arg1 ->
        when (arg1.action) {
            MotionEvent.ACTION_DOWN -> {
                buttonRepeatedMinus.isPressed = true
            }
            MotionEvent.ACTION_UP -> {
                buttonRepeatedMinus.isPressed = false
                buttonRepeatedMinus.performClick()
            }
            MotionEvent.ACTION_CANCEL -> {
                buttonRepeatedMinus.isPressed = false
            }
        }
        true
    }

    private fun isStrongStickerNumber() = if (stickerBD.id <= 20) {
        false
    } else {
        if (stickerBD.id % 20 != 0 && (stickerBD.id / 20) % 2 == 0) {
            false
        } else !(stickerBD.id % 20 == 0 && (stickerBD.id / 20) % 2 == 1)
    }
}
