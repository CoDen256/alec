package coden.alec.app.config.scale

import coden.alec.app.actuators.scale.ScaleUseCaseInvoker
import coden.alec.core.ScaleUseCaseFactory
import coden.alec.interactors.definer.scale.*

class BaseScaleUseCaseInvoker(private val useCaseFactory: ScaleUseCaseFactory) : ScaleUseCaseInvoker {
    override fun listScales(): Result<ListScalesResponse> {
        return useCaseFactory.listScales().execute(ListScalesRequest()) as Result<ListScalesResponse>
    }

    override fun createScale(request: CreateScaleRequest): Result<CreateScaleResponse> {
        return useCaseFactory.createScale().execute(request) as Result<CreateScaleResponse>
    }

    override fun deleteScale(request: DeleteScaleRequest): Result<DeleteScaleResponse> {
        return useCaseFactory.deleteScale().execute(request) as Result<DeleteScaleResponse>
    }

    override fun purgeScale(request: PurgeScaleRequest): Result<PurgeScaleResponse> {
        return useCaseFactory.purgeScale().execute(request) as Result<PurgeScaleResponse>
    }

    override fun updateScale(request: UpdateScaleRequest): Result<UpdateScaleResponse> {
        return useCaseFactory.updateScale().execute(request) as Result<UpdateScaleResponse>
    }
}