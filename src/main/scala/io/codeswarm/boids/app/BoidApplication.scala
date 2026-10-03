package io.codeswarm.boids.app

import io.codeswarm.boids.model.SimulationConfig
import io.codeswarm.boids.rendering.scalafx.{IdBasedColorStrategy, ScalaFxRenderer}
import io.codeswarm.boids.ui.{MetricsPanel, SimulationControlPanel, SimulationController, SimulationView}
import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane

/** Desktop ScalaFX entry point for Reynolds Boids Visualization milestone 0.4. */
object BoidApplication extends JFXApp3 {

  /** Creates the interactive simulation laboratory and animation loop. */
  override def start(): Unit = {
    val controller = new SimulationController(SimulationConfig.Desktop)
    val view = new SimulationView(controller.config.world)
    val renderer = new ScalaFxRenderer(view.canvas.graphicsContext2D, controller.config.world, new IdBasedColorStrategy)
    val metricsPanel = new MetricsPanel
    val controls = new SimulationControlPanel(controller, metricsPanel)

    stage = new JFXApp3.PrimaryStage {
      title = "Reynolds Boids Visualization 0.4.0"
      scene = new Scene(controller.config.world.width + 320.0, controller.config.world.height) {
        root = new BorderPane {
          center = view
          right = controls
        }
      }
    }

    renderer.updateConfig(controller.config.render, controller.config.behavior.perceptionRadius)
    renderer.render(controller.state)
    controls.refresh()

    AnimationTimer { _ =>
      val frameStarted = System.nanoTime()
      controller.advance()
      renderer.updateConfig(controller.config.render, controller.config.behavior.perceptionRadius)
      val renderStarted = System.nanoTime()
      renderer.render(controller.state)
      val renderNanos = System.nanoTime() - renderStarted
      val frameNanos = System.nanoTime() - frameStarted
      controller.recordFrame(renderNanos, frameNanos)
      controls.refresh()
    }.start()
  }
}
