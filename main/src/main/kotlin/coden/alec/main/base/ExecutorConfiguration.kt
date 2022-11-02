package coden.alec.main.base

import coden.alec.app.actuators.HelpActuator
import coden.alec.app.actuators.ScaleActuator
import coden.alec.app.fsm.Start
import coden.alec.main.table.HelpTable
import coden.alec.main.table.ScaleTable
import coden.fsm.CommandExecutor
import coden.fsm.FSM
import coden.fsm.FSMTable
import coden.fsm.LoggingCommandExecutor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ExecutorConfiguration {

    @Bean
    fun helpTable(helpActuator: HelpActuator): FSMTable {
        return HelpTable(helpActuator)
    }

    @Bean
    fun scaleTable(scaleActuator: ScaleActuator): FSMTable {
        return ScaleTable(scaleActuator)
    }

    @Bean
    fun fsm(tables: List<FSMTable>): FSM {
        return FSM(Start,
                tables.reduceRight {l,r -> l + r}
            )
    }

    @Bean
    fun executor(fsm: FSM): CommandExecutor{
        return LoggingCommandExecutor(fsm)
    }

}