package com.danilovfa.shapesdrawer.model.factories

import com.danilovfa.shapesdrawer.model.Coordinate
import com.danilovfa.shapesdrawer.model.shapes.Rectangle
import com.danilovfa.shapesdrawer.model.shapes.Shape

open class RectangleFactory(coord1: Coordinate, coord2: Coordinate) : ShapeFactory(coord1, coord2) {
    override fun Create(): Shape {
        return Rectangle(coord1, coord2)
    }
}