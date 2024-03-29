package coden.alec.app.actuators.scale

import coden.alec.data.Scale

interface ScaleFormatter {
    fun format(response: List<Scale>): String = response.toString()
    fun format(scale: Scale): String = scale.toString()
}

