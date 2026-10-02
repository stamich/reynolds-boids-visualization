package io.codeswarm.boids.behavior

import io.codeswarm.boids.math.Vector2
import io.codeswarm.boids.model.Boid

/** Implements cohesion: a boid steers toward the local center of mass. */
final class Cohesion extends SteeringBehavior {

  /** Computes steering toward the mean position of visible neighbors. */
  override def force(boid: Boid, neighbors: IndexedSeq[Boid], context: SteeringContext): Vector2 =
    if (neighbors.isEmpty) Vector2.Zero
    else {
      val center = SteeringMath.average(neighbors.map(_.position))
      SteeringMath.steerToward(boid, center, context)
    }
}
