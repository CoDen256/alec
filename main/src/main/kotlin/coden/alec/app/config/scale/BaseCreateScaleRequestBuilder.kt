package coden.alec.app.config.scale

import coden.alec.app.actuators.scale.CreateScaleRequestBuilder
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.UpdateScaleRequest

class BaseCreateScaleRequestBuilder: CreateScaleRequestBuilder {

    private var id: String? = null
    private var name: String? = null
    private var unit: String? = null
    private var divisions: Map<Long, String>? = null
    override fun setId(parsedId: String) {
        this.id = id
    }

    override fun setName(parsedName: String) {
        this.name = parsedName
    }

    override fun setUnit(parsedUnit: String) {
        this.unit = parsedUnit
    }

    override fun setDivisions(parsedDivision: Map<Long, String>) {
        this.divisions = parsedDivision
    }

    override fun buildCreateRequest(): CreateScaleRequest {
        return CreateScaleRequest(
            name!!,
            unit!!,
            divisions!!
        )
    }

    override fun buildUpdateRequest(): UpdateScaleRequest {
        return UpdateScaleRequest(
            id!!, name, unit, divisions
        )
    }

    override fun reset() {
        id = null
        name = null
        unit = null
        divisions = null
    }
}