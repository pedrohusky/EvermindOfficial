package com.example.evermemo

import android.graphics.Path
import java.io.Writer

class EverDrawLine(val x: Float, val y: Float) : EverDrawActionInterface {

    override fun perform(path: Path) {
        path.lineTo(x, y)
    }

    override fun perform(writer: Writer) {
        writer.write("L$x,$y")
    }
}