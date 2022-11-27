package coden.alec.main

import coden.alec.core.*
import coden.alec.data.Scale
import coden.alec.data.ScaleDivision
import coden.alec.interactors.definer.scale.*
import gateway.memory.ScaleInMemoryGateway
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class ScaleInteractorGatewayIntegrationTest{

    private val gateway = ScaleInMemoryGateway()

    private val scaleUseCaseFactory = object: ScaleUseCaseFactory{
        override fun listScales(): ListScalesInteractor {
            return BaseListScalesInteractor(gateway)
        }

        override fun createScale(): CreateScaleInteractor {
            return BaseCreateScaleInteractor(gateway)
        }

        override fun deleteScale(): DeleteScaleInteractor {
            return BaseDeleteScaleInteractor(gateway)
        }

        override fun purgeScale(): PurgeScaleInteractor {
            return BasePurgeScaleInteractor(gateway)
        }

        override fun updateScale(): UpdateScaleInteractor {
            return BaseUpdateScaleInteractor(gateway)
        }

    }

    private fun list(request: ListScalesRequest): Result<ListScalesResponse>{
        return scaleUseCaseFactory.listScales().execute(request) as Result<ListScalesResponse>
    }

    private fun list(): Result<ListScalesResponse>{
        return list(ListScalesRequest())
    }

    private fun create(request: CreateScaleRequest): Result<CreateScaleResponse>{
        return scaleUseCaseFactory.createScale().execute(request) as Result<CreateScaleResponse>
    }

    private fun delete(request: DeleteScaleRequest): Result<DeleteScaleResponse>{
        return scaleUseCaseFactory.deleteScale().execute(request) as Result<DeleteScaleResponse>
    }

    private fun purge(request: PurgeScaleRequest): Result<PurgeScaleResponse>{
        return scaleUseCaseFactory.purgeScale().execute(request) as Result<PurgeScaleResponse>
    }

    private fun update(request: UpdateScaleRequest): Result<UpdateScaleResponse>{
        return scaleUseCaseFactory.updateScale().execute(request) as Result<UpdateScaleResponse>
    }

    @BeforeEach
    fun setUp() {
        gateway.deleteAll().getOrThrow()
    }

    @Test
    fun listEmpty() {
        val response = list(ListScalesRequest())
        assertEquals(0, response.getOrThrow().scales.size)
    }

    @Test
    fun listTwo() {
        gateway.addScaleOrUpdate(Scale("i1", "n1", "u1", false, listOf()))
        gateway.addScaleOrUpdate(Scale("i2", "n2", "u2", true, listOf(ScaleDivision(1, "desc"))))

        val response = list()

        response.onSuccess {
            val scales = it.scales
            assertEquals(2, scales.size)
            val scale =  scales.last()
            assertEquals("i2", scale.id)
            assertEquals("n2", scale.name)
            assertEquals("u2", scale.unit)
            assertTrue(scale.deleted)
            assertEquals(1, scale.divisions.size)
            assertEquals(1L, scale.divisions.first().value)
            assertEquals("desc", scale.divisions.first().description)
        }
    }

    @Test
    fun createOne() {
        create(CreateScaleRequest("n", "u", mapOf(1L to "something"))).getOrThrow()

        val listAfterAdd = list()
        val scales = listAfterAdd.getOrThrow().scales
        assertEquals(1, scales.size)
        assertEquals("scale-0", scales[0].id)
        assertEquals("n", scales[0].name)
        assertEquals("u", scales[0].unit)
        assertEquals(listOf(ScaleDivision(1, "something")), scales[0].divisions)
    }

    @Test
    fun createInvalid() {
        create(CreateScaleRequest("n", "u", mapOf())).onSuccess {
            fail()
        }.onFailure {
            assertTrue(it is IllegalArgumentException)
        }

        create(CreateScaleRequest("", "u", mapOf(1L to "something"))).onSuccess {
            fail()
        }.onFailure {
            assertTrue(it is IllegalArgumentException)
        }

        create(CreateScaleRequest("n", "", mapOf(1L to "something"))).onSuccess {
            fail()
        }.onFailure {
            assertTrue(it is IllegalArgumentException)
        }
    }


    @Test
    fun purgeScales() {
        gateway.addScaleOrUpdate(Scale("scale-0","n1", "u", true, listOf(ScaleDivision(1, "s"))))
        gateway.addScaleOrUpdate(Scale("scale-1","n2", "u", true, listOf(ScaleDivision(1,"s"))))
        val scales0 = list().getOrThrow().scales
        assertEquals(2, scales0.size)
        assertEquals("scale-0", scales0[0].id)
        assertEquals("scale-1", scales0[1].id)

        purge(PurgeScaleRequest("scale-0")).getOrThrow()
        val scales1 = list().getOrThrow().scales
        assertEquals(1, scales1.size)
        assertEquals("scale-1", scales1[0].id)


        create(CreateScaleRequest("n3", "u", mapOf(1L to "something"))).getOrThrow()
        val scales2 = list().getOrThrow().scales
        assertEquals(2, scales2.size)
        assertEquals("scale-1", scales2[0].id)
        assertEquals("scale-2", scales2[1].id)
    }
//
//    @Test
//    fun deleteScalesDoesNotExist() {
//        val interactor = BaseDeleteScaleInteractor(gateway)
//
//        whenever(gateway.getScaleById("scale-0")).thenReturn(Result.failure(ScaleDoesNotExistException("scale-0")))
//        val response = interactor.execute(DeleteScaleRequest("scale-0")) as Result<DeleteScaleResponse>
//
//
//        response.onFailure { Assertions.assertTrue(it is ScaleDoesNotExistException) }
//        response.onSuccess {
//            fail()
//        }
//
//        verify(gateway).getScaleById("scale-0")
//    }
//
//    @Test
//    fun purgeScale() {
//        val interactor = BasePurgeScaleInteractor(gateway)
//        whenever(gateway.getScaleById("scale-0")).thenReturn(
//            Result.success(Scale("scale-0", "name", "unit", true, emptyList()))
//        )
//
//        val response = interactor.execute(PurgeScaleRequest("scale-0")) as Result<PurgeScaleResponse>
//
//
//        response.onFailure { fail() }
//
//        verify(gateway).getScaleById("scale-0")
//        verify(gateway).deleteScale("scale-0")
//    }
//
//    @Test
//    fun purgeScaleNotDeleted() {
//        val interactor = BasePurgeScaleInteractor(gateway)
//        whenever(gateway.getScaleById("scale-0")).thenReturn(
//            Result.success(Scale("scale-0", "name", "unit", false, emptyList()))
//        )
//
//        val response = interactor.execute(PurgeScaleRequest("scale-0")) as Result<PurgeScaleResponse>
//
//
//        response.onFailure { Assertions.assertTrue(it is ScaleIsNotDeletedException) }
//        response.onSuccess {
//            fail()
//        }
//
//        verify(gateway).getScaleById("scale-0")
//    }
//
//    @Test
//    fun purgeScaleDoesNotExist() {
//        val interactor = BasePurgeScaleInteractor(gateway)
//
//        whenever(gateway.getScaleById("scale-0")).thenReturn(Result.failure(ScaleDoesNotExistException("scale-0")))
//        val response = interactor.execute(PurgeScaleRequest("scale-0")) as Result<PurgeScaleResponse>
//
//
//        response.onFailure { Assertions.assertTrue(it is ScaleDoesNotExistException) }
//        response.onSuccess {
//            fail()
//        }
//
//        verify(gateway).getScaleById("scale-0")
//    }
//
//    @Test
//    fun updateScaleNothing() {
//        val interactor = BaseUpdateScaleInteractor(gateway)
//
//        val scale = Scale("scale-0", "name", "unit", false, emptyList())
//        whenever(gateway.getScaleById("scale-0")).thenReturn(Result.success(scale))
//        val response = interactor.execute(UpdateScaleRequest("scale-0")) as Result<UpdateScaleResponse>
//
//
//        response.onFailure { fail() }
//
//        verify(gateway).getScaleById("scale-0")
//        verify(gateway).addScaleOrUpdate(scale)
//    }
//
//    @Test
//    fun updateScaleName() {
//        val interactor = BaseUpdateScaleInteractor(gateway)
//
//        val scale = Scale("scale-0", "name", "unit", false, emptyList())
//        val updatedScale = Scale("scale-0", "name2", "unit", false, emptyList())
//        whenever(gateway.getScaleById("scale-0")).thenReturn(Result.success(scale))
//        val response = interactor.execute(UpdateScaleRequest("scale-0", name = "name2")) as Result<UpdateScaleResponse>
//
//
//        response.onFailure { fail() }
//
//        verify(gateway).getScaleById("scale-0")
//        verify(gateway).addScaleOrUpdate(updatedScale)
//    }
//
//    @Test
//    fun updateScaleUnit() {
//        val interactor = BaseUpdateScaleInteractor(gateway)
//
//        val scale = Scale("scale-0", "name", "unit", false, emptyList())
//        val updatedScale = Scale("scale-0", "name", "unit2", false, emptyList())
//        whenever(gateway.getScaleById("scale-0")).thenReturn(Result.success(scale))
//        val response = interactor.execute(UpdateScaleRequest("scale-0", unit = "unit2")) as Result<UpdateScaleResponse>
//
//
//        response.onFailure { fail() }
//
//        verify(gateway).getScaleById("scale-0")
//        verify(gateway).addScaleOrUpdate(updatedScale)
//    }
//
//    @Test
//    fun updateScaleDivisions() {
//        val interactor = BaseUpdateScaleInteractor(gateway)
//
//        val scale = Scale("scale-0", "name", "unit", false, emptyList())
//        val updatedScale = Scale("scale-0", "name", "unit", false, listOf(ScaleDivision(1L, "div")))
//        whenever(gateway.getScaleById("scale-0")).thenReturn(Result.success(scale))
//        val divisions = mapOf(1L to "div")
//        val response = interactor.execute(UpdateScaleRequest("scale-0", divisions = divisions)) as Result<UpdateScaleResponse>
//
//
//        response.onFailure { fail() }
//
//        verify(gateway).getScaleById("scale-0")
//        verify(gateway).addScaleOrUpdate(updatedScale)
//    }
//
//    @Test
//    fun updateScaleFull() {
//        val interactor = BaseUpdateScaleInteractor(gateway)
//
//        val scale = Scale("scale-0", "name", "unit", false, emptyList())
//        val updatedScale = Scale("scale-0", "name2", "unit2", false, listOf(ScaleDivision(1L, "div")))
//        whenever(gateway.getScaleById("scale-0")).thenReturn(Result.success(scale))
//        val divisions = mapOf(1L to "div")
//        val response = interactor.execute(UpdateScaleRequest("scale-0",
//            name = "name2", unit = "unit2",
//            divisions = divisions)) as Result<UpdateScaleResponse>
//
//
//        response.onFailure { fail() }
//
//        verify(gateway).getScaleById("scale-0")
//        verify(gateway).addScaleOrUpdate(updatedScale)
//    }

}