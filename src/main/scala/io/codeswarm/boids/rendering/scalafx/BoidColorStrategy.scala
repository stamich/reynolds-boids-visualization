package io.codeswarm.boids.rendering.scalafx

import io.codeswarm.boids.model.Boid
import scalafx.scene.paint.Color

/** Selects a stable display color for a boid without adding UI data to the domain model. */
trait BoidColorStrategy {

  /** Returns the color used to render `boid`. */
  def colorFor(boid: Boid): Color
}

/** Assigns deterministic, well-separated hues from `BoidId`. */
final class IdBasedColorStrategy extends BoidColorStrategy {
  private val GoldenAngleDegrees = 137.50776405003785

  /** Computes a stable high-saturation color from the numeric identifier. */
  override def colorFor(boid: Boid): Color = {
    val hue = ((boid.id.value * GoldenAngleDegrees) % 360.0 + 360.0) % 360.0
    Color.hsb(hue, 0.78, 0.96)
  }
}

/** Colors boids by the fraction of their current speed relative to `maxSpeed`. */
final class SpeedBasedColorStrategy(maxSpeed: Double) extends BoidColorStrategy {
  require(maxSpeed > 0.0, "maxSpeed must be positive")

  /** Maps slow boids toward blue and fast boids toward red. */
  override def colorFor(boid: Boid): Color = {
    val fraction = math.max(0.0, math.min(1.0, boid.velocity.magnitude / maxSpeed))
    Color.hsb(240.0 * (1.0 - fraction), 0.8, 0.96)
  }
}
