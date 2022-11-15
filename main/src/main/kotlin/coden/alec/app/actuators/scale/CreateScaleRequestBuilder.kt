package coden.alec.app.actuators.scale

import coden.alec.interactors.definer.scale.CreateScaleRequest

interface CreateScaleRequestBuilder {
    fun setName(parsedName: String)
    fun setUnit(parsedUnit: String)
    fun setDivisions(parsedDivision: Map<Long, String>)

    fun build(): CreateScaleRequest
    fun reset()
}