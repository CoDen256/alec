package coden.alec.app.actuators

import coden.fsm.Command
import java.util.StringJoiner


interface HelpActuator {
    fun displayHelp(command: Command)
}


interface ScaleActuator {
    fun getAndDisplayScales(command: Command)

    fun isValidScale(command: Command): Boolean
    fun createAndDisplayScale(command: Command)
    fun rejectScale(command: Command)

    fun displayScaleNamePrompt(command: Command)
    fun isValidScaleName(command: Command): Boolean
    fun handleScaleName(command: Command)
    fun rejectScaleName(command: Command)

    fun displayScaleUnitPrompt(command: Command)
    fun isValidScaleUnit(command: Command): Boolean
    fun handleScaleUnit(command: Command)
    fun rejectScaleUnit(command: Command)

    fun displayScaleDivisionsPrompt(command: Command)
    fun isValidScaleDivisions(command: Command): Boolean
    fun handleScaleDivisions(command: Command)
    fun rejectScaleDivisions(command: Command)

    fun resetScale(command: Command)
}


class InvalidScaleFormatException(msg: String) : RuntimeException(msg)