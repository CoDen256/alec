package coden.alec.app.config.scale

import coden.alec.app.actuators.ScaleActuator
import coden.alec.app.actuators.scale.CreateScaleRequestBuilder
import coden.alec.app.actuators.scale.ScaleParser
import coden.alec.app.actuators.scale.ScaleResponder
import coden.alec.core.ScaleUseCaseFactory
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesRequest
import coden.alec.interactors.definer.scale.ListScalesResponse

class BaseScaleActuator(
    private val useCaseFactory: ScaleUseCaseFactory,
    private val responder: ScaleResponder,
    private val parser: ScaleParser,
    private val builder: CreateScaleRequestBuilder
) : ScaleActuator,
    ScaleResponder by responder,
    ScaleParser by parser,
    CreateScaleRequestBuilder by builder {


    override fun listScales(): Result<ListScalesResponse> {
        return useCaseFactory.listScales().execute(ListScalesRequest()) as Result<ListScalesResponse>
    }

    override fun createScale(request: CreateScaleRequest): Result<CreateScaleResponse> {
        return useCaseFactory.createScale().execute(request) as Result<CreateScaleResponse>
    }
}