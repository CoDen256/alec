package coden.alec.interactors.definer.scale

import coden.alec.core.*
import coden.alec.data.ScaleAlreadyExistsException
import coden.alec.data.ScaleDivision
import coden.alec.data.ScaleDoesNotExistException
import coden.alec.gateway.memory.ScaleInMemoryGateway
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.test.assertIs

class ScaleInteractorGatewayIntegrationTest {

    private val gateway = ScaleInMemoryGateway()

    private val scaleUseCaseFactory = object : ScaleUseCaseFactory {
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

    private fun list(request: ListScalesRequest): Result<ListScalesResponse> {
        return scaleUseCaseFactory.listScales().execute(request) as Result<ListScalesResponse>
    }

    private fun list(): Result<ListScalesResponse> {
        return list(ListScalesRequest())
    }

    private fun create(request: CreateScaleRequest): Result<CreateScaleResponse> {
        return scaleUseCaseFactory.createScale().execute(request) as Result<CreateScaleResponse>
    }

    private fun delete(request: DeleteScaleRequest): Result<DeleteScaleResponse> {
        return scaleUseCaseFactory.deleteScale().execute(request) as Result<DeleteScaleResponse>
    }

    private fun purge(request: PurgeScaleRequest): Result<PurgeScaleResponse> {
        return scaleUseCaseFactory.purgeScale().execute(request) as Result<PurgeScaleResponse>
    }

    private fun update(request: UpdateScaleRequest): Result<UpdateScaleResponse> {
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
    fun createOneListOne() {
        create(CreateScaleRequest("n", "u", mapOf(1L to "something"))).getOrThrow()

        val listAfterAdd = list()
        val scales = listAfterAdd.getOrThrow().scales
        assertEquals(1, scales.size)
        assertEquals("n", scales[0].name)
        assertEquals("n", scales[0].id)
        assertEquals("u", scales[0].unit)
        assertEquals(listOf(ScaleDivision(1, "something")), scales[0].divisions)
    }

    @Test
    fun createOne_createExistingName_fails() {
        create(CreateScaleRequest("n", "u", mapOf(1L to "something"))).getOrThrow()
        create(CreateScaleRequest("n", "u", mapOf(1L to "something"))).onSuccess {
            fail()
        }.onFailure {
            assertIs<ScaleAlreadyExistsException>(it)
        }
    }

    @Test
    fun createTwoListTwo() {

        create(CreateScaleRequest("n1", "u1", mapOf(1L to "desc"))).getOrThrow()
        val id1 = create(CreateScaleRequest("n2", "u2", mapOf(1L to "desc"))).getOrThrow().scaleId

        list().onSuccess { r ->
            val scales = r.scales
            assertEquals(2, scales.size)
            val scale = scales.find { it.id == id1 }!!
            assertEquals("n2", scale.name)
            assertEquals("n2", scale.id)
            assertEquals("u2", scale.unit)
            assertFalse(scale.deleted)
            assertEquals(1, scale.divisions.size)
            assertEquals(1L, scale.divisions.first().value)
            assertEquals("desc", scale.divisions.first().description)
        }.onFailure {
            fail()
        }
    }


    @Test
    fun createInvalid() {
        create(CreateScaleRequest("n", "u", mapOf())).onSuccess {
            fail()
        }.onFailure {
            assertIs<IllegalArgumentException>(it)
        }

        create(CreateScaleRequest("", "u", mapOf(1L to "something"))).onSuccess {
            fail()
        }.onFailure {
            assertIs<IllegalArgumentException>(it)
        }

        create(CreateScaleRequest("n", "", mapOf(1L to "something"))).onSuccess {
            fail()
        }.onFailure {
            assertIs<IllegalArgumentException>(it)
        }
    }

    @Test
    fun addOne_ListOne_DeleteOne_ListOneDeleted() {
        val id = create(CreateScaleRequest("n1", "u1", mapOf(1L to "desc"))).getOrThrow().scaleId

        delete(DeleteScaleRequest(id)).getOrThrow()

        val scales = list().getOrThrow().scales
        val deleted = scales.first()
        assertEquals(1, scales.size)
        assertEquals("n1", deleted.id)
        assertEquals("n1", deleted.name)
        assertEquals("u1", deleted.unit)
        assertTrue(deleted.deleted)
        assertEquals(1, deleted.divisions.size)
        assertEquals(1L, deleted.divisions.first().value)
        assertEquals("desc", deleted.divisions.first().description)
    }

    @Test
    fun addOne_PurgeOne_ErrorNotDeleted() {
        val id = create(CreateScaleRequest("n1", "u1", mapOf(1L to "desc"))).getOrThrow().scaleId

        purge(PurgeScaleRequest(id)).onSuccess {
            fail()
        }.onFailure {
            assertIs<ScaleIsNotDeletedException>(it)
        }

    }

    @Test
    fun addOne_DeleteOne_PurgeOne_ListEmpty() {
        val id = create(CreateScaleRequest("n1", "u1", mapOf(1L to "desc"))).getOrThrow().scaleId
        delete(DeleteScaleRequest(id)).getOrThrow()

        purge(PurgeScaleRequest(id)).getOrThrow()

        assertEquals(0, list().getOrThrow().scales.size)
    }

    @Test
    fun addTwo_DeletePurgeOne_ListOne_CreateOne_ListTwo() {
        val id1 = create(CreateScaleRequest("n1", "u", mapOf(1L to "desc"))).getOrThrow().scaleId
        val id2 = create(CreateScaleRequest("n2", "u", mapOf(1L to "desc"))).getOrThrow().scaleId

        delete(DeleteScaleRequest(id1)).getOrThrow()
        purge(PurgeScaleRequest(id1)).getOrThrow()
        val purged = list().getOrThrow().scales
        assertEquals(1, purged.size)
        assertEquals(id2, purged[0].id)


        val id3 = create(CreateScaleRequest("n3", "u", mapOf(1L to "desc"))).getOrThrow().scaleId
        val created = list().getOrThrow().scales
        assertEquals(2, created.size)
        created.distinct().forEach {
            assertTrue {
                id2 == it.id || id3 == it.id
            }
        }
    }

    @Test
    fun addOne_DeleteOneWrong_ErrorDoesNotExist() {
        create(CreateScaleRequest("n1", "u", mapOf(1L to "desc"))).getOrThrow()

        delete(DeleteScaleRequest("invalid")).onSuccess {
            fail()
        }.onFailure {
            assertIs<ScaleDoesNotExistException>(it)
        }
    }

    @Test
    fun purgeOneWrong_ErrorDoesNotExist() {
        purge(PurgeScaleRequest("invalid")).onSuccess {
            fail()
        }.onFailure {
            assertIs<ScaleDoesNotExistException>(it)
        }
    }

    @Test
    fun addOne_UpdateOneNothing() {
        val id = create(CreateScaleRequest("n", "u", mapOf(1L to "desc"))).getOrThrow().scaleId
        update(UpdateScaleRequest(id)).getOrThrow()

        val scales = list().getOrThrow().scales
        assertEquals(1, scales.size)
        assertEquals("n", scales[0].name)
        assertEquals("n", scales[0].id)
        assertEquals("u", scales[0].unit)
        assertEquals(listOf(ScaleDivision(1, "desc")), scales[0].divisions)
    }

    @Test
    fun addOne_UpdateOneName() {
        val id = create(CreateScaleRequest("n", "u", mapOf(1L to "desc"))).getOrThrow().scaleId
        update(UpdateScaleRequest(id, name = "n2")).getOrThrow()

        val scales = list().getOrThrow().scales
        assertEquals(1, scales.size)
        assertEquals("n2", scales[0].name)
        assertEquals("n2", scales[0].id)
        assertEquals("u", scales[0].unit)
        assertEquals(listOf(ScaleDivision(1, "desc")), scales[0].divisions)
    }

    @Test
    fun addOne_UpdateOneUnit() {
        val id = create(CreateScaleRequest("n", "u", mapOf(1L to "desc"))).getOrThrow().scaleId
        update(UpdateScaleRequest(id, unit = "u2")).getOrThrow()

        val scales = list().getOrThrow().scales
        assertEquals(1, scales.size)
        assertEquals("n", scales[0].id)
        assertEquals("n", scales[0].name)
        assertEquals("u2", scales[0].unit)
        assertEquals(listOf(ScaleDivision(1, "desc")), scales[0].divisions)
    }

    @Test
    fun addOne_UpdateOneDivisions() {
        val id = create(CreateScaleRequest("n", "u", mapOf(1L to "desc"))).getOrThrow().scaleId
        update(UpdateScaleRequest(id, divisions = mapOf(1L to "desc", 2L to "d"))).getOrThrow()

        val scales = list().getOrThrow().scales
        assertEquals(1, scales.size)
        assertEquals("n", scales[0].id)
        assertEquals("n", scales[0].name)
        assertEquals("u", scales[0].unit)
        assertEquals(
            listOf(
                ScaleDivision(1, "desc"),
                ScaleDivision(2, "d")
            ), scales[0].divisions
        )
    }

    @Test
    fun addOne_UpdateOneAll() {
        val id = create(CreateScaleRequest("n", "u", mapOf(1L to "desc"))).getOrThrow().scaleId
        update(
            UpdateScaleRequest(
                id, name = "n2", unit = "u2", divisions = mapOf(
                    1L to "d2", 2L to "d3"
                )
            )
        ).getOrThrow()

        val scales = list().getOrThrow().scales
        assertEquals(1, scales.size)
        assertEquals("n2", scales[0].name)
        assertEquals("n2", scales[0].id)
        assertEquals("u2", scales[0].unit)
        assertEquals(
            listOf(
                ScaleDivision(1, "d2"),
                ScaleDivision(2, "d3")
            ), scales[0].divisions
        )
    }

    @Test
    fun addOne_UpdateSame() {
        val id = create(CreateScaleRequest("n", "u", mapOf(1L to "desc"))).getOrThrow().scaleId
        update(
            UpdateScaleRequest(
                id, name = "n", unit = "u", divisions = mapOf(1L to "desc")
            )
        ).getOrThrow()

        val scales = list().getOrThrow().scales
        assertEquals(1, scales.size)
        assertEquals("n", scales[0].name)
        assertEquals("n", scales[0].id)
        assertEquals("u", scales[0].unit)
        assertEquals(
            listOf(
                ScaleDivision(1, "desc"),
            ), scales[0].divisions
        )
    }

    @Test
    fun addTwo_UpdateOneExistingName() {
        val id = create(CreateScaleRequest("n", "u", mapOf(1L to "desc"))).getOrThrow().scaleId
        create(CreateScaleRequest("n2", "u", mapOf(1L to "desc"))).getOrThrow().scaleId
        update(
            UpdateScaleRequest(
                id, name = "n2", unit = "u", divisions = mapOf(1L to "desc")
            )
        ).onSuccess {
            fail()
        }.onFailure {
            assertIs<ScaleAlreadyExistsException>(it)
        }
    }

    @Test
    fun addOne_UpdateOneWrong() {
        create(CreateScaleRequest("n", "u", mapOf(1L to "desc"))).getOrThrow()

        update(
            UpdateScaleRequest(
                "invalid", name = "n2", unit = "u2", divisions = mapOf(
                    1L to "d2", 2L to "d3"
                )
            )
        ).onFailure {
            assertIs<ScaleDoesNotExistException>(it)
        }.onSuccess {
            fail()
        }

    }
}