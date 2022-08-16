package coden.child.interactors.definer.scale

import coden.child.core.DeleteScaleActivator
import coden.child.core.DeleteScaleResponder
import coden.child.core.Request
import coden.child.core.Response
import coden.child.entities.ScaleGateway

class DeleteScaleInteractor(
    private val gateway: ScaleGateway,
    private val responder: DeleteScaleResponder
) : DeleteScaleActivator {

    override fun execute(request: Request) {
        request as DeleteScaleRequest
        gateway.updateScaleSetDeleted(request.id, true)
        responder.submit(DeleteScaleResponse(Result.success(Unit)))
    }

}

data class DeleteScaleRequest(
    val id: String
): Request

data class DeleteScaleResponse(val result: Result<Unit>): Response
