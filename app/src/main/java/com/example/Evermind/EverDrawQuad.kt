package com.example.Evermind

import android.graphics.Path
import java.io.Writer

class EverDrawQuad(private val x1: Float, private val y1: Float, private val x2: Float, private val y2: Float) :
    EverDrawActionInterface {

    override fun perform(path: Path) {
        path.quadTo(x1, y1, x2, y2)
    }

    override fun perform(writer: Writer) {
        writer.write("Q$x1,$y1 $x2,$y2")
    }
}