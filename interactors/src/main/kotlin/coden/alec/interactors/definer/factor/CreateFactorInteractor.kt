package coden.alec.interactors.definer.factor

import coden.alec.core.CreateFactorActivator
import coden.alec.core.CreateFactorResponder
import coden.alec.core.Request
import coden.alec.core.Response
import coden.alec.data.Factor
import coden.alec.data.FactorGateway

class CreateFactorInteractor(
    private val gateway: FactorGateway,
    private val responder: CreateFactorResponder
    ) : CreateFactorActivator {

    override fun execute(request: Request) {
        val r = request as CreateFactorRequest
        val newFactor = Factor(
            name = r.name,
            description = r.description,
            scaleId = r.scaleId,
            deleted = false,
            id = "factor-${gateway.getFactorsCount()}"
        )
        gateway.addFactor(newFactor)
        responder.submit(CreateFactorResponse(newFactor.id))
    }

}

data class CreateFactorRequest(
    val name: String,
    val description: String,
    val scaleId: String
): Request

data class CreateFactorResponse(val factorId: String): Response






