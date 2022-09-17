package coden.fsm

class StateExecutor(private val fsm: FSM) {
    private var current = fsm.start

    fun submit(submittedCommand: Command){
        println("[State]: ${current.javaClass.simpleName}")
        println("[Command]: $submittedCommand")
        for ((inputMatcher, command, transition) in fsm.table) {
            if (inputMatcher(current) && command.isInstance(submittedCommand)){
                current = transition(current, submittedCommand)
                println("[Transition]: ${inputMatcher.javaClass.simpleName} -> ${current.javaClass.simpleName}")
                break
            }
        }
        println()
    }

}