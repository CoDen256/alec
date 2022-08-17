package coden.alec.interactors.definer.scale

import coden.alec.core.ListScalesActivator
import coden.alec.core.ListScalesResponder
import coden.alec.core.Request
import coden.alec.core.Response
import coden.alec.data.Scale
import coden.alec.data.ScaleGateway

class ListScalesInteractor(
    private val gateway: ScaleGateway,
    private val responder: ListScalesResponder
) : ListScalesActivator {

    override fun execute(request: Request) {
        request as ListScalesRequest
        val scales = gateway.getScales()
        responder.submit(ListScalesResponse(Result.success(scales)))
    }
}

class ListScalesRequest: Request

data class ListScalesResponse(val scales: Result<List<Scale>>): Response



