package coden.alec.app.fsm

import coden.fsm.State

object Start: State
object WaitScaleName: State
object WaitScaleUnit: State
object WaitScaleDivision: State
object WaitScaleIdForDelete: State
object WaitScaleIdForPurge: State
object WaitScaleIdForUpdateName: State
object WaitScaleIdForUpdateUnit: State
object WaitScaleIdForUpdateDivisions: State
object WaitScaleUpdateName: State
object WaitScaleUpdateUnit: State
object WaitScaleUpdateDivisions: State