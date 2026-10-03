package io.codeswarm.boids.simulation

import io.codeswarm.boids.model.{FlockConfig, SimulationConfig}
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Test

/** Integration-level invariants of the milestone-0.4 simulation engine. */
final class SimulationEngineTest {

  @Test
  def stepAdvancesTickAndPreservesFlockSize(): Unit = {
    val config = SimulationConfig.Default.copy(flock = FlockConfig(25, 4.0, 0.08), seed = 7L)
    val initializer = new RandomSimulationInitializer
    val initial = initializer.initialize(config)
    val next = SimulationComponents.engine(config).step(initial)
    assertEquals(1L, next.tick)
    assertEquals(initial.boids.size, next.boids.size)
  }

  @Test
  def longRunPreservesCoreInvariants(): Unit = {
    val config = SimulationConfig.Default.copy(flock = FlockConfig(80, 4.0, 0.08), seed = 42L)
    val initializer = new RandomSimulationInitializer
    val finalState = new SimulationRunner(SimulationComponents.engine(config)).run(initializer.initialize(config), 300)

    assertEquals(300L, finalState.tick)
    assertEquals(80, finalState.boids.size)
    assertTrue(finalState.boids.forall(_.position.isFinite))
    assertTrue(finalState.boids.forall(_.velocity.isFinite))
    assertTrue(finalState.boids.forall(_.velocity.magnitude <= config.flock.maxSpeed + 1e-9))
    assertTrue(finalState.boids.forall(b => b.position.x >= 0.0 && b.position.x < config.world.width))
    assertTrue(finalState.boids.forall(b => b.position.y >= 0.0 && b.position.y < config.world.height))
  }

  @Test
  def identicalSeedAndConfigurationProduceIdenticalTrajectory(): Unit = {
    val config = SimulationConfig.Default.copy(flock = FlockConfig(30, 4.0, 0.08), seed = 99L)
    val initializer = new RandomSimulationInitializer
    val runner = new SimulationRunner(SimulationComponents.engine(config))
    val first = runner.run(initializer.initialize(config), 100)
    val second = runner.run(initializer.initialize(config), 100)
    assertEquals(first, second)
  }
}
