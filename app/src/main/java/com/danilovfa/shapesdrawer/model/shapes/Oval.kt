package com.danilovfa.shapesdrawer.model.shapes

import android.graphics.Canvas
import android.util.Log
import com.danilovfa.shapesdrawer.model.Coordinate

class Oval : Rectangle {
    constructor() : super()
    constructor(coord1: Coordinate, coord2: Coordinate) : super(coord1, coord2)
    constructor(coord: Coordinate, width: Float, height: Float) : super(coord, width, height)
    constructor(data: ShapeData) : super(data)

    override fun drawShape(canvas: Canvas) {

        canvas.drawOval(coord1.x, coord1.y, coord2.x, coord2.y, paint)
    }

    override fun contains(coord: Coordinate): Boolean {
        val dx = coord.x - center.x
        val dy = coord.y - center.y
        return (dx * dx) / (width * width) + (dy * dy) / (height * height) <= 1
    }

    override fun toDataClass(): ShapeData {
        return ShapeData(
            name = "Oval",
            coord1 = this.coord1,
            coord2 = this.coord2,
            penWidth = this.penWidth,
            angle = this.angle,
            penColor = this.penColor,
            fillColor = this.fillColor
        )
    }
}