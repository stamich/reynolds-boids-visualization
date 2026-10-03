package io.codeswarm.boids.simulation

import io.codeswarm.boids.model.{FlockConfig, SimulationConfig}
import io.codeswarm.boids.neighbor.{NaiveNeighborSearch, UniformGridNeighborSearch}
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/** Regression tests proving that spatial indexing does not change the Reynolds neighborhood semantics. */
final class NeighborSearchEngineEquivalenceTest {

  /** Naive and grid engines produce the same deterministic trajectory from the same initial snapshot. */
  @Test
  def producesEquivalentTrajectory(): Unit = {
    val base = SimulationConfig.Default
    val config = base.copy(
      flock = FlockConfig(boidCount = 300, maxSpeed = 4.0, maxForce = 0.08),
      seed = 77L
    )
    val initial = new RandomSimulationInitializer().initialize(config)
    val naiveEngine = SimulationComponents.engine(config, new NaiveNeighborSearch)
    val gridEngine = SimulationComponents.engine(
      config,
      new UniformGridNeighborSearch(config.behavior.perceptionRadius)
    )

    val naiveFinal = new SimulationRunner(naiveEngine).run(initial, 50)
    val gridFinal = new SimulationRunner(gridEngine).run(initial, 50)

    assertEquals(naiveFinal, gridFinal)
  }
}
