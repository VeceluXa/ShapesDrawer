package com.danilovfa.shapesdrawer.model
import kotlinx.serialization.Serializable

@Serializable
data class Coordinate(var x: Float, var y: Float) {
    override fun toString(): String {
        return "[x=$x, y=$y]"
    }
}