package com.danilovfa.shapesdrawer.model.factories

import com.danilovfa.shapesdrawer.model.Coordinate
import com.danilovfa.shapesdrawer.model.shapes.Oval
import com.danilovfa.shapesdrawer.model.shapes.Shape

class OvalFactory(coord1: Coordinate, coord2: Coordinate) : RectangleFactory(coord1, coord2) {
    override fun Create(): Shape {
        return Oval(coord1, coord2)
    }
}