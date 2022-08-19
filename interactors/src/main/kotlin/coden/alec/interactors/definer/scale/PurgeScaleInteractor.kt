package coden.alec.interactors.definer.scale

import coden.alec.core.PurgeScaleActivator
import coden.alec.core.Request
import coden.alec.core.Response
import coden.alec.data.ScaleGateway

class PurgeScaleInteractor(
    private val gateway: ScaleGateway,
) : PurgeScaleActivator {

    override fun execute(request: Request) : Response {
        request as PurgeScaleRequest
        gateway.deleteScale(request.id)
        return PurgeScaleResponse(Result.success(Unit))
    }
}

data class PurgeScaleRequest (val id: String): Request

data class PurgeScaleResponse(val result: Result<Unit>): Response