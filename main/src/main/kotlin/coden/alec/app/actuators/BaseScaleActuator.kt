package coden.alec.app.actuators

import coden.alec.app.display.CreateScaleRequestBuilder
import coden.alec.app.display.ParsedScaleRequest
import coden.alec.app.display.ScaleParser
import coden.alec.app.display.ScaleResponder
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

    override fun createScale(request: ParsedScaleRequest): Result<CreateScaleResponse> {
        return useCaseFactory.createScale().execute(
            CreateScaleRequest(
                name = request.parsedName,
                unit = request.parsedUnit,
                divisions = request.parsedDivisions
            )
        ) as Result<CreateScaleResponse>
    }
}