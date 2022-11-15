package coden.alec.app.actuators

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

open class UserException(msg: String): RuntimeException(msg)
open class InternalException(msg: String): RuntimeException(msg)
class InvalidScaleFormatException(msg: String) : UserException(msg)