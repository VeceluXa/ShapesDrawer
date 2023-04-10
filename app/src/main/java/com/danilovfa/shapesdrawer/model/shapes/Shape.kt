package com.danilovfa.shapesdrawer.model.shapes

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.danilovfa.shapesdrawer.model.Coordinate
import java.lang.Float.max
import java.lang.Float.min
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

open class Shape(
    var coord1: Coordinate = Coordinate(0f, 0f),
    var coord2: Coordinate = Coordinate(0f, 0f)
) {
    protected val paint: Paint = Paint()
    var penWidth = 15f
    var penColor = Color.WHITE
    var fillColor = Color.TRANSPARENT
    var angle = 0f


    // Get area of shape
    open val area: Float
        get() = 0f

    // Get width of shape
    val width: Float
        get() = abs(coord1.x - coord2.x)

    // Get height of shape
    val height: Float
        get() = abs(coord1.y - coord2.y)

    /**
     * get distance between 2 points
     * @param coord1 first coordinate
     * @param coord2 second coordinate
     * @return distance between 2 points
     */
    protected fun getDistance(coord1: Coordinate, coord2: Coordinate) : Float =
        sqrt((coord2.x - coord1.x).toDouble().pow(2.0) + (coord2.y - coord1.y).toDouble().pow(2.0)).toFloat()

    /**
     * Convert shape to string
     */
    override fun toString(): String = "Shape"

    /**
     * Draw shape
     */
    fun draw(canvas: Canvas) {
        // Save canvas state
        canvas.save()
        // Rotate canvas
        canvas.rotate(angle)

        // Draw fill
        Log.d("DrawShape", "draw: fill")
        paint.style = Paint.Style.FILL
        paint.color = fillColor
        drawShape(canvas)

        // Draw stroke
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = penWidth
        paint.color = penColor
        drawShape(canvas)

        // Restore canvas angle
        canvas.rotate(-angle)
        canvas.restore()
    }
    open fun drawShape(canvas: Canvas) { }
}