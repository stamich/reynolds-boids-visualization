package io.codeswarm.boids.behavior

import io.codeswarm.boids.math.Vector2
import io.codeswarm.boids.model.Boid

/** Shared pure mathematics used by the classic steering behaviors. */
private[behavior] object SteeringMath {

  /** Computes an arithmetic mean, returning zero for an empty collection. */
  def average(vectors: IndexedSeq[Vector2]): Vector2 =
    if (vectors.isEmpty) Vector2.Zero else vectors.foldLeft(Vector2.Zero)(_ + _) / vectors.size.toDouble

  /** Converts an average desired direction/velocity into a limited steering vector. */
  def steerFromAverage(vectors: IndexedSeq[Vector2], currentVelocity: Vector2, context: SteeringContext): Vector2 = {
    val mean = average(vectors)
    if (mean.isZero) Vector2.Zero
    else (mean.normalized * context.maxSpeed - currentVelocity).limit(context.maxForce)
  }

  /** Computes limited steering toward a target point. */
  def steerToward(boid: Boid, target: Vector2, context: SteeringContext): Vector2 = {
    val direction = target - boid.position
    if (direction.isZero) Vector2.Zero
    else (direction.normalized * context.maxSpeed - boid.velocity).limit(context.maxForce)
  }
}
