package coden.alec.app.actuators

import coden.alec.app.display.ScaleResponder
import coden.alec.core.ScaleUseCaseFactory
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesRequest
import coden.alec.interactors.definer.scale.ListScalesResponse
import coden.fsm.Command
import java.util.regex.Pattern

class BaseScaleActuator(
    private val useCaseFactory: ScaleUseCaseFactory,
    private val responder: ScaleResponder
) : ScaleActuator {

    private val scalePattern = Pattern.compile(
        "" +
                "[A-Za-z0-9_-]{1,10}" +
                "\n[A-Za-z/-]{1,10}" +
                "(\n\\d+-[A-Za-z0-9_-]+)+"
    )

    private val namePattern = Pattern.compile("\\w+")

    private val divisionPattern = Pattern.compile(
        "\\d+-[A-Za-z0-9_-]+" +
                "(\n\\d+-[A-Za-z0-9_-]+)*"
    )

    private var name: String? = null
    private var unit: String? = null
    private var divisions: String? = null

    override fun getAndDisplayScales(command: Command) {
        val listScales = useCaseFactory.listScales()
        val response = listScales.execute(ListScalesRequest()) as ListScalesResponse
        responder.respondListScales(response)
    }

    override fun isValidScale(command: Command): Boolean {
        return if (name != null && unit != null){
            true
        }else {
            command.arguments.getOrNull()?.matches(scalePattern.toRegex()) ?: false
        }

    }

    override fun createAndDisplayScale(command: Command) {
        if (!isValidScale(command)) throw InvalidScaleFormatException("Invalid scale format: ${command.arguments.getOrNull().orEmpty()}")
        val (name, unit, divisionString) = collectArgs(command.arguments.getOrThrow())
        val divisions = HashMap<Long, String>()
        for (arg in divisionString.split("\n")) {
            val division = arg.split("-")
            divisions[division[0].toLong()] = division[1]
        }
        val createScale = useCaseFactory.createScale()
        val response = createScale.execute(
            CreateScaleRequest(
                name = name,
                unit = unit,
                divisions
            )
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
        return command.arguments.getOrNull()?.matches(namePattern.toRegex()) ?: false
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
        return command.arguments.getOrNull()?.matches(namePattern.toRegex()) ?: false
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
        return command.arguments.getOrNull()?.matches(divisionPattern.toRegex()) ?: false
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

    private fun collectArgs(args: String): Triple<String, String, String> {
        if (name != null && unit != null && divisions != null) {
            return Triple(name!!, unit!!, divisions!!)
        }
        val split = args.split("\n", limit = 3)
        return Triple(split[0], split[1], split[2])
    }
}