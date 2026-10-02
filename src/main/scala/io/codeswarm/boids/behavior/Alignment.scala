package io.codeswarm.boids.behavior

import io.codeswarm.boids.math.Vector2
import io.codeswarm.boids.model.Boid

/** Implements alignment: a boid steers toward the average velocity of its visible neighbors. */
final class Alignment extends SteeringBehavior {

  /** Computes steering toward the local average heading. */
  override def force(boid: Boid, neighbors: IndexedSeq[Boid], context: SteeringContext): Vector2 =
    SteeringMath.steerFromAverage(neighbors.map(_.velocity), boid.velocity, context)
}
