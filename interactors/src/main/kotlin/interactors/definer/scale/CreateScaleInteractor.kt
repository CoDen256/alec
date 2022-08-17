package interactors.definer.scale

import coden.child.core.CreateScaleActivator
import coden.child.core.CreateScaleResponder
import coden.child.core.Request
import coden.child.core.Response
import coden.child.entities.Scale
import coden.child.entities.ScaleDivision
import coden.child.entities.ScaleGateway

class CreateScaleInteractor(
    private val gateway: ScaleGateway,
    private val responder: CreateScaleResponder
    ) : CreateScaleActivator {

    override fun execute(request: Request) {
        request as CreateScaleRequest
        val newScale = Scale(
            name = request.name,
            unit = request.unit,
            deleted = false,
            id = "scale-${gateway.getScalesCount()}",
            divisions = createDivisions(request.divisions)
        )
        gateway.addScale(newScale)
        responder.submit(CreateScaleResponse(Result.success(newScale.id)))
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

data class CreateScaleResponse(val scaleId: Result<String>): Response






