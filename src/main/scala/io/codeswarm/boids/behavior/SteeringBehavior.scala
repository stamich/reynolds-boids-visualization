package io.codeswarm.boids.behavior

import io.codeswarm.boids.math.Vector2
import io.codeswarm.boids.model.Boid

/** Runtime values required by steering rules.
  *
  * @param maxSpeed
  *   maximum desired speed
  * @param maxForce
  *   maximum steering magnitude produced by one behavior
  */
final case class SteeringContext(maxSpeed: Double, maxForce: Double) {
  require(maxSpeed > 0.0, "maxSpeed must be positive")
  require(maxForce > 0.0, "maxForce must be positive")
}

/** Computes one steering contribution for a boid.
  *
  * Implementations are pure: they must not mutate the boid or its neighbors.
  */
trait SteeringBehavior {

  /** Computes a steering force for `boid` from a preselected local neighborhood.
    *
    * @param boid
    *   boid being updated
    * @param neighbors
    *   local boids visible to the behavior
    * @param context
    *   physical steering limits
    * @return
    *   steering force for the current tick
    */
  def force(boid: Boid, neighbors: IndexedSeq[Boid], context: SteeringContext): Vector2
}

/** Associates a steering behavior with its contribution weight.
  *
  * @param behavior
  *   steering implementation
  * @param weight
  *   non-negative multiplier
  */
final case class WeightedBehavior(behavior: SteeringBehavior, weight: Double) {
  require(weight >= 0.0, "weight must be non-negative")
}
