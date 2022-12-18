package coden.alec.main.table

import coden.alec.app.actuators.FSMTableBuilder
import coden.alec.app.actuators.ScaleActuator
import coden.alec.app.actuators.scale.InvalidScaleFormatException
import coden.alec.app.actuators.scale.InvalidScalePropertyFormatException
import coden.alec.app.fsm.*
import coden.alec.core.ScaleIsNotDeletedException
import coden.alec.data.ScaleAlreadyExistsException
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
                .onError<ScaleAlreadyExistsException> { respondScaleAlreadyExists(it);  Start }
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
                .map { buildCreateRequest() }
                .flatMap { createScale(it) }
                .then { respondCreateScale(it) }
                .then { reset() }
                .state { Start }
                .onError<InvalidScalePropertyFormatException> { respondInvalidScalePropertyFormat(it); respondPromptScaleDivisions(); WaitScaleDivision }
                .onError<ScaleAlreadyExistsException> { respondScaleAlreadyExists(it); reset(); Start }
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
        // UPDATE NAME
        entry(Start, UpdateScaleNameCommand::class, requireArgument { arg ->
            parseUpdateNameRequest(arg)
                .flatMap { updateScale(it) }
                .then { respondUpdateName(it) }
                .state { Start }
                .onError<InvalidScalePropertyFormatException> { respondInvalidScalePropertyFormat(it); Start }
                .onError<ScaleAlreadyExistsException> { respondScaleAlreadyExists(it);  Start }
                .onError<Throwable> { respondInternalError(it); Start }
                .get()
        }),

        entry(Start, UpdateScaleNameCommandNoArgs){respondPromptScaleId(); WaitScaleIdForUpdateName},
        entry(WaitScaleIdForUpdateName, TextCommand::class, requireArgument { arg ->
            parseScaleId(arg)
                .then {setId(it)}
                .then { respondPromptScaleName() }
                .state { WaitScaleUpdateName }
                .onError<Throwable> { respondInternalError(it); Start }
                .get()
        }),
        entry(WaitScaleUpdateName, TextCommand::class, requireArgument { arg ->
            parseScaleName(arg)
                .then {setName(it)}
                .map { buildUpdateRequest() }
                .flatMap { updateScale(it) }
                .then { respondUpdateName(it) }
                .then { reset() }
                .state { Start }
                .onError<InvalidScalePropertyFormatException> { respondInvalidScalePropertyFormat(it); respondPromptScaleName(); WaitScaleUpdateName }
                .onError<ScaleAlreadyExistsException> { respondScaleAlreadyExists(it); reset(); Start }
                .onError<Throwable> { respondInternalError(it); reset(); Start }
                .get()
        }),

                // UPDATE UNIT
        entry(Start, UpdateScaleDivisionsCommand::class, requireArgument { arg ->
            parseUpdateDivisionRequest(arg)
                .flatMap { updateScale(it) }
                .then { respondUpdateDivisions(it) }
                .state { Start }
                .onError<InvalidScalePropertyFormatException> { respondInvalidScalePropertyFormat(it); Start }
                .onError<ScaleAlreadyExistsException> { respondScaleAlreadyExists(it);  Start }
                .onError<Throwable> { respondInternalError(it); Start }
                .get()
        }),

        entry(Start, UpdateScaleUnitCommandNoArgs){respondPromptScaleId(); WaitScaleIdForUpdateUnit},
        entry(WaitScaleIdForUpdateUnit, TextCommand::class, requireArgument { arg ->
            parseScaleId(arg)
                .then {setId(it)}
                .then { respondPromptScaleUnit() }
                .state { WaitScaleUpdateUnit }
                .onError<Throwable> { respondInternalError(it); Start }
                .get()
        }),
        entry(WaitScaleUpdateUnit, TextCommand::class, requireArgument { arg ->
            parseScaleUnit(arg)
                .then {setUnit(it)}
                .map { buildUpdateRequest() }
                .flatMap { updateScale(it) }
                .then { respondUpdateName(it) }
                .then { reset() }
                .state { Start }
                .onError<InvalidScalePropertyFormatException> { respondInvalidScalePropertyFormat(it); respondPromptScaleUnit(); WaitScaleUpdateUnit }
                .onError<ScaleAlreadyExistsException> { respondScaleAlreadyExists(it); reset(); Start }
                .onError<Throwable> { respondInternalError(it); reset(); Start }
                .get()
        }),

        // UPDATE DIVISION
        entry(Start, UpdateScaleDivisionsCommand::class, requireArgument { arg ->
            parseUpdateDivisionRequest(arg)
                .flatMap { updateScale(it) }
                .then { respondUpdateDivisions(it) }
                .state { Start }
                .onError<InvalidScalePropertyFormatException> { respondInvalidScalePropertyFormat(it); Start }
                .onError<ScaleAlreadyExistsException> { respondScaleAlreadyExists(it);  Start }
                .onError<Throwable> { respondInternalError(it); Start }
                .get()
        }),

        entry(Start, UpdateScaleDivisionsCommandNoArgs){respondPromptScaleId(); WaitScaleIdForUpdateDivisions},
        entry(WaitScaleIdForUpdateDivisions, TextCommand::class, requireArgument { arg ->
            parseScaleId(arg)
                .then {setId(it)}
                .then { respondPromptScaleDivisions() }
                .state { WaitScaleUpdateDivisions }
                .onError<Throwable> { respondInternalError(it); Start }
                .get()
        }),
        entry(WaitScaleUpdateDivisions, TextCommand::class, requireArgument { arg ->
            parseScaleDivisions(arg)
                .then {setDivisions(it)}
                .map { buildUpdateRequest() }
                .flatMap { updateScale(it) }
                .then { respondUpdateDivisions(it) }
                .then { reset() }
                .state { Start }
                .onError<InvalidScalePropertyFormatException> { respondInvalidScalePropertyFormat(it); respondPromptScaleDivisions(); WaitScaleUpdateDivisions }
                .onError<ScaleAlreadyExistsException> { respondScaleAlreadyExists(it); reset(); Start }
                .onError<Throwable> { respondInternalError(it); reset(); Start }
                .get()
        }),
    )
}
