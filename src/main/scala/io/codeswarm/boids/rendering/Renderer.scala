package io.codeswarm.boids.rendering

import io.codeswarm.boids.model.SimulationState

/** Read-only rendering boundary for simulation states. */
trait Renderer {

  /** Renders one immutable simulation snapshot. */
  def render(state: SimulationState): Unit
}
