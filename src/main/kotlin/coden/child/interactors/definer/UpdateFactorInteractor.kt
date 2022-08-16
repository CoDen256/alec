package coden.child.interactors.definer

import coden.child.core.ChangeFactorActivator
import coden.child.core.Request
import coden.child.core.Response
import coden.child.entities.Factor
import coden.child.entities.FactorGateway

class ChangeFactorInteractor(private val gateway: FactorGateway) : ChangeFactorActivator {

    override fun execute(request: Request) {
        val r = request as UpdateFactorRequest
        gateway.updateFactor(r.factor)
    }

}

data class UpdateFactorRequest(
    val factor: Factor
): Request

class UpdateFactorResponse: Response