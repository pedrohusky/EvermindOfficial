package com.example.evermemo

import android.graphics.Color
import android.graphics.Paint

data class EverDrawPaintOptions(
    var color: Int = Color.BLACK,
    var strokeWidth: Float = 8f,
    var alpha: Int = 255,
    var style: Paint.Style = Paint.Style.STROKE
)