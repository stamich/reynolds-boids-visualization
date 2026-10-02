package io.codeswarm.boids.boundary

import io.codeswarm.boids.math.Vector2
import io.codeswarm.boids.model.WorldConfig
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Test

/** Tests modulo-based world wrapping including large overshoots. */
final class WrapAroundBoundaryTest {
  private val boundary = new WrapAroundBoundary
  private val world = WorldConfig(100.0, 80.0)

  @Test def wrapsNegativeAndPositiveCoordinates(): Unit = {
    assertEquals(Vector2(99.0, 1.0), boundary(Vector2(-1.0, 81.0), world))
    assertEquals(Vector2(1.0, 79.0), boundary(Vector2(201.0, -1.0), world))
  }
}
