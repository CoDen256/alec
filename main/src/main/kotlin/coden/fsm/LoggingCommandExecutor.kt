package coden.fsm

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.lang.StringBuilder

class LoggingCommandExecutor(fsm: FSM): StateBasedCommandExecutor(fsm) {

    private val log: Logger = LogManager.getLogger()
    private var debugLine = StringBuilder()

    override fun submit(command: Command) {
        debugLine.append("$command: ")
        super.submit(command)
        log.debug(debugLine.toString())
        debugLine.clear()
    }

    override fun doTransit(transition: (Command) -> State, submittedCommand: Command) {
        debugLine.append(current.javaClass.simpleName)
        super.doTransit(transition, submittedCommand)
        debugLine.append(" -> $submittedCommand -> ${current.javaClass.simpleName}")
    }
}