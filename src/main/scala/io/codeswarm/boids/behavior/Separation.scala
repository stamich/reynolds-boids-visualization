package io.codeswarm.boids.behavior

import io.codeswarm.boids.math.Vector2
import io.codeswarm.boids.model.Boid

/** Implements short-range separation: nearby boids repel each other.
  *
  * The neighbor list is expected to be filtered to `radius` by the caller. Distance is still checked defensively so the behavior remains correct when used
  * independently in tests or future compositions.
  *
  * @param radius
  *   maximum separation distance
  */
final class Separation(radius: Double) extends SteeringBehavior {
  require(radius > 0.0, "radius must be positive")

  /** Computes a distance-weighted repulsion force. */
  override def force(boid: Boid, neighbors: IndexedSeq[Boid], context: SteeringContext): Vector2 = {
    val radiusSquared = radius * radius
    val contributions = neighbors.iterator.flatMap { other =>
      val delta = boid.position - other.position
      val distanceSquared = delta.magnitudeSquared
      if (distanceSquared > 0.0 && distanceSquared < radiusSquared) {
        val distance = math.sqrt(distanceSquared)
        Some(delta.normalized / distance)
      } else None
    }.toVector

    SteeringMath.steerFromAverage(contributions, boid.velocity, context)
  }
}
