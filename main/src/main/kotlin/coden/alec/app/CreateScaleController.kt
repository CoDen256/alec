package coden.alec.app

import coden.alec.bot.presenter.View
import coden.alec.core.CreateScaleActivator
import coden.alec.core.CreateScaleResponder
import coden.alec.core.Response
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse

class CreateScaleController(
    private val createScaleActivator: CreateScaleActivator,
    private val createScaleResponder: CreateScaleResponder
) {

    fun handle(args: Map<String, Any>) {
        createScaleActivator.execute(
            CreateScaleRequest(
                args["name"] as String,
                args["unit"] as String,
                args["divisions"] as Map<Long, String>
            )
        )
    }

}


class CreateScalePresenter(
    private val view: View
) : CreateScaleResponder {
    override fun submit(response: Response) {
        response as CreateScaleResponse
        response.scaleId.onSuccess {
            view.displayMessage("Id of new scale: $it")
        }
    }
}