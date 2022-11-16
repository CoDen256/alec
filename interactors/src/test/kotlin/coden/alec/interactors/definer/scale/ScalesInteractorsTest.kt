package coden.alec.interactors.definer.scale

import coden.alec.data.Scale
import coden.alec.data.ScaleDivision
import coden.alec.data.ScaleDoesNotExistException
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

        val response = interactor.execute(ListScalesRequest()) as Result<ListScalesResponse>
        response.onFailure { fail() }
        response.onSuccess {
            val scales = it.scales
            assertEquals(1, scales.size)
            val scale =  scales.first()
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
        )) as Result<CreateScaleResponse>

        response.onFailure { fail() }
        response.onSuccess {
            assertEquals("scale-3", it.scaleId)
        }

        verify(gateway, times(1)).getScalesCount()
        verify(gateway).addScale(Scale("scale-3", "scale", "unit", false, listOf(
            ScaleDivision(1, "first"),
             ScaleDivision(2, "second")
        )))
    }

    @Test
    fun deleteScales() {
        val interactor = BaseDeleteScaleInteractor(gateway)

        val response = interactor.execute(DeleteScaleRequest("scale-0")) as Result<DeleteScaleResponse>


        response.onFailure { fail() }
        response.onSuccess {
            assertTrue(true)
        }

        verify(gateway, times(1)).updateScaleSetDeleted("scale-0", true)
    }

    @Test
    fun deleteScalesDoesNotExist() {
        val interactor = BaseDeleteScaleInteractor(gateway)

        whenever(gateway.getScaleById("scale-0")).thenReturn(Result.failure(ScaleDoesNotExistException("scale-0")))
        val response = interactor.execute(DeleteScaleRequest("scale-0")) as Result<DeleteScaleResponse>


        response.onFailure { assertTrue(it is ScaleDoesNotExistException) }
        response.onSuccess {
            fail()
        }

        verify(gateway).getScaleById("scale-0")
    }
}