package io.codeswarm.boids.app

import io.codeswarm.boids.model.{SimulationConfig, SimulationState}
import io.codeswarm.boids.rendering.scalafx.ScalaFxRenderer
import io.codeswarm.boids.simulation.{RandomSimulationInitializer, SimulationComponents, SimulationEngine}
import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas

/** Desktop ScalaFX entry point for Reynolds Boids Visualization milestone 0.3. */
object BoidApplication extends JFXApp3 {

  private val config = SimulationConfig.Default
  private val initializer = new RandomSimulationInitializer
  private val engine: SimulationEngine = SimulationComponents.engine(config)
  private var state: SimulationState = initializer.initialize(config)

  /** Creates the window, renderer and animation loop. */
  override def start(): Unit = {
    val canvas = new Canvas(config.world.width, config.world.height)
    val renderer = new ScalaFxRenderer(canvas.graphicsContext2D, config.world, config.render)

    stage = new JFXApp3.PrimaryStage {
      title = "Reynolds Boids Visualization 0.3.0"
      scene = new Scene(config.world.width, config.world.height) {
        content = canvas
      }
    }

    renderer.render(state)
    AnimationTimer { _ =>
      state = engine.step(state)
      renderer.render(state)
    }.start()
  }
}
