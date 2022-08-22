package coden.alec.app.states

import coden.alec.app.FiniteStateMachine

class StateExecutor(private val fsm: FiniteStateMachine) {
    private var current = fsm.start

    fun submit(submittedCommand: Command){
        println("[Command]: ${submittedCommand.javaClass.simpleName} ${submittedCommand.arguments}")
        for ((input, command, transition) in fsm.table) {
            if (input == current && command.isInstance(submittedCommand)){
                current = transition(submittedCommand)
                println("[Transition]: $input -> $current")
                break
            }
        }
    }

}