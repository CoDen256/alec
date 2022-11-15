package coden.alec.main.table

import coden.alec.app.actuators.InvalidScaleFormatException
import coden.alec.app.actuators.ScaleActuator
import coden.alec.app.fsm.*
import coden.fsm.*
import coden.fsm.Entry.Companion.entry

class ScaleTable(scale: ScaleActuator) : FSMTable(
    entry(Start, ListScalesCommand) { scale.listScales(); Start },

//    entry(Start, CreateScaleCommand::class, requireArgument { arg ->
//        scale.parseCreateScaleRequest(arg)
//            .mapCatching { scale.createScale(it).getOrThrow() }
//
//            .get({ req ->
//                scale.createScale(req).get(
//                    { scale.respondCreateScale(it); Start },
//                    handle<InvalidScaleFormatException> { scale.respondUserError(it); Start }
//
//            ) })
//
//    }),
//
//    entry(Start, CreateScaleCommandNoArgs) { scale.respondPromptScaleName(); WaitScaleName },
//
//    entry(WaitScaleName, TextCommand::class, requireArgument { arg ->
//        scale.parseScaleName(arg).get(
//            { scale.setName(it); scale.respondPromptScaleUnit(); WaitScaleUnit },
//            { scale.respondUserError(it); WaitScaleName },
//            { scale.respondInternalError(it); scale.reset(); Start }
//        )
//
//    }),
//    entry(WaitScaleUnit, TextCommand::class, requireArgument { arg ->
//        scale.parseScaleUnit(arg).get(
//            { scale.setUnit(it); scale.respondPromptScaleDivisions(); WaitScaleDivision },
//            { scale.respondUserError(it); WaitScaleUnit },
//            { scale.respondInternalError(it); scale.reset(); Start }
//        )
//
//    }),
//
//    entry(WaitScaleDivision, TextCommand::class, requireArgument { arg ->
//        scale.parseScaleDivisions(arg).get(
//            {
//                scale.setDivisions(it)
//                scale.createScale(scale.build()).get(
//                    { r -> scale.respondCreateScale(r); Start },
//                    { t -> scale.respondUserError(t); Start },
//                    { t -> scale.respondInternalError(t); Start }
//                )
//            },
//            { scale.respondUserError(it); WaitScaleDivision },
//            { scale.respondInternalError(it); scale.reset(); Start }
//        )
//    }),
)
