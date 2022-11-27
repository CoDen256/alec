package coden.alec.data.generator

import coden.alec.data.Scale
import coden.alec.data.ScaleDivision
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.random.Random

class BaseScaleGenerator : ScaleGenerator {
    override fun generateScalesSequentially(): Stream<Scale> {
        return Stream.iterate(0) { it + 1 }
            .map {
                Scale(
                    "scale-$it",
                    generateName(),
                    generateUnit(),
                    false,
                    generateDivisionsSequentially().limit(4).collect(Collectors.toList())
                )
            }
    }

    override fun generateScales(): Stream<Scale> {
        return Stream.generate {
            generateScale()
        }
    }

    override fun generateScale(): Scale {
        return Scale(
            generateId(),
            generateName(),
            generateUnit(),
            false,
            generateDivisions().limit(5).collect(Collectors.toList())
        )
    }

    override fun generateName(): String {
        return "name-${generateString(3)}"
    }

    override fun generateUnit(): String {
        return "unit-${generateString(3)}"
    }

    private fun generateString(max: Int) =
        (1..Random.nextInt() % max).map { Random.nextInt().toChar() }.joinToString("")

    override fun generateId(): String {
        return "scale-${Random.nextInt() % 10}"
    }

    override fun generateDivisionsSequentially(): Stream<ScaleDivision> {
        return Stream.iterate(0) {it + 1}
            .map { ScaleDivision(it.toLong(), generateString(5)) }
    }

    override fun generateDivisions(): Stream<ScaleDivision> {
        return Stream.generate{
            generateDivision()
        }
    }

    override fun generateDivisionsAsMap(max: Int): Map<Long, String> {
        return (0..max).map { generateDivisionAsEntry() }.associate { it }
    }

    override fun generateDivision(): ScaleDivision {
        val d = generateDivisionAsEntry()
        return ScaleDivision(d.first, d.second)
    }

    override fun generateDivisionAsEntry(): Pair<Long, String> {
        return Random.nextLong() % 10 to generateString(5)
    }
}