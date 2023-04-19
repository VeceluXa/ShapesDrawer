package com.danilovfa.shapesdrawer.model.shapes

import android.content.ContentValues.TAG
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
    }
    constructor(data: ShapeData) : super(data)



    override val area: Float
        get() = abs(coord1.x - coord2.x) * abs(coord1.y - coord2.y)

    override fun drawShape(canvas: Canvas) {
        Log.d("DrawShape", "drawShape: Rectangle")
        canvas.drawRect(coord1.x, coord1.y, coord2.x, coord2.y, paint)
    }

    override fun contains(coord: Coordinate): Boolean {
        val rect = RectF(coord1.x, coord1.y, coord2.x, coord2.y)
        return rect.contains(coord.x, coord.y)
    }
    override fun toDataClass(): ShapeData {
        val shapeData = ShapeData(
            name = "Rectangle",
            coord1 = this.coord1,
            coord2 = this.coord2,
            penWidth = this.penWidth,
            angle = this.angle,
            penColor = this.penColor,
            fillColor = this.fillColor
        )
        Log.d(TAG, "toDataClass: $shapeData")
        return shapeData
    }
}