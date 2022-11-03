package coden.alec.app.actuators

import coden.alec.app.formatter.ListScalesResponseFormatter
import coden.alec.app.resources.MessageResource
import coden.alec.app.util.s
import coden.alec.core.UseCaseFactory
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesRequest
import coden.alec.interactors.definer.scale.ListScalesResponse
import coden.display.displays.MessageDisplay
import coden.fsm.Command
import java.util.regex.Pattern

class BaseScaleActuator(
    private val useCaseFactory: UseCaseFactory,
    private val display: MessageDisplay,
    private val messages: MessageResource,
    private val formatter: ListScalesResponseFormatter
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
        response.scales.onSuccess {
            if (it.isEmpty()) {
                display.displayMessage(messages.listScalesEmptyMessage)
            } else {
                display.displayMessage(messages.listScalesMessage.s(formatter.format(it)))
            }
        }.onFailure {
            display.displayError("${messages.errorMessage} ${it.message}")
        }
    }

    override fun isValidScale(command: Command): Boolean {
        return if (name != null && unit != null){
            true
        }else {
            command.arguments.getOrNull()?.matches(scalePattern.toRegex()) ?: false
        }

    }

    override fun createAndDisplayScale(command: Command) {
        val (name, unit, divisionString) = collectArgs(command)
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
        response.scaleId.onSuccess {
            display.displayMessage("Added id: $it")
        }.onFailure {
            display.displayError("${messages.errorMessage} ${it.message}")
        }
    }

    override fun rejectScale(command: Command) {
        display.displayError("Scale is invalid")
    }


    override fun displayScaleNamePrompt(command: Command) {
        display.displayPrompt("Input the name of the scale:")
    }

    override fun isValidScaleName(command: Command): Boolean {
        return command.arguments.getOrNull()?.matches(namePattern.toRegex()) ?: false
    }

    override fun handleScaleName(command: Command) {
        command.arguments.onSuccess {
            name = it
        }.onFailure {
            display.displayError("Invalid name format")
        }
    }

    override fun rejectScaleName(command: Command) {
        display.displayError("Invalid scale name")
    }

    override fun displayScaleUnitPrompt(command: Command) {
        display.displayPrompt("Input the unit")
    }

    override fun isValidScaleUnit(command: Command): Boolean {
        return command.arguments.getOrNull()?.matches(namePattern.toRegex()) ?: false
    }

    override fun handleScaleUnit(command: Command) {
        command.arguments.onSuccess {
            unit = it
        }.onFailure {
            display.displayError("Invalid name format")
        }
    }

    override fun rejectScaleUnit(command: Command) {
        display.displayError("Invalid format of the unit")
    }

    override fun displayScaleDivisionsPrompt(command: Command) {
        display.displayPrompt("Input the divisions:")
    }

    override fun isValidScaleDivisions(command: Command): Boolean {
        return command.arguments.getOrNull()?.matches(divisionPattern.toRegex()) ?: false
    }

    override fun handleScaleDivisions(command: Command) {
        command.arguments.onSuccess {
            divisions = it
        }.onFailure {
            display.displayError("Invalid name format")
        }
    }

    override fun rejectScaleDivisions(command: Command) {
        display.displayError("Invalid division format")
    }

    override fun resetScale(command: Command) {
        name = null
        unit = null
        divisions = null
    }

    private fun collectArgs(command: Command): Triple<String, String, String> {
        if (name != null && unit != null && divisions != null) {
            return Triple(name!!, unit!!, divisions!!)
        }
        val split = command.arguments.getOrThrow().split("\n", limit = 3)
        return Triple(split[0], split[1], split[2])
    }
}