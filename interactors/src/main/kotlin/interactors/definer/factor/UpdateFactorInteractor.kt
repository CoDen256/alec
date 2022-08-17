package interactors.definer.factor

import coden.child.core.UpdateFactorActivator
import coden.child.core.Request
import coden.child.core.Response
import coden.child.entities.Factor
import coden.child.entities.FactorGateway

class UpdateFactorInteractor(private val gateway: FactorGateway) : UpdateFactorActivator {

    override fun execute(request: Request) {
        val r = request as UpdateFactorRequest
        gateway.updateFactor(r.factor)
    }

}

data class UpdateFactorRequest(
    val factor: Factor
): Request

class UpdateFactorResponse: Response