package coden.alec.interactors.definer.scale

import coden.alec.core.PurgeScaleInteractor
import coden.alec.core.Request
import coden.alec.core.Response
import coden.alec.core.ScaleIsNotDeletedException
import coden.alec.data.ScaleGateway

class BasePurgeScaleInteractor(
    private val gateway: ScaleGateway,
) : PurgeScaleInteractor {

    override fun execute(request: Request) : Result<Response> {
        request as PurgeScaleRequest
        return gateway.getScaleById(request.id)
            .mapCatching {
                if (!it.deleted) throw ScaleIsNotDeletedException(it.id)
                it
            }.map {
                gateway.deleteScale(it.id)
            }.map {
                PurgeScaleResponse()
            }
    }
}

data class PurgeScaleRequest (val id: String): Request

class PurgeScaleResponse: Response