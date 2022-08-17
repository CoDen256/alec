package coden.alec.data

data class Scale (
    val id: String,
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
    fun getScales(): List<Scale>
    fun getScalesCount(): Int
    fun addScale(scale: Scale)
    fun updateScaleSetDeleted(scaleId: String, deleted: Boolean)
    fun deleteScale(scaleId: String)
}

