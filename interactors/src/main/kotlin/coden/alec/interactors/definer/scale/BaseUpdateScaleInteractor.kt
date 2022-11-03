package coden.alec.interactors.definer.scale

import coden.alec.core.UpdateScaleInteractor
import coden.alec.core.Request
import coden.alec.core.Response
import coden.alec.data.Scale
import coden.alec.data.ScaleDivision
import coden.alec.data.ScaleGateway

class BaseUpdateScaleInteractor(
    private val gateway: ScaleGateway,
) : UpdateScaleInteractor {

    override fun execute(request: Request): Response {
        request as UpdateScaleRequest
        val updatedScale = Scale(
            name = request.name,
            unit = request.unit,
            deleted = false,
            id = request.id,
            divisions = createDivisions(request.divisions)
        )
        gateway.addScale(updatedScale)
        return UpdateScaleResponse(Result.success(Unit))
    }

    private fun createDivisions(divisions: Map<Long, String>): List<ScaleDivision>{
        return divisions.entries.map { ScaleDivision(it.key, it.value) }
    }
}

data class UpdateScaleRequest(
    val id: String, val name: String,
    val unit: String,
    val divisions: Map<Long, String>
) : Request

data class UpdateScaleResponse(val result: Result<Unit>) : Response