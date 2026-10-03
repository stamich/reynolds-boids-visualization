package io.codeswarm.boids.neighbor

import io.codeswarm.boids.model.{Boid, WorldConfig}

/** Prepared read-only neighborhood index for one immutable flock snapshot.
  *
  * A simulation tick builds an index once and then reuses it for all boid queries. This contract allows a simple reference implementation and spatial indexes
  * to share the same engine integration.
  */
trait NeighborIndex {

  /** Returns neighbors within `radius`, excluding the query boid itself.
    *
    * @param boid
    *   boid whose neighborhood is requested
    * @param radius
    *   strict Euclidean query radius
    * @return
    *   boids whose distance from `boid` is greater than zero and smaller than `radius`
    */
  def neighborsOf(boid: Boid, radius: Double): IndexedSeq[Boid]
}

/** Strategy that prepares a neighborhood index for one immutable flock snapshot. */
trait NeighborSearch {

  /** Builds a query structure for `flock`.
    *
    * @param flock
    *   immutable flock snapshot used throughout one simulation tick
    * @param world
    *   world dimensions available to spatial implementations
    * @return
    *   prepared neighborhood index
    */
  def index(flock: IndexedSeq[Boid], world: WorldConfig): NeighborIndex
}
