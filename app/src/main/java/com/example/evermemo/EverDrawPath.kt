package com.example.evermemo


import android.graphics.Path
import java.io.ObjectInputStream
import java.io.Serializable
import java.util.*

class EverDrawPath : Path(), Serializable {
    val actions = LinkedList<EverDrawActionInterface>()

    private fun readObject(inputStream: ObjectInputStream) {
        inputStream.defaultReadObject()

        val copiedActions = actions.map { it }
        copiedActions.forEach {
            it.perform(this)
        }
    }

    override fun reset() {
        actions.clear()
        super.reset()
    }

    override fun moveTo(x: Float, y: Float) {
        actions.add(EverDrawMove(x, y))
        super.moveTo(x, y)
    }

    override fun lineTo(x: Float, y: Float) {
        actions.add(EverDrawLine(x, y))
        super.lineTo(x, y)
    }

    override fun quadTo(x1: Float, y1: Float, x2: Float, y2: Float) {
        actions.add(EverDrawQuad(x1, y1, x2, y2))
        super.quadTo(x1, y1, x2, y2)
    }
}