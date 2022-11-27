package coden.alec.data.generator

import coden.alec.data.Scale
import coden.alec.data.ScaleDivision
import java.util.stream.Stream

interface ScaleGenerator {
    fun generateScalesSequentially(): Stream<Scale>
    fun generateScales(): Stream<Scale>
    fun generateScale(): Scale
    fun generateName(): String
    fun generateUnit(): String
    fun generateId(): String

    fun generateDivisionsSequentially(): Stream<ScaleDivision>
    fun generateDivisions(): Stream<ScaleDivision>

    fun generateDivisionsAsMap(max: Int): Map<Long, String>

    fun generateDivision(): ScaleDivision
    fun generateDivisionAsEntry(): Pair<Long, String>
}