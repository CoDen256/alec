package coden.alec.app.config.scale

import coden.alec.app.actuators.scale.InvalidScaleFormatException
import coden.alec.app.actuators.scale.InvalidScalePropertyFormatException
import coden.alec.app.actuators.scale.ScaleFormatter
import coden.alec.app.actuators.scale.ScaleResponder
import coden.alec.app.resources.MessageResource
import coden.alec.app.util.sub
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesResponse
import coden.display.displays.MessageDisplay

class BaseScaleResponder(
    private val display: MessageDisplay,
    private val messages: MessageResource,
    private val formatter: ScaleFormatter
) : ScaleResponder {

    override fun respondInvalidScaleFormat(it: InvalidScaleFormatException) {
        display.displayError(messages.rejectScaleMessage.sub(it.value))
    }

    override fun respondInvalidScalePropertyFormat(it: InvalidScalePropertyFormatException) {
        display.displayError(messages.rejectScalePropertyMessage.sub(
            it.property, it.value
        ))
    }

    override fun respondInternalError(it: Throwable) {
        display.displayError("${messages.errorMessage}:\n${it.javaClass.simpleName}:${it.message}")

    }
    override fun respondListScales(response: ListScalesResponse) {
        val scales = response.scales
        if (scales.isEmpty()) {
            display.displayMessage(messages.listScalesEmptyMessage)
        } else {
            display.displayMessage(messages.listScalesMessage.sub(formatter.format(scales)))
        }
    }

    override fun respondCreateScale(response: CreateScaleResponse) {
        val scaleId = response.scaleId
        display.displayMessage(messages.createdScaleMessage + formatter.formatId(scaleId))
    }

    override fun respondPromptScaleName() {
        display.displayPrompt(messages.scaleNamePrompt)
    }

    override fun respondPromptScaleUnit() {
        display.displayPrompt(messages.scaleUnitPrompt)

    }

    override fun respondPromptScaleDivisions() {
        display.displayPrompt(messages.scaleDivisionsPrompt)

    }
}