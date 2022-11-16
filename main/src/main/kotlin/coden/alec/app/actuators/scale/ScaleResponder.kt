package coden.alec.app.actuators.scale

import coden.alec.data.ScaleDoesNotExistException
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.DeleteScaleResponse
import coden.alec.interactors.definer.scale.ListScalesResponse

interface ScaleResponder {
    fun respondInternalError(throwable: Throwable)

    fun respondInvalidScaleFormat(throwable: InvalidScaleFormatException)
    fun respondInvalidScalePropertyFormat(throwable: InvalidScalePropertyFormatException)
    fun respondScaleDoesNotExist(throwable: ScaleDoesNotExistException)

    fun respondListScales(response: ListScalesResponse)
    fun respondCreateScale(response: CreateScaleResponse)
    fun respondDeleteScale(response: DeleteScaleResponse)

    fun respondPromptScaleName()
    fun respondPromptScaleUnit()
    fun respondPromptScaleDivisions()
    fun respondPromptScaleId()
}