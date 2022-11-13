package coden.alec.app.actuators


interface HelpActuator {
    fun displayHelp()
}


interface ScaleActuator {
    fun getAndDisplayScales()

    fun isValidScale(input: String): Boolean
    fun createAndDisplayScale(input: String)
    fun rejectScale()

    fun displayScaleNamePrompt()
    fun isValidScaleName(input: String): Boolean
    fun handleScaleName(input: String)
    fun rejectScaleName()

    fun displayScaleUnitPrompt()
    fun isValidScaleUnit(input: String): Boolean
    fun handleScaleUnit(input: String)
    fun rejectScaleUnit()

    fun displayScaleDivisionsPrompt()
    fun isValidScaleDivisions(input: String): Boolean
    fun handleScaleDivisions(input: String)
    fun rejectScaleDivisions()

    fun isValidScaleFromPreviousInput(input: String): Boolean //TODO rewrite to contain only Result<Unit>
    fun createFromPreviousInputAndDisplayScale()

    fun resetPreviousInputScale()
}


class InvalidScaleFormatException(msg: String) : RuntimeException(msg)