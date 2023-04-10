package com.danilovfa.shapesdrawer.model.factories

import android.graphics.Color
import com.danilovfa.shapesdrawer.model.Coordinate
import com.danilovfa.shapesdrawer.model.shapes.Shape

abstract class ShapeFactory(var coord1: Coordinate, var coord2: Coordinate, props: FactoryProps) {
    var penColor: Int
    var penWidth: Float
    var fillColor: Int
    var angle: Float
    init {
        this.penColor = props.penColor
        this.fillColor = props.fillColor
        this.penWidth = props.penWidth
        this.angle = props.angle
    }
    abstract fun create(): Shape
}