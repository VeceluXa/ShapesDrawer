package com.danilovfa.shapesdrawer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.danilovfa.shapesdrawer.model.Coordinate
import com.danilovfa.shapesdrawer.model.shapes.Shape

class CanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    // OnClickListener to open dialog
    private var onClickListener: OnClickListener? = null

    // Coordinate where last touched
    var coordinate = Coordinate(0f, 0f)

    // Canvas
    val paint = Paint()
    var canvas: Canvas? = null

    // List of shapes to draw
    val shapes = mutableListOf<Shape>()

    /**
     * Draw shapes from shapes list.
     * @param canvas
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.canvas = canvas
        paint.strokeWidth = 5f
        paint.color = Color.WHITE
        for (shape in shapes) {
            draw(shape)
        }
    }

    /**
    * Draws a given shape object onto a canvas.
    * @param shape the shape object to be drawn.
    * @throws ClassCastException if the passed object is not an instance of either Oval, Line or Rectangle.
    */
    fun draw(shape: Shape) {
        Log.d("CreateShape", "draw: ${shape.javaClass}")

        val shapeClass = shape.javaClass
        val shapeMethods = shapeClass.declaredMethods.forEach {
            if (it.name == "draw") {
                it.invoke(shape, canvas)
            }
        }
    }

    /**
     * Redraw canvas. Used when adding new shapes to shapes list
     */
    fun redraw() {
        invalidate()
    }

    /**
     * Check if touch event is ACTION_DOWN. If so, save coordinates and call onClick.
     * @param event motion event to control touch gestures
     */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            coordinate.x = event.x
            coordinate.y = event.y
            onClickListener?.onClick(this)
        }

        return super.dispatchTouchEvent(event)
    }
}