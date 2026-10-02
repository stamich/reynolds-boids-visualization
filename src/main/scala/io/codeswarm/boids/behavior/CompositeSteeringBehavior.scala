package io.codeswarm.boids.behavior

import io.codeswarm.boids.math.Vector2
import io.codeswarm.boids.model.Boid

/** Combines independent steering behaviors through explicit weights.
  *
  * This removes knowledge of concrete rules from `SimulationEngine`. Future behaviors such as obstacle avoidance or
  * predator avoidance can be added by composition rather than engine modification.
  *
  * @param behaviors
  *   weighted steering components
  */
final class CompositeSteeringBehavior(behaviors: Vector[WeightedBehavior]) extends SteeringBehavior {
  require(behaviors.nonEmpty, "at least one steering behavior is required")

  /** Sums all weighted steering contributions. */
  override def force(boid: Boid, neighbors: IndexedSeq[Boid], context: SteeringContext): Vector2 =
    behaviors.foldLeft(Vector2.Zero) { case (sum, WeightedBehavior(behavior, weight)) =>
      sum + behavior.force(boid, neighbors, context) * weight
    }
}
