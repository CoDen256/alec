package coden.alec.app.states

interface TransitionCondition {
    fun verify(command: Command): Boolean

    fun not() = object : TransitionCondition{
        override fun verify(command: Command): Boolean {
            return !this@TransitionCondition.verify(command)
        }
    }

    operator fun times(other: TransitionCondition): TransitionCondition {
        return object : TransitionCondition {
            override fun verify(command: Command): Boolean {
                return this@TransitionCondition.verify(command) && other.verify(command)
            }
        }
    }
}


object True: TransitionCondition {
    override fun verify(command: Command): Boolean {
        return true
    }
}


class CommandEquals(private val command: Command): TransitionCondition{
    override fun verify(command: Command): Boolean {
        return command == this.command
    }
}


fun equals(command: Command): TransitionCondition = CommandEquals(command)
fun eq(command: Command) = equals(command)
fun not(transitionCondition: TransitionCondition) = transitionCondition.not()