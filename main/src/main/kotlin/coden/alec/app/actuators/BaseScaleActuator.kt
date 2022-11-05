package coden.alec.app.actuators

import coden.alec.app.display.ScaleParser
import coden.alec.app.display.ScaleResponder
import coden.alec.core.ScaleUseCaseFactory
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesRequest
import coden.alec.interactors.definer.scale.ListScalesResponse
import coden.fsm.Command

class BaseScaleActuator(
    private val useCaseFactory: ScaleUseCaseFactory,
    private val responder: ScaleResponder,
    private val parser: ScaleParser
) : ScaleActuator {

    private var name: String? = null
    private var unit: String? = null
    private var divisions: String? = null

    override fun getAndDisplayScales(command: Command) {
        val response = useCaseFactory.listScales().execute(ListScalesRequest()) as ListScalesResponse
        responder.respondListScales(response)
    }

    override fun isValidScale(command: Command): Boolean {
        val arguments = command.arguments.getOrThrow()
        return if (name != null && unit != null) {
            true
        } else {
            return parser.isValidCreateScaleRequest(arguments)
        }

    }

    override fun createAndDisplayScale(command: Command) {
        val request = if (isScaleInputSplit()) parseSplitScaleInput() else
            parseCompleteScaleInput(command)

        val response = useCaseFactory.createScale().execute(request) as CreateScaleResponse
        responder.respondCreateScale(response)
    }

    private fun isScaleInputSplit(): Boolean {
        return name != null && unit != null && divisions != null
    }

    private fun parseSplitScaleInput(): CreateScaleRequest {
        return parser.parseCreateScaleRequest(name!!, unit!!, divisions!!)
    }

    private fun parseCompleteScaleInput(command: Command): CreateScaleRequest {
        val argument = command.arguments.getOrThrow()
        if (!parser.isValidCreateScaleRequest(argument)) throw InvalidScaleFormatException("Invalid scale format: $argument")
        return parser.parseCreateScaleRequest(argument)
    }

    override fun rejectScale(command: Command) {
        responder.respondRejectScale()
    }


    override fun displayScaleNamePrompt(command: Command) {
        responder.respondPromptScaleName()
    }

    override fun isValidScaleName(command: Command): Boolean {
        return parser.isValidScaleName(command.arguments.getOrThrow())
    }

    override fun handleScaleName(command: Command) {
        val arguments = command.arguments.getOrThrow()
        if (parser.isValidScaleName(arguments)){
            saveName(arguments)
        }else {

        }
    }

    private fun saveName(arguments: String) {
        name = arguments
    }

    override fun rejectScaleName(command: Command) {
        responder.respondRejectScaleName()
    }

    override fun displayScaleUnitPrompt(command: Command) {
        responder.respondPromptScaleUnit()
    }

    override fun isValidScaleUnit(command: Command): Boolean {
        return parser.isValidScaleUnit(command.arguments.getOrThrow())

    }

    override fun handleScaleUnit(command: Command) {
        command.arguments.onSuccess {
            unit = it
        }.onFailure {
            rejectScaleUnit(command)
        }
    }

    override fun rejectScaleUnit(command: Command) {
        responder.respondRejectScaleUnit()
    }

    override fun displayScaleDivisionsPrompt(command: Command) {
        responder.respondPromptScaleDivisions()
    }

    override fun isValidScaleDivisions(command: Command): Boolean {
        return parser.isValidDivisions(command.arguments.getOrThrow())

    }

    override fun handleScaleDivisions(command: Command) {
        command.arguments.onSuccess {
            divisions = it
        }.onFailure {
            rejectScaleDivisions(command)
        }
    }

    override fun rejectScaleDivisions(command: Command) {
        responder.respondRejectScaleDivisions()
    }

    override fun resetScale(command: Command) {
        name = null
        unit = null
        divisions = null
    }
}