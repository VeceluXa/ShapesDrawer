package com.danilovfa.shapesdrawer.model.shapes

import android.graphics.Canvas
import com.danilovfa.shapesdrawer.model.Coordinate

class Oval: Rectangle {
    constructor(): super()
    constructor(coord1: Coordinate, coord2: Coordinate): super(coord1, coord2)
    constructor(coord: Coordinate, width: Float, height: Float): super(coord, width, height)

    override fun draw(canvas: Canvas) {
        paint.strokeWidth = penWidth
        paint.color = penColor
        canvas.drawOval(coord1.x, coord1.y, coord2.x, coord2.y, paint)
    }

    override fun toString(): String {
        return "Oval : (${coord1.x}, ${coord1.y}),(${coord2.x}, ${coord2.y})"
    }
}