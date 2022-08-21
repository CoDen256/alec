package coden.alec.app

import coden.alec.app.states.Entry
import coden.alec.app.states.State

data class FiniteStateMachine(val start: State, val entries: List<Entry>)
