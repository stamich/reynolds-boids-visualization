package io.codeswarm.boids.simulation

import io.codeswarm.boids.model.{FlockConfig, SimulationConfig}
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Test

/** Tests the headless multi-step runner. */
final class SimulationRunnerTest {

  @Test
  def zeroStepsReturnsInitialState(): Unit = {
    val config = SimulationConfig.Default.copy(flock = FlockConfig(5, 4.0, 0.08))
    val initial = new RandomSimulationInitializer().initialize(config)
    val result = new SimulationRunner(SimulationComponents.engine(config)).run(initial, 0)
    assertEquals(initial, result)
  }

  @Test
  def lazyStatesStartsAtInitialAndThenAdvances(): Unit = {
    val config = SimulationConfig.Default.copy(flock = FlockConfig(5, 4.0, 0.08))
    val initial = new RandomSimulationInitializer().initialize(config)
    val states = new SimulationRunner(SimulationComponents.engine(config)).states(initial).take(3).toList
    assertEquals(List(0L, 1L, 2L), states.map(_.tick))
  }
}
