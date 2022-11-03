package coden.alec.interactors.definer.scale

import coden.alec.core.DeleteScaleInteractor
import coden.alec.core.Request
import coden.alec.core.Response
import coden.alec.data.ScaleGateway

class BaseDeleteScaleInteractor(
    private val gateway: ScaleGateway,
) : DeleteScaleInteractor {

    override fun execute(request: Request): Response {
        request as DeleteScaleRequest
        gateway.updateScaleSetDeleted(request.id, true)
        return DeleteScaleResponse(Result.success(Unit))
    }

}

data class DeleteScaleRequest(
    val id: String
): Request

data class DeleteScaleResponse(val result: Result<Unit>): Response
