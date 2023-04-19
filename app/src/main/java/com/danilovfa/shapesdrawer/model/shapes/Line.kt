package com.danilovfa.shapesdrawer.model.shapes

import android.graphics.Canvas
import android.util.Log
import com.danilovfa.shapesdrawer.model.Coordinate
import kotlin.math.abs

class Line : Shape {
    constructor() : super()
    constructor(coord1: Coordinate, coord2: Coordinate) : super(coord1, coord2)
    constructor(data: ShapeData) : super(data)

    override fun drawShape(canvas: Canvas) {
        Log.d("DrawShape", "drawShape: Line")
        canvas.drawLine(coord1.x, coord1.y, coord2.x, coord2.y, paint)
    }

    override fun contains(coord: Coordinate): Boolean {
        val EPSILON = 0.01f
        if (abs(coord1.x - coord2.x) < EPSILON) {
            return abs(coord.x - coord1.x) < EPSILON
        } else {
            val m = (coord2.y - coord1.y) / (coord2.x - coord1.x)
            val b = coord1.y - m * coord1.x
            return abs(coord.y - (m * coord.x + b)) < EPSILON
        }
    }

    override fun toDataClass(): ShapeData {
        return ShapeData(
            name = "Line",
            coord1 = this.coord1,
            coord2 = this.coord2,
            penWidth = this.penWidth,
            angle = this.angle,
            penColor = this.penColor,
            fillColor = this.fillColor
        )
    }
}