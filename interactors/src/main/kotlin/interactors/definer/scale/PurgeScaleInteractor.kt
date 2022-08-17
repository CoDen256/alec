package interactors.definer.scale

import coden.child.core.PurgeScaleActivator
import coden.child.core.PurgeScaleResponder
import coden.child.core.Request
import coden.child.core.Response
import coden.child.entities.ScaleGateway

class PurgeScaleInteractor(
    private val gateway: ScaleGateway,
    private val responder: PurgeScaleResponder
) : PurgeScaleActivator {

    override fun execute(request: Request) {
        request as PurgeScaleRequest
        gateway.deleteScale(request.id)
        responder.submit(PurgeScaleResponse(Result.success(Unit)))
    }
}

data class PurgeScaleRequest (val id: String): Request

data class PurgeScaleResponse(val result: Result<Unit>): Response