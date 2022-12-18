package coden.alec.gateway.memory

import coden.alec.data.Scale
import coden.alec.data.ScaleDoesNotExistException
import coden.alec.data.ScaleGateway

class ScaleInMemoryGateway() : ScaleGateway {

    private val scales = HashMap<String, Scale>()

    constructor(scales: Collection<Scale>) : this() {
        scales.forEach {
            this.scales[it.id] = it
        }
    }


    override fun getScales(): Result<List<Scale>> {
        return Result.success(scales.values.sortedBy { it.id }.toList())
    }

    override fun getScaleById(scaleId: String): Result<Scale> {
        return scales[scaleId]?.let {
            Result.success(it)
        } ?: Result.failure(ScaleDoesNotExistException(scaleId))
    }

    override fun addScaleOrUpdate(scale: Scale): Result<Unit> {
        scales[scale.id] = scale
        return Result.success(Unit)
    }

    override fun updateScaleSetDeleted(scaleId: String, deleted: Boolean): Result<Unit> {
        scales.compute(scaleId) { _, scale ->
            scale?.copy(deleted = deleted)
        }
        return Result.success(Unit)
    }

    override fun deleteScale(scaleId: String): Result<Unit> {
        scales.remove(scaleId)
        return Result.success(Unit)
    }

    override fun deleteAll(): Result<Unit> {
        scales.clear()
        return Result.success(Unit)
    }
}