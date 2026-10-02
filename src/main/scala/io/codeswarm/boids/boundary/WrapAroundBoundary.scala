package io.codeswarm.boids.boundary

import io.codeswarm.boids.math.Vector2
import io.codeswarm.boids.model.WorldConfig

/** Toroidal screen boundary that wraps positions independently on both axes. */
final class WrapAroundBoundary extends BoundaryPolicy {

  /** Wraps arbitrary positive or negative coordinate overshoot using modulo arithmetic. */
  override def apply(position: Vector2, world: WorldConfig): Vector2 =
    Vector2(wrapCoordinate(position.x, world.width), wrapCoordinate(position.y, world.height))

  /** Wraps one coordinate into `[0, upperBound)`. */
  private def wrapCoordinate(value: Double, upperBound: Double): Double = {
    val remainder = value % upperBound
    if (remainder < 0.0) remainder + upperBound else remainder
  }
}
