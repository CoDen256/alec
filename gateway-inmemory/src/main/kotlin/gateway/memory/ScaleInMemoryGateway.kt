package gateway.memory

import coden.child.entities.Scale
import coden.child.entities.ScaleGateway

class ScaleInMemoryGateway: ScaleGateway {

    private val scales = ArrayList<Scale>()

    override fun getScales(): List<Scale> {
        return scales
    }

    override fun getScalesCount(): Int {
        return scales.size
    }

    override fun addScale(scale: Scale) {
        scales.add(scale)
    }

    override fun updateScaleSetDeleted(scaleId: String, deleted: Boolean) {
        scales.filter { it.id == scaleId }.map { it.copy(deleted = deleted) }
    }

    override fun deleteScale(scaleId: String) {
        scales.removeIf { it.id == scaleId }
    }
}