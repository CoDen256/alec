package coden.alec.main.table

import coden.alec.app.actuators.ScaleActuator
import coden.alec.app.fsm.*
import coden.fsm.Entry.Companion.entry
import coden.fsm.FSMTable

class ScaleTable(scale: ScaleActuator) : FSMTable(
    entry(Start, ListScalesCommand) { scale.getAndDisplayScales(it); Start },

    entry(Start, CreateScaleCommand::class) {
        when {
            scale.isValidScale(it) -> {
                scale.createAndDisplayScale(it); Start
            }

            else -> {
                scale.rejectScale(it); Start
            }
        }
    },

    entry(Start, CreateScaleCommandNoArgs) { scale.displayScaleNamePrompt(it); WaitScaleName },

    entry(WaitScaleName, TextCommand::class) {
        when {
            scale.isValidScaleName(it) -> {
                scale.handleScaleName(it)
                scale.displayScaleUnitPrompt(it)
                WaitScaleUnit
            }

            else -> {
                scale.rejectScaleName(it)
                scale.displayScaleNamePrompt(it)
                WaitScaleName
            }
        }
    },
    entry(WaitScaleUnit, TextCommand::class) {
        when {
            scale.isValidScaleUnit(it) -> {
                scale.handleScaleUnit(it)
                scale.displayScaleDivisionsPrompt(it)
                WaitScaleDivision
            }

            else -> {
                scale.rejectScaleUnit(it)
                scale.displayScaleUnitPrompt(it)
                WaitScaleUnit
            }
        }
    },

    entry(WaitScaleDivision, TextCommand::class) {
        when {
            !scale.isValidScaleDivisions(it) -> {
                scale.rejectScaleDivisions(it)
                scale.displayScaleDivisionsPrompt(it)
                WaitScaleDivision
            }

            !scale.isValidScale(it) -> {
                scale.rejectScale(it)
                Start
            }

            else -> {
                scale.handleScaleDivisions(it)
                scale.createAndDisplayScale(it)
                Start
            }
        }
    },
)