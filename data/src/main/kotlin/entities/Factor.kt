package entities

data class Factor(
    val id: String,
    val name: String,
    val description: String,
    val scaleId: String,
    val deleted: Boolean
)


interface FactorGateway{
    fun getFactors(): List<Factor>
    fun getFactorsCount(): Long
    fun addFactor(factor: Factor)
    fun updateFactor(factor: Factor)
    fun deleteFactor(factorId: String)
}