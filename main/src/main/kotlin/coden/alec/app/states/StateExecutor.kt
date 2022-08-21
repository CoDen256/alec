package coden.alec.app.states

import coden.alec.app.FiniteStateMachine

class StateExecutor(private val fsm: FiniteStateMachine) {
    private var current = fsm.start

    fun submit(submittedCommand: Command){
        println("[Command]: ${submittedCommand.javaClass.simpleName} ${submittedCommand.arguments}")
        for ((input, command, condition, output, action, fallback, fallbackAction) in fsm.entries) {
            if (input == current && command.isInstance(submittedCommand) && condition.verify(submittedCommand)){
                val success = action(submittedCommand)
                current = if (success){
                    output
                }else{
                    fallbackAction(submittedCommand)
                    fallback
                }
                break
            }
        }
    }

}