package coden.alec.app.actuators

import coden.alec.data.Scale

interface ScaleFormatter {
    fun format(response: List<Scale>): String = response.toString()
    fun format(scale: Scale): String = scale.toString()
    fun formatId(scaleId: String): String = scaleId
}