package io.codeswarm.boids.model

/** Rectangular simulation world dimensions.
  *
  * @param width
  *   horizontal extent
  * @param height
  *   vertical extent
  */
final case class WorldConfig(width: Double = 1200.0, height: Double = 800.0) {
  require(width > 0.0, "width must be positive")
  require(height > 0.0, "height must be positive")
}

/** Flock-wide physical limits.
  *
  * @param boidCount
  *   number of boids generated at initialization
  * @param maxSpeed
  *   maximum velocity magnitude
  * @param maxForce
  *   maximum magnitude of one steering contribution
  */
final case class FlockConfig(boidCount: Int = 200, maxSpeed: Double = 4.0, maxForce: Double = 0.08) {
  require(boidCount > 0, "boidCount must be positive")
  require(maxSpeed > 0.0, "maxSpeed must be positive")
  require(maxForce > 0.0, "maxForce must be positive")
}

/** Parameters of the three classic Reynolds steering rules.
  *
  * @param perceptionRadius
  *   neighborhood radius used by alignment and cohesion
  * @param separationRadius
  *   short-range neighborhood radius used by separation
  * @param separationWeight
  *   weight applied to separation
  * @param alignmentWeight
  *   weight applied to alignment
  * @param cohesionWeight
  *   weight applied to cohesion
  */
final case class BehaviorConfig(
    perceptionRadius: Double = 70.0,
    separationRadius: Double = 25.0,
    separationWeight: Double = 1.5,
    alignmentWeight: Double = 1.0,
    cohesionWeight: Double = 1.0
) {
  require(perceptionRadius > 0.0, "perceptionRadius must be positive")
  require(separationRadius > 0.0, "separationRadius must be positive")
  require(separationRadius <= perceptionRadius, "separationRadius should not exceed perceptionRadius")
  require(separationWeight >= 0.0, "separationWeight must be non-negative")
  require(alignmentWeight >= 0.0, "alignmentWeight must be non-negative")
  require(cohesionWeight >= 0.0, "cohesionWeight must be non-negative")
}

/** Rendering parameters kept outside the simulation physics.
  *
  * @param boidSize
  *   triangle size in pixels
  */
final case class RenderConfig(boidSize: Double = 7.0) {
  require(boidSize > 0.0, "boidSize must be positive")
}

/** Complete deterministic configuration of a simulation run.
  *
  * @param world
  *   world dimensions
  * @param flock
  *   physical flock limits
  * @param behavior
  *   steering-rule parameters
  * @param render
  *   rendering-only configuration
  * @param seed
  *   random seed used by the initializer
  */
final case class SimulationConfig(
    world: WorldConfig = WorldConfig(),
    flock: FlockConfig = FlockConfig(),
    behavior: BehaviorConfig = BehaviorConfig(),
    render: RenderConfig = RenderConfig(),
    seed: Long = 42L
)

/** Default desktop configuration. */
object SimulationConfig {
  val Default: SimulationConfig = SimulationConfig()
}
