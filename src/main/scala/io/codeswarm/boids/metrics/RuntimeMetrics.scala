package io.codeswarm.boids.metrics

/** Runtime measurements shown by the desktop diagnostics panel.
  *
  * Timings are observations of the current process and are not part of deterministic simulation state.
  *
  * @param tick
  *   current simulation tick
  * @param boidCount
  *   current flock size
  * @param averageSpeed
  *   mean velocity magnitude
  * @param simulationStepMillis
  *   last simulation-step duration
  * @param renderMillis
  *   last render duration
  * @param frameMillis
  *   last complete animation-frame duration
  * @param fps
  *   instantaneous frames per second derived from frame time
  * @param spatial
  *   optional spatial-index diagnostics
  */
final case class RuntimeMetrics(
    tick: Long = 0L,
    boidCount: Int = 0,
    averageSpeed: Double = 0.0,
    simulationStepMillis: Double = 0.0,
    renderMillis: Double = 0.0,
    frameMillis: Double = 0.0,
    fps: Double = 0.0,
    spatial: Option[SpatialMetrics] = None
)
