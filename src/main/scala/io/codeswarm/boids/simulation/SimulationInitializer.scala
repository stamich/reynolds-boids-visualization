package io.codeswarm.boids.simulation

import io.codeswarm.boids.math.Vector2
import io.codeswarm.boids.model.{Boid, BoidId, SimulationConfig, SimulationState}

import scala.util.Random

/** Creates the initial immutable simulation state. */
trait SimulationInitializer {

  /** Creates tick zero for the supplied configuration. */
  def initialize(config: SimulationConfig): SimulationState
}

/** Deterministic random initializer driven by `SimulationConfig.seed`. */
final class RandomSimulationInitializer extends SimulationInitializer {

  /** Creates uniformly distributed positions and random headings/speeds. */
  override def initialize(config: SimulationConfig): SimulationState = {
    val random = new Random(config.seed)
    val boids = Vector.tabulate(config.flock.boidCount) { index =>
      val position = Vector2(
        random.nextDouble() * config.world.width,
        random.nextDouble() * config.world.height
      )
      val angle = random.nextDouble() * math.Pi * 2.0
      val speed = config.flock.maxSpeed * (0.5 + random.nextDouble() * 0.5)
      val velocity = Vector2.fromAngle(angle) * speed
      Boid(BoidId(index.toLong), position, velocity)
    }
    SimulationState(tick = 0L, boids = boids)
  }
}
