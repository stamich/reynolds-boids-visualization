package io.codeswarm.boids.boundary

import io.codeswarm.boids.math.Vector2
import io.codeswarm.boids.model.WorldConfig

/** Policy applied to positions leaving the simulation world. */
trait BoundaryPolicy {

  /** Maps a candidate position back into the valid world according to the policy. */
  def apply(position: Vector2, world: WorldConfig): Vector2
}
