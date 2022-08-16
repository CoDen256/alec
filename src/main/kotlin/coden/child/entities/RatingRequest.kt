package coden.child.entities

import java.time.Instant

data class RatingRequest(
    val id: String,
    val factorId: String,
    val raterId: String,
    val timestamp: Instant,
    val replied: Boolean,
    val repliedTimestamp: Instant?
)


interface RatingRequestGateway{
    fun getRatingRequests(): List<RatingRequest>
    fun addRatingRequest(request: RatingRequest)
    fun updateRatingRequest(request: RatingRequest)
    fun deleteRatingRequest(requestId: String)
}