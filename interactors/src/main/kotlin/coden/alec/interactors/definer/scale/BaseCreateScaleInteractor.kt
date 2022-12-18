package coden.alec.interactors.definer.scale

import coden.alec.core.CreateScaleInteractor
import coden.alec.core.Request
import coden.alec.core.Response
import coden.alec.data.Scale
import coden.alec.data.ScaleDivision
import coden.alec.data.ScaleGateway
import coden.alec.utils.flatMap

class BaseCreateScaleInteractor(
    private val gateway: ScaleGateway,
) : CreateScaleInteractor {

    override fun execute(request: Request): Result<Response> {
        request as CreateScaleRequest
        return verifyRequest(request)
            .map {
                Scale(
                    id = request.name,
                    name = request.name,
                    unit = request.unit,
                    deleted = false,
                    divisions = request.divisions.entries.map { ScaleDivision(it.key, it.value) }
                )
            }.flatMap { scale ->
                gateway.addScale(scale).map { scale.id } // IF ALREADY EXISTS, CHANGE ID (like windows with folders)
            }.map {
                CreateScaleResponse(it)
            }
    }

    private fun verifyRequest(request: CreateScaleRequest): Result<Unit> {
        if (request.divisions.isEmpty()) {
            return Result.failure(IllegalArgumentException("Divisions must be not empty"))
        }
        if (request.name.isBlank()) {
            return Result.failure(IllegalArgumentException("Name must not be blank"))
        }
        if (request.unit.isBlank()) {
            return Result.failure(IllegalArgumentException("Unit must not be blank"))
        }
        return Result.success(Unit)
    }

}

data class CreateScaleRequest(
    val name: String,
    val unit: String,
    val divisions: Map<Long, String>
) : Request

data class CreateScaleResponse(val scaleId: String) : Response






