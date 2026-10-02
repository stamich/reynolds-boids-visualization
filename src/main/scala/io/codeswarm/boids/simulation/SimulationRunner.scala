package io.codeswarm.boids.simulation

import io.codeswarm.boids.model.SimulationState

/** Executes a `SimulationEngine` without any rendering dependency. */
final class SimulationRunner(engine: SimulationEngine) {

  /** Runs exactly `steps` ticks and returns the final state.
    *
    * @throws IllegalArgumentException
    *   when `steps` is negative
    */
  def run(initial: SimulationState, steps: Int): SimulationState = {
    require(steps >= 0, "steps must be non-negative")
    Iterator.iterate(initial)(engine.step).drop(steps).next()
  }

  /** Returns a lazy infinite stream starting with `initial` and containing every subsequent state. */
  def states(initial: SimulationState): LazyList[SimulationState] = LazyList.iterate(initial)(engine.step)
}
