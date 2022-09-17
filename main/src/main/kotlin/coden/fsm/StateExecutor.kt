package coden.fsm

class StateExecutor(private val fsm: FSM) {
    private var current = fsm.start

    fun submit(submittedCommand: Command){
        println("[State]: ${current.javaClass.simpleName}")
        println("[Command]: $submittedCommand")
        for ((inputCondition, command, transition) in fsm.table) {
            if (inputCondition(current) && command.isInstance(submittedCommand)){
                current = transition(current, submittedCommand)
                println("[Transition]: ${inputCondition.javaClass.simpleName} -> ${current.javaClass.simpleName}")
                break
            }
        }
        println()
    }

}