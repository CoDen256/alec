package coden.alec.main.table

import coden.alec.app.actuators.ScaleActuator
import coden.alec.app.actuators.scale.InvalidScaleFormatException
import coden.alec.app.actuators.scale.InvalidScalePropertyFormatException
import coden.alec.app.fsm.*
import coden.alec.app.util.flatMap
import coden.alec.app.util.then
import coden.fsm.*
import coden.fsm.Entry.Companion.entry

class ScaleTable(scale: ScaleActuator) : FSMTable(
    entry(Start, ListScalesCommand) { scale.listScales(); Start },

    entry(Start, CreateScaleCommand::class, requireArgument { arg ->
        scale.parseCreateScaleRequest(arg)
            .flatMap { scale.createScale(it) }
            .then { scale.respondCreateScale(it) }
            .state { Start }
            .onErrors(
                handle<InvalidScaleFormatException> { scale.respondInvalidScaleFormat(it); Start },
                handle<Throwable> { scale.respondInternalError(it); Start }
            )
    }),

    entry(Start, CreateScaleCommandNoArgs) { scale.respondPromptScaleName(); WaitScaleName },

    entry(WaitScaleName, TextCommand::class, requireArgument { arg ->
        scale.parseScaleName(arg)
            .then { scale.setName(it) }
            .then { scale.respondPromptScaleUnit() }
            .state { WaitScaleUnit }
            .onErrors(
                handle<InvalidScalePropertyFormatException> { scale.respondInvalidScalePropertyFormat(it); WaitScaleName },
                handle<Throwable> { scale.respondInternalError(it); Start }
        )

    }),
    entry(WaitScaleUnit, TextCommand::class, requireArgument { arg ->
        scale.parseScaleUnit(arg)
            .then { scale.setUnit(it) }
            .then { scale.respondPromptScaleDivisions() }
            .state { WaitScaleDivision }
            .onErrors(
                handle<InvalidScalePropertyFormatException> { scale.respondInvalidScalePropertyFormat(it); WaitScaleUnit },
                handle<Throwable> { scale.respondInternalError(it); Start }
            )

    }),
    entry(WaitScaleDivision, TextCommand::class, requireArgument { arg ->
        scale.parseScaleDivisions(arg)
            .then { scale.setDivisions(it) }
            .map { scale.build() }
            .flatMap { scale.createScale(it) }
            .then { scale.respondCreateScale(it)  }
            .state { Start }
            .onErrors(
                handle<InvalidScalePropertyFormatException> { scale.respondInvalidScalePropertyFormat(it); WaitScaleUnit },
                handle<Throwable> { scale.respondInternalError(it); Start }
            )
    }),
)
