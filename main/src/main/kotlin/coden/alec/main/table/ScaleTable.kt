package coden.alec.main.table

import coden.alec.app.actuators.ScaleActuator
import coden.alec.app.fsm.*
import coden.fsm.Entry.Companion.entry
import coden.fsm.FSMTable
import coden.fsm.requireArgument

class ScaleTable(scale: ScaleActuator) : FSMTable(
    entry(Start, ListScalesCommand) { scale.getAndDisplayScales(); Start },

    entry(Start, CreateScaleCommand::class, requireArgument {
        when {
            scale.isValidScale(it) -> {
                scale.createAndDisplayScale(it); Start
            }

            else -> {
                scale.rejectScale(); Start
            }
        }
    }),

    entry(Start, CreateScaleCommandNoArgs) { scale.displayScaleNamePrompt(); WaitScaleName },

    entry(WaitScaleName, TextCommand::class, requireArgument {
        when {
            scale.isValidScaleName(it) -> {
                scale.handleScaleName(it)
                scale.displayScaleUnitPrompt()
                WaitScaleUnit
            }

            else -> {
                scale.rejectScaleName()
                scale.displayScaleNamePrompt()
                WaitScaleName
            }
        }
    }),
    entry(WaitScaleUnit, TextCommand::class, requireArgument {
        when {
            scale.isValidScaleUnit(it) -> {
                scale.handleScaleUnit(it)
                scale.displayScaleDivisionsPrompt()
                WaitScaleDivision
            }

            else -> {
                scale.rejectScaleUnit()
                scale.displayScaleUnitPrompt()
                WaitScaleUnit
            }
        }
    }),

    entry(WaitScaleDivision, TextCommand::class, requireArgument {
        if (!scale.isValidScaleDivisions(it)){
            scale.rejectScaleDivisions()
            scale.displayScaleDivisionsPrompt()
            return@requireArgument WaitScaleDivision
        }
        scale.handleScaleDivisions(it)

        if(!scale.isValidScaleFromPreviousInput(it)){
            scale.rejectScale()
            return@requireArgument Start
        }
        scale.createFromPreviousInputAndDisplayScale()
        Start
    }),
)
