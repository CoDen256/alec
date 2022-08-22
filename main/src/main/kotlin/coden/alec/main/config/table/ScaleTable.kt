package coden.alec.main.config.table

import coden.alec.app.FiniteStateMachineTable
import coden.alec.app.actuator.ScaleActuator
import coden.alec.app.states.*
import coden.alec.app.states.Entry.Companion.entry

class ScaleTable(scale: ScaleActuator) : FiniteStateMachineTable(
    entry(State.Start, ListScalesCommand) { scale.getAndDisplayScales(it); State.Start },

    entry(State.Start, CreateScaleCommand::class) {
        when {
            scale.isValidScale(it) -> {
                scale.createAndDisplayScale(it); State.Start
            }

            else -> {
                scale.rejectScale(it); State.Start
            }
        }
    },

    entry(State.Start, CreateScaleCommandNoArgs) { scale.displayScaleNamePrompt(it); State.WaitScaleName },

    entry(State.WaitScaleName, TextCommand::class) {
        when {
            scale.isValidScaleName(it) -> {
                scale.handleScaleName(it)
                scale.displayScaleUnitPrompt(it)
                State.WaitScaleUnit
            }

            else -> {
                scale.rejectScaleName(it)
                scale.displayScaleNamePrompt(it)
                State.WaitScaleName
            }
        }
    },
    entry(State.WaitScaleUnit, TextCommand::class) {
        when {
            scale.isValidScaleUnit(it) -> {
                scale.handleScaleUnit(it)
                scale.displayScaleDivisionsPrompt(it)
                State.WaitScaleDivision
            }

            else -> {
                scale.rejectScaleUnit(it)
                scale.displayScaleUnitPrompt(it)
                State.WaitScaleUnit
            }
        }
    },

    entry(State.WaitScaleDivision, TextCommand::class) {
        when {
            !scale.isValidScaleDivisions(it) -> {
                scale.rejectScaleDivisions(it)
                scale.displayScaleDivisionsPrompt(it)
                State.WaitScaleDivision
            }

            !scale.isValidScale(it) -> {
                scale.rejectScale(it)
                State.Start
            }

            else -> {
                scale.handleScaleDivisions(it)
                scale.createAndDisplayScale(it)
                State.Start
            }
        }
    },
)