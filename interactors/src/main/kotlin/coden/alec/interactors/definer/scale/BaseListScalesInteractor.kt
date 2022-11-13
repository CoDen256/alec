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
        val scales = gateway.getScales()
        return Result.success(ListScalesResponse(scales))
    }
}

class ListScalesRequest: Request

data class ListScalesResponse(val scales: List<Scale>): Response



