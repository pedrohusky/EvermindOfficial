package com.example.evermemo


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import java.util.*


class EverDraw(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var mPaths = LinkedHashMap<EverDrawPath, EverDrawPaintOptions>()

    // private var ThisView = context

    private var mLastPaths = LinkedHashMap<EverDrawPath, EverDrawPaintOptions>()
    private var mUndonePaths = LinkedHashMap<EverDrawPath, EverDrawPaintOptions>()

    private var mPaint = Paint()
    private var mCanvas = Canvas()
    private var mPath = EverDrawPath()
    private var mPaintOptions = EverDrawPaintOptions()

    private var mCurX = 0f
    private var mCurY = 0f
    private var mStartX = 0f
    private var mStartY = 0f
    private var mIsSaving = false
    private var mIsStrokeWidthBarEnabled = false
    //   private val paths = ArrayList<LinkedHashMap<EverDrawPath, EverDrawPaintOptions>>()
    //   private val options = ArrayList<String>()

    init {
        mPaint.apply {
            color = mPaintOptions.color
            style = mPaintOptions.style
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = mPaintOptions.strokeWidth
            isAntiAlias = true
        }
    }

    fun undo() {
        if (mPaths.isEmpty() && mLastPaths.isNotEmpty()) {
            mPaths = mLastPaths.clone() as LinkedHashMap<EverDrawPath, EverDrawPaintOptions>
            mLastPaths.clear()
            invalidate()
            return
        }
        if (mPaths.isEmpty()) {
            return
        }
        val lastPath = mPaths.values.lastOrNull()
        val lastKey = mPaths.keys.lastOrNull()

        mPaths.remove(lastKey)
        if (lastPath != null && lastKey != null) {
            mUndonePaths[lastKey] = lastPath
        }
        invalidate()
    }

    fun redo() {
        if (mUndonePaths.keys.isEmpty()) {
            return
        }

        val lastKey = mUndonePaths.keys.last()
        addPath(lastKey, mUndonePaths.values.last())
        mUndonePaths.remove(lastKey)
        invalidate()
    }

    fun setColor(newColor: Int) {
        @ColorInt
        val alphaColor = ColorUtils.setAlphaComponent(newColor, mPaintOptions.alpha)
        mPaintOptions.color = alphaColor
        if (mIsStrokeWidthBarEnabled) {
            invalidate()
        }
    }

    fun setAlpha(newAlpha: Int) {
        val alpha = (newAlpha * 255) / 100
        mPaintOptions.alpha = alpha
        setColor(mPaintOptions.color)
    }

    fun setStrokeWidth(newStrokeWidth: Float) {
        mPaintOptions.strokeWidth = newStrokeWidth
        if (mIsStrokeWidthBarEnabled) {
            invalidate()
        }
    }

    fun fillColor(color: Int) {
        // TODO(make the fill background when needed)
    }

    fun paintType(type: String) {
        when (type) {
            "stroke" -> {
                mPaintOptions.style = Paint.Style.STROKE
            }
            "fill" -> {
                mPaintOptions.style = Paint.Style.FILL
            }
            "fillStroke" -> {
                mPaintOptions.style = Paint.Style.FILL_AND_STROKE
            }
        }
    }

    fun getBitmap(color: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(color)
        mIsSaving = true
        draw(canvas)
        mIsSaving = false
        return bitmap
    }

    private fun addPath(path: EverDrawPath, options: EverDrawPaintOptions) {
        mPaths[path] = options
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mCanvas = canvas
        for ((key, value) in mPaths) {
            changePaint(value)
            canvas.drawPath(key, mPaint)
        }

        changePaint(mPaintOptions)
        canvas.drawPath(mPath, mPaint)
    }

    private fun changePaint(paintOptions: EverDrawPaintOptions) {
        mPaint.color = paintOptions.color
        mPaint.strokeWidth = paintOptions.strokeWidth
        mPaint.style = paintOptions.style
    }

    fun clearCanvas() {
        mLastPaths = mPaths.clone() as LinkedHashMap<EverDrawPath, EverDrawPaintOptions>
        mPath.reset()
        mPaths.clear()
        invalidate()
    }

    private fun actionDown(x: Float, y: Float) {
        mPath.reset()
        mPath.moveTo(x, y)
        mCurX = x
        mCurY = y
    }

    private fun actionMove(x: Float, y: Float) {

        mPath.quadTo(mCurX, mCurY, (x + mCurX) / 2, (y + mCurY) / 2)
        mCurX = x
        mCurY = y
    }

    private fun actionUp() {
        mPath.lineTo(mCurX, mCurY)

        // draw a dot on click
        if (mStartX == mCurX && mStartY == mCurY) {
            mPath.lineTo(mCurX, mCurY + 2)
            mPath.lineTo(mCurX + 1, mCurY + 2)
            mPath.lineTo(mCurX + 1, mCurY)
        }

        mPaths.put(mPath, mPaintOptions)

        mPath = EverDrawPath()
        mPaintOptions = EverDrawPaintOptions(
            mPaintOptions.color,
            mPaintOptions.strokeWidth,
            mPaintOptions.alpha,
            mPaintOptions.style
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        this.height
        this.width

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                mStartX = x
                mStartY = y
                actionDown(x, y)
                mUndonePaths.clear()
            }
            MotionEvent.ACTION_MOVE -> actionMove(x, y)
            MotionEvent.ACTION_UP -> actionUp()
        }

        invalidate()
        return true
    }
}
