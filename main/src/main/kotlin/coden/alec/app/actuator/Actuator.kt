package coden.alec.app.actuator

import coden.alec.app.states.Command
import coden.alec.app.states.UseCaseFactory
import coden.alec.bot.messages.MessageResource
import coden.alec.bot.presenter.View
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesRequest
import coden.alec.interactors.definer.scale.ListScalesResponse
import java.util.regex.Pattern


interface HelpActuator {
    fun displayHelp(command: Command)
}


interface ScaleActuator {
    fun getAndDisplayScales(command: Command)
    fun createAndDisplayScale(command: Command)

    fun displayScaleNamePrompt(command: Command)
    fun isValidScaleName(command: Command): Boolean
    fun handleScaleName(command: Command)

    fun displayScaleUnitPrompt(command: Command)
    fun isValidUnitName(command: Command) : Boolean
    fun handleScaleUnit(command: Command)

    fun displayScaleDivisionsPrompt(command: Command)
    fun isValidScaleDivisions(command: Command) : Boolean
    fun handleScaleDivisions(command: Command)

    fun resetScale()
}

class BaseHelpActuator(
    private val useCaseFactory: UseCaseFactory,
    private val view: View,
    private val messages: MessageResource,
): HelpActuator {
    override fun displayHelp(command: Command) {
        view.displayMessage(messages.startMessage)
    }
}


class BaseScaleActuator(
    private val useCaseFactory: UseCaseFactory,
    private val view: View,
    private val messages: MessageResource,
) : ScaleActuator {

    private val scalePattern = Pattern.compile("" +
            "[A-Za-z0-9_-]{1,10}" +
            "\n[A-Za-z/-]{1,10}" +
            "(\n\\d+-[A-Za-z0-9_-]+)+")

    private val namePattern = Pattern.compile("\\w+")

    private val divisionPattern = Pattern.compile("\\d+-[A-Za-z0-9_-]+" +
            "(\n\\d+-[A-Za-z0-9_-]+)*")

    private var name: String? = null
    private var unit: String? = null
    private var divisions: String? = null

    override fun getAndDisplayScales(command: Command) {
        val listScales = useCaseFactory.listScales()
        val response = listScales.execute(ListScalesRequest()) as ListScalesResponse
        response.scales.onSuccess {
            if (it.isEmpty()){
                view.displayMessage(messages.listScalesEmptyMessage)
            }else {
                view.displayMessage(messages.listScalesMessage + it)
            }
        }.onFailure {
            view.displayError("${messages.errorMessage} ${it.message}")
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

    private fun collectArgs(command: Command): Triple<String, String, String> {
        if (name != null && unit != null && divisions != null) {
            return Triple(name!!, unit!!, divisions!!)
        }
        val split = command.arguments.getOrThrow().split("\n", limit = 2)
        return Triple(split[0], split[1], split[2])
    }

    override fun displayScaleNamePrompt(command: Command) {
        view.displayPrompt("Input the name of the scale:")
    }

    override fun isValidScaleName(command: Command): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleScaleName(command: Command) {
        return command.arguments.onSuccess {
            name = it
        }.onFailure {
            view.displayError("Invalid name format")
        }.isSuccess
    }

    override fun displayScaleUnitPrompt(command: Command) {
        view.displayError("Invalid format of the unit")
    }

    override fun isValidUnitName(command: Command): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleScaleUnit(command: Command) {
        return command.arguments.onSuccess {
            name = it
        }.onFailure {
            view.displayError("Invalid name format")
        }.isSuccess
    }

    override fun displayScaleDivisionsPrompt(command: Command) {
        view.displayPrompt("Input the divisions:")
    }

    override fun isValidScaleDivisions(command: Command): Boolean {
        TODO("Not yet implemented")
    }

    override fun handleScaleDivisions(command: Command) {
        command.arguments.onSuccess {
            name = it
        }.onFailure {
            view.displayError("Invalid name format")
        }.isSuccess
    }

    override fun resetScale() {
        name = null
        unit = null
        divisions = null
    }
}