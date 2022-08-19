package coden.alec.app.states

import coden.alec.bot.messages.MessageResource
import coden.alec.bot.presenter.View

class StateExecutor(
    start: State,
    private val fsm: List<Entry>,
    private val view: View,
    private val useCaseFactory: UseCaseFactory,
    private val messages: MessageResource
) {

    private var current = start

    fun submit(command: Command, args: String = ""){
        for (entry in fsm) {
            if (entry.input == current  && entry.condition.verify(command, args)){
                current = entry.output
                entry.action.execute(ActionContext(useCaseFactory, view, messages, args))
            }
        }
    }

}