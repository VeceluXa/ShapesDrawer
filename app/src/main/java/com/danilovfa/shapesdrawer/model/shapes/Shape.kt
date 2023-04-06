package com.danilovfa.shapesdrawer.model.shapes

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
    var penWidth = 5f
    var penColor = Color.WHITE
    var fillColor = Color.TRANSPARENT

    init {
        val coordTemp1 = coord1
        val coordTemp2 = coord2
        this.coord1 = Coordinate(min(coordTemp1.x, coordTemp2.x), min(coordTemp1.y, coordTemp2.y))
        this.coord2 = Coordinate(max(coordTemp1.x, coordTemp2.x), max(coordTemp1.y, coordTemp2.y))
    }

    // Get area of shape
    open val area: Float
        get() = 0f

    val width: Float
        get() = abs(coord1.x - coord2.x)

    val height: Float
        get() = abs(coord1.y - coord2.y)

    protected fun getDistance(coord1: Coordinate, coord2: Coordinate) : Float =
        sqrt((coord2.x - coord1.x).toDouble().pow(2.0) + (coord2.y - coord1.y).toDouble().pow(2.0)).toFloat()

    override fun toString(): String {
        var string = "Shape"
        return super.toString()
    }

    open fun draw(canvas: Canvas) { }
}