package io.codeswarm.boids.neighbor

import io.codeswarm.boids.model.Boid

/** Strategy used by the simulation engine to discover local neighbors.
  *
  * Milestone 0.2 ships only the reference O(n²) implementation. The abstraction is introduced now so milestone 0.3 can
  * add grid- or tree-based searches without changing the engine or steering rules.
  */
trait NeighborSearch {

  /** Returns neighbors within `radius`, excluding the query boid itself. */
  def neighborsOf(boid: Boid, flock: IndexedSeq[Boid], radius: Double): IndexedSeq[Boid]
}
