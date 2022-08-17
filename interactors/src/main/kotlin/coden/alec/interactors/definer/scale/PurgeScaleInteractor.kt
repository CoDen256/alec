package coden.alec.interactors.definer.scale

import coden.alec.core.PurgeScaleActivator
import coden.alec.core.PurgeScaleResponder
import coden.alec.core.Request
import coden.alec.core.Response
import coden.alec.data.ScaleGateway

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