package coden.alec.interactors.definer.scale

import coden.alec.data.Scale
import coden.alec.data.ScaleDivision
import coden.alec.data.ScaleGateway
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class ScalesInteractorsTest {
    @Mock
    lateinit var gateway: ScaleGateway

    @Test
    fun executeListScales() {
        whenever(gateway.getScales()).thenReturn(
            listOf(
                Scale(
                    "0", "first", "unit", false, listOf(
                        ScaleDivision(1L, "hello")
                    )
                ),
            )
        )

        val interactor = BaseListScalesInteractor(gateway)

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

    @Test
    fun executeCreateScale() {
        whenever(gateway.getScalesCount()).thenReturn(3)

        val interactor = BaseCreateScaleInteractor(gateway)

        val response = interactor.execute(CreateScaleRequest(
            name = "scale",
            unit = "unit",
            divisions = mapOf(1L to "first", 2L to "second")
        )) as CreateScaleResponse

        response.scaleId.onFailure { fail() }
        response.scaleId.onSuccess {
            assertEquals("scale-3", it)
        }

        verify(gateway, times(1)).getScalesCount()
        verify(gateway).addScale(Scale("scale-3", "scale", "unit", false, listOf(
            ScaleDivision(1, "first"),
             ScaleDivision(2, "second")
        )))
    }

}