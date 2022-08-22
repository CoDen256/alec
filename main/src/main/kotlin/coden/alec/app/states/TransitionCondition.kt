package coden.alec.app.states


class CommandEquals(private val command: Command): (Command) -> Boolean{
    override fun invoke(p1: Command): Boolean {
        return command == this.command
    }

}


fun equals(command: Command): (Command) -> Boolean = CommandEquals(command)
fun eq(command: Command) = equals(command)
fun not(condition: (Command) -> Boolean): (Command) -> Boolean = {!condition(it)}