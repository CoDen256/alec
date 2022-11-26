package gateway.memory

import coden.alec.data.generator.BaseScaleGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ScaleInMemoryGatewayTest {

    private val sut = ScaleInMemoryGateway()
    private val gen = BaseScaleGenerator()

    @Test
    fun getScales() {
    }

    @Test
    fun getScaleById() {
    }

    @Test
    fun getScalesCount() {
    }

    @Test
    fun addScale() {
        gen.generateScalesSequentially()
            .limit(10)
            .forEach { sut.addScale(it) }
        assertEquals(10, sut.getScalesCount())
        var current = 0
        sut.getScales().sortedBy { it.id }.forEach {
            assertEquals(current++, it.id.split("-")[1].toInt())
        }
    }

    @Test
    fun updateScaleSetDeleted() {
        gen.generateScalesSequentially()
            .limit(10)
            .forEach { sut.addScale(it) }

        sut.updateScaleSetDeleted("scale-0", true)

        assertEquals(9, sut.getScales().count { !it.deleted })
    }

    @Test
    fun deleteScale() {
        gen.generateScalesSequentially()
            .limit(10)
            .forEach { sut.addScale(it) }

    }
}