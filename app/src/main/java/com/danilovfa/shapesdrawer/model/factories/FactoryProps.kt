package com.danilovfa.shapesdrawer.model.factories

import android.graphics.Color

data class FactoryProps(
    var penColor: Int = Color.WHITE,
    var fillColor: Int = Color.TRANSPARENT,
    var penWidth: Float = 5f,
    var angle: Float = 0f,
)