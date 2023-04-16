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
import com.danilovfa.shapesdrawer.databinding.BottomSheetCreateLayoutBinding
import com.danilovfa.shapesdrawer.databinding.BottomSheetEditLayoutBinding
import com.danilovfa.shapesdrawer.databinding.FragmentMainBinding
import com.danilovfa.shapesdrawer.model.Coordinate
import com.danilovfa.shapesdrawer.model.factories.*
import com.danilovfa.shapesdrawer.model.shapes.Shape
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class MainFragment : Fragment() {
    // Fragment binding
    private lateinit var binding: FragmentMainBinding
    // Bottom sheet bindings
    private lateinit var bottomSheetCreateBinding: BottomSheetCreateLayoutBinding
    private lateinit var bottomSheetEditBinding: BottomSheetEditLayoutBinding
    // Shape color
    var penColor: Int = Color.WHITE
    var fillColor: Int = Color.TRANSPARENT
    var penWidth: Float = 5f
    var angle: Float = 0f
    private val TAG = "Canvas"

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
        val coord1 = binding.canvas.coordDown
        val coord2 = binding.canvas.coordUp

        val distance = binding.canvas.getDistance(coord1, coord2)
        val shapes = binding.canvas.shapes
        // If distance between coordinates is low consider it a click
        // Else consider it a drag and open bottom sheed create dialog
        if (distance <= 50f) {
            // If clicked check all shapes if it contains clicked coordinate
            // If contains open bottom sheet edit dialog
            for (i in shapes.indices.reversed()) {
                if (shapes[i].contains(coord2)) {
                    showBottomSheetEdit(i)
                    break
                }
            }
        } else showBottomSheetCreate(coord1, coord2)
    }

    /**
     * Dialog to create new shape. Opened when user clicks on canvas.
     * @param coords Coordinate (x and y) of click
     */
    private fun showBottomSheetCreate(coord1: Coordinate, coord2: Coordinate) {
        Log.d(TAG, "showBottomSheet: Down=(${coord1.x}, ${coord1.y}), Up=(${coord2.x}, ${coord2.y})")
        // Inflate the bottom sheet layout
        bottomSheetCreateBinding = BottomSheetCreateLayoutBinding.inflate(layoutInflater)

        // Create the bottom sheet dialog
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetCreateBinding.root)
        // Canvas onClick listener to open bottom sheet
        bottomSheetCreateBinding.buttonCreateShape.setOnClickListener {
            onCreateSave(coord1, coord2)
            bottomSheetDialog.dismiss()
        }
        // Color pickers listeners
        bottomSheetCreateBinding.buttonPenColor.setOnClickListener { onPenColorPicker() }
        bottomSheetCreateBinding.buttonFillColor.setOnClickListener { onFillColorPicker() }
        // Create spinner with shapes selection
        addCreateSpinner()
        // Show the bottom sheet dialog
        bottomSheetDialog.show()
    }

    /**
     * Dialog to edit shape. Opened when clicked on existing shape.
     * @param id id of shape in list of shapes in canvas
     */
    private fun showBottomSheetEdit(id: Int) {
        Log.d(TAG, "showBottomSheetEdit: Id=$id")
        // Inflate the bottom sheet layout
        bottomSheetEditBinding = BottomSheetEditLayoutBinding.inflate(layoutInflater)

        // Create the bottom sheet dialog
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetEditBinding.root)

        // Button listeners
        bottomSheetEditBinding.buttonPenColor.setOnClickListener { onPenColorPicker() }
        bottomSheetEditBinding.buttonFillColor.setOnClickListener { onFillColorPicker() }
        bottomSheetEditBinding.buttonSave.setOnClickListener {
            onEditSave(id)
            bottomSheetDialog.dismiss()
        }
        bottomSheetEditBinding.buttonDelete.setOnClickListener {
            onEditDelete(id)
            bottomSheetDialog.dismiss()
        }

        val shape = binding.canvas.getShape(id)

        // Set texts
        bottomSheetEditBinding.editTextX1.setText(shape.coord1.x.toString())
        bottomSheetEditBinding.editTextY1.setText(shape.coord1.y.toString())
        bottomSheetEditBinding.editTextX2.setText(shape.coord2.x.toString())
        bottomSheetEditBinding.editTextY2.setText(shape.coord2.y.toString())
        bottomSheetEditBinding.editTextPenWidth.setText(shape.penWidth.toString())
        bottomSheetEditBinding.editTextAngle.setText(shape.angle.toString())

        // Show the bottom sheet dialog
        bottomSheetDialog.show()
    }

    /**
     * Deletes shape at specified index
     * @param id index of shape to remove
     */
    private fun onEditDelete(id: Int) {
        binding.canvas.removeShape(id)
    }

    /**
     * Replaces edited shape at specified index
     * @param id index of shape to replace
     */
    private fun onEditSave(id: Int) {
        val shape = binding.canvas.getShape(id)

        shape.coord1.x = bottomSheetEditBinding.editTextX1.text.toString().toFloatOrNull()?:0f
        shape.coord1.y = bottomSheetEditBinding.editTextY1.text.toString().toFloatOrNull()?:0f
        shape.coord2.x = bottomSheetEditBinding.editTextX2.text.toString().toFloatOrNull()?:0f
        shape.coord2.y = bottomSheetEditBinding.editTextY2.text.toString().toFloatOrNull()?:0f
        shape.penWidth = bottomSheetEditBinding.editTextPenWidth.text.toString().toFloatOrNull()?:5f
        shape.angle = bottomSheetEditBinding.editTextAngle.text.toString().toFloatOrNull()?:0f
        shape.fillColor = fillColor
        shape.penColor = penColor

        binding.canvas.replaceShape(id, shape)
    }

    private fun onPenColorPicker() {
        val colorPicker = ColorPickerDialog.Builder(requireContext())
            .setTitle("Select Stroke Color")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton("Select",
                ColorEnvelopeListener { colorEnvelope: ColorEnvelope, b: Boolean ->
                    Log.d(TAG, "onClickColorPicker: Color ${colorEnvelope.color}")
                    penColor = colorEnvelope.color
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                })
            .attachAlphaSlideBar(true)
            .attachBrightnessSlideBar(true)
            .setBottomSpace(12)
            .show()
    }

    private fun onFillColorPicker() {
        val colorPicker = ColorPickerDialog.Builder(requireContext())
            .setTitle("Select Stroke Color")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton("Select",
                ColorEnvelopeListener { colorEnvelope: ColorEnvelope, b: Boolean ->
                    Log.d(TAG, "onClickColorPicker: Color ${colorEnvelope.color}")
                    fillColor = colorEnvelope.color
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                })
            .attachAlphaSlideBar(true)
            .attachBrightnessSlideBar(true)
            .setBottomSpace(12)
            .show()
    }

    private fun onCreateSave(coord1: Coordinate, coord2: Coordinate) {
        Log.d(TAG, "onCreateShape: $coord1, $coord2")

        val shapesClasses = mapOf(
            "Line" to LineFactory::class.java,
            "Oval" to OvalFactory::class.java,
            "Rectangle" to RectangleFactory::class.java
        )

        val spinner = bottomSheetCreateBinding.spinnerShapes

        val canvas = binding.canvas.canvas
        if (canvas != null) {
            // Get shape factory from spinner text name
            val shapeClass = shapesClasses[spinner.selectedItem.toString()]!!
            // Shape factory constructor
            val shapeConstructor = shapeClass.declaredConstructors[0]
            // Shape factory create method
            val shapeCreateMethod = shapeClass.declaredMethods[0]
            // Shape fields
            Log.d(TAG, "onCreateShape: ${shapeClass.fields} ${shapeClass.fields.size}")

            val props = FactoryProps()
            props.penColor = penColor
            props.fillColor = fillColor
            props.penWidth = bottomSheetCreateBinding.editTextPenWidth.text.toString().toFloatOrNull()?:5f
            props.angle = bottomSheetCreateBinding.editTextAngle.text.toString().toFloatOrNull()?:0f

            // Create instance of shape
            val shape = shapeConstructor.newInstance(coord1.copy(), coord2.copy(), props)
            // Invoke create method and add shape to canvas
            binding.canvas.addShape(shapeCreateMethod.invoke(shape) as Shape)
        }
    }

    /**
     * Function to create spinner for shape selection inside bottom sheet dialog
     */
    private fun addCreateSpinner() {
        val spinnerItems = arrayOf("Line", "Rectangle", "Oval")
        // Initialize spinner
        val spinner = bottomSheetCreateBinding.spinnerShapes
        // Create adapter
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set spinner adapter
        spinner.adapter = adapter
        // Select first item to be displayed automatically
        spinner.setSelection(0)
    }
}