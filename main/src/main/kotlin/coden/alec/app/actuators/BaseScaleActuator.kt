package coden.alec.app.actuators

import coden.alec.app.display.ScaleParser
import coden.alec.app.display.ScaleResponder
import coden.alec.core.ScaleUseCaseFactory
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesRequest
import coden.alec.interactors.definer.scale.ListScalesResponse
import coden.fsm.NoArgException

class BaseScaleActuator(
    private val useCaseFactory: ScaleUseCaseFactory,
    private val responder: ScaleResponder,
    private val parser: ScaleParser
) : ScaleActuator {

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
        val response = useCaseFactory.listScales().execute(ListScalesRequest()) as ListScalesResponse
        responder.respondListScales(response)
    }

    override fun isValidScale(input: String): Boolean {
        return parser.isValidCreateScaleRequest(input)
    }

    override fun createAndDisplayScale(input: String) {
        respondToRequest(parser.parseCreateScaleRequest(input))
    }

    private fun respondToRequest(request: CreateScaleRequest) {
        val response = useCaseFactory.createScale().execute(request) as CreateScaleResponse
        responder.respondCreateScale(response)
    }

    override fun rejectScale() {
        responder.respondRejectScale()
    }

    override fun displayScaleNamePrompt() {
        responder.respondPromptScaleName()
    }

    override fun isValidScaleName(input: String): Boolean {
        return parser.isValidScaleName(input)
    }

    override fun handleScaleName(input: String) {
        state.setName(input)
    }


    override fun rejectScaleName() {
        responder.respondRejectScaleName()
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
        val name = state.currentName.getOrNull() ?: return false
        val unit = state.currentUnit.getOrNull() ?: return false
        val divisions = state.currentDivisions.getOrNull() ?: return false
        return isValidScaleName(name) && isValidScaleUnit(unit) && isValidScaleDivisions(divisions)
    }

    override fun createFromPreviousInputAndDisplayScale() {
        respondToRequest(
            parser.parseCreateScaleRequest(
                state.currentName.getOrThrow(),
                state.currentUnit.getOrThrow(),
                state.currentDivisions.getOrThrow()
            )
        )
    }

    override fun resetPreviousInputScale() {
        state.clear()
    }
}