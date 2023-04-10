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
    var coordUp = Coordinate(0f, 0f)
    var coordDown = Coordinate(0f, 0f)

    // Canvas
    private val paint = Paint()
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
            Log.d("DrawShape", "onDraw: (${shape.coord1.x}, ${shape.coord1.y}), (${shape.coord2.x}, ${shape.coord2.y})")
            draw(shape)
        }
    }

    /**
    * Draws a given shape object onto a canvas.
    * @param shape the shape object to be drawn.
    * @throws ClassCastException if the passed object is not an instance of either Oval, Line or Rectangle.
    */
    fun draw(shape: Shape) {
//        Log.d("CreateShape", "draw: ${shape.javaClass}")

        val shapeClass = shape.javaClass
        val shapeMethods = shapeClass.methods.forEach {
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
            Log.d("Canvas", "Touch Down: (${event.x}, ${event.y})")
            coordDown.x = event.x
            coordDown.y = event.y
        }
        if (event.action == MotionEvent.ACTION_UP) {
            Log.d("Canvas", "Touch Up: (${event.x}, ${event.y})")
            coordUp.x = event.x
            coordUp.y = event.y
            onClickListener?.onClick(this)
        }

        return super.dispatchTouchEvent(event)
    }
}