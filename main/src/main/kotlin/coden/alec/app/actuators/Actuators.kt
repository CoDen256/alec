package coden.alec.app.actuators

import coden.alec.app.actuators.scale.*
import coden.fsm.FSMTable


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



interface FSMTableBuilder<T> {
    fun buildTableFrom(builder: T) = builder.buildTable()
    fun T.buildTable(): FSMTable
}

open class UserException(msg: String? = null) : RuntimeException(msg)
