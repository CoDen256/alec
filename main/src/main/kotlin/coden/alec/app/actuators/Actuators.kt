package coden.alec.app.actuators

import coden.alec.app.actuators.scale.CreateScaleRequestBuilder
import coden.alec.app.actuators.scale.ScaleParser
import coden.alec.app.actuators.scale.ScaleResponder
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesResponse


interface HelpActuator {
    fun displayHelp()
}


interface ScaleActuator: ScaleResponder, ScaleParser, CreateScaleRequestBuilder {
    fun listScales(): Result<ListScalesResponse>
    fun createScale(request: CreateScaleRequest): Result<CreateScaleResponse>
}

open class UserException(msg: String? = null): RuntimeException(msg)
class InvalidScaleFormatException(val value: String) : UserException()
class InvalidScalePropertyFormatException(val property: String, val value: String) : UserException()