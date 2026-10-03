package io.codeswarm.boids.simulation

import io.codeswarm.boids.behavior.{SteeringBehavior, SteeringContext}
import io.codeswarm.boids.boundary.BoundaryPolicy
import io.codeswarm.boids.math.Vector2
import io.codeswarm.boids.model.{Boid, SimulationConfig, SimulationState}
import io.codeswarm.boids.neighbor.NeighborSearch

/** Advances immutable simulation snapshots. */
trait SimulationEngine {

  /** Advances `state` by exactly one logical simulation tick. */
  def step(state: SimulationState): SimulationState
}

/** Default Reynolds boids engine.
  *
  * Every boid reads the same immutable `SimulationState(t)` snapshot and all resulting boids are published together as `SimulationState(t + 1)`. The configured
  * `NeighborSearch` prepares one index for that snapshot and all boid queries reuse it during the tick.
  *
  * @param config
  *   simulation configuration
  * @param steering
  *   composite steering behavior
  * @param neighborSearch
  *   neighbor-index construction strategy
  * @param boundaryPolicy
  *   world-boundary behavior
  */
final class DefaultSimulationEngine(
    config: SimulationConfig,
    steering: SteeringBehavior,
    neighborSearch: NeighborSearch,
    boundaryPolicy: BoundaryPolicy
) extends SimulationEngine {

  private val steeringContext = SteeringContext(config.flock.maxSpeed, config.flock.maxForce)

  /** Computes the next state from the current immutable flock snapshot. */
  override def step(state: SimulationState): SimulationState = {
    val currentFlock = state.boids
    val perceptionRadius = config.behavior.perceptionRadius
    val neighborIndex = neighborSearch.index(currentFlock, config.world)

    val nextBoids = currentFlock.map { boid =>
      val neighbors = neighborIndex.neighborsOf(boid, perceptionRadius)
      val force = steering.force(boid, neighbors, steeringContext)
      integrate(boid, force)
    }

    SimulationState(state.tick + 1L, nextBoids)
  }

  /** Applies steering, speed limiting and the configured boundary policy to one boid. */
  private def integrate(boid: Boid, force: Vector2): Boid = {
    val nextVelocity = (boid.velocity + force).limit(config.flock.maxSpeed)
    val nextPosition = boundaryPolicy(boid.position + nextVelocity, config.world)
    Boid(boid.id, nextPosition, nextVelocity)
  }
}
