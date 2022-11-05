package coden.alec.app.display

import coden.alec.interactors.definer.scale.CreateScaleRequest

interface ScaleParser {
    fun parseCreateScaleRequest(input: String): CreateScaleRequest
    fun parseCreateScaleRequest(name: String, unit: String, divisions: String): CreateScaleRequest
    fun parseScaleName(input: String): String
    fun parseScaleUnit(input: String): String
    fun parseScaleDivisions(input: String): Map<String, Long>

    fun isValidCreateScaleRequest(input: String): Boolean
    fun isValidScaleName(input: String): Boolean
    fun isValidScaleUnit(input: String): Boolean
    fun isValidDivisions(input: String): Boolean
}