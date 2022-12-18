package coden.alec.app.actuators.scale

import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.UpdateScaleRequest

interface CreateScaleRequestBuilder {
    fun setId(parsedId: String)
    fun setName(parsedName: String)
    fun setUnit(parsedUnit: String)
    fun setDivisions(parsedDivision: Map<Long, String>)

    fun buildCreateRequest(): CreateScaleRequest
    fun buildUpdateRequest(): UpdateScaleRequest

    fun reset()
}