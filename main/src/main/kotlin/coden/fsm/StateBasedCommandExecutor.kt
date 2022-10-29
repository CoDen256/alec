package coden.fsm


open class StateBasedCommandExecutor(private val fsm: FSM): CommandExecutor{

    var current = fsm.start
        private set

    override fun submit(command: Command){
        for ((input, targetCommand, transition) in fsm.table) {
            if (input == current && targetCommand.isInstance(command)){
                doTransit(transition, command)
                break
            }
        }
    }

    protected open fun doTransit(transition: (Command) -> State, submittedCommand: Command) {
        current = transition(submittedCommand)
    }

}