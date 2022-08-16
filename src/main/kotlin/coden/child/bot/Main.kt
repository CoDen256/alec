package coden.child.bot

import coden.child.core.CreateScaleActivator
import coden.child.core.CreateScaleResponder
import coden.child.core.Response
import coden.child.entities.ScaleGateway
import coden.child.gateway.memory.ScaleInMemoryGateway
import coden.child.interactors.definer.scale.CreateScaleInteractor
import coden.child.interactors.definer.scale.CreateScaleRequest
import coden.child.interactors.definer.scale.CreateScaleResponse

fun main() {
    val token = ""
    println("Running")

    val gateway: ScaleGateway = ScaleInMemoryGateway()
    val responder: CreateScaleResponder = object: CreateScaleResponder {
        override fun submit(response: Response) {
            response as CreateScaleResponse
        }
    }

    val createScale: CreateScaleActivator = CreateScaleInteractor(gateway, responder)


    createScale.execute(CreateScaleRequest(
        "scale-0",
        "hours",
        mapOf(1L to "minimal", 2L to "maximal")
    ))

    gateway.getScales();
}


