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
    fun displayHelp(command: Command): Boolean
}


interface ScaleActuator {
    fun getAndDisplayScales(command: Command) : Boolean
    fun createAndDisplayScale(command: Command): Boolean

    fun displayScaleNamePrompt(command: Command): Boolean
    fun handleScaleName(command: Command): Boolean

    fun displayScaleUnitPrompt(command: Command): Boolean
    fun handleScaleUnit(command: Command): Boolean

    fun displayScaleDivisionsPrompt(command: Command): Boolean
    fun handleScaleDivisions(command: Command): Boolean

    fun resetScale(): Boolean
}

class BaseHelpActuator(
    private val useCaseFactory: UseCaseFactory,
    private val view: View,
    private val messages: MessageResource,
): HelpActuator {
    override fun displayHelp(command: Command): Boolean {
        view.displayMessage(messages.startMessage)
        return true
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

    override fun getAndDisplayScales(command: Command): Boolean {
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
        return true
    }

    override fun createAndDisplayScale(command: Command): Boolean {
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
        return true
    }

    private fun collectArgs(command: Command): Triple<String, String, String> {
        if (name != null && unit != null && divisions != null) {
            return Triple(name!!, unit!!, divisions!!)
        }
        val split = command.arguments.getOrThrow().split("\n", limit = 2)
        return Triple(split[0], split[1], split[2])
    }

    override fun displayScaleNamePrompt(command: Command): Boolean {
        view.displayPrompt("Input the name of the scale:")
        return true
    }

    override fun handleScaleName(command: Command): Boolean {
        return command.arguments.onSuccess {
            name = it
        }.onFailure {
            view.displayError("Invalid name format")
        }.isSuccess
    }

    override fun displayScaleUnitPrompt(command: Command): Boolean {
        view.displayError("Invalid format of the unit")
        return true
    }

    override fun handleScaleUnit(command: Command): Boolean {
        return command.arguments.onSuccess {
            name = it
        }.onFailure {
            view.displayError("Invalid name format")
        }.isSuccess
    }

    override fun displayScaleDivisionsPrompt(command: Command): Boolean {
        view.displayPrompt("Input the divisions:")
        return true
    }

    override fun handleScaleDivisions(command: Command): Boolean {
        return command.arguments.onSuccess {
            name = it
        }.onFailure {
            view.displayError("Invalid name format")
        }.isSuccess
    }

    override fun resetScale(): Boolean {
        name = null
        unit = null
        divisions = null
        return true
    }
}