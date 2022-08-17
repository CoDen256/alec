package interactors.definer.scale

import coden.child.core.ListScalesActivator
import coden.child.core.ListScalesResponder
import coden.child.core.Request
import coden.child.core.Response
import coden.child.entities.Scale
import coden.child.entities.ScaleGateway

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



