package coden.alec.main.table

import coden.alec.app.actuators.ScaleActuator
import coden.alec.app.fsm.*
import coden.alec.app.util.unpack
import coden.fsm.Entry.Companion.entry
import coden.fsm.FSMTable
import coden.fsm.requireArgument

class ScaleTable(scale: ScaleActuator) : FSMTable(
    entry(Start, ListScalesCommand) { scale.listScales(); Start },

    entry(Start, CreateScaleCommand::class, requireArgument { arg ->
        scale.parseCreateScaleRequest(arg).unpack(
            {
                scale.createScale(it).unpack(
                    { r -> scale.respondCreateScale(r); Start },
                    { t -> scale.respondUserError(t); Start },
                    { t -> scale.respondInternalError(t); Start }
                )
            },
            { scale.respondUserError(it); Start },
            { scale.respondInternalError(it); Start }
        )
    }),

    entry(Start, CreateScaleCommandNoArgs) { scale.respondPromptScaleName(); WaitScaleName },

    entry(WaitScaleName, TextCommand::class, requireArgument { arg ->
        scale.parseScaleName(arg).unpack(
            { scale.setName(it); scale.respondPromptScaleUnit(); WaitScaleUnit },
            { scale.respondUserError(it); WaitScaleName },
            { scale.respondInternalError(it); scale.reset(); Start }
        )

    }),
    entry(WaitScaleUnit, TextCommand::class, requireArgument { arg ->
        scale.parseScaleUnit(arg).unpack(
            { scale.setUnit(it); scale.respondPromptScaleDivisions(); WaitScaleDivision },
            { scale.respondUserError(it); WaitScaleUnit },
            { scale.respondInternalError(it); scale.reset(); Start }
        )

    }),

    entry(WaitScaleDivision, TextCommand::class, requireArgument { arg ->
        scale.parseScaleDivisions(arg).unpack(
            {
                scale.setDivisions(it)
                scale.createScale(scale.build()).unpack(
                    { r -> scale.respondCreateScale(r); Start },
                    { t -> scale.respondUserError(t); Start },
                    { t -> scale.respondInternalError(t); Start }
                )
            },
            { scale.respondUserError(it); WaitScaleDivision },
            { scale.respondInternalError(it); scale.reset(); Start }
        )
    }),
)
