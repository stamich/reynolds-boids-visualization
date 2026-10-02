package io.codeswarm.boids.simulation

import breeze.linalg.DenseVector
import io.codeswarm.boids.model.{Boid, SimulationConfig}

import scala.util.Random

/** Creates the initial flock used by the desktop visualization. */
object BoidFactory {

  /** Creates `config.boidCount` boids with random positions and velocities.
    *
    * @param config
    *   simulation parameters
    * @param random
    *   random number source; injectable to keep unit tests reproducible
    */
  def randomFlock(config: SimulationConfig, random: Random = new Random()): IndexedSeq[Boid] =
    Vector.fill(config.boidCount) {
      val position = DenseVector(random.nextDouble() * config.width, random.nextDouble() * config.height)
      val velocity = randomVelocity(config.maxSpeed, random)
      Boid(position, velocity)
    }

  private def randomVelocity(maxSpeed: Double, random: Random): DenseVector[Double] = {
    val angle = random.nextDouble() * math.Pi * 2.0
    val speed = maxSpeed * (0.5 + random.nextDouble() * 0.5)
    DenseVector(math.cos(angle) * speed, math.sin(angle) * speed)
  }
}
