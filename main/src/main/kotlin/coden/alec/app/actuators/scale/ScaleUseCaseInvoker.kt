package coden.alec.app.actuators.scale

import coden.alec.interactors.definer.scale.*

interface ScaleUseCaseInvoker {
    fun listScales(): Result<ListScalesResponse>
    fun createScale(request: CreateScaleRequest): Result<CreateScaleResponse>
    fun deleteScale(request: DeleteScaleRequest): Result<DeleteScaleResponse>
    fun purgeScale(request: PurgeScaleRequest): Result<PurgeScaleResponse>
    fun updateScale(request: UpdateScaleRequest): Result<UpdateScaleResponse>
}