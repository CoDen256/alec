package coden.alec.app.display

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
    override fun respondListScales(response: ListScalesResponse) {
        response.scales.onSuccess {
            if (it.isEmpty()) {
                display.displayMessage(messages.listScalesEmptyMessage)
            } else {
                display.displayMessage(messages.listScalesMessage.sub(formatter.format(it)))
            }
        }.onFailure {
            display.displayError("${messages.errorMessage} ${it.message}")
        }
    }

    override fun respondCreateScale(response: CreateScaleResponse) {
        response.scaleId.onSuccess {
            display.displayMessage(messages.createdScaleMessage+formatter.formatId(it))
        }.onFailure {
            display.displayError("${messages.errorMessage} ${it.message}")
        }
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

    override fun respondRejectScale() {
        display.displayError(messages.rejectScaleMessage)
    }

    override fun respondRejectScaleName() {
        display.displayError(messages.rejectScaleNameMessage)
    }

    override fun respondRejectScaleUnit() {
        display.displayError(messages.rejectScaleUnitMessage)
    }

    override fun respondRejectScaleDivisions() {
        display.displayError(messages.rejectScaleDivisionsMessage)
    }
}