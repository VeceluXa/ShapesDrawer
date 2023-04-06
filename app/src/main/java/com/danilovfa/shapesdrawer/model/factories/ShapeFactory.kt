package com.danilovfa.shapesdrawer.model.factories

import com.danilovfa.shapesdrawer.model.Coordinate
import com.danilovfa.shapesdrawer.model.shapes.Shape

abstract class ShapeFactory(var coord1: Coordinate, var coord2: Coordinate) {
    abstract fun Create(): Shape
}