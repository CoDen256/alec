package coden.alec.app.actuators.scale

import coden.alec.app.actuators.InvalidScaleFormatException
import coden.alec.app.actuators.InvalidScalePropertyFormatException
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesResponse

interface ScaleResponder {
    fun respondInvalidScaleFormat(it: InvalidScaleFormatException)
    fun respondInvalidScalePropertyFormat(it: InvalidScalePropertyFormatException)
    fun respondInternalError(it: Throwable)

    fun respondListScales(response: ListScalesResponse)
    fun respondCreateScale(response: CreateScaleResponse)

    fun respondPromptScaleName()
    fun respondPromptScaleUnit()
    fun respondPromptScaleDivisions()
}