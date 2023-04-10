package com.danilovfa.shapesdrawer

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.danilovfa.shapesdrawer.databinding.BottomSheetLayoutBinding
import com.danilovfa.shapesdrawer.databinding.FragmentMainBinding
import com.danilovfa.shapesdrawer.model.Coordinate
import com.danilovfa.shapesdrawer.model.factories.*
import com.danilovfa.shapesdrawer.model.shapes.Shape
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

//interface IMainFragmentProps: FactoryProps {
//    override var fillColor: Int?
//    override var penColor: Int?
//    override var penWidth: Float?
//
//}
class MainFragment : Fragment() {
    // Fragment binding
    private lateinit var binding: FragmentMainBinding
    // Bottom sheet binding
    private lateinit var bottomSheetBinding: BottomSheetLayoutBinding
    // Shape color
    var penColor: Int = Color.WHITE
    var fillColor: Int = Color.TRANSPARENT
    var penWidth: Float = 5f
    var angle: Float = 0f

    object IMainFragmentProps

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up canvas onClickListener
        binding.canvas.setOnClickListener { onCanvasClick(view) }
    }

    /**
     * onClick listener for canvas.
     * When clicked, bottom sheet dialog is opened.
     * @param view fragment view
     */
    private fun onCanvasClick(view: View) {
        val data = binding.canvas.coordinate
        showBottomSheet(data)
    }

    /**
     * Dialog to create new shape. Opened when user clicks on canvas.
     * @param coords Coordinate (x and y) of click
     */
    private fun showBottomSheet(coords: Coordinate) {
        Log.d("CanvasClick", "showBottomSheet: x=${coords.x}, y=${coords.y}")
        // Inflate the bottom sheet layout
        bottomSheetBinding = BottomSheetLayoutBinding.inflate(layoutInflater)
        // Create the bottom sheet dialog
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        // Canvas onClick listener to open bottom sheet
        bottomSheetBinding.buttonCreateShape.setOnClickListener { onCreateShape(coords) }
        // Color pickers listeners
        bottomSheetBinding.penColorButton.setOnClickListener { onPenColorPicker() }
        bottomSheetBinding.fillColorButton.setOnClickListener { onFillColorPicker() }
        // Create spinner with shapes selection
        createSpinner()
        // Show the bottom sheet dialog
        bottomSheetDialog.show()
    }

    private fun onPenColorPicker() {
        val colorPicker = ColorPickerDialog.Builder(requireContext())
            .setTitle("Select Stroke Color")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton("Select",
                ColorEnvelopeListener { colorEnvelope: ColorEnvelope, b: Boolean ->
                    Log.d("ColorPicker", "onClickColorPicker: Color ${colorEnvelope.color}")
                    penColor = colorEnvelope.color
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                })
            .attachAlphaSlideBar(false)
            .attachBrightnessSlideBar(false)
            .setBottomSpace(12)
            .show()
    }

    private fun onFillColorPicker() {
        val colorPicker = ColorPickerDialog.Builder(requireContext())
            .setTitle("Select Stroke Color")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton("Select",
                ColorEnvelopeListener { colorEnvelope: ColorEnvelope, b: Boolean ->
                    Log.d("ColorPicker", "onClickColorPicker: Color ${colorEnvelope.color}")
                    fillColor = colorEnvelope.color
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                })
            .attachAlphaSlideBar(false)
            .attachBrightnessSlideBar(false)
            .setBottomSpace(12)
            .show()
    }

    private fun onCreateShape(coord1: Coordinate) {
        var coord2: Coordinate
        try {
            coord2 = Coordinate(bottomSheetBinding.textInput1.text.toString().toFloat(), bottomSheetBinding.textInput2.text.toString().toFloat())
        } catch (e: Exception) {
            Log.d("CreateShape", "onCreateShape: CAN'T CONVERT TO FLOAT")
            return
        }
        Log.d("CreateShape", "onCreateShape: $coord1, $coord2")

        val shapesClasses = mapOf(
            "Line" to LineFactory::class.java,
            "Oval" to OvalFactory::class.java,
            "Rectangle" to RectangleFactory::class.java
        )

        val spinner = bottomSheetBinding.spinnerShapes

        val canvas = binding.canvas.canvas
        if (canvas != null) {
            // Get shape factory from spinner text name
            val shapeClass = shapesClasses[spinner.selectedItem.toString()]!!
            // Shape factory constructor
            val shapeConstructor = shapeClass.declaredConstructors[0]
            // Shape factory create method
            val shapeCreateMethod = shapeClass.declaredMethods[0]
            // Shape fields
            Log.d("CreateShape", "onCreateShape: ${shapeClass.fields} ${shapeClass.fields.size}")

            val props = FactoryProps()
            props.penColor = penColor
            props.fillColor = fillColor
            props.penWidth = bottomSheetBinding.penWidthInput.text.toString().toFloatOrNull()?:5f
            props.angle = bottomSheetBinding.angleInput.text.toString().toFloatOrNull()?:0f

            // Create instance of shape
            val shape = shapeConstructor.newInstance(coord1, coord2, props)
            // Invoke create method and add shape to list of shapes in canvas view
            binding.canvas.shapes.add(shapeCreateMethod.invoke(shape) as Shape)
            // Redraw canvas to get new shapes
            binding.canvas.redraw()
        }
    }

    /**
     * Function to create spinner for shape selection inside bottom sheet dialog
     */
    private fun createSpinner() {
        val spinnerItems = arrayOf("Line", "Rectangle", "Oval")
        // Initialize spinner
        val spinner = bottomSheetBinding.spinnerShapes
        // Create adapter
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set spinner adapter
        spinner.adapter = adapter
        // Select first item to be displayed automatically
        spinner.setSelection(0)
    }
}