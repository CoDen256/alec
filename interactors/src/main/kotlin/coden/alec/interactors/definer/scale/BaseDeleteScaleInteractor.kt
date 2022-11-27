package coden.alec.interactors.definer.scale

import coden.alec.core.DeleteScaleInteractor
import coden.alec.core.Request
import coden.alec.core.Response
import coden.alec.data.ScaleGateway
import coden.alec.utils.flatMap

class BaseDeleteScaleInteractor(
    private val gateway: ScaleGateway,
) : DeleteScaleInteractor {

    override fun execute(request: Request): Result<Response> {
        request as DeleteScaleRequest
        return gateway.getScaleById(request.id)
            .flatMap {
                gateway.updateScaleSetDeleted(request.id, true)
            }.map {
                DeleteScaleResponse()
            }
    }

}

data class DeleteScaleRequest(
    val id: String
) : Request

class DeleteScaleResponse : Response
