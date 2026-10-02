package io.codeswarm.boids.model

/** Immutable configuration of the baseline Reynolds boids simulation.
  *
  * Milestone 0.1.1 deliberately keeps the original three-rule model small. The configuration merely centralizes the
  * constants that otherwise tend to become scattered throughout rendering and update code.
  *
  * @param width
  *   width of the simulation canvas in pixels
  * @param height
  *   height of the simulation canvas in pixels
  * @param boidCount
  *   number of boids created at application startup
  * @param perceptionRadius
  *   maximum distance at which another boid influences alignment and cohesion
  * @param separationRadius
  *   maximum distance at which another boid contributes to separation
  * @param maxSpeed
  *   maximum allowed velocity magnitude
  * @param maxForce
  *   maximum magnitude of a single steering force
  * @param separationWeight
  *   weight of the separation force
  * @param alignmentWeight
  *   weight of the alignment force
  * @param cohesionWeight
  *   weight of the cohesion force
  * @param boidSize
  *   visual size of a boid triangle
  */
final case class SimulationConfig(
    width: Double = 1200.0,
    height: Double = 800.0,
    boidCount: Int = 200,
    perceptionRadius: Double = 70.0,
    separationRadius: Double = 25.0,
    maxSpeed: Double = 4.0,
    maxForce: Double = 0.08,
    separationWeight: Double = 1.5,
    alignmentWeight: Double = 1.0,
    cohesionWeight: Double = 1.0,
    boidSize: Double = 7.0
) {
  require(width > 0.0, "width must be positive")
  require(height > 0.0, "height must be positive")
  require(boidCount > 0, "boidCount must be positive")
  require(perceptionRadius > 0.0, "perceptionRadius must be positive")
  require(separationRadius > 0.0, "separationRadius must be positive")
  require(maxSpeed > 0.0, "maxSpeed must be positive")
  require(maxForce > 0.0, "maxForce must be positive")
  require(boidSize > 0.0, "boidSize must be positive")
}

/** Default configuration used by the desktop demo. */
object SimulationConfig {
  val Default: SimulationConfig = SimulationConfig()
}
