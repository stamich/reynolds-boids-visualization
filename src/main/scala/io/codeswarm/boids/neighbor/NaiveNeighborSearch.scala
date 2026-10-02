package io.codeswarm.boids.neighbor

import io.codeswarm.boids.model.Boid

/** Direct O(n) neighbor query used as the correctness and performance baseline for future spatial indexes. */
final class NaiveNeighborSearch extends NeighborSearch {

  /** Scans the complete flock and filters by squared Euclidean distance. */
  override def neighborsOf(boid: Boid, flock: IndexedSeq[Boid], radius: Double): IndexedSeq[Boid] = {
    require(radius > 0.0, "radius must be positive")
    val radiusSquared = radius * radius
    flock.filter { other =>
      other.id != boid.id && {
        val distanceSquared = boid.position.distanceSquaredTo(other.position)
        distanceSquared > 0.0 && distanceSquared < radiusSquared
      }
    }
  }
}
