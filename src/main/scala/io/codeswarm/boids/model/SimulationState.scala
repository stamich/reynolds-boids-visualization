package io.codeswarm.boids.model

/** Immutable snapshot of the simulation at one logical tick.
  *
  * Renderers and headless clients consume this type without access to mutable engine internals.
  *
  * @param tick
  *   logical simulation tick, starting at zero
  * @param boids
  *   current immutable flock
  */
final case class SimulationState(tick: Long, boids: Vector[Boid]) {
  require(tick >= 0L, "tick must be non-negative")
}
