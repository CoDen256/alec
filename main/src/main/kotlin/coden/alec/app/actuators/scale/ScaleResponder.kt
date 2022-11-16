package coden.alec.app.actuators.scale

import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesResponse

interface ScaleResponder {
    fun respondInvalidScaleFormat(throwable: InvalidScaleFormatException)
    fun respondInvalidScalePropertyFormat(throwable: InvalidScalePropertyFormatException)
    fun respondInternalError(throwable: Throwable)

    fun respondListScales(response: ListScalesResponse)
    fun respondCreateScale(response: CreateScaleResponse)

    fun respondPromptScaleName()
    fun respondPromptScaleUnit()
    fun respondPromptScaleDivisions()
}