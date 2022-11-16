package coden.alec.app.config.scale

import coden.alec.app.actuators.scale.InvalidScaleFormatException
import coden.alec.app.actuators.scale.InvalidScalePropertyFormatException
import coden.alec.app.actuators.scale.ScaleFormatter
import coden.alec.app.actuators.scale.ScaleResponder
import coden.alec.app.resources.MessageResource
import coden.alec.app.util.inline
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesResponse
import coden.display.displays.MessageDisplay

class BaseScaleResponder(
    private val display: MessageDisplay,
    private val messages: MessageResource,
    private val formatter: ScaleFormatter
) : ScaleResponder {

    override fun respondInvalidScaleFormat(throwable: InvalidScaleFormatException) {
        display.displayError(messages.rejectScaleMessage.inline("value" to throwable.value))
    }

    override fun respondInvalidScalePropertyFormat(throwable: InvalidScalePropertyFormatException) {
        display.displayError(messages.rejectScalePropertyMessage.inline(
            "prop" to throwable.property,
            "value" to throwable.value
        ))
    }

    override fun respondInternalError(throwable: Throwable) {
        display.displayError(messages.errorMessage.inline(
            "name" to throwable.javaClass.simpleName,
            "message" to throwable.message
        ))
    }
    override fun respondListScales(response: ListScalesResponse) {
        val scales = response.scales
        if (scales.isEmpty()) {
            display.displayMessage(messages.listScalesEmptyMessage)
        } else {
            display.displayMessage(messages.listScalesMessage.inline("scales" to formatter.format(scales)))
        }
    }

    override fun respondCreateScale(response: CreateScaleResponse) {
        display.displayMessage(messages.createdScaleMessage.inline("id" to response.scaleId))
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