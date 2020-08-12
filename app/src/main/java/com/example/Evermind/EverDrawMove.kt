package com.example.Evermind

import android.graphics.Path
import java.io.Writer

class EverDrawMove(val x: Float, val y: Float) : EverDrawActionInterface {

    override fun perform(path: Path) {
        path.moveTo(x, y)
    }

    override fun perform(writer: Writer) {
        writer.write("M$x,$y")
    }
}