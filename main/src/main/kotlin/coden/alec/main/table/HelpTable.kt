package coden.alec.main.table

import coden.alec.app.actuators.HelpActuator
import coden.alec.app.fsm.HelpCommand
import coden.alec.app.fsm.Start
import coden.fsm.Entry.Companion.entry
import coden.fsm.FSMTable


class HelpTable(help: HelpActuator) : FSMTable(
    entry(Start, HelpCommand) { help.displayHelp(); Start },

    )