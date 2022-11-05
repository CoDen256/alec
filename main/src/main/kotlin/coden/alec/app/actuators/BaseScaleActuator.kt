package coden.alec.app.actuators

import coden.alec.app.display.ScaleParser
import coden.alec.app.display.ScaleResponder
import coden.alec.core.ScaleUseCaseFactory
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
        return if (name != null && unit != null) {
            true
        } else {
            return command.arguments.getOrNull()?.let { parser.isValidCreateScaleRequest(it) } ?: false
        }

    }

    override fun createAndDisplayScale(command: Command) {
        if (!isValidScale(command)) throw InvalidScaleFormatException(
            "Invalid scale format: ${
                command.arguments.getOrNull().orEmpty()
            }"
        )

        val input = command.arguments.getOrNull()!!
        val response = useCaseFactory.createScale().execute(
            parser.parseCreateScaleRequest(input)
        ) as CreateScaleResponse
        responder.respondCreateScale(response)
    }

    override fun rejectScale(command: Command) {
        responder.respondRejectScale()
    }


    override fun displayScaleNamePrompt(command: Command) {
        responder.respondPromptScaleName()
    }

    override fun isValidScaleName(command: Command): Boolean {
        return command.arguments.getOrNull()?.let { parser.isValidScaleName(it) } ?: false
    }

    override fun handleScaleName(command: Command) {
        command.arguments.onSuccess {
            name = it
        }.onFailure {
            rejectScaleName(command)
        }
    }

    override fun rejectScaleName(command: Command) {
        responder.respondRejectScaleName()
    }

    override fun displayScaleUnitPrompt(command: Command) {
        responder.respondPromptScaleUnit()
    }

    override fun isValidScaleUnit(command: Command): Boolean {
        return command.arguments.getOrNull()?.let { parser.isValidScaleName(it) } ?: false

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
        return command.arguments.getOrNull()?.let { parser.isValidScaleName(it) } ?: false

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