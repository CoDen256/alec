package coden.fsm

class StateExecutor(private val fsm: FSM) {
    private var current = fsm.start

    fun submit(submittedCommand: Command){
        println("[State]: ${current.javaClass.simpleName}")
        println("[Command]: $submittedCommand")
        for ((input, command, transition) in fsm.table) {
            if (input == current && command.isInstance(submittedCommand)){
                current = transition(submittedCommand)
                println("[Transition]: ${input.javaClass.simpleName} -> ${current.javaClass.simpleName}")
                break
            }
        }
        println()
    }

}