package gateway.memory

import coden.alec.data.Scale
import coden.alec.data.ScaleDoesNotExistException
import coden.alec.data.ScaleGateway

class ScaleInMemoryGateway : ScaleGateway {

    private val scales = ArrayList<Scale>()

    override fun getScales(): List<Scale> {
        return scales
    }

    override fun getScaleById(scaleId: String): Result<Scale> {
        return scales.firstOrNull { it.id == scaleId }?.let {
            Result.success(it)
        } ?: Result.failure(ScaleDoesNotExistException(scaleId))
    }


    override fun getScalesCount(): Int {
        return scales.size
    }

    override fun addScale(scale: Scale) {
        scales.add(scale)
    }

    override fun updateScaleSetDeleted(scaleId: String, deleted: Boolean) {
        scales.replaceAll {
            if (it.id == scaleId) it.copy(deleted = deleted)
            else it
        }
    }

    override fun deleteScale(scaleId: String) {
        scales.removeIf { it.id == scaleId }
    }
}