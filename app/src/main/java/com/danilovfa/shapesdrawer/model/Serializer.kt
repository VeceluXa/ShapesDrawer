package com.danilovfa.shapesdrawer.model

import android.graphics.Color
import android.util.Log
import com.danilovfa.shapesdrawer.model.shapes.Shape
import com.danilovfa.shapesdrawer.model.shapes.ShapeData
import kotlinx.serialization.json.*

class Serializer {
    private val TAG = "Serializer"

    /**
     * Convert list of shapes to json array
     * @param shapes list of shapes
     * @return json array
     */
    fun toJson(shapes: List<Shape>) : JsonArray {
        val jsonArr = buildJsonArray {
            addJsonArray {
                shapes.forEach {
                    add(Json.encodeToJsonElement(ShapeData.serializer(), it.toDataClass()))
                }
            }
        }
        Log.d(TAG, "toJson: $jsonArr")
        return jsonArr
    }

    /**
     * Get list of shapes from json array
     * @param jsonArray json array
     * @return list of shapes
     */
    fun fromJson(jsonArray: JsonArray) : List<Shape> {
        val list = mutableListOf<Shape>()
        jsonArray.firstOrNull()?.jsonArray?.forEach {
            val data: ShapeData = Json.decodeFromJsonElement(it)
            list.add(data.toShape())
        }
        Log.d(TAG, "fromJson: $list")
        return list
    }

    /**
     * Convert list of shapes to text
     * @param shapes list of shapes
     * @return multi-line string
     */
    fun toTxt(shapes: List<Shape>) : String {
        val sb = StringBuilder()
        shapes.forEach {
            val data = it.toDataClass()
            sb.appendLine(data.name)
            sb.appendLine(data.coord1.x)
            sb.appendLine(data.coord1.y)
            sb.appendLine(data.coord2.x)
            sb.appendLine(data.coord2.y)
            sb.appendLine(data.angle)
            sb.appendLine(data.penWidth)
            sb.appendLine(data.penColor)
            sb.appendLine(data.fillColor)
        }
        val str = sb.toString()
        Log.d(TAG, "toTxt: $str")
        return str
    }

    /**
     * Gets list of shapes from text
     * @param str text
     * @return list of shapes
     */
    fun fromTxt(str: String) : List<Shape> {
        val list = mutableListOf<Shape>()
        val lines = str.split('\n')
        for (i in 0 until lines.size/9) {
            val data = ShapeData()
            data.name = lines[i*9]
            data.coord1 = Coordinate(
                lines[i*9+1].toFloatOrNull()?:0f,
                lines[i*9+2].toFloatOrNull()?:0f,
            )
            data.coord2 = Coordinate(
                lines[i*9+3].toFloatOrNull()?:0f,
                lines[i*9+4].toFloatOrNull()?:0f,
            )
            data.angle = lines[i*9+5].toFloatOrNull()?:0f
            data.penWidth = lines[i*9+6].toFloatOrNull()?:15f
            data.penColor = lines[i*9+7].toIntOrNull()?:Color.WHITE
            data.fillColor = lines[i*9+8].toIntOrNull()?:Color.TRANSPARENT

            list.add(data.toShape())
        }
        Log.d(TAG, "fromTxt: $list")
        return list
    }

    /**
     * Convert list of shape to binary format
     * @param shapes list of shapes
     * @return byte array
     */
    fun toBin(shapes: List<Shape>) : ByteArray {
        val json = toJson(shapes).toString()
        return json.toByteArray()
    }

    /**
     * Gets list of shapes from byte array
     * @param byteArray byte array
     * @return list of shapes
     */
    fun fromBin(byteArray: ByteArray) : List<Shape> {
        val str = String(byteArray)
        Log.d(TAG, "fromBin: $str")
        val jsonArray = Json.parseToJsonElement(str).jsonArray
        return fromJson(jsonArray)
    }
}