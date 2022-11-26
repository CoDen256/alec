package coden.alec.app.actuators.scale

import coden.alec.core.ScaleIsNotDeletedException
import coden.alec.data.ScaleDoesNotExistException
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.DeleteScaleResponse
import coden.alec.interactors.definer.scale.ListScalesResponse
import coden.alec.interactors.definer.scale.PurgeScaleResponse

interface ScaleResponder {
    fun respondInternalError(throwable: Throwable)

    fun respondInvalidScaleFormat(throwable: InvalidScaleFormatException)
    fun respondInvalidScalePropertyFormat(throwable: InvalidScalePropertyFormatException)
    fun respondScaleDoesNotExist(throwable: ScaleDoesNotExistException)
    fun respondScaleIsNotDeleted(throwable: ScaleIsNotDeletedException)

    fun respondListScales(response: ListScalesResponse)
    fun respondCreateScale(response: CreateScaleResponse)
    fun respondDeleteScale(response: DeleteScaleResponse)
    fun respondPurgeScale(response: PurgeScaleResponse)

    fun respondWarnAboutPurging()

    fun respondPromptScaleName()
    fun respondPromptScaleUnit()
    fun respondPromptScaleDivisions()
    fun respondPromptScaleId()
}