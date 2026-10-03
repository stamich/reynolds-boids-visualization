package io.codeswarm.boids.rendering.scalafx

import io.codeswarm.boids.math.Vector2
import io.codeswarm.boids.model.{Boid, BoidId}
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Test

/** Tests deterministic renderer-only color strategies. */
final class BoidColorStrategyTest {

  @Test
  def idBasedColorIsStableAcrossCalls(): Unit = {
    val strategy = new IdBasedColorStrategy
    val boid = Boid(BoidId(42L), Vector2(1.0, 2.0), Vector2(3.0, 0.0))
    assertEquals(strategy.colorFor(boid), strategy.colorFor(boid))
  }

  @Test
  def differentIdentifiersNormallyProduceDifferentColors(): Unit = {
    val strategy = new IdBasedColorStrategy
    val first = Boid(BoidId(1L), Vector2.Zero, Vector2(1.0, 0.0))
    val second = Boid(BoidId(2L), Vector2.Zero, Vector2(1.0, 0.0))
    assertNotEquals(strategy.colorFor(first), strategy.colorFor(second))
  }

  @Test
  def speedBasedColorChangesWithVelocityMagnitude(): Unit = {
    val strategy = new SpeedBasedColorStrategy(4.0)
    val slow = Boid(BoidId(1L), Vector2.Zero, Vector2(0.1, 0.0))
    val fast = Boid(BoidId(2L), Vector2.Zero, Vector2(4.0, 0.0))
    assertNotEquals(strategy.colorFor(slow), strategy.colorFor(fast))
  }
}
