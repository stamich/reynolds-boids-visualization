package io.codeswarm.boids.model

import io.codeswarm.boids.math.Vector2

/** Immutable state of one boid.
  *
  * Acceleration is intentionally not persisted in the domain model. Steering is a derived value for one logical tick;
  * the engine computes it from `SimulationState(t)` and immediately integrates it into velocity and position.
  *
  * @param id
  *   stable boid identifier
  * @param position
  *   current position in world coordinates
  * @param velocity
  *   current velocity in world units per simulation tick
  */
final case class Boid(id: BoidId, position: Vector2, velocity: Vector2) {
  require(position.isFinite, "position must contain finite components")
  require(velocity.isFinite, "velocity must contain finite components")
}
