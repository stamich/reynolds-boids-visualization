package io.codeswarm.boids.ui

import io.codeswarm.boids.metrics.RuntimeMetrics
import io.codeswarm.boids.model.{BehaviorConfig, FlockConfig, RenderConfig, SimulationConfig, SimulationState}
import io.codeswarm.boids.neighbor.NeighborSearchStrategy
import io.codeswarm.boids.simulation.{RandomSimulationInitializer, SimulationComponents, SimulationEngine, SimulationInitializer}

/** Coordinates mutable desktop concerns while keeping simulation snapshots immutable.
  *
  * The controller owns the current state/configuration and lifecycle. Updating a live parameter rebuilds the lightweight engine wiring but preserves the
  * current flock. Restart-required changes update configuration and are applied when `restart()` is invoked.
  */
final class SimulationController(
    initialConfig: SimulationConfig,
    initializer: SimulationInitializer = new RandomSimulationInitializer,
    neighborStrategy: NeighborSearchStrategy = NeighborSearchStrategy.UniformGrid
) {
  private var configValue = initialConfig
  private var stateValue = initializer.initialize(initialConfig)
  private var statusValue: SimulationStatus = SimulationStatus.Ready
  private var engine: SimulationEngine = SimulationComponents.engine(initialConfig, neighborStrategy)
  private var restartRequiredValue = false
  private var metricsValue = RuntimeMetrics(boidCount = stateValue.boids.size)

  /** Current immutable configuration. */
  def config: SimulationConfig = configValue

  /** Current immutable simulation snapshot. */
  def state: SimulationState = stateValue

  /** Current application lifecycle status. */
  def status: SimulationStatus = statusValue

  /** Whether one or more changed parameters require reinitialization. */
  def restartRequired: Boolean = restartRequiredValue

  /** Latest runtime measurements. */
  def metrics: RuntimeMetrics = metricsValue

  /** Starts or resumes simulation advancement. */
  def start(): Unit = statusValue = SimulationStatus.Running

  /** Pauses simulation advancement without changing the current snapshot. */
  def pause(): Unit = if (statusValue == SimulationStatus.Running) statusValue = SimulationStatus.Paused

  /** Reinitializes tick zero from the current configuration and seed. */
  def restart(): Unit = {
    stateValue = initializer.initialize(configValue)
    engine = SimulationComponents.engine(configValue, neighborStrategy)
    restartRequiredValue = false
    statusValue = SimulationStatus.Ready
    metricsValue = RuntimeMetrics(boidCount = stateValue.boids.size)
  }

  /** Advances one tick only while the lifecycle is `Running`. */
  def advance(): SimulationState = {
    if (statusValue == SimulationStatus.Running) {
      val started = System.nanoTime()
      val result = engine.stepDetailed(stateValue)
      val elapsed = System.nanoTime() - started
      stateValue = result.state
      val averageSpeed =
        if (stateValue.boids.isEmpty) 0.0 else stateValue.boids.iterator.map(_.velocity.magnitude).sum / stateValue.boids.size
      metricsValue = metricsValue.copy(
        tick = stateValue.tick,
        boidCount = stateValue.boids.size,
        averageSpeed = averageSpeed,
        simulationStepMillis = nanosToMillis(elapsed),
        spatial = result.spatialMetrics
      )
    }
    stateValue
  }

  /** Records render and complete-frame timings supplied by the animation loop. */
  def recordFrame(renderNanos: Long, frameNanos: Long): Unit = {
    val frameMillis = nanosToMillis(frameNanos)
    metricsValue = metricsValue.copy(
      renderMillis = nanosToMillis(renderNanos),
      frameMillis = frameMillis,
      fps = if (frameMillis <= 0.0) 0.0 else 1000.0 / frameMillis
    )
  }

  /** Changes boid count; the new population is created on restart. */
  def updateBoidCount(value: Int): Unit = {
    configValue = configValue.copy(flock = configValue.flock.copy(boidCount = value))
    restartRequiredValue = true
  }

  /** Changes the deterministic seed; it is applied on restart. */
  def updateSeed(value: Long): Unit = {
    configValue = configValue.copy(seed = value)
    restartRequiredValue = true
  }

  /** Changes maximum speed and applies it to subsequent ticks. */
  def updateMaxSpeed(value: Double): Unit = updateLive(flock = Some(configValue.flock.copy(maxSpeed = value)))

  /** Changes maximum steering force and applies it to subsequent ticks. */
  def updateMaxForce(value: Double): Unit = updateLive(flock = Some(configValue.flock.copy(maxForce = value)))

  /** Changes perception radius while maintaining separationRadius <= perceptionRadius. */
  def updatePerceptionRadius(value: Double): Unit = {
    val behavior = configValue.behavior
    updateLive(
      behavior = Some(
        behavior.copy(
          perceptionRadius = value,
          separationRadius = math.min(behavior.separationRadius, value)
        )
      )
    )
  }

  /** Changes separation radius and clamps it to the current perception radius. */
  def updateSeparationRadius(value: Double): Unit =
    updateLive(behavior = Some(configValue.behavior.copy(separationRadius = math.min(value, configValue.behavior.perceptionRadius))))

  /** Changes the separation steering weight. */
  def updateSeparationWeight(value: Double): Unit = updateBehavior(_.copy(separationWeight = value))

  /** Changes the alignment steering weight. */
  def updateAlignmentWeight(value: Double): Unit = updateBehavior(_.copy(alignmentWeight = value))

  /** Changes the cohesion steering weight. */
  def updateCohesionWeight(value: Double): Unit = updateBehavior(_.copy(cohesionWeight = value))

  /** Changes boid triangle size without altering simulation state. */
  def updateBoidSize(value: Double): Unit = updateRender(configValue.render.copy(boidSize = value))

  /** Toggles uniform-grid overlay rendering. */
  def updateShowGrid(value: Boolean): Unit = updateRender(configValue.render.copy(showGrid = value))

  private def updateBehavior(f: BehaviorConfig => BehaviorConfig): Unit = updateLive(behavior = Some(f(configValue.behavior)))

  private def updateLive(
      flock: Option[FlockConfig] = None,
      behavior: Option[BehaviorConfig] = None
  ): Unit = {
    configValue = configValue.copy(
      flock = flock.getOrElse(configValue.flock),
      behavior = behavior.getOrElse(configValue.behavior)
    )
    engine = SimulationComponents.engine(configValue, neighborStrategy)
  }

  /** Applies renderer-only configuration without rebuilding simulation components. */
  private def updateRender(render: RenderConfig): Unit = {
    configValue = configValue.copy(render = render)
  }

  private def nanosToMillis(value: Long): Double = value.toDouble / 1_000_000.0
}
