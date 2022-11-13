package coden.alec.main.table

import coden.alec.app.actuators.ScaleActuator
import coden.alec.app.fsm.*
import coden.alec.app.util.unpack
import coden.fsm.Entry.Companion.entry
import coden.fsm.FSMTable
import coden.fsm.requireArgument

class ScaleTable(scale: ScaleActuator) : FSMTable(
    entry(Start, ListScalesCommand) { scale.getAndDisplayScales(); Start },

    entry(Start, CreateScaleCommand::class, requireArgument { arg ->
        scale.createScale(arg).unpack(
            { scale.respondCreateScale(it); Start },
            { scale.respondUserError(it); Start },
            { scale.respondInternalError(it); Start }
        )
    }),

    entry(Start, CreateScaleCommandNoArgs) { scale.displayScaleNamePrompt(); WaitScaleName },

    entry(WaitScaleName, TextCommand::class, requireArgument { arg ->
        scale.parseScaleName(arg).unpack(
            { scale.handleScaleName(it); scale.displayScaleUnitPrompt(); WaitScaleUnit },
            { scale.respondUserError(it); WaitScaleName },
            { scale.respondInternalError(it); scale.resetPreviousInputScale(); Start }
        )

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
        if (!scale.isValidScaleDivisions(it)) {
            scale.rejectScaleDivisions()
            scale.displayScaleDivisionsPrompt()
            return@requireArgument WaitScaleDivision
        }
        scale.handleScaleDivisions(it)

        if (!scale.isValidScaleFromPreviousInput(it)) {
            scale.rejectScale()
            scale.resetPreviousInputScale()
            return@requireArgument Start
        }
        scale.createFromPreviousInputAndDisplayScale()
        scale.resetPreviousInputScale()
        Start
    }),
)
