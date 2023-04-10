package com.danilovfa.shapesdrawer.model.factories

import com.danilovfa.shapesdrawer.model.Coordinate
import com.danilovfa.shapesdrawer.model.shapes.Line
import com.danilovfa.shapesdrawer.model.shapes.Shape

class LineFactory(coord1: Coordinate, coord2: Coordinate, props: FactoryProps) : ShapeFactory(coord1, coord2, props) {
    override fun create(): Shape {
        val line = Line(coord1, coord2)
        line.penColor = this.penColor
        line.penWidth = this.penWidth
        line.angle = this.angle
        return line
    }
}