package coden.alec.app.actuators.scale

import coden.alec.core.ScaleIsNotDeletedException
import coden.alec.data.ScaleAlreadyExistsException
import coden.alec.data.ScaleDoesNotExistException
import coden.alec.interactors.definer.scale.*

interface ScaleResponder {
    fun respondInternalError(throwable: Throwable)

    fun respondInvalidScaleFormat(throwable: InvalidScaleFormatException)
    fun respondInvalidScalePropertyFormat(throwable: InvalidScalePropertyFormatException)
    fun respondScaleAlreadyExists(throwable: ScaleAlreadyExistsException)
    fun respondScaleDoesNotExist(throwable: ScaleDoesNotExistException)
    fun respondScaleIsNotDeleted(throwable: ScaleIsNotDeletedException)

    fun respondListScales(response: ListScalesResponse)
    fun respondCreateScale(response: CreateScaleResponse)
    fun respondDeleteScale(response: DeleteScaleResponse)
    fun respondPurgeScale(response: PurgeScaleResponse)
    fun respondUpdateName(response: UpdateScaleResponse)
    fun respondUpdateUnit(response: UpdateScaleResponse)
    fun respondUpdateDivisions(response: UpdateScaleResponse)

    fun respondWarnAboutPurging()

    fun respondPromptScaleName()
    fun respondPromptScaleUnit()
    fun respondPromptScaleDivisions()
    fun respondPromptScaleId()
}