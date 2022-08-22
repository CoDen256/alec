package coden.alec.app.states

import coden.alec.app.FiniteStateMachine

class StateExecutor(private val fsm: FiniteStateMachine) {
    private var current = fsm.start

    fun submit(submittedCommand: Command){
        println("[Command]: ${submittedCommand.javaClass.simpleName} ${submittedCommand.arguments}")
        for ((input, command, transition, action) in fsm.entries) {
            if (input == current && command.isInstance(submittedCommand)){
                transition()
                val success = action(submittedCommand)
                break
            }
        }
    }

}