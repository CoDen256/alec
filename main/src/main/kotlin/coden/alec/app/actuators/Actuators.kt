package coden.alec.app.actuators

import coden.alec.interactors.definer.scale.CreateScaleResponse


interface HelpActuator {
    fun displayHelp()
}


interface ScaleActuator {
    fun getAndDisplayScales()

    fun createScale(input: String): Result<CreateScaleResponse>
    fun displayScale(response: CreateScaleResponse)

    fun displayScaleNamePrompt()
    fun isValidScaleName(input: String): Boolean
    fun handleScaleName(input: String)
    fun rejectScaleName()

    fun displayScaleUnitPrompt()
    fun isValidScaleUnit(input: String): Boolean
    fun handleScaleUnit(input: String)
    fun rejectScaleUnit()

    fun displayScaleDivisionsPrompt()
    fun isValidScaleDivisions(input: String): Boolean
    fun handleScaleDivisions(input: String)
    fun rejectScaleDivisions()

    fun isValidScaleFromPreviousInput(input: String): Boolean //TODO rewrite to contain only Result<Unit>
    fun createFromPreviousInputAndDisplayScale()

    fun rejectScale()
    fun onError(throwable: Throwable)
    fun onInternalError(throwable: Throwable)
    fun resetPreviousInputScale()
}

open class UserException(msg: String): RuntimeException(msg)
open class InternalError(msg: String): RuntimeException(msg)
class InvalidScaleFormatException(msg: String) : UserException(msg)