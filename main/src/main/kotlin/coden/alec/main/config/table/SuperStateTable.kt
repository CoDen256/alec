package coden.alec.main.config.table

import coden.alec.app.fsm.InlineCommand
import coden.fsm.Entry.Companion.entry
import coden.fsm.FSMTable

class SuperStateTable () : FSMTable(
    entry({ true }, InlineCommand::class) { state, _ -> state },
)