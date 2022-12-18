package coden.alec.app.actuators.scale

import coden.alec.app.actuators.UserException
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.DeleteScaleRequest
import coden.alec.interactors.definer.scale.PurgeScaleRequest
import coden.alec.interactors.definer.scale.UpdateScaleRequest

interface ScaleParser {
    fun parseCreateScaleRequest(input: String): Result<CreateScaleRequest>
    fun parseScaleName(input: String): Result<String>
    fun parseScaleUnit(input: String): Result<String>
    fun parseScaleDivisions(input: String): Result<Map<Long, String>>
    fun parseScaleId(input: String): Result<String>
    fun parseDeleteScaleRequest(input: String): Result<DeleteScaleRequest>
    fun parsePurgeScaleRequest(input: String): Result<PurgeScaleRequest>
    fun parseUpdateNameRequest(input: String): Result<UpdateScaleRequest>
    fun parseUpdateUnitRequest(input: String): Result<UpdateScaleRequest>
    fun parseUpdateDivisionRequest(input: String): Result<UpdateScaleRequest>
}

class InvalidScaleFormatException(val value: String) : UserException()
class InvalidScalePropertyFormatException(val property: String, val value: String) : UserException()