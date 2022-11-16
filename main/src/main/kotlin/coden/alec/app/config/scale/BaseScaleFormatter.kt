package coden.alec.app.config.scale

import coden.alec.app.actuators.scale.ScaleFormatter
import coden.alec.data.Scale

class BaseScaleFormatter : ScaleFormatter {
    override fun format(response: List<Scale>): String {
        return response.mapIndexed { index, scale ->
            "${index}.${format(scale)}"
        }.joinToString("\n\n")
    }

    override fun format(scale: Scale): String {
        return "[${scale.id}] - ${scale.name}(${scale.unit}):\n" +
                scale.divisions.joinToString("\n") { "\t${it.value} - ${it.description}" }
    }
}