package coden.alec.app.display

import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesResponse

interface ScaleResponder {
    fun respondUserError(it: Throwable)
    fun respondInternalError(it: Throwable)
    fun respondListScales(response: ListScalesResponse)
    fun respondCreateScale(response: CreateScaleResponse)

    fun respondPromptScaleName()
    fun respondPromptScaleUnit()
    fun respondPromptScaleDivisions()

    fun respondRejectScale()
    fun respondRejectScaleName()
    fun respondRejectScaleUnit()
    fun respondRejectScaleDivisions()
}