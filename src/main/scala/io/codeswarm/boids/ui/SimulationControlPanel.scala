package io.codeswarm.boids.ui

import scalafx.geometry.Insets
import scalafx.scene.control.{Button, CheckBox, Label, Separator, TextField}
import scalafx.scene.layout.{HBox, VBox}

/** Desktop control surface for lifecycle, live tuning and restart-required parameters. */
final class SimulationControlPanel(controller: SimulationController, metricsPanel: MetricsPanel) extends VBox(7.0) {
  private val statusLabel = new Label
  private val restartLabel = new Label
  private val seedField = new TextField {
    text = controller.config.seed.toString
    prefColumnCount = 9
  }

  private val boids = new IntParameterControl("Boids *", 10, 10000, controller.config.flock.boidCount, controller.updateBoidCount)
  private val maxSpeed = new DoubleParameterControl("Max speed", 0.1, 10.0, controller.config.flock.maxSpeed, controller.updateMaxSpeed)
  private val maxForce = new DoubleParameterControl("Max force", 0.001, 1.0, controller.config.flock.maxForce, controller.updateMaxForce)
  private val perception =
    new DoubleParameterControl("Perception radius", 5.0, 200.0, controller.config.behavior.perceptionRadius, controller.updatePerceptionRadius)
  private val separation =
    new DoubleParameterControl("Separation radius", 1.0, 100.0, controller.config.behavior.separationRadius, controller.updateSeparationRadius)
  private val separationWeight =
    new DoubleParameterControl("Separation weight", 0.0, 5.0, controller.config.behavior.separationWeight, controller.updateSeparationWeight)
  private val alignmentWeight =
    new DoubleParameterControl("Alignment weight", 0.0, 5.0, controller.config.behavior.alignmentWeight, controller.updateAlignmentWeight)
  private val cohesionWeight =
    new DoubleParameterControl("Cohesion weight", 0.0, 5.0, controller.config.behavior.cohesionWeight, controller.updateCohesionWeight)
  private val boidSize = new DoubleParameterControl("Boid size", 2.0, 15.0, controller.config.render.boidSize, controller.updateBoidSize)
  private val showGrid = new CheckBox("Show spatial grid") { selected = controller.config.render.showGrid }

  private val startButton = new Button("Start") { onAction = _ => controller.start() }
  private val pauseButton = new Button("Pause") { onAction = _ => controller.pause() }
  private val restartButton = new Button("Restart") { onAction = _ => controller.restart() }

  showGrid.selected.onChange { (_, _, selected) => controller.updateShowGrid(selected) }
  seedField.onAction = _ => commitSeed()
  seedField.focused.onChange { (_, _, focused) => if (!focused) commitSeed() }

  padding = Insets(10.0)
  prefWidth = 310.0
  children = Seq(
    new Label("Simulation controls"),
    new HBox(6.0, startButton, pauseButton, restartButton),
    statusLabel,
    restartLabel,
    new Separator,
    boids,
    new HBox(6.0, new Label("Seed *"), seedField),
    maxSpeed,
    maxForce,
    perception,
    separation,
    separationWeight,
    alignmentWeight,
    cohesionWeight,
    boidSize,
    showGrid,
    new Label("* requires Restart"),
    new Separator,
    metricsPanel
  )

  /** Refreshes lifecycle/restart labels and metrics after an animation frame. */
  def refresh(): Unit = {
    statusLabel.text = s"Status: ${controller.status}"
    restartLabel.text = if (controller.restartRequired) "Configuration changed — restart required" else ""
    separation.setValue(controller.config.behavior.separationRadius)
    metricsPanel.update(controller.metrics)
  }

  private def commitSeed(): Unit =
    scala.util.Try(seedField.text.value.trim.toLong).toOption match {
      case Some(value) => controller.updateSeed(value)
      case None => seedField.text = controller.config.seed.toString
    }
}
