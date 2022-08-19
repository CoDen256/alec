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

    fun submit(command: Command){
        for (entry in fsm) {
            if (entry.input == current  && entry.command == command){
                current = entry.output
                val handled = entry.action.execute(useCaseFactory, view, messages)
                if (handled) continue
            }
        }
    }

}