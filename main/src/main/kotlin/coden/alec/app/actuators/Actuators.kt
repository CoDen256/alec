package coden.alec.app.actuators

import coden.alec.app.display.ScaleParser
import coden.alec.app.display.ScaleResponder
import coden.alec.interactors.definer.scale.CreateScaleResponse


interface HelpActuator {
    fun displayHelp()
}


interface ScaleActuator: ScaleResponder, ScaleParser {
    fun getAndDisplayScales()

    fun createScale(input: String): Result<CreateScaleResponse>
    fun displayScaleNamePrompt()
    fun handleScaleName(parsedName: String)

    fun displayScaleUnitPrompt()
    fun handleScaleUnit(input: String)
    fun rejectScaleUnit()

    fun displayScaleDivisionsPrompt()
    fun isValidScaleDivisions(input: String): Boolean
    fun handleScaleDivisions(input: String)
    fun rejectScaleDivisions()

    fun isValidScaleFromPreviousInput(input: String): Boolean //TODO rewrite to contain only Result<Unit>
    fun createFromPreviousInputAndDisplayScale()

    fun rejectScale()
    fun onUserError(throwable: UserException)
    fun onInternalError(throwable: InternalException)
    fun resetPreviousInputScale()
}

open class UserException(msg: String): RuntimeException(msg)
open class InternalException(msg: String): RuntimeException(msg)
class InvalidScaleFormatException(msg: String) : UserException(msg)