package io.codeswarm.boids.ui

import io.codeswarm.boids.metrics.RuntimeMetrics
import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox

/** Compact read-only diagnostics panel for runtime and spatial metrics. */
final class MetricsPanel extends VBox(2.0) {
  private val fps = new Label
  private val tick = new Label
  private val boids = new Label
  private val step = new Label
  private val render = new Label
  private val averageSpeed = new Label
  private val neighbors = new Label
  private val candidates = new Label
  private val occupancy = new Label

  padding = Insets(8.0)
  children = Seq(new Label("Metrics"), fps, tick, boids, step, render, averageSpeed, neighbors, candidates, occupancy)
  update(RuntimeMetrics())

  /** Refreshes all labels from one immutable metrics snapshot. */
  def update(metrics: RuntimeMetrics): Unit = {
    fps.text = f"FPS: ${metrics.fps}%.1f"
    tick.text = s"Tick: ${metrics.tick}"
    boids.text = s"Boids: ${metrics.boidCount}"
    step.text = f"Step: ${metrics.simulationStepMillis}%.3f ms"
    render.text = f"Render: ${metrics.renderMillis}%.3f ms"
    averageSpeed.text = f"Avg speed: ${metrics.averageSpeed}%.3f"
    neighbors.text = f"Avg neighbors: ${metrics.spatial.map(_.averageNeighborsPerQuery).getOrElse(0.0)}%.2f"
    candidates.text = f"Avg candidates: ${metrics.spatial.map(_.averageCandidatesPerQuery).getOrElse(0.0)}%.2f"
    occupancy.text = metrics.spatial match {
      case Some(value) => f"Cells: ${value.occupiedCells}, avg/cell: ${value.averageBoidsPerCell}%.2f, max: ${value.maxBoidsPerCell}"
      case None => "Cells: metrics disabled"
    }
  }
}
