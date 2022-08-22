package coden.alec.main.config.table

import coden.alec.app.FiniteStateMachineTable
import coden.alec.app.actuator.HelpActuator
import coden.alec.app.states.Entry.Companion.entry
import coden.alec.app.states.HelpCommand
import coden.alec.app.states.State.*

class HelpTable(help: HelpActuator) : FiniteStateMachineTable(
    entry(Start, HelpCommand) { help.displayHelp(it); Start },

    )