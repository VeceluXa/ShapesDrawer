package com.danilovfa.shapesdrawer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.danilovfa.shapesdrawer.databinding.BottomSheetLayoutBinding
import com.danilovfa.shapesdrawer.databinding.FragmentMainBinding
import com.danilovfa.shapesdrawer.model.Coordinate
import com.danilovfa.shapesdrawer.model.factories.LineFactory
import com.danilovfa.shapesdrawer.model.factories.OvalFactory
import com.danilovfa.shapesdrawer.model.factories.RectangleFactory
import com.danilovfa.shapesdrawer.model.shapes.Shape
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    // Fragment binding
    private lateinit var binding: FragmentMainBinding
    // Bottom sheet binding
    private lateinit var bottomSheetBinding: BottomSheetLayoutBinding

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
        // Set bottom sheets listeners
        bottomSheetBinding.buttonCreateShape.setOnClickListener { onCreateShape(coords) }
        bottomSheetDialog.setOnDismissListener { binding.canvas.redraw() }
        // Create spinner with shapes selection
        createSpinner()
        // Show the bottom sheet dialog
        bottomSheetDialog.show()
    }

    private fun onCreateShape(coord1: Coordinate) {
        val coord2 = Coordinate(bottomSheetBinding.textInput1.text.toString().toFloat(), bottomSheetBinding.textInput2.text.toString().toFloat())
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
            // Create instance of shape
            val shape = shapeConstructor.newInstance(coord1, coord2)
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