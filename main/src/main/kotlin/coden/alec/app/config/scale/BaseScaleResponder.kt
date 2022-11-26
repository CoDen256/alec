package coden.alec.app.config.scale

import coden.alec.app.actuators.scale.InvalidScaleFormatException
import coden.alec.app.actuators.scale.InvalidScalePropertyFormatException
import coden.alec.app.actuators.scale.ScaleFormatter
import coden.alec.app.actuators.scale.ScaleResponder
import coden.alec.app.resources.MessageResource
import coden.alec.app.util.inline
import coden.alec.core.ScaleIsNotDeletedException
import coden.alec.data.ScaleDoesNotExistException
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.DeleteScaleResponse
import coden.alec.interactors.definer.scale.ListScalesResponse
import coden.alec.interactors.definer.scale.PurgeScaleResponse
import coden.display.displays.MessageDisplay

class BaseScaleResponder(
    private val display: MessageDisplay,
    private val messages: MessageResource,
    private val formatter: ScaleFormatter
) : ScaleResponder {

    override fun respondInvalidScaleFormat(throwable: InvalidScaleFormatException) {
        display.displayError(messages.rejectScale.inline("value" to throwable.value))
    }

    override fun respondInvalidScalePropertyFormat(throwable: InvalidScalePropertyFormatException) {
        display.displayError(messages.rejectScaleProperty.inline(
            "prop" to throwable.property,
            "value" to throwable.value
        ))
    }

    override fun respondScaleDoesNotExist(throwable: ScaleDoesNotExistException) {
        display.displayError(messages.scaleDoesNotExist.inline(
            "id" to throwable.scaleId
        ))
    }

    override fun respondScaleIsNotDeleted(throwable: ScaleIsNotDeletedException) {
        display.displayError(messages.scaleIsNotMarkedAsDeleted.inline("id" to throwable.scaleId))
    }

    override fun respondInternalError(throwable: Throwable) {
        display.displayError(messages.error.inline(
            "name" to throwable.javaClass.simpleName,
            "message" to throwable.message
        ))
    }
    override fun respondListScales(response: ListScalesResponse) {
        val scales = response.scales
        if (scales.isEmpty()) {
            display.displayMessage(messages.listScalesEmpty)
        } else {
            display.displayMessage(messages.listScales.inline("scales" to formatter.format(scales)))
        }
    }

    override fun respondCreateScale(response: CreateScaleResponse) {
        display.displayMessage(messages.createdScale.inline("id" to response.scaleId))
    }

    override fun respondDeleteScale(response: DeleteScaleResponse) {
        display.displayMessage(messages.deletedScale)
    }

    override fun respondPurgeScale(response: PurgeScaleResponse) {
        display.displayMessage(messages.purgedScale)
    }

    override fun respondWarnAboutPurging() {
        display.displayMessage(messages.warnAboutPurging)
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

    override fun respondPromptScaleId() {
        display.displayPrompt(messages.scaleIdPrompt)
    }
}