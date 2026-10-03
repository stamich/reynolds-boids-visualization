package io.codeswarm.boids.simulation

import io.codeswarm.boids.model.{FlockConfig, SimulationConfig}
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Test

/** Determinism tests for seeded initialization. */
final class SimulationInitializerTest {

  @Test
  def sameSeedProducesSameInitialState(): Unit = {
    val config = SimulationConfig.Default.copy(flock = FlockConfig(50, 4.0, 0.08), seed = 123L)
    val initializer = new RandomSimulationInitializer
    assertEquals(initializer.initialize(config), initializer.initialize(config))
  }

  @Test
  def differentSeedChangesInitialState(): Unit = {
    val initializer = new RandomSimulationInitializer
    assertNotEquals(
      initializer.initialize(SimulationConfig.Default.copy(seed = 1L)),
      initializer.initialize(SimulationConfig.Default.copy(seed = 2L))
    )
  }
}
