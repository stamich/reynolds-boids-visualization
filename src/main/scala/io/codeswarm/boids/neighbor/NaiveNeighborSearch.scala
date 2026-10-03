package io.codeswarm.boids.neighbor

import io.codeswarm.boids.model.{Boid, WorldConfig}

/** Reference neighbor strategy that scans the complete flock for every query.
  *
  * The implementation is intentionally retained in milestone 0.4 as the correctness baseline for spatial indexing and as the JMH performance baseline against
  * which the uniform-grid implementation is measured.
  */
final class NaiveNeighborSearch extends NeighborSearch {

  /** Captures the immutable flock snapshot in a simple linear-scan index. */
  override def index(flock: IndexedSeq[Boid], world: WorldConfig): NeighborIndex =
    new NeighborIndex {
      override def neighborsOf(boid: Boid, radius: Double): IndexedSeq[Boid] = {
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
}
