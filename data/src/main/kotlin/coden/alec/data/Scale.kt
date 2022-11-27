package coden.alec.data

import java.lang.RuntimeException
import java.util.UUID

data class Scale (
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val unit: String,
    val deleted: Boolean,
    val divisions: List<ScaleDivision>
    )


data class ScaleDivision (
    val value: Long,
    val description: String
)

interface ScaleGateway{
    fun getScales(): Result<List<Scale>>
    fun getScaleById(scaleId: String): Result<Scale>

    fun addScaleOrUpdate(scale: Scale): Result<Unit>
    fun updateScaleSetDeleted(scaleId: String, deleted: Boolean): Result<Unit>
    fun deleteScale(scaleId: String): Result<Unit>
    fun deleteAll(): Result<Unit>
}

class ScaleDoesNotExistException(val scaleId: String): RuntimeException()