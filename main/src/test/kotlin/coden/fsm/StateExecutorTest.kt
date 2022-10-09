package coden.fsm

import coden.fsm.Entry.Companion.entry
import org.junit.jupiter.api.Test

internal class StateExecutorTest {

    val se = StateExecutor()

    init {
        se.apply {
            submit(Start)
            submit(Back)

        }
    }
    /**
     * S1 -> (start) -> S2 -> (back) -> S1 -> (back) -> S1
     * S1 -> (help) -> S3 -> (back) -> S1
     * S2 -> (start) -> S3 -> (start) -> S1
     * S3 -> (help) -> S2 -> (help) -> S1
     * S1 -> (text("1")) -> S1
     * S1 -> (text("2")) -> S2
     * S1 -> (text("3")) -> S3
     * S2 -> (text(any)) -> S1
     * S3 -> (text(any)) -> S2
     * S1 -> (text(any)) -> S4
     * S4 -> any -> S4
     * */

    @Test
    internal fun name() {

        var commands = ""
        var path = ""
        val expectedPath = ""
        val expectedCommands = ""
        val executor = StateExecutor(
            FSM(S1,
            FSMTable(
                entry(S1, Start, S1) {
                    path+="S1"
                    commands += it
                                     },
                entry(S1, Help, S2) {
                    path += "S2"
                    commands += it
                                    },
                entry(S1, Text::class) { cmd ->
                    (cmd.arguments.getOrNull()?.let {
                        commands += "$cmd($it)"
                        if (it == "1") {
                            S1
                        }
                        if (it == "2"){
                            S2
                        }
                        if (it == "3"){
                            S3
                        }
                        else {
                            S4
                        }
                    } ?: S4).also {
                        path += it
                    }
                },

                entry(S2, Help, S1) {
                    path += "S1"
                    commands += it
                                    },
                entry(S2, Start, S3) {
                    path += "S3"
                    commands += it},
                entry(S2, Back, S1) {
                    path += "S1"
                    commands += it},
                entry(S2, Text::class, S1) {
                    path += "S1"
                    commands += it},

                entry(S3, Help, S2) {
                    path += "S2"
                    commands += it},
                entry(S3, Start, S1) {
                    path += "S1"
                    commands += it},
                entry(S3, Back, S1) {
                    path += "S1"
                    commands += it},
                entry(S3, Text::class, S2) {
                    path += "S2"
                    commands += it},

                entry(S4, Command::class, S4) {
                    path += "S4"
                    commands += it
                }
                )
        ))



    }
}

class TestStateExecutor(fsm: FSM): StateExecutor(fsm){

    private var path = ""
    private var commands = ""

    override fun submit(submittedCommand: Command) {
        commands += submittedCommand.javaClass.simpleName
        super.submit(submittedCommand)
        path += current
    }
}

object S1: State
object S2: State
object S3: State
object S4: State

object Start: BaseCommand()
object Help: BaseCommand()
object Back: BaseCommand()
class Text(text: String) : BaseCommand(text)