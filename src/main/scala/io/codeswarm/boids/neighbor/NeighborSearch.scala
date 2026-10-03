package io.codeswarm.boids.neighbor

import io.codeswarm.boids.metrics.SpatialMetrics
import io.codeswarm.boids.model.{Boid, WorldConfig}

/** Prepared read-only neighborhood index for one immutable flock snapshot. */
trait NeighborIndex {

  /** Returns neighbors within `radius`, excluding the query boid itself. */
  def neighborsOf(boid: Boid, radius: Double): IndexedSeq[Boid]
}

/** Optional diagnostics exposed by an instrumented index. */
trait InstrumentedNeighborIndex extends NeighborIndex {

  /** Returns a snapshot of counters accumulated by queries executed so far. */
  def spatialMetrics: SpatialMetrics
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
