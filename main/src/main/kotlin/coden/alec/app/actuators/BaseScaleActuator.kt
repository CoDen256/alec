package coden.alec.app.actuators

import coden.alec.app.display.ScaleParser
import coden.alec.app.display.ScaleResponder
import coden.alec.core.ScaleUseCaseFactory
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesRequest
import coden.alec.interactors.definer.scale.ListScalesResponse
import coden.fsm.NoArgException

class BaseScaleActuator(
    private val useCaseFactory: ScaleUseCaseFactory,
    private val responder: ScaleResponder,
    private val parser: ScaleParser
) : ScaleActuator, ScaleResponder by responder, ScaleParser by parser {

    class ScaleCreatingState(
        private var name: String? = null,
        private var unit: String? = null,
        private var divisions: String? = null
    ) {
        val currentName: Result<String> get() = name.nullToError("scale name")
        val currentUnit: Result<String> get() = name.nullToError("scale unit")
        val currentDivisions: Result<String> get() = name.nullToError("scale divisions")

        private fun String?.nullToError(property: String): Result<String> =
            this?.let { Result.success(it) } ?: Result.failure(NoArgException("No $property provided"))

        fun setName(name: String) {
            this.name = name
        }

        fun setUnit(unit: String) {
            this.unit = unit
        }

        fun setDivisions(divisions: String) {
            this.divisions = divisions
        }

        fun clear() {
            name = null
            unit = null
            divisions = null
        }
    }

    private val state = ScaleCreatingState()

    override fun getAndDisplayScales() {
        val response = useCaseFactory.listScales().execute(ListScalesRequest()) as Result<ListScalesResponse>
        responder.respondListScales(response.getOrThrow())
    }


    override fun createScale(input: String): Result<CreateScaleResponse> {
        return useCaseFactory.createScale().execute(parser.parseCreateScaleRequest(input)) as Result<CreateScaleResponse>
    }


    override fun rejectScale() {
        responder.respondRejectScale()
    }


    override fun displayScaleNamePrompt() {
        responder.respondPromptScaleName()
    }

    override fun handleScaleName(parsedName: String) {
        TODO("Not yet implemented")
    }

    override fun parseScaleName(input: String): Result<String> {
        return parser.parseScaleName(input)
    }


    override fun displayScaleUnitPrompt() {
        responder.respondPromptScaleUnit()
    }

    override fun isValidScaleUnit(input: String): Boolean {
        return parser.isValidScaleUnit(input)
    }

    override fun handleScaleUnit(input: String) {
        state.setUnit(input)
    }

    override fun rejectScaleUnit() {
        responder.respondRejectScaleUnit()
    }

    override fun displayScaleDivisionsPrompt() {
        responder.respondPromptScaleDivisions()
    }

    override fun isValidScaleDivisions(input: String): Boolean {
        return parser.isValidDivisions(input)
    }

    override fun handleScaleDivisions(input: String) {
        state.setDivisions(input)
    }

    override fun rejectScaleDivisions() {
        responder.respondRejectScaleDivisions()
    }

    override fun isValidScaleFromPreviousInput(input: String): Boolean {
        val unit = state.currentUnit.getOrNull() ?: return false
        val divisions = state.currentDivisions.getOrNull() ?: return false
        return  isValidScaleUnit(unit) && isValidScaleDivisions(divisions)
    }

    override fun createFromPreviousInputAndDisplayScale() {
        useCaseFactory.createScale().execute(
            parser.parseCreateScaleRequest(
                state.currentName.getOrThrow(),
                state.currentUnit.getOrThrow(),
                state.currentDivisions.getOrThrow()
            )
        ) as Result<CreateScaleResponse>
    }

    override fun resetPreviousInputScale() {
        state.clear()
    }
}