package coden.fsm

import coden.fsm.Entry.Companion.entry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class StateExecutorTest {

    /**
     * A -> (start) -> B -> (back) -> A -> (back) -> A
     * A -> (help) -> C -> (back) -> A
     * B -> (start) -> C -> (start) -> A
     * C -> (help) -> B -> (help) -> A
     * A -> (text("1")) -> A
     * A -> (text("2")) -> B
     * A -> (text("3")) -> C
     * B -> (text(any)) -> A
     * C -> (text(any)) -> B
     * A -> (text(any)) -> D
     * D -> any -> D
     * */

    @Test
    internal fun name() {

        val executor = TestStateExecutor(
            FSM(A,
            FSMTable(
                entry(A, Start, B) {},
                entry(A, Help, C) {},
                entry(A, Back, A) {},
                entry(A, Text::class) { cmd ->
                    cmd.arguments.getOrNull()?.let {
                        when (it) {
                            "1" -> A
                            "2" -> B
                            "3" -> C
                            else -> D
                        }
                    } ?: D
                },

                entry(B, Help, A) {},
                entry(B, Start, C) {},
                entry(B, Back, A) {},
                entry(B, Text::class, A) {},

                entry(C, Help, B) {},
                entry(C, Start, A) {},
                entry(C, Back, A) {},
                entry(C, Text::class, B) {},

                entry(D, Command::class, D) {}
                )
        ))

        executor.apply {
            // A
            submit(Start)// B
            submit(Back)
            submit(Back) // A
            submit(Back)
            submit(Back)
            submit(Back) // A

            submit(Help) // C
            submit(Back) // A
            submit(Help) // C
            submit(Help) // B
            submit(Start) // C
            submit(Help) // B
            submit(Start) // C

            submit(Text("1")) // B
            submit(Start) // C
            submit(Text("2")) // B
            submit(Start) // C
            submit(Text("3")) // B
            submit(Back) // A

            submit(Text("1"))
            submit(Back)
            submit(Text("2"))
            submit(Back)
            submit(Text("3"))
            submit(Back)

            submit(Start) // B
            submit(Text("Any")) // A
            submit(Help) // C
            submit(Text("Any")) // B
            submit(Text("Any")) // A
            submit(Text("Any")) // D

            submit(Help)
            submit(Start)
            submit(Help)
            submit(Start)
            submit(Start)
            submit(Text("Any"))
            submit(Back)
            submit(Text("Any"))
            submit(Start)
        }

        val expectedCommands = "StartBackBackBackBackBack" +
                "HelpBackHelpHelpStartHelpStart" +
                "Text(1)StartText(2)StartText(3)Back" +
                "Text(1)BackText(2)BackText(3)Back" +
                "StartText(Any)HelpText(Any)Text(Any)Text(Any)" +
                "HelpStartHelpStartStartText(Any)BackText(Any)Start"
        val expectedPath = "ABAAAAA" +
                "CACBCBC" +
                "BCBCBA" +
                "AABACA" +
                "BACBAD" +
                "DDDDDDDDD"
        assertEquals(expectedCommands, executor.commands)
        assertEquals(expectedPath, executor.path)
    }
}

class TestStateExecutor(fsm: FSM): StateBasedCommandExecutor(fsm){

    var path = fsm.start.javaClass.simpleName
    var commands = ""

    override fun submit(submittedCommand: Command) {
        commands += submittedCommand
        super.submit(submittedCommand)
        path += current.javaClass.simpleName
    }
}

object A: State
object B: State
object C: State
object D: State

object Start: BaseCommand()
object Help: BaseCommand()
object Back: BaseCommand()
class Text(text: String) : BaseCommand(text)