package coden.alec.app.config.scale

import coden.alec.app.actuators.scale.CreateScaleRequestBuilder
import coden.alec.interactors.definer.scale.CreateScaleRequest

class BaseCreateScaleRequestBuilder: CreateScaleRequestBuilder {

    private var name: String? = null
    private var unit: String? = null
    private var divisions: Map<Long, String>? = null

    override fun setName(parsedName: String) {
        this.name = parsedName
    }

    override fun setUnit(parsedUnit: String) {
        this.unit = parsedUnit
    }

    override fun setDivisions(parsedDivision: Map<Long, String>) {
        this.divisions = parsedDivision
    }

    override fun build(): CreateScaleRequest {
        return CreateScaleRequest(
            name!!,
            unit!!,
            divisions!!
        )
    }

    override fun reset() {
        name = null
        unit = null
        divisions = null
    }
}