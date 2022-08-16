package coden.child.entities

data class Scale (
    val id: String,
    val name: String,
    val unit: String,
    val deleted: Boolean,
    val division: List<ScaleDivision>
    )


data class ScaleDivision (
    val value: Long,
    val description: String
)

interface ScaleGateway{
    fun getScales(): List<Scale>
    fun getScalesCount(): Long
    fun addScale(scale: Scale)
    fun updateScale(scale: Scale)
    fun deleteScale(scaleId: String)
}

