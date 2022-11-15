package coden.alec.app.actuators

import coden.alec.interactors.definer.scale.CreateScaleRequest

interface ScaleParser {
    fun parseCreateScaleRequest(input: String): Result<CreateScaleRequest>
    fun parseScaleName(input: String): Result<String>
    fun parseScaleUnit(input: String): Result<String>
    fun parseScaleDivisions(input: String): Result<Map<Long, String>>
}