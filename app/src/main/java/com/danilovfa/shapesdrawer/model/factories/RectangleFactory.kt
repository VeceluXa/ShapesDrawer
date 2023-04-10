package com.danilovfa.shapesdrawer.model.factories

import com.danilovfa.shapesdrawer.model.Coordinate
import com.danilovfa.shapesdrawer.model.shapes.Rectangle
import com.danilovfa.shapesdrawer.model.shapes.Shape

open class RectangleFactory(coord1: Coordinate, coord2: Coordinate, props: FactoryProps) : ShapeFactory(coord1, coord2, props) {
    override fun create(): Shape {
        val rectangle = Rectangle(coord1, coord2)
        rectangle.penColor = this.penColor
        rectangle.fillColor = this.fillColor
        rectangle.penWidth = this.penWidth
        rectangle.angle = this.angle
        return rectangle
    }
}