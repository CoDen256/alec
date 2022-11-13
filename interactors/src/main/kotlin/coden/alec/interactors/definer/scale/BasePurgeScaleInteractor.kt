package coden.alec.interactors.definer.scale

import coden.alec.core.PurgeScaleInteractor
import coden.alec.core.Request
import coden.alec.core.Response
import coden.alec.data.ScaleGateway

class BasePurgeScaleInteractor(
    private val gateway: ScaleGateway,
) : PurgeScaleInteractor {

    override fun execute(request: Request) : Result<Response> {
        request as PurgeScaleRequest
        gateway.deleteScale(request.id)
        return Result.success(PurgeScaleResponse())
    }
}

data class PurgeScaleRequest (val id: String): Request

class PurgeScaleResponse: Response