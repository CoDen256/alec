//package coden.alec.interactors.definer.factor
//
//import coden.alec.core.Request
//import coden.alec.core.Response
//import coden.alec.core.UpdateFactorActivator
//import coden.alec.data.Factor
//import coden.alec.data.FactorGateway
//
//class UpdateFactorInteractor(private val gateway: FactorGateway) : UpdateFactorActivator {
//
//    override fun execute(request: Request) {
//        val r = request as UpdateFactorRequest
//        gateway.updateFactor(r.factor)
//    }
//
//}
//
//data class UpdateFactorRequest(
//    val factor: Factor
//): Request
//
//class UpdateFactorResponse: Response