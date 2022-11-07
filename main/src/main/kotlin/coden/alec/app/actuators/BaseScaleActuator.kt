package coden.alec.app.actuators

import coden.alec.app.display.ScaleParser
import coden.alec.app.display.ScaleResponder
import coden.alec.core.ScaleUseCaseFactory
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesRequest
import coden.alec.interactors.definer.scale.ListScalesResponse
import coden.fsm.Command
import coden.fsm.NoArgException
import java.lang.IllegalArgumentException

class BaseScaleActuator(
    private val useCaseFactory: ScaleUseCaseFactory,
    private val responder: ScaleResponder,
    private val parser: ScaleParser
) : ScaleActuator {

    class ScaleCreationgState(
        private var name: String? = null,
        private var unit: String? = null,
        private var divisions: String? = null
    ){
        val currentName: Result<String> = name?.let {Result.success(it)} ?: Result.failure(NoArgException("No name provided"))

        fun setName(name: String){
            this.name = name
        }
        fun setUnit(unit: String){
            this.unit = unit
        }
        fun setDivisions(divisions: String){
            this.divisions = divisions
        }
    }

    var name: String? = null
    var unit: String? = null
    var divisions: String? = null

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
        saveName(command.arguments.getOrThrow())
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