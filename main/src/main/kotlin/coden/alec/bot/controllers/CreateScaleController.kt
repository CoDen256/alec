package coden.alec.bot.controllers

import coden.alec.core.CreateScaleActivator
import coden.alec.interactors.definer.scale.CreateScaleRequest

class CreateScaleController (private val createScaleActivator: CreateScaleActivator) {

    fun handle(args: Map<String, Any>) {
        createScaleActivator.execute(CreateScaleRequest(
            args["name"] as String,
            args["unit"] as String,
            args["divisions"] as Map<Long, String>
        ))
    }


}

