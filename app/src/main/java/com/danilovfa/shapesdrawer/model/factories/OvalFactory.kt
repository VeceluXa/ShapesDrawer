package com.danilovfa.shapesdrawer.model.factories

import com.danilovfa.shapesdrawer.model.Coordinate
import com.danilovfa.shapesdrawer.model.shapes.Oval
import com.danilovfa.shapesdrawer.model.shapes.Shape

class OvalFactory(coord1: Coordinate, coord2: Coordinate, props: FactoryProps) : RectangleFactory(coord1, coord2, props) {
    override fun create(): Shape {
        val oval = Oval(coord1, coord2)
        oval.penColor = this.penColor
        oval.fillColor = this.fillColor
        oval.penWidth = this.penWidth
        oval.angle = this.angle
        return oval
    }
}