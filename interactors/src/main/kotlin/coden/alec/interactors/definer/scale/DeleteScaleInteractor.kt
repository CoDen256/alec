package coden.alec.interactors.definer.scale

import coden.alec.core.DeleteScaleActivator
import coden.alec.core.DeleteScaleResponder
import coden.alec.core.Request
import coden.alec.core.Response
import coden.alec.data.ScaleGateway

class DeleteScaleInteractor(
    private val gateway: ScaleGateway,
) : DeleteScaleActivator {

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
