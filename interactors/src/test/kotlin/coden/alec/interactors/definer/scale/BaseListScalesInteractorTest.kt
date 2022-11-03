package coden.alec.interactors.definer.scale

import coden.alec.data.Scale
import coden.alec.data.ScaleDivision
import coden.alec.data.ScaleGateway
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BaseListScalesInteractorTest{
    @Test
    fun execute() {
        val interactor = BaseListScalesInteractor(mockGateway)

        val response = interactor.execute(ListScalesRequest()) as ListScalesResponse
        response.scales.onFailure { fail() }
        response.scales.onSuccess {
            assertEquals(1, it.size)
            val scale = it.first()
            assertEquals("0", scale.id)
            assertEquals("first", scale.name)
            assertEquals("unit", scale.unit)
            assertFalse(scale.deleted)
            assertEquals(1, scale.divisions.size)
            assertEquals(1L, scale.divisions.first().value)
            assertEquals("hello", scale.divisions.first().description)
        }
    }

    val mockGateway = object: ScaleGateway{
        override fun getScales(): List<Scale> {
            return listOf(
                Scale("0", "first", "unit", false, listOf(
                    ScaleDivision(1L, "hello")
                )),
            )
        }

        override fun getScalesCount(): Int {
            return fail()
        }

        override fun addScale(scale: Scale) {
            return fail()
        }

        override fun updateScaleSetDeleted(scaleId: String, deleted: Boolean) {
            return fail()
        }

        override fun deleteScale(scaleId: String) {
            return fail()
        }

    }

}