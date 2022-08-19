package coden.alec.app.states

import java.util.regex.Pattern

interface TransitionCondition {
    fun verify(command: Command, args: String): Boolean

    fun not() = object : TransitionCondition{
        override fun verify(command: Command, args: String): Boolean {
            return !this@TransitionCondition.verify(command, args)
        }
    }

    operator fun times(other: TransitionCondition): TransitionCondition {
        return object : TransitionCondition {
            override fun verify(command: Command, args: String): Boolean {
                return this@TransitionCondition.verify(command, args) && other.verify(command, args)
            }
        }
    }

}

open class CommandEquals(private val command: Command): TransitionCondition{
    override fun verify(command: Command, args: String): Boolean {
        return command == this.command
    }
}

object CreateScaleArgsAreValid: TransitionCondition {
    private val pattern = Pattern.compile("" +
            "[A-Za-z0-9_-]{1,10}" +
            "\n[A-Za-z/-]{1,10}" +
            "(\n\\d+-[A-Za-z0-9_-]+)+")

    override fun verify(command: Command, args: String): Boolean {
        return args.matches(pattern.toRegex())
    }
}

fun equals(command: Command): TransitionCondition = CommandEquals(command)
fun eq(command: Command) = equals(command)
fun not(transitionCondition: TransitionCondition) = transitionCondition.not()