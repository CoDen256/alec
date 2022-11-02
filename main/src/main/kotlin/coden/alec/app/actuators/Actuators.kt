package coden.alec.app.actuators

import coden.alec.app.resources.MessageResource
import coden.display.displays.MessageDisplay
import coden.alec.core.UseCaseFactory
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesRequest
import coden.alec.interactors.definer.scale.ListScalesResponse
import coden.fsm.Command
import java.util.regex.Pattern


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

class BaseHelpActuator(
    private val useCaseFactory: UseCaseFactory,
    private val view: MessageDisplay,
    private val messages: MessageResource,
) : HelpActuator {
    override fun displayHelp(command: Command) {
//        view.displayMenu(messages.startMessage)
    }
}


class BaseScaleActuator(
    private val useCaseFactory: UseCaseFactory,
    private val view: MessageDisplay,
    private val messages: MessageResource,
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
                view.displayMessage(messages.listScalesEmptyMessage)
            } else {
                view.displayMessage(messages.listScalesMessage + it)
            }
        }.onFailure {
            view.displayError("${messages.errorMessage} ${it.message}")
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
            view.displayMessage("Added id: $it")
        }.onFailure {
            view.displayError("${messages.errorMessage} ${it.message}")
        }
    }

    override fun rejectScale(command: Command) {
        view.displayError("Scale is invalid")
    }


    override fun displayScaleNamePrompt(command: Command) {
        view.displayPrompt("Input the name of the scale:")
    }

    override fun isValidScaleName(command: Command): Boolean {
        return command.arguments.getOrNull()?.matches(namePattern.toRegex()) ?: false
    }

    override fun handleScaleName(command: Command) {
        command.arguments.onSuccess {
            name = it
        }.onFailure {
            view.displayError("Invalid name format")
        }
    }

    override fun rejectScaleName(command: Command) {
        view.displayError("Invalid scale name")
    }

    override fun displayScaleUnitPrompt(command: Command) {
        view.displayError("Input the unit")
    }

    override fun isValidScaleUnit(command: Command): Boolean {
        return command.arguments.getOrNull()?.matches(namePattern.toRegex()) ?: false
    }

    override fun handleScaleUnit(command: Command) {
        command.arguments.onSuccess {
            unit = it
        }.onFailure {
            view.displayError("Invalid name format")
        }
    }

    override fun rejectScaleUnit(command: Command) {
        view.displayError("Invalid format of the unit")
    }

    override fun displayScaleDivisionsPrompt(command: Command) {
        view.displayPrompt("Input the divisions:")
    }

    override fun isValidScaleDivisions(command: Command): Boolean {
        return command.arguments.getOrNull()?.matches(divisionPattern.toRegex()) ?: false
    }

    override fun handleScaleDivisions(command: Command) {
        command.arguments.onSuccess {
            divisions = it
        }.onFailure {
            view.displayError("Invalid name format")
        }
    }

    override fun rejectScaleDivisions(command: Command) {
        view.displayError("Invalid division format")
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