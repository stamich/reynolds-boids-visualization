package io.codeswarm.boids.model

import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Test

/** Validation tests for grouped milestone-0.2 configuration. */
final class SimulationConfigTest {

  @Test def defaultsAreValid(): Unit = {
    val config = SimulationConfig.Default
    assertTrue(config.world.width > 0.0)
    assertTrue(config.flock.boidCount > 0)
    assertTrue(config.behavior.separationRadius <= config.behavior.perceptionRadius)
  }

  @Test def invalidWorldIsRejected(): Unit =
    assertThrows(classOf[IllegalArgumentException], () => WorldConfig(0.0, 100.0))

  @Test def invalidBehaviorRadiiAreRejected(): Unit =
    assertThrows(
      classOf[IllegalArgumentException],
      () => BehaviorConfig(perceptionRadius = 20.0, separationRadius = 30.0)
    )
}
