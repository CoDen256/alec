package coden.alec.app.display

import coden.fsm.NoArgException

class ScaleCreatingState(
    private var name: String? = null,
    private var unit: String? = null,
    private var divisions: Map<Long, String>? = null
) : CreateScaleRequestBuilder {
    override val currentName: Result<String> get() = name.nullToError("scale name")
    override val currentUnit: Result<String> get() = unit.nullToError("scale unit")
    override val currentDivisions: Result<Map<Long, String>> get() = divisions.nullToError("scale divisions")

    private fun <T> T?.nullToError(property: String): Result<T> =
        this?.let { Result.success(it) } ?: Result.failure(NoArgException("No $property provided"))

    override fun setName(parsedName: String) {
        this.name = parsedName
    }

    override fun setUnit(parsedUnit: String) {
        this.unit = parsedUnit
    }

    override fun setDivisions(parsedDivision: Map<Long, String>) {
        this.divisions = parsedDivision
    }

    override fun build(): ParsedScaleRequest {
        return ParsedScaleRequest(
            currentName.getOrThrow(),
            currentUnit.getOrThrow(),
            currentDivisions.getOrThrow()
        )
    }

    override fun reset() {
        name = null
        unit = null
        divisions = null
    }
}