package br.cup.stickercontrol.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sticker")
class Sticker(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "number")
    val number: String,

    @ColumnInfo(name = "isMarked")
    var isMarked: Boolean
)