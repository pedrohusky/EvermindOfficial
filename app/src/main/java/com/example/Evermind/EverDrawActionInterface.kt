package com.example.Evermind

import android.graphics.Path
import java.io.Serializable
import java.io.Writer

interface EverDrawActionInterface: Serializable {
    fun perform(path: Path)

    fun perform(writer: Writer)
}
