package coden.alec.app.actuators.scale

import coden.alec.app.actuators.UserException
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.DeleteScaleRequest

interface ScaleParser {
    fun parseCreateScaleRequest(input: String): Result<CreateScaleRequest>
    fun parseScaleName(input: String): Result<String>
    fun parseScaleUnit(input: String): Result<String>
    fun parseScaleDivisions(input: String): Result<Map<Long, String>>
    fun parseDeleteScaleRequest(input: String): Result<DeleteScaleRequest>
}

class InvalidScaleFormatException(val value: String) : UserException()
class InvalidScalePropertyFormatException(val property: String, val value: String) : UserException()