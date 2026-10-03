package io.codeswarm.boids.simulation

import io.codeswarm.boids.behavior.{SteeringBehavior, SteeringContext}
import io.codeswarm.boids.boundary.BoundaryPolicy
import io.codeswarm.boids.math.Vector2
import io.codeswarm.boids.metrics.SpatialMetrics
import io.codeswarm.boids.model.{Boid, SimulationConfig, SimulationState}
import io.codeswarm.boids.neighbor.{InstrumentedNeighborIndex, NeighborSearch}

/** Result of one simulation tick with optional diagnostic information. */
final case class SimulationStepResult(state: SimulationState, spatialMetrics: Option[SpatialMetrics])

/** Advances immutable simulation snapshots. */
trait SimulationEngine {

  /** Advances `state` by exactly one logical simulation tick. */
  def step(state: SimulationState): SimulationState = stepDetailed(state).state

  /** Advances `state` and returns diagnostics produced by the tick. */
  def stepDetailed(state: SimulationState): SimulationStepResult
}

/** Default Reynolds boids engine using snapshot semantics. */
final class DefaultSimulationEngine(
    config: SimulationConfig,
    steering: SteeringBehavior,
    neighborSearch: NeighborSearch,
    boundaryPolicy: BoundaryPolicy
) extends SimulationEngine {

  private val steeringContext = SteeringContext(config.flock.maxSpeed, config.flock.maxForce)

  /** Computes the next state from the current immutable flock snapshot. */
  override def stepDetailed(state: SimulationState): SimulationStepResult = {
    val currentFlock = state.boids
    val perceptionRadius = config.behavior.perceptionRadius
    val neighborIndex = neighborSearch.index(currentFlock, config.world)

    val nextBoids = currentFlock.map { boid =>
      val neighbors = neighborIndex.neighborsOf(boid, perceptionRadius)
      val force = steering.force(boid, neighbors, steeringContext)
      integrate(boid, force)
    }

    val metrics = neighborIndex match {
      case instrumented: InstrumentedNeighborIndex => Some(instrumented.spatialMetrics)
      case _ => None
    }
    SimulationStepResult(SimulationState(state.tick + 1L, nextBoids), metrics)
  }

  /** Applies steering, speed limiting and the configured boundary policy to one boid. */
  private def integrate(boid: Boid, force: Vector2): Boid = {
    val nextVelocity = (boid.velocity + force).limit(config.flock.maxSpeed)
    val nextPosition = boundaryPolicy(boid.position + nextVelocity, config.world)
    Boid(boid.id, nextPosition, nextVelocity)
  }
}
