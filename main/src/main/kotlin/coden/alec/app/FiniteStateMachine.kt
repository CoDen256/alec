package coden.alec.app

import coden.alec.app.states.Entry
import coden.alec.app.states.State

data class FiniteStateMachine(val start: State, val table: FiniteStateMachineTable)

open class FiniteStateMachineTable(private val entries: List<Entry>): ArrayList<Entry>(entries) {
    constructor(vararg entries: Entry) : this(arrayListOf(*entries))

    operator fun plus (table: FiniteStateMachineTable): FiniteStateMachineTable{
        return FiniteStateMachineTable(this.entries + table.entries)
    }
}
