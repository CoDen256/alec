package coden.alec.app.actuators

import coden.alec.app.actuators.scale.CreateScaleRequestBuilder
import coden.alec.app.actuators.scale.ScaleParser
import coden.alec.app.actuators.scale.ScaleResponder
import coden.alec.app.actuators.scale.ScaleUseCaseInvoker


interface HelpActuator {
    fun displayHelp()
}


interface ScaleActuator: ScaleResponder, ScaleParser, CreateScaleRequestBuilder, ScaleUseCaseInvoker
class BaseScaleActuator(
    private val responder: ScaleResponder,
    private val parser: ScaleParser,
    private val builder: CreateScaleRequestBuilder,
    private val useCaseInvoker: ScaleUseCaseInvoker
) : ScaleActuator,
    ScaleUseCaseInvoker by useCaseInvoker,
    ScaleResponder by responder,
    ScaleParser by parser,
    CreateScaleRequestBuilder by builder


open class UserException(msg: String? = null): RuntimeException(msg)
