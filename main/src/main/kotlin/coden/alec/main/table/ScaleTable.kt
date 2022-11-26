package coden.alec.main.table

import coden.alec.app.actuators.FSMTableBuilder
import coden.alec.app.actuators.ScaleActuator
import coden.alec.app.actuators.scale.InvalidScaleFormatException
import coden.alec.app.actuators.scale.InvalidScalePropertyFormatException
import coden.alec.app.fsm.*
import coden.alec.app.util.flatMap
import coden.alec.app.util.then
import coden.alec.core.ScaleIsNotDeletedException
import coden.alec.data.ScaleDoesNotExistException
import coden.fsm.*
import coden.fsm.Entry.Companion.entry

class ScaleTableBuilder : FSMTableBuilder<ScaleActuator> { // TODO: improve repetetive code
    override fun ScaleActuator.buildTable() = FSMTable(
        entry(Start, ListScalesCommand) {
            listScales()
                .then { respondListScales(it) }
                .state { Start }
                .onErrors(
                    handle<Throwable> { respondInternalError(it); Start }
                )
        },
        // CREATE //

        entry(Start, CreateScaleCommand::class, requireArgument { arg ->
            parseCreateScaleRequest(arg)
                .flatMap { createScale(it) }
                .then { respondCreateScale(it) }
                .state { Start }
                .onErrors(
                    handle<InvalidScaleFormatException> { respondInvalidScaleFormat(it); Start },
                    handle<Throwable> { respondInternalError(it); Start }
                )
        }),

        entry(Start, CreateScaleCommandNoArgs) { respondPromptScaleName(); WaitScaleName },

        entry(WaitScaleName, TextCommand::class, requireArgument { arg ->
            parseScaleName(arg)
                .then { setName(it) }
                .then { respondPromptScaleUnit() }
                .state { WaitScaleUnit }
                .onErrors(
                    handle<InvalidScalePropertyFormatException> { respondInvalidScalePropertyFormat(it); respondPromptScaleName(); WaitScaleName },
                    handle<Throwable> { respondInternalError(it); reset(); Start }
                )

        }),
        entry(WaitScaleUnit, TextCommand::class, requireArgument { arg ->
            parseScaleUnit(arg)
                .then { setUnit(it) }
                .then { respondPromptScaleDivisions() }
                .state { WaitScaleDivision }
                .onErrors(
                    handle<InvalidScalePropertyFormatException> { respondInvalidScalePropertyFormat(it); respondPromptScaleUnit(); WaitScaleUnit },
                    handle<Throwable> { respondInternalError(it); reset(); Start }
                )

        }),
        entry(WaitScaleDivision, TextCommand::class, requireArgument { arg ->
            parseScaleDivisions(arg)
                .then { setDivisions(it) }
                .map { build() }
                .flatMap { createScale(it) }
                .then { respondCreateScale(it) }
                .then { reset() }
                .state { Start }
                .onErrors(
                    handle<InvalidScalePropertyFormatException> { respondInvalidScalePropertyFormat(it); respondPromptScaleDivisions(); WaitScaleDivision },
                    handle<Throwable> { respondInternalError(it); reset(); Start }
                )
        }),
        // DELETE //
        entry(Start, DeleteScaleCommand::class, requireArgument {  arg ->
            parseDeleteScaleRequest(arg)
                .flatMap{ deleteScale(it) }
                .then { respondDeleteScale(it) }
                .state { Start }
                .onErrors(
                    handle<ScaleDoesNotExistException> {respondScaleDoesNotExist(it); Start},
                    handle<Throwable> { respondInternalError(it); Start }
                )
        }),


        entry(Start, DeleteScaleCommandNoArgs) {respondPromptScaleId(); WaitScaleIdForDelete},
        entry(WaitScaleIdForDelete, TextCommand::class, requireArgument { arg ->
            parseDeleteScaleRequest(arg)
                .flatMap { deleteScale(it) }
                .then { respondDeleteScale(it) }
                .state { Start }
                .onErrors(
                    handle<ScaleDoesNotExistException> {respondScaleDoesNotExist(it); respondPromptScaleId(); WaitScaleIdForDelete},
                    handle<Throwable> { respondInternalError(it); Start }
                )
        }),

        // PURGE //
        entry(Start, PurgeScaleCommand::class, requireArgument { arg ->
            parsePurgeScaleRequest(arg)
                .flatMap{ purgeScale(it) }
                .then { respondPurgeScale(it) }
                .state { Start }
                .onErrors(
                    handle<ScaleIsNotDeletedException> {respondScaleIsNotDeleted(it); Start},
                    handle<ScaleDoesNotExistException> {respondScaleDoesNotExist(it); Start},
                    handle<Throwable> { respondInternalError(it); Start }
                )
        }),
        entry(Start, PurgeScaleCommandNoArgs) {respondWarnAboutPurging(); respondPromptScaleId(); WaitScaleIdForPurge},
        entry(WaitScaleIdForPurge, TextCommand::class, requireArgument { arg ->
            parsePurgeScaleRequest(arg)
                .flatMap { purgeScale(it) }
                .then { respondPurgeScale(it) }
                .state { Start }
                .onErrors(
                    handle<ScaleIsNotDeletedException> {respondScaleIsNotDeleted(it); Start},
                    handle<ScaleDoesNotExistException> {respondScaleDoesNotExist(it); respondPromptScaleId(); WaitScaleIdForPurge},
                    handle<Throwable> { respondInternalError(it); Start }
                )
        }),

    )
}
