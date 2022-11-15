package coden.alec.app.actuators.scale

import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesResponse

interface ScaleUseCaseInvoker {
    fun listScales(): Result<ListScalesResponse>
    fun createScale(request: CreateScaleRequest): Result<CreateScaleResponse>
}