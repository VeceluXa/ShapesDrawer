package com.danilovfa.shapesdrawer.model.shapes

import android.graphics.*
import android.util.Log
import com.danilovfa.shapesdrawer.model.Coordinate
import kotlin.math.abs

open class Rectangle: Shape {
    constructor(): super()
    constructor(coord1: Coordinate, coord2: Coordinate): super(coord1, coord2)
    constructor(coord: Coordinate, width: Float, height: Float): super() {
        this.coord1 = coord
        this.coord2 = Coordinate(coord1.x + width, coord2.y + height)
//        shape = RectF(coord1.x, coord1.y, coord2.x, coord2.y)
    }

    override val area: Float
        get() = abs(coord1.x - coord2.x) * abs(coord1.y - coord2.y)

    override fun toString(): String {
        return "Rectangle : (${coord1.x}, ${coord1.y}),(${coord2.x}, ${coord2.y})"
    }

    override fun drawShape(canvas: Canvas) {
        Log.d("DrawShape", "drawShape: Rectangle")

        canvas.drawRect(coord1.x, coord1.y, coord2.x, coord2.y, paint)
//        canvas.drawRect(shape as Rect, paint)
    }

    override fun contains(coord: Coordinate): Boolean {
        val rect = RectF(coord1.x, coord1.y, coord2.x, coord2.y)
        return rect.contains(coord.x, coord.y)
//        val inverseMatrix = Matrix()
//
//        if (angle == 0f && coord.x in coord1.x..coord2.x && coord.y in coord1.y..coord2.y) return true
//        return false
    }
}