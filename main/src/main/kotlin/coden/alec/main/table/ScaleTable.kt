package coden.alec.main.table

import coden.alec.app.actuators.FSMTableBuilder
import coden.alec.app.actuators.ScaleActuator
import coden.alec.app.actuators.scale.InvalidScaleFormatException
import coden.alec.app.actuators.scale.InvalidScalePropertyFormatException
import coden.alec.app.fsm.*
import coden.alec.core.ScaleIsNotDeletedException
import coden.alec.data.ScaleDoesNotExistException
import coden.alec.utils.then
import coden.alec.utils.flatMap
import coden.fsm.*
import coden.fsm.Entry.Companion.entry

class ScaleTableBuilder : FSMTableBuilder<ScaleActuator> { // TODO: improve repetitive code
    override fun ScaleActuator.buildTable() = FSMTable(
        entry(Start, ListScalesCommand) {
            listScales()
                .then { respondListScales(it) }
                .state { Start }
                .onError<Throwable> { respondInternalError(it); Start }
                .get()
        },
        // CREATE //

        entry(Start, CreateScaleCommand::class, requireArgument { arg ->
            parseCreateScaleRequest(arg)
                .flatMap { createScale(it) }
                .then { respondCreateScale(it) }
                .state { Start }
                .onError<InvalidScaleFormatException> { respondInvalidScaleFormat(it); Start }
                .onError<Throwable> { respondInternalError(it); Start }
                .get()
        }),

        entry(Start, CreateScaleCommandNoArgs) { respondPromptScaleName(); WaitScaleName },

        entry(WaitScaleName, TextCommand::class, requireArgument { arg ->
            parseScaleName(arg)
                .then { setName(it) }
                .then { respondPromptScaleUnit() }
                .state { WaitScaleUnit }
                .onError<InvalidScalePropertyFormatException> { respondInvalidScalePropertyFormat(it); respondPromptScaleName(); WaitScaleName }
                .onError<Throwable> { respondInternalError(it); reset(); Start }
                .get()
        }),
        entry(WaitScaleUnit, TextCommand::class, requireArgument { arg ->
            parseScaleUnit(arg)
                .then { setUnit(it) }
                .then { respondPromptScaleDivisions() }
                .state { WaitScaleDivision }
                .onError<InvalidScalePropertyFormatException> { respondInvalidScalePropertyFormat(it); respondPromptScaleUnit(); WaitScaleUnit }
                .onError<Throwable> { respondInternalError(it); reset(); Start }
                .get()

        }),
        entry(WaitScaleDivision, TextCommand::class, requireArgument { arg ->
            parseScaleDivisions(arg)
                .then { setDivisions(it) }
                .map { build() }
                .flatMap { createScale(it) }
                .then { respondCreateScale(it) }
                .then { reset() }
                .state { Start }
                .onError<InvalidScalePropertyFormatException> { respondInvalidScalePropertyFormat(it); respondPromptScaleDivisions(); WaitScaleDivision }
                .onError<Throwable> { respondInternalError(it); reset(); Start }
                .get()
        }),
        // DELETE //
        entry(Start, DeleteScaleCommand::class, requireArgument {  arg ->
            parseDeleteScaleRequest(arg)
                .flatMap{ deleteScale(it) }
                .then { respondDeleteScale(it) }
                .state { Start }
                .onError<ScaleDoesNotExistException> {respondScaleDoesNotExist(it); Start}
                .onError<Throwable> { respondInternalError(it); Start }
                .get()
        }),


        entry(Start, DeleteScaleCommandNoArgs) {respondPromptScaleId(); WaitScaleIdForDelete},
        entry(WaitScaleIdForDelete, TextCommand::class, requireArgument { arg ->
            parseDeleteScaleRequest(arg)
                .flatMap { deleteScale(it) }
                .then { respondDeleteScale(it) }
                .state { Start }
                .onError<ScaleDoesNotExistException> {respondScaleDoesNotExist(it); respondPromptScaleId(); WaitScaleIdForDelete}
                .onError<Throwable> { respondInternalError(it); Start }
                .get()
        }),

        // PURGE //
        entry(Start, PurgeScaleCommand::class, requireArgument { arg ->
            parsePurgeScaleRequest(arg)
                .flatMap{ purgeScale(it) }
                .then { respondPurgeScale(it) }
                .state { Start }
                    .onError<ScaleIsNotDeletedException> {respondScaleIsNotDeleted(it); Start}
                    .onError<ScaleDoesNotExistException> {respondScaleDoesNotExist(it); Start}
                .onError<Throwable> { respondInternalError(it); Start }
                .get()
        }),
        entry(Start, PurgeScaleCommandNoArgs) {respondWarnAboutPurging(); respondPromptScaleId(); WaitScaleIdForPurge},
        entry(WaitScaleIdForPurge, TextCommand::class, requireArgument { arg ->
            parsePurgeScaleRequest(arg)
                .flatMap { purgeScale(it) }
                .then { respondPurgeScale(it) }
                .state { Start }
                 .onError<ScaleIsNotDeletedException> {respondScaleIsNotDeleted(it); Start}
                 .onError<ScaleDoesNotExistException> {respondScaleDoesNotExist(it); respondPromptScaleId(); WaitScaleIdForPurge}
                .onError<Throwable> { respondInternalError(it); Start }
                .get()
        }),

    )
}
