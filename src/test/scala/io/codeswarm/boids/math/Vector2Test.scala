package io.codeswarm.boids.math

import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Test

/** Unit tests for the dedicated simulation vector type. */
final class Vector2Test {

  @Test
  def arithmeticAndMagnitude(): Unit = {
    val v = Vector2(3.0, 4.0)
    assertEquals(Vector2(4.0, 6.0), v + Vector2(1.0, 2.0))
    assertEquals(Vector2(2.0, 2.0), v - Vector2(1.0, 2.0))
    assertEquals(Vector2(6.0, 8.0), v * 2.0)
    assertEquals(25.0, v.magnitudeSquared, 1e-12)
    assertEquals(5.0, v.magnitude, 1e-12)
  }

  @Test
  def normalizationIsSafeForZero(): Unit = {
    assertEquals(Vector2.Zero, Vector2.Zero.normalized)
    assertEquals(1.0, Vector2(3.0, 4.0).normalized.magnitude, 1e-12)
  }

  @Test
  def limitNeverExceedsMaximum(): Unit = {
    assertTrue(Vector2(30.0, 40.0).limit(3.0).magnitude <= 3.0 + 1e-12)
    assertEquals(Vector2(1.0, 1.0), Vector2(1.0, 1.0).limit(3.0))
  }

  @Test
  def distanceAndDotAreCorrect(): Unit = {
    assertEquals(5.0, Vector2(0.0, 0.0).distanceTo(Vector2(3.0, 4.0)), 1e-12)
    assertEquals(11.0, Vector2(1.0, 2.0).dot(Vector2(3.0, 4.0)), 1e-12)
  }
}
