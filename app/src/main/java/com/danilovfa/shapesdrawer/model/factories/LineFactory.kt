package com.danilovfa.shapesdrawer.model.factories

import com.danilovfa.shapesdrawer.model.Coordinate
import com.danilovfa.shapesdrawer.model.shapes.Line
import com.danilovfa.shapesdrawer.model.shapes.Shape

class LineFactory(coord1: Coordinate, coord2: Coordinate) : ShapeFactory(coord1, coord2) {
    override fun Create(): Shape {
        return Line(coord1, coord2)
    }
}