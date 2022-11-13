package coden.alec.app.display

interface CreateScaleRequestBuilder {
    fun setName(parsedName: String)
    fun setUnit(parsedUnit: String)
    fun setDivisions(parsedDivision: Map<Long, String>)

    fun build(): ParsedScaleRequest
    fun reset()

    val currentName: Result<String>
    val currentUnit: Result<String>
    val currentDivisions: Result<Map<Long, String>>
}