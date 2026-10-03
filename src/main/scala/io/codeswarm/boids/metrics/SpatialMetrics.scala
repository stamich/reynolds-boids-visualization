package io.codeswarm.boids.metrics

/** Diagnostics describing work performed by a prepared spatial index.
  *
  * @param occupiedCells
  *   number of non-empty uniform-grid cells
  * @param averageBoidsPerCell
  *   mean occupancy across non-empty cells
  * @param maxBoidsPerCell
  *   maximum occupancy of a single non-empty cell
  * @param averageCandidatesPerQuery
  *   mean number of exact-distance candidates inspected per query
  * @param averageNeighborsPerQuery
  *   mean number of accepted neighbors per query
  */
final case class SpatialMetrics(
    occupiedCells: Int,
    averageBoidsPerCell: Double,
    maxBoidsPerCell: Int,
    averageCandidatesPerQuery: Double,
    averageNeighborsPerQuery: Double
)
