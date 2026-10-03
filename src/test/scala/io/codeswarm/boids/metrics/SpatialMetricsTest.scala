package io.codeswarm.boids.metrics

import io.codeswarm.boids.model.SimulationConfig
import io.codeswarm.boids.neighbor.{InstrumentedNeighborIndex, UniformGridNeighborSearch}
import io.codeswarm.boids.simulation.RandomSimulationInitializer
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Test

/** Verifies that optional grid instrumentation reports useful work metrics. */
final class SpatialMetricsTest {

  @Test
  def instrumentedGridReportsQueriesAndOccupancy(): Unit = {
    val config = SimulationConfig.Default.copy(flock = SimulationConfig.Default.flock.copy(boidCount = 200))
    val state = new RandomSimulationInitializer().initialize(config)
    val index = new UniformGridNeighborSearch(config.behavior.perceptionRadius, metricsEnabled = true)
      .index(state.boids, config.world)
      .asInstanceOf[InstrumentedNeighborIndex]

    state.boids.foreach(boid => index.neighborsOf(boid, config.behavior.perceptionRadius))
    val metrics = index.spatialMetrics
    assertTrue(metrics.occupiedCells > 0)
    assertTrue(metrics.averageBoidsPerCell > 0.0)
    assertTrue(metrics.maxBoidsPerCell >= 1)
    assertTrue(metrics.averageCandidatesPerQuery >= metrics.averageNeighborsPerQuery)
  }
}
