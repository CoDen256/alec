package gateway.memory

import coden.alec.data.Scale
import coden.alec.data.ScaleDoesNotExistException
import coden.alec.data.ScaleGateway

class ScaleInMemoryGateway : ScaleGateway {

    private val scales = HashMap<String, Scale>()

    override fun getScales(): Result<List<Scale>> {
        return Result.success(scales.values.toList())
    }

    override fun getScaleById(scaleId: String): Result<Scale> {
        return scales[scaleId]?.let {
            Result.success(it)
        } ?: Result.failure(ScaleDoesNotExistException(scaleId))
    }


    override fun getScalesCount(): Result<Int> {
        return Result.success(scales.size)
    }

    override fun addScaleOrUpdate(scale: Scale): Result<Unit> {
        scales[scale.id] = scale
        return Result.success(Unit)
    }

    override fun updateScaleSetDeleted(scaleId: String, deleted: Boolean): Result<Unit> {
        scales.compute(scaleId){ _, scale ->
            scale?.copy(deleted = deleted)
        }
        return Result.success(Unit)
    }

    override fun deleteScale(scaleId: String): Result<Unit> {
        scales.remove(scaleId)
        return Result.success(Unit)
    }
}