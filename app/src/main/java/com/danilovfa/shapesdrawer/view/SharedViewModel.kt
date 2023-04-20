package com.danilovfa.shapesdrawer.view

import androidx.lifecycle.ViewModel
import com.danilovfa.shapesdrawer.model.shapes.Shape

class SharedViewModel: ViewModel() {
    var shapes: List<Shape>? = null
}