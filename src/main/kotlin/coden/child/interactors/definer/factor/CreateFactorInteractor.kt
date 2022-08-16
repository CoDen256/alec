package coden.child.interactors.definer.factor

import coden.child.core.CreateFactorActivator
import coden.child.core.CreateFactorResponder
import coden.child.core.Request
import coden.child.core.Response
import coden.child.entities.Factor
import coden.child.entities.FactorGateway

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






