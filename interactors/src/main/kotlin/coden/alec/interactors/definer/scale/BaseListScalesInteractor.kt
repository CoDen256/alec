package coden.alec.interactors.definer.scale

import coden.alec.core.ListScalesInteractor
import coden.alec.core.Request
import coden.alec.core.Response
import coden.alec.data.Scale
import coden.alec.data.ScaleGateway

class BaseListScalesInteractor(
    private val gateway: ScaleGateway,
) : ListScalesInteractor {

    override fun execute(request: Request): Result<Response> {
        request as ListScalesRequest
        return gateway.getScales()
            .map { ListScalesResponse(it) }
    }
}

class ListScalesRequest: Request

data class ListScalesResponse(val scales: List<Scale>): Response



