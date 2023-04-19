package com.danilovfa.shapesdrawer.model.shapes

import android.graphics.Color
import android.util.Log
import com.danilovfa.shapesdrawer.model.Coordinate
import com.danilovfa.shapesdrawer.model.factories.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ShapeData(
    @SerialName("name")
    var name: String = "Shape",
    @SerialName("coord_1")
    var coord1: Coordinate = Coordinate(0f, 0f),
    @SerialName("coord_2")
    var coord2: Coordinate = Coordinate(0f, 0f),
    @SerialName("pen_width")
    var penWidth: Float = 15f,
    @SerialName("angle")
    var angle: Float = 0f,
    @SerialName("pen_color")
    var penColor: Int = Color.WHITE,
    @SerialName("fill_color")
    var fillColor: Int = Color.TRANSPARENT
    ) {
    private val TAG = "ShapeData"

    fun toFactoryProps() : FactoryProps {
        val factoryProps = FactoryProps()
        factoryProps.angle = this.angle
        factoryProps.penWidth = this.penWidth
        factoryProps.penColor = this.penColor
        factoryProps.fillColor = this.fillColor
        return factoryProps
    }

    fun toShape() : Shape {
        val shapesClasses = mapOf(
            "Line" to LineFactory::class.java,
            "Oval" to OvalFactory::class.java,
            "Rectangle" to RectangleFactory::class.java
        )

        // Shape class
        val shapeClass = shapesClasses[name]!!
        // Shape factory constructor
        val shapeConstructor = shapeClass.declaredConstructors[0]
        // Shape factory create method
        val shapeCreateMethod = shapeClass.declaredMethods[0]
        Log.d(TAG, "toShape: $shapeClass")

        // Create instance of shape
        val shapeFactory = shapeConstructor.newInstance(coord1.copy(), coord2.copy(), toFactoryProps()) as ShapeFactory
        return shapeFactory.create()
    }

    override fun toString() = "[$name, $coord1, $coord2, $penWidth, $angle, $penColor, $fillColor]"
}