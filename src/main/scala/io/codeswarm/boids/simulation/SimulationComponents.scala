package io.codeswarm.boids.simulation

import io.codeswarm.boids.behavior.{Alignment, Cohesion, CompositeSteeringBehavior, Separation, WeightedBehavior}
import io.codeswarm.boids.boundary.WrapAroundBoundary
import io.codeswarm.boids.model.SimulationConfig
import io.codeswarm.boids.neighbor.{NaiveNeighborSearch, NeighborSearch, NeighborSearchStrategy, UniformGridNeighborSearch}

/** Central factory for production simulation components. */
object SimulationComponents {

  /** Creates the classic weighted separation/alignment/cohesion composite. */
  def steering(config: SimulationConfig): CompositeSteeringBehavior =
    new CompositeSteeringBehavior(
      Vector(
        WeightedBehavior(new Separation(config.behavior.separationRadius), config.behavior.separationWeight),
        WeightedBehavior(new Alignment, config.behavior.alignmentWeight),
        WeightedBehavior(new Cohesion, config.behavior.cohesionWeight)
      )
    )

  /** Creates a neighbor-search implementation for the requested strategy. */
  def neighborSearch(config: SimulationConfig, strategy: NeighborSearchStrategy): NeighborSearch =
    strategy match {
      case NeighborSearchStrategy.Naive => new NaiveNeighborSearch
      case NeighborSearchStrategy.UniformGrid =>
        new UniformGridNeighborSearch(
          cellSize = config.behavior.perceptionRadius,
          metricsEnabled = config.diagnostics.spatialMetricsEnabled
        )
    }

  /** Creates an engine using the default uniform-grid strategy. */
  def engine(config: SimulationConfig): SimulationEngine = engine(config, NeighborSearchStrategy.UniformGrid)

  /** Creates an engine using a named neighbor-search strategy. */
  def engine(config: SimulationConfig, strategy: NeighborSearchStrategy): SimulationEngine =
    engine(config, neighborSearch(config, strategy))

  /** Creates an engine using an explicitly supplied neighbor-search implementation. */
  def engine(config: SimulationConfig, neighborSearch: NeighborSearch): SimulationEngine =
    new DefaultSimulationEngine(config, steering(config), neighborSearch, new WrapAroundBoundary)
}
