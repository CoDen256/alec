package coden.alec.app.actuators

import coden.alec.app.display.CreateScaleRequestBuilder
import coden.alec.app.display.ParsedScaleRequest
import coden.alec.app.display.ScaleParser
import coden.alec.app.display.ScaleResponder
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesResponse


interface HelpActuator {
    fun displayHelp()
}


interface ScaleActuator: ScaleResponder, ScaleParser, CreateScaleRequestBuilder {
    fun listScales(): Result<ListScalesResponse>
    fun createScale(request: ParsedScaleRequest): Result<CreateScaleResponse>
}

open class UserException(msg: String): RuntimeException(msg)
open class InternalException(msg: String): RuntimeException(msg)
class InvalidScaleFormatException(msg: String) : UserException(msg)