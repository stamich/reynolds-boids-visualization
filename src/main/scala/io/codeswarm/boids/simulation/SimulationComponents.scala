package io.codeswarm.boids.simulation

import io.codeswarm.boids.behavior.{Alignment, Cohesion, CompositeSteeringBehavior, Separation, WeightedBehavior}
import io.codeswarm.boids.boundary.WrapAroundBoundary
import io.codeswarm.boids.model.SimulationConfig
import io.codeswarm.boids.neighbor.NaiveNeighborSearch

/** Central factory for the default milestone-0.2 simulation wiring.
  *
  * Keeping construction in one place prevents the desktop and headless entry points from drifting apart while leaving
  * the individual components directly testable.
  */
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

  /** Creates the default reference engine for the supplied configuration. */
  def engine(config: SimulationConfig): SimulationEngine =
    new DefaultSimulationEngine(
      config = config,
      steering = steering(config),
      neighborSearch = new NaiveNeighborSearch,
      boundaryPolicy = new WrapAroundBoundary
    )
}
