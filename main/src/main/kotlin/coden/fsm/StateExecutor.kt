package coden.fsm


open class StateExecutor(private val fsm: FSM) {

    var current = fsm.start
        private set

    open fun submit(submittedCommand: Command){
        for ((input, command, transition) in fsm.table) {
            if (input == current && command.isInstance(submittedCommand)){
                doTransit(transition, submittedCommand)
                break
            }
        }
    }

    internal open fun doTransit(transition: (Command) -> State, submittedCommand: Command) {
        current = transition(submittedCommand)
    }

}