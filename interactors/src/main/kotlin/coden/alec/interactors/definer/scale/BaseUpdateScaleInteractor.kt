package coden.alec.interactors.definer.scale

import coden.alec.core.UpdateScaleInteractor
import coden.alec.core.Request
import coden.alec.core.Response
import coden.alec.data.ScaleDivision
import coden.alec.data.ScaleGateway
import coden.alec.utils.flatMap

class BaseUpdateScaleInteractor(
    private val gateway: ScaleGateway,
) : UpdateScaleInteractor {

    override fun execute(request: Request): Result<Response> {
        request as UpdateScaleRequest
        return gateway.getScaleById(request.id)
            .map { scale ->
                scale.copy(
                    name = request.name ?: scale.name,
                    unit = request.unit ?: scale.unit,
                    divisions = request.divisions?.let { createDivisions(it) } ?: scale.divisions
                )
            }.flatMap { scale ->
                gateway.addScaleOrUpdate(scale)
            }.map {
                UpdateScaleResponse()
            }
    }

    private fun createDivisions(divisions: Map<Long, String>): List<ScaleDivision> {
        return divisions.entries.map { ScaleDivision(it.key, it.value) }
    }
}

data class UpdateScaleRequest(
    val id: String,
    val name: String? = null,
    val unit: String? = null,
    val divisions: Map<Long, String>? = null
) : Request

class UpdateScaleResponse : Response