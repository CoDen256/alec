package coden.alec.interactors.definer.scale

import coden.alec.core.CreateScaleInteractor
import coden.alec.core.Request
import coden.alec.core.Response
import coden.alec.data.Scale
import coden.alec.data.ScaleDivision
import coden.alec.data.ScaleGateway

class BaseCreateScaleInteractor(
    private val gateway: ScaleGateway,
) : CreateScaleInteractor {

    override fun execute(request: Request): Result<Response> {
        request as CreateScaleRequest
        val newScale = Scale(
            name = request.name,
            unit = request.unit,
            deleted = false,
            id = "scale-${gateway.getScalesCount()}",
            divisions = createDivisions(request.divisions)
        )
        gateway.addScale(newScale)
        return Result.success(CreateScaleResponse(newScale.id))
    }

    private fun createDivisions(divisions: Map<Long, String>): List<ScaleDivision>{
        return divisions.entries.map { ScaleDivision(it.key, it.value) }
    }

}

data class CreateScaleRequest(
    val name: String,
    val unit: String,
    val divisions: Map<Long, String>
): Request

data class CreateScaleResponse(val scaleId: String): Response






